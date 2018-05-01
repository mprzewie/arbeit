package pl.edu.agh.arbeit.data.report;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import pl.edu.agh.arbeit.tracker.events.Event;
import pl.edu.agh.arbeit.tracker.events.EventType;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.time.*;
import java.util.stream.Stream;

public class CsvReport implements Report {

    private final List<Event> events;
    // initial set of events is going to be needed in order to calculate the activities properly
//    private final Collection<Event> predecessors;
    private final List<LocalDate> dates;
    private final CSVFormat format;

    public CsvReport(List<Event> events) {

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
            events.stream()
                    .map(Event::getTopic)
                    .distinct()
                    .forEach(topic ->
                            Arrays.asList(EventType.ACTIVE, EventType.PASSIVE).forEach(activityType -> {
                        try {
                            printer.printRecord(activityRecord(topic, activityType));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }));
        }
    }

    private List<String> activityRecord(String topic, EventType activityType){
        LinkedList<String> record = new LinkedList<>();
        record.addLast(topic);
        record.addLast(activityType.toString());
        System.out.println(topic + " " + activityType.toString());
        Map<LocalDateTime, Duration> map = splitDurationMap(topic, activityType);
        System.out.println(new MapDebug<>(map));

        record.addAll(dates.stream()
                .map(date ->
                        activityLength(
                                map,
                                date
                        ).toMinutes()
                )
                .map(String::valueOf)
                .collect(Collectors.toList())
        );

        return record;
    }

    private Duration activityLength(Map<LocalDateTime, Duration> splitDurations, LocalDate date){
        return Duration.ofSeconds(splitDurations.keySet().stream()
                .filter(startDate -> startDate.toLocalDate().isEqual(date))
                .mapToLong(startDate -> splitDurations.get(startDate).getSeconds())
                .sum());
    }

    private Map<LocalDateTime, Duration> splitDurationMap(String topic, EventType activityType){
        Map<LocalDateTime, Duration> durations;
        if(activityType.equals(EventType.ACTIVE)){
            durations = activeDurations(topic);
        } else {
            durations = passiveDurations(topic);
        }
         return splitAtMidnight(durations);
    }

    private Map<LocalDateTime, Duration> splitAtMidnight(Map<LocalDateTime, Duration> map){
        Map<LocalDateTime, Duration> result = new HashMap<>();
        for(LocalDateTime startTime : map.keySet()){
            Duration duration = map.get(startTime);
            LocalDate startDate = startTime.toLocalDate();
            LocalDate endDate = startTime.plusSeconds(duration.getSeconds()).toLocalDate();
            if(startDate.isEqual(endDate)){
                result.put(startTime, duration);
            } else {
                LocalDateTime mockStartTime = endDate.atStartOfDay();
                Duration durationPreMidnight = Duration.ofSeconds(startTime.until(mockStartTime ,ChronoUnit.SECONDS));
                Duration durationPostMidnight = duration.minus(durationPreMidnight);
                result.put(startTime, durationPreMidnight);
                result.put(mockStartTime, durationPostMidnight);
            }
        }
        return result;
    }


    // this is a map of active periods of tracked topic (application)
    // key -> time of the start of the period
    // value -> duration of the period
    private Map<LocalDateTime, Duration> activeDurations(String topic) {
        List<Event> relevantEvents =
                getSortedRelevantEvents(topic).collect(Collectors.toList());

        // for now I assume that the application has been previously closed
        // TODO use initialStates
        Event currentTopic = new Event() {
            @Override
            public String getTopic() {
                return topic;
            }

            @Override
            public EventType getType() {
                return EventType.STOP;
            }
        };

        Map<LocalDateTime, Duration> result = new HashMap<>();

        for (Event event : relevantEvents) {
            // systemEvents are currently ignored
            // let's assume for now that when AppEvents are happening,
            // system is active
            if(event.getTopic().equals(topic)){
                // activity stops when passive mode is entered or app has been stopped
                if(!event.getType().equals(EventType.ACTIVE)
                        && currentTopic.getType().equals(EventType.ACTIVE)){
                    result.put(currentTopic.getDateTime(),
                            Duration.ofSeconds(
                                    currentTopic
                                            .getDateTime()
                                            .until(event.getDateTime(), ChronoUnit.SECONDS)
                            )
                    );
                }
                currentTopic = event;
            }
        }
        if(currentTopic.getType().equals(EventType.ACTIVE)) {
            result.put(currentTopic.getDateTime(),
                    Duration.ofSeconds(
                            currentTopic.getDateTime()
                                    .until(
                                            relevantEvents.get(relevantEvents.size() - 1)
                                                    .getDateTime(),
                                            ChronoUnit.SECONDS)
                    ));
        }
        return result;
    }


    // this is a map of passive periods of tracked topic (application)
    // key -> time of the start of the period
    // value -> duration of the period
    private Map<LocalDateTime, Duration> passiveDurations(String topic) {
        List<Event> relevantEvents =
                getSortedRelevantEvents(topic).collect(Collectors.toList());

        // for now I assume that the application has been previously closed
        // TODO use initialStates
        Event currentTopic = new Event() {
            @Override
            public String getTopic() {
                return topic;
            }

            @Override
            public EventType getType() {
                return EventType.STOP;
            }
        };

        Map<LocalDateTime, Duration> result = new HashMap<>();

        for (Event event : relevantEvents) {
            // systemEvents are currently ignored
            // let's assume for now that when AppEvents are happening,
            // system is active
            if(event.getTopic().equals(topic)){
                // passiveness stops when passive mode is entered or app has been stopped
                if(!event.getTopic().equals("system")) System.out.println("-" + event);

                if(!event.getType().equals(EventType.PASSIVE)
                        && currentTopic.getType().equals(EventType.PASSIVE)){
                    result.put(currentTopic.getDateTime(),
                            Duration.ofSeconds(
                                    currentTopic
                                            .getDateTime()
                                            .until(event.getDateTime(), ChronoUnit.SECONDS)
                            )
                    );
                }
                currentTopic = event;
            }

        }
        // if app is currently passive
        if(currentTopic.getType().equals(EventType.PASSIVE)){
            result.put(currentTopic.getDateTime(),
                    Duration.ofSeconds(
                            currentTopic.getDateTime()
                                    .until(
                                            relevantEvents.get(relevantEvents.size()-1)
                                                    .getDateTime(),
                                            ChronoUnit.SECONDS)
                    ));
        }
        return result;
    }

    // This method returns a stream of events relevant to given topic sorted by datetime.
    // As SystemEvents are relevant to every topic, they are included there too
    private Stream<Event> getSortedRelevantEvents(String topic){
       return events
                .parallelStream()
                .filter(event -> event.getTopic().equals(topic) || event.getTopic().equals("system"))
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
