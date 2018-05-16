package pl.edu.agh.arbeit.data.report;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import pl.edu.agh.arbeit.data.repository.DatabaseEventRepository;
import pl.edu.agh.arbeit.tracker.events.Event;
import pl.edu.agh.arbeit.tracker.events.EventType;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvReport implements Report {

    private final List<Event> events;
// initial set of events is going to be needed in order to calculate the activities properly
//    private final Collection<Event> predecessors;
    private final List<LocalDate> dates;
    private final List<String> appsToReport;
    private final CSVFormat format;

    public CsvReport(List<String> appsToReport, List<Event> events) {
        this.appsToReport = appsToReport;
//        this.predecessors = predecessors;
        this.events = events;
        // list of distinct LocalDates when systemEvents happened
        this.dates = getSortedRelevantEvents("system")
                .map(Event::getLocalDate)
                .distinct()
                .collect(Collectors.toList());

        ArrayList<String> headerList = new ArrayList<>();
        headerList.add("topic");
        headerList.add("activity");
        headerList.addAll(dates.stream().map(LocalDate::toString).distinct().collect(Collectors.toList()));
        String[] header = headerList.toArray(new String[0]);
        this.format = CSVFormat.DEFAULT.withHeader(header);
    }


    public void writeCsv(Path path) throws IOException {
        FileWriter writer = new FileWriter(path.toString());
        try(CSVPrinter printer = new CSVPrinter(writer, format)){
            appsToReport.stream()
                    .distinct()
                    .forEach(topic -> {
                        DatabaseEventRepository repo = new DatabaseEventRepository();
                        Optional<Event> opt = repo.getPreviousEventTypeForApp(getSortedRelevantEvents(topic)
                                .collect(Collectors.toList()));
                        DurationCalculator calculator = new DurationCalculator(
                                topic,
                                getSortedRelevantEvents(topic)
                                        .collect(Collectors.toList()),
                                opt
                        );
                        Arrays.asList(EventType.ACTIVE, EventType.PASSIVE)
                                .forEach(activityType -> {
                                    LinkedList<String> record = new LinkedList<>();
                                    record.addLast(topic);
                                    record.addLast(activityType.toString());

                                    record.addAll(
                                            dates.stream()
                                            .map(date -> calculator.activityLength(date, activityType))
                                            .map(Duration::toMinutes)
                                            .map(String::valueOf)
                                            .collect(Collectors.toList()));
                                        try {
                                            printer.printRecord(record);
                                        } catch (IOException e) {
                                        e.printStackTrace();
                                        }
                                });
                        });
        }
    }

    // This method returns a stream of events relevant to given topic sorted by datetime.
    // As SystemEvents are relevant to every topic, they are included there too
    private Stream<Event> getSortedRelevantEvents(String topic){
       return events
                .parallelStream()
                .filter(event -> event.getTopic().equals(topic))
                .sorted((o1, o2) -> {
                    if(o1.getDateTime().isBefore(o2.getDateTime())) return -1;
                    else if(o2.getDateTime().isBefore(o1.getDateTime())) return 1;
                    else return 0;
                });
    }

    @Override
    public List<Event> getEvents() {
        return events;
    }
}
