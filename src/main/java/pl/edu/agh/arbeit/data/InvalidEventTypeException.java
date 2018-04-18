package pl.edu.agh.arbeit.data;

import pl.edu.agh.arbeit.tracker.events.Event;

public class InvalidEventTypeException extends Exception {
    public InvalidEventTypeException(Event event) {
        super("Invalid Event : " + event.getTopic() + event.getType() + event.getDate());
    }
}
