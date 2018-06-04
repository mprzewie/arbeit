package pl.edu.agh.arbeit.tracker.trackers;

import pl.edu.agh.arbeit.tracker.events.EventType;
import pl.edu.agh.arbeit.tracker.events.SystemEvent;
import pl.edu.agh.arbeit.tracker.system.KeyboardTracker;
import pl.edu.agh.arbeit.tracker.system.LockScreenTracker;
import pl.edu.agh.arbeit.tracker.system.MouseTracker;

import java.time.Duration;

/**
 * @author marcin on 4/8/18
 **/
public class SystemTracker extends AsyncTracker {

    private MouseTracker mouseTracker;
    private KeyboardTracker keyboardTracker;
    private LockScreenTracker lockScreenTracker;
    private Duration timeToBecomePassive;
    public SystemTracker(Duration pingTime, Duration timeToBecomePassive) {
        super(pingTime);
        keyboardTracker = new KeyboardTracker();
        lockScreenTracker = new LockScreenTracker();
        lockScreenTracker.start();
        this.timeToBecomePassive = timeToBecomePassive;
        mouseTracker = new MouseTracker(timeToBecomePassive);
        keyboardTracker.start();
    }

    @Override
    protected void actOnStatus() {
        bus.post(currentStateEvent());
    }

    public SystemEvent currentStateEvent(){
        if (lockScreenTracker.isUserUnlocked()){
            if(mouseTracker.getTimeSinceLastMoveNoticed().compareTo(timeToBecomePassive) > 0
                    && keyboardTracker.getTimeSinceLastKeyPressed().compareTo(timeToBecomePassive) > 0) {
                return new SystemEvent(EventType.PASSIVE);
            }
            else {
                return new SystemEvent(EventType.ACTIVE);
            }
        }
        else {
            return new SystemEvent(EventType.PASSIVE);
        }
    }
    @Override
    protected void stopTracking() {
        bus.post(new SystemEvent(EventType.STOP));
    }

}
