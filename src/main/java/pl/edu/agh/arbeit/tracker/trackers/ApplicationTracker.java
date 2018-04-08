package pl.edu.agh.arbeit.tracker.trackers;

import com.google.common.eventbus.EventBus;
import pl.edu.agh.arbeit.tracker.Application;
import pl.edu.agh.arbeit.tracker.events.ApplicationEvent;
import pl.edu.agh.arbeit.tracker.events.Event;
import pl.edu.agh.arbeit.tracker.events.EventType;

import java.util.Stack;

public class ApplicationTracker implements Tracker {

    private final EventBus bus;
    private final Application application;
    private final Stack<Event> previousEvents;
    private final long pingTime = 3;

    public ApplicationTracker(EventBus eventBus, Application application) {
        this.bus = eventBus;
        this.application = application;
        previousEvents = new Stack<>();
    }

    @Override
    public void run() {
        previousEvents.push(new ApplicationEvent(EventType.START, application));
        bus.post(previousEvents.peek());
        while (application.isRunning()){
            try {
                Thread.sleep(pingTime * 1000);
                Event newEvent = application.stateEvent();
                if(!newEvent.equals(previousEvents.peek())){
                    previousEvents.push(newEvent);
                    bus.post(newEvent);
                }
            } catch (InterruptedException ignored) {}
        }
        bus.post(new ApplicationEvent(EventType.STOP, application));
    }

    public Application getApplication() {
        return application;
    }


    @Override
    public EventBus getBus() {
        return bus;
    }

    @Override
    public Long getPingTime() {
        return pingTime;
    }
}
