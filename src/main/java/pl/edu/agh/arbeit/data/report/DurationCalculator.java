package pl.edu.agh.arbeit.data.report;

import pl.edu.agh.arbeit.tracker.events.Event;
import pl.edu.agh.arbeit.tracker.events.EventType;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DurationCalculator {

    private final String topic;
    private final List<Event> relevantEvents;
    private final Map<EventType, Map<LocalDateTime, Duration>> durationMaps;

    public DurationCalculator(String topic, List<Event> events) {
        this.topic = topic;
        this.relevantEvents = events;
        this.durationMaps = new HashMap<>();
        durationMaps.put(
                EventType.ACTIVE,
                splitAtMidnight(activeDurations())
                );
        durationMaps.put(
                EventType.PASSIVE,
                splitAtMidnight(passiveDurations())
        );
    }

    public Duration activityLength(LocalDate date, EventType activityType){

        return Duration.ofSeconds(
                durationMaps.get(activityType)
                .keySet().stream()
                .filter(startDate -> startDate.toLocalDate().isEqual(date))
                .mapToLong(startDate ->
                        durationMaps.get(activityType)
                                .get(startDate).getSeconds()
                )
                .sum());
    }



    // this is a map of active periods of tracked topic (application)
    // key -> time of the start of the period
    // value -> duration of the period
    private Map<LocalDateTime, Duration> activeDurations() {

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
    private Map<LocalDateTime, Duration> passiveDurations() {
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

}