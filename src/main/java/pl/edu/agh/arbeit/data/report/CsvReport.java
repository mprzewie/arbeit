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
    private final String[] firstColumnArray;
   // private final CSVFormat format;

    public CsvReport(List<String> appsToReport, List<Event> events) {
        this.appsToReport = appsToReport;
//        this.predecessors = predecessors;
        this.events = events;
        // list of distinct LocalDates when systemEvents happened
        this.dates = getSortedRelevantEvents("system")
                .map(Event::getLocalDate)
                .distinct()
                .collect(Collectors.toList());

        ArrayList<String> firstColumnList = new ArrayList<>();
        firstColumnList.add("topic");
        firstColumnList.add("activity");
        firstColumnList.addAll(dates.stream().map(LocalDate::toString).distinct().collect(Collectors.toList()));
        //this.firstColumnList = firstColumnList;
        this.firstColumnArray = firstColumnList.toArray(new String[0]);
        //this.format = CSVFormat.DEFAULT.withHeader(header);
    }


    public void writeCsv(Path path) throws IOException {
        FileWriter writer = new FileWriter(path.toString());
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.EXCEL);

        LinkedList<String> record = new LinkedList<>();

        /* first row */
        record.addLast(firstColumnArray[0]);
        appsToReport.forEach(app -> {
            int i = 0;
            while(i++ != 2) record.addLast(app);
        });
        printer.printRecord(record);
        record.clear();

        /* second row */
        record.addLast(firstColumnArray[1]);
        appsToReport.forEach(app -> {
            record.addLast("ACTIVE");
            record.addLast("PASSIVE");
        });
        printer.printRecord(record);
        record.clear();

        /* data rows */
        for(int i = 2; i < firstColumnArray.length; i++) {
            record.addLast(firstColumnArray[i]);

            appsToReport.forEach(app -> {
                DatabaseEventRepository repo = new DatabaseEventRepository();
                Optional<Event> opt = repo.getPreviousEventTypeForApp(getSortedRelevantEvents(app)
                        .collect(Collectors.toList()));
                DurationCalculator calculator = new DurationCalculator(
                        app,
                        getSortedRelevantEvents(app)
                                .collect(Collectors.toList()),
                        opt
                );
                Arrays.asList(EventType.ACTIVE, EventType.PASSIVE)
                        .forEach(activityType -> {
                            record.addAll(
                                    dates.stream()
                                            .map(date -> calculator.activityLength(date, activityType))
                                            .map(Duration::toMinutes)
                                            .map(String::valueOf)
                                            .collect(Collectors.toList()));

                        });
                });
                printer.printRecord(record);
                record.clear();
        }


        printer.close();

        /*
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
        */
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
