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
    private Optional<Event> eventBefore;
    private Event previousEvent;

    public DurationCalculator(String topic, List<Event> events, Optional<Event> eventBefore) {
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
        putDurations();
        this.eventBefore = eventBefore;
    }

    private void putDurations() {
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
        EventType inverseThanTrackedType = typeWeAreTracking.equals(EventType.ACTIVE) ? EventType.PASSIVE : EventType.ACTIVE;
        EventType startingOrStoppingEvent = typeWeAreTracking.equals(EventType.ACTIVE) ? EventType.STOP : EventType.START;

        for (Event event : relevantEvents) {
            // systemEvents are currently ignored
            // let's assume for now that when AppEvents are happening,
            // system is active
            // below: because could be sdystem event
            if(event.getTopic().equals(topic)){
                // activity stops when passive mode is entered or app has been stopped
                // if current event is not active (one of three remaining) and previous event is active event
                if((event.getType().equals(inverseThanTrackedType)|| event.getType().equals(startingOrStoppingEvent))
                        && localPreviousEvent.getType().equals(typeWeAreTracking)){
                    result.put(localPreviousEvent.getDateTime(),
                            Duration.ofSeconds(
                                    localPreviousEvent
                                            .getDateTime()
                                            .until(event.getDateTime(), ChronoUnit.SECONDS)
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
            result.put(localPreviousEvent.getDateTime(),
                    Duration.ofSeconds(
                            localPreviousEvent.getDateTime()
                                    .until(
                                            relevantEvents.get(relevantEvents.size() - 1)
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

