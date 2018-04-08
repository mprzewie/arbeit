package pl.edu.agh.arbeit.tracker.trackers;

import com.google.common.eventbus.EventBus;
import pl.edu.agh.arbeit.tracker.events.EventType;
import pl.edu.agh.arbeit.tracker.events.SystemEvent;

/**
 * @author marcin on 4/8/18
 **/
public class SystemTracker implements Tracker {

    private final long pingTime = 3;

    private final EventBus bus;

    public SystemTracker(EventBus bus) {
        this.bus = bus;
    }

    @Override
    public void run() {

        bus.post(new SystemEvent(EventType.START));

        while (true){
            try {
                Thread.sleep(pingTime * 1000);
                bus.post(new SystemEvent(EventType.ACTIVE));
            } catch (InterruptedException e) {
                bus.post(new SystemEvent(EventType.STOP));
                return;
            }
        }
    }

    public EventBus getBus() {
        return bus;
    }

    @Override
    public Long getPingTime() {
        return pingTime;
    }
}
