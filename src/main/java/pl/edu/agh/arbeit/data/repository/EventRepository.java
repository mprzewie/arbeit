package pl.edu.agh.arbeit.data.repository;

import pl.edu.agh.arbeit.tracker.events.Event;

import java.util.Date;
import java.util.List;

public interface EventRepository {

    void put(Event event);

    List<Event> getAllEvents();

    List<Event> getEventsForGivenApps(String[] applications);

    List<Event> getEventForGivenAppinRange(String application, Date startDate, Date endDate);
}
