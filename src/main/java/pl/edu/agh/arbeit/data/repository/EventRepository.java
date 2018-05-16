package pl.edu.agh.arbeit.data.repository;

import pl.edu.agh.arbeit.tracker.events.Event;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface EventRepository {

    void put(Event event);

    List<Event> getAllEvents();

    Optional<Event> getPreviousEventTypeForApp(List<Event> events);

    List<Event> getEventsForGivenApps(String[] applications);

    List<Event> getEventForGivenAppinRange(String application, Date startDate, Date endDate);
}
