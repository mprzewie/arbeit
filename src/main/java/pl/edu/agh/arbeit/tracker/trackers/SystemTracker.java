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

    MouseTracker mouseTracker;
    KeyboardTracker keyboardTracker = new KeyboardTracker();
    LockScreenTracker lockScreenTracker;
    int secondsToBecomePassive;
    public SystemTracker(long pingTime, int secondsToBecomePassive) {
        super(pingTime);
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
        if (!lockScreenTracker.isUserUnlocked())
        {
            bus.post(new SystemEvent(EventType.PASSIVE));
            return;
        }
        if(mouseTracker.getSecondsSinceLastMoveNoticed()>secondsToBecomePassive
                && keyboardTracker.getSecondsSinceLastKeyPressed()>secondsToBecomePassive)
            bus.post(new SystemEvent(EventType.PASSIVE));
        else {
            bus.post(new SystemEvent(EventType.ACTIVE));
        }
    }

    @Override
    protected void stopTracking() {
        bus.post(new SystemEvent(EventType.STOP));
    }

}
