package pl.edu.agh.arbeit.tracker.trackers;

import pl.edu.agh.arbeit.tracker.events.EventType;
import pl.edu.agh.arbeit.tracker.events.SystemEvent;
import pl.edu.agh.arbeit.tracker.system.KeyboardTracker;
import pl.edu.agh.arbeit.tracker.system.LockScreenTracker;
import pl.edu.agh.arbeit.tracker.system.MouseTracker;

import java.awt.*;

/**
 * @author marcin on 4/8/18
 **/
public class SystemTracker extends AsyncTracker {

    private MouseTracker mouseTracker;
    private KeyboardTracker keyboardTracker;
    private LockScreenTracker lockScreenTracker;
    private int secondsToBecomePassive;
    public SystemTracker(long pingTime, int secondsToBecomePassive) {
        super(pingTime);
        keyboardTracker = new KeyboardTracker();
        lockScreenTracker = new LockScreenTracker();
        lockScreenTracker.start();
        this.secondsToBecomePassive = secondsToBecomePassive;
        try {
            mouseTracker = new MouseTracker(secondsToBecomePassive/4);
        } catch (AWTException e) {
            e.printStackTrace();
        }
        keyboardTracker.start();
    }

    @Override
    protected void actOnStatus() {
        bus.post(currentStateEvent());
    }

    public SystemEvent currentStateEvent(){
        if (lockScreenTracker.isUserUnlocked()){
            if(mouseTracker.getSecondsSinceLastMoveNoticed() > secondsToBecomePassive
                    && keyboardTracker.getSecondsSinceLastKeyPressed() > secondsToBecomePassive) {
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
