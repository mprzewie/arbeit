package pl.edu.agh.arbeit.data.repository;

import pl.edu.agh.arbeit.tracker.events.Event;

import java.util.List;

public interface EventRepository {

    void put(Event event);

    List<Event> getEvents();

}
