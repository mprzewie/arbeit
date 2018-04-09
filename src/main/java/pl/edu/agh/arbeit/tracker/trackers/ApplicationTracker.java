package pl.edu.agh.arbeit.tracker.trackers;

import pl.edu.agh.arbeit.tracker.Application;
import pl.edu.agh.arbeit.tracker.events.ApplicationEvent;
import pl.edu.agh.arbeit.tracker.events.Event;
import pl.edu.agh.arbeit.tracker.events.EventType;

import java.util.Stack;

public class ApplicationTracker extends AsyncTracker {

    private final Application application;
    private final Stack<Event> previousEvents;

    public ApplicationTracker(long pingTime, Application application) {
        super(pingTime);
        this.application = application;
        previousEvents = new Stack<>();
        previousEvents.push(new ApplicationEvent(EventType.STOP, application));
    }

    @Override
    protected void actOnStatus() {
        Event currentStateEvent = application.getCurrentStateEvent();
        if(!currentStateEvent.equals(previousEvents.peek())){
            previousEvents.push(currentStateEvent);
            bus.post(currentStateEvent);
        }
    }

    @Override
    protected void stopTracking() {
        bus.post(new ApplicationEvent(EventType.STOP, application));
    }

    public Application getApplication() {
        return application;
    }

}
