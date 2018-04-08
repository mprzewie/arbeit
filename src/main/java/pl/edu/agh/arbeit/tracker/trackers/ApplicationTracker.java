package pl.edu.agh.arbeit.tracker.trackers;

import com.google.common.eventbus.EventBus;
import pl.edu.agh.arbeit.tracker.Application;
import pl.edu.agh.arbeit.tracker.events.ApplicationEvent;
import pl.edu.agh.arbeit.tracker.events.EventType;

public class ApplicationTracker implements Tracker {

    private final EventBus bus;
    private final Application application;
    private final long pingTime = 3;

    public ApplicationTracker(EventBus eventBus, Application application) {
        this.bus = eventBus;
        this.application = application;
    }

    @Override
    public void run() {
        bus.post(new ApplicationEvent(EventType.START, application));

        while (application.isRunning()){
            try {
                Thread.sleep(pingTime * 1000);
                if(application.isActive()){
                    bus.post(new ApplicationEvent(EventType.ACTIVE, application));
                }
                else {
                    bus.post(new ApplicationEvent(EventType.PASSIVE, application));
                }
            } catch (InterruptedException e) {
            }
        }
        bus.post(new ApplicationEvent(EventType.STOP, application));

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
