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
import java.util.Optional;

public class DurationCalculator {

    private final String topic;
    private final List<Event> relevantEvents;
    private final Map<EventType, Map<LocalDateTime, Duration>> durationMaps;
    private Event previousEvent;
    private Event finalEvent;

    public DurationCalculator(String topic, List<Event> events, Optional<Event> eventBefore, Event finalEvent) {
        this.topic = topic;
        this.relevantEvents = events;
        this.durationMaps = new HashMap<>();
        this.previousEvent = eventBefore.orElseGet(() -> new Event() {
            @Override
            public String getTopic() {
                return topic;
            }

            @Override
            public EventType getType() {
                return EventType.STOP;
            }
        });
        this.finalEvent = finalEvent;

        calculateDurations();
    }

    private void calculateDurations() {
        System.out.println(topic + " " + relevantEvents.size());
        durationMaps.put(EventType.ACTIVE, splitAtMidnight(durations(EventType.ACTIVE)));
        durationMaps.put(EventType.PASSIVE, splitAtMidnight(durations(EventType.PASSIVE)));
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
    private Map<LocalDateTime, Duration> durations(EventType typeWeAreTracking) {
        // the event below is for comparing
        Event localPreviousEvent = this.previousEvent;
        Map<LocalDateTime, Duration> result = new HashMap<>();
        for (Event event : relevantEvents) {
            if(event.getTopic().equals(topic) && !event.getType().equals(localPreviousEvent.getType())){
                // the first requirement ensures that we only take into consideration events regarding the given topic
                // the second requirement is because two events of the same type in a row are redundant - we only care about the first one
                if(localPreviousEvent.getType().equals(typeWeAreTracking) &&
                        ! typeWeAreTracking.equals(event.getType())){
                    result.put(localPreviousEvent.getLocalDateTime(),
                            Duration.ofSeconds(
                                    localPreviousEvent
                                            .getLocalDateTime()
                                            .until(event.getLocalDateTime(), ChronoUnit.SECONDS)
                            )
                    );
                }
                localPreviousEvent = event;
            }
        }
        // after iteration
        // if previous event is active it means that we still use this application
        // it must be considered in report as additional but non existing event (only adding duration in time vector)
        if(localPreviousEvent.getType().equals(typeWeAreTracking)) {
            result.put(localPreviousEvent.getLocalDateTime(),
                    Duration.ofSeconds(
                            localPreviousEvent.getLocalDateTime()
                                    .until(
                                            finalEvent
                                                    .getLocalDateTime(),
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

