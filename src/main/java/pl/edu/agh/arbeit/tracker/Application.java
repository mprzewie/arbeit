package pl.edu.agh.arbeit.tracker;

import pl.edu.agh.arbeit.tracker.events.ApplicationEvent;
import pl.edu.agh.arbeit.tracker.events.EventType;
import pl.edu.agh.arbeit.tracker.events.SystemEvent;
import pl.edu.agh.arbeit.tracker.system.SystemHandler;
import pl.edu.agh.arbeit.tracker.system.WindowsSystemHandler;
import pl.edu.agh.arbeit.tracker.trackers.SystemTracker;

/**
 * Created by Albert on 06.04.2018.
 */

// TODO the whole logic of whether the app is running or not should be stored here
public class Application {
    private final String displayName;
    private final String programName;
    private final SystemHandler handler;
    private final SystemTracker systemTracker;

    public Application(String displayName, String programName, SystemTracker tracker) {
        this.displayName = displayName;
        this.programName = programName;
        this.handler = new WindowsSystemHandler();
        this.systemTracker = tracker;
    }

    public Application(String displayName, String programName, SystemHandler handler, SystemTracker tracker) {
        this.displayName = displayName;
        this.programName = programName;
        this.handler = handler;
        this.systemTracker = tracker;
    }

    public boolean isRunning() {
        return handler.getRunningApplications().contains(programName);
    }
    
    public ApplicationEvent getCurrentStateEvent(){
        if(isRunning()){
            if(isActive()) return new ApplicationEvent(EventType.ACTIVE, this);
            else return new ApplicationEvent(EventType.PASSIVE, this);
        } else return new ApplicationEvent(EventType.STOP, this);

    }

    public boolean isActive() {
        SystemEvent currentSystemEvent = systemTracker.currentStateEvent();
        return currentSystemEvent.getType().equals(EventType.ACTIVE) && programName.equals(handler.getFocusedApplicationName());
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getProgramName() {
        return programName;
    }


    //to determine if new systemTracker should be created
    public boolean equals(Application other) {
        return this.displayName.equals(other.displayName) && this.programName.equals(other.programName);
    }    
}




