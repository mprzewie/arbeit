package pl.edu.agh.arbeit.data.report;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import pl.edu.agh.arbeit.data.repository.DatabaseEventRepository;
import pl.edu.agh.arbeit.tracker.Application;
import pl.edu.agh.arbeit.tracker.events.Event;
import pl.edu.agh.arbeit.tracker.events.EventType;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
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
    private final List<LocalDate> dates;
    private final List<String> appsToReport;

    public CsvReport(List<String> appsToReport, List<Event> events) {
        this.appsToReport = appsToReport;
        this.events = events;
        // list of distinct LocalDates when systemEvents happened
        this.dates = getSortedRelevantEvents("system")
                .stream()
                .map(Event::getLocalDate)
                .distinct()
                .collect(Collectors.toList());
    }


    public void writeCsv(Path path) throws IOException {
        FileWriter writer = new FileWriter(path.toString());
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.EXCEL);

        LinkedList<String> record = new LinkedList<>();

        /* first row */
        record.addLast("topic");
        appsToReport.forEach(app -> {
            // for ACTIVE column
            record.addLast(app);
            // for PASSIVE column
            record.addLast(app);
        });
        printer.printRecord(record);
        record.clear();

        /* second row */
        record.addLast("activity");
        appsToReport.forEach(app -> {
            record.addLast("ACTIVE");
            record.addLast("PASSIVE");
        });
        printer.printRecord(record);
        record.clear();

        // create a DurationCalculator for each app only once
        // no need to calculate the same stuff for each date
        DatabaseEventRepository repo = new DatabaseEventRepository();

        List<Event> systemEvents = getSortedRelevantEvents("system");
        Event lastSystemEvent = systemEvents.get(systemEvents.size()-1);

        List<DurationCalculator> appDurationCalculators = appsToReport.stream()
                .map(app -> {
                    Optional<Event> previousEvent =
                            repo.getPreviousEventTypeForApp(getSortedRelevantEvents(app));
                    return new DurationCalculator(
                            app,
                            getSortedRelevantEvents(app),
                            previousEvent,
                            lastSystemEvent
                    );
        }).collect(Collectors.toList());

        /* data rows */
        dates.forEach(date -> {
            record.addLast(date.toString());
            appDurationCalculators.forEach(calculator -> {
                Arrays.asList(EventType.ACTIVE, EventType.PASSIVE)
                        .forEach(activityType ->
                                record.add(String.valueOf(
                                                calculator.activityLength(date, activityType).toMinutes()
                                        ))
                        );
            });
            try {
                printer.printRecord(record);
            } catch (IOException ignored) {}
            record.clear();
        });

        printer.close();

    }

    // This method returns a stream of events relevant to given topic sorted by datetime.
    // As SystemEvents are relevant to every topic, they are included there too
    private List<Event> getSortedRelevantEvents(String topic){
       return events
                .parallelStream()
                .filter(event -> event.getTopic().equals(topic))
                .sorted((o1, o2) -> {
                    if(o1.getDateTime().isBefore(o2.getDateTime())) return -1;
                    else if(o2.getDateTime().isBefore(o1.getDateTime())) return 1;
                    else return 0;
                })
               .collect(Collectors.toList());
    }

    @Override
    public List<Event> getEvents() {
        return events;
    }
}
