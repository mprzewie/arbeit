package pl.edu.agh.arbeit.tracker.trackers;

import com.google.common.eventbus.EventBus;
import pl.edu.agh.arbeit.tracker.events.Event;
import pl.edu.agh.arbeit.tracker.events.EventType;
import pl.edu.agh.arbeit.tracker.events.SystemEvent;

/**
 * @author marcin on 4/8/18
 **/
public class SystemTracker extends AsyncTracker {

    public SystemTracker(long pingTime) {
        super(pingTime);
    }

    @Override
    protected void actOnStatus() {
        bus.post(new SystemEvent(EventType.ACTIVE));
    }

    @Override
    protected void stopTracking() {
        bus.post(new SystemEvent(EventType.STOP));
    }


}
