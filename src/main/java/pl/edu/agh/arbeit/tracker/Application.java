package pl.edu.agh.arbeit.tracker;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.scene.paint.Color;
import pl.edu.agh.arbeit.gui.model.ApplicationInfo;
import pl.edu.agh.arbeit.gui.model.JsonColor;
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
    private String displayName;
    private String programName;
    private SystemHandler handler = new WindowsSystemHandler();
    private SystemTracker systemTracker;
    private JsonColor activeColor = new JsonColor(Color.rgb(10, 128, 4));
    private JsonColor passiveColor = new JsonColor(Color.rgb(193, 16, 9));
    private JsonColor backgroundColor = new JsonColor(Color.grayRgb(70));
    private int pingTimeInSeconds = 5;
    private ApplicationInfo applicationInfo;

    public Application() {
    }

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

    public Application(String displayName, String programName, SystemTracker systemTracker, JsonColor activeColor, JsonColor passiveColor, JsonColor backgroundColor, int pingTimeInSeconds, ApplicationInfo applicationInfo) {
        this.displayName = displayName;
        this.programName = programName;
        this.handler = new WindowsSystemHandler();
        this.systemTracker = systemTracker;
        this.activeColor = activeColor;
        this.passiveColor = passiveColor;
        this.backgroundColor = backgroundColor;
        this.pingTimeInSeconds = pingTimeInSeconds;
        this.applicationInfo = applicationInfo;
    }

    @JsonIgnore
    public boolean isRunning() {
        return handler.getRunningApplications().contains(programName);
    }

    @JsonIgnore
    public ApplicationEvent getCurrentStateEvent(){
        if(isRunning()){
            if(isActive()) return new ApplicationEvent(EventType.ACTIVE, this);
            else return new ApplicationEvent(EventType.PASSIVE, this);
        } else return new ApplicationEvent(EventType.STOP, this);

    }

    @JsonIgnore
    public boolean isActive() {
        if(systemTracker != null) {
            SystemEvent currentSystemEvent = systemTracker.currentStateEvent();
            return currentSystemEvent.getType().equals(EventType.ACTIVE) && programName.equals(handler.getFocusedApplicationName());
        }
        return false;
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

    public int getPingTimeInSeconds() {
        return pingTimeInSeconds;
    }

    public void setPingTimeInSeconds(int pingTimeInSeconds) {
        if(applicationInfo != null)
            this.applicationInfo.setPingTimeInSeconds(pingTimeInSeconds);
        this.pingTimeInSeconds = pingTimeInSeconds;
    }

    public JsonColor getActiveColor() {
        return activeColor;
    }

    public void setActiveColor(JsonColor activeColor) {
        if(applicationInfo != null)
            this.applicationInfo.setActiveColor(activeColor);
        this.activeColor = activeColor;
    }

    public JsonColor getPassiveColor() {
        return passiveColor;
    }

    public void setPassiveColor(JsonColor passiveColor) {
        if(applicationInfo != null)
            this.applicationInfo.setPassiveColor(passiveColor);
        this.passiveColor = passiveColor;
    }

    public JsonColor getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(JsonColor backgroundColor) {
        if(applicationInfo != null)
            this.applicationInfo.setBackgroundColor(backgroundColor);
        this.backgroundColor = backgroundColor;
    }

    public void setDisplayName(String displayName) {
        if(applicationInfo != null)
            this.applicationInfo.setDisplayName(displayName);
        this.displayName = displayName;
    }

    public void setProgramName(String programName) {
        if(applicationInfo != null)
            this.applicationInfo.setProgramName(programName);
        this.programName = programName;
    }

    public ApplicationInfo getApplicationInfo() {
        if(applicationInfo == null)
            applicationInfo = new ApplicationInfo(
                    getDisplayName(),
                    getProgramName(),
                    getActiveColor(),
                    getPassiveColor(),
                    getBackgroundColor(),
                    getPingTimeInSeconds());
        return applicationInfo;
    }
}




