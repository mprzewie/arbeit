package pl.edu.agh.arbeit.gui.model;

import javafx.scene.paint.Color;
import pl.edu.agh.arbeit.tracker.Application;
import pl.edu.agh.arbeit.tracker.trackers.SystemTracker;

public class ApplicationInfo {
    private String displayName;
    private String programName;
    private JsonColor activeColor = new JsonColor(Color.rgb(10, 128, 4));
    private JsonColor passiveColor = new JsonColor(Color.rgb(193, 16, 9));
    private JsonColor backgroundColor = new JsonColor(Color.grayRgb(70));
    private int pingTimeInSeconds = 5;

    public ApplicationInfo() {
    }

    public ApplicationInfo(String displayName, String programName, JsonColor activeColor, JsonColor passiveColor, JsonColor backgroundColor, int pingTimeInSeconds) {
        this.displayName = displayName;
        this.programName = programName;
        this.activeColor = activeColor;
        this.passiveColor = passiveColor;
        this.backgroundColor = backgroundColor;
        this.pingTimeInSeconds = pingTimeInSeconds;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public JsonColor getActiveColor() {
        return activeColor;
    }

    public void setActiveColor(JsonColor activeColor) {
        this.activeColor = activeColor;
    }

    public JsonColor getPassiveColor() {
        return passiveColor;
    }

    public void setPassiveColor(JsonColor passiveColor) {
        this.passiveColor = passiveColor;
    }

    public JsonColor getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(JsonColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getPingTimeInSeconds() {
        return pingTimeInSeconds;
    }

    public void setPingTimeInSeconds(int pingTimeInSeconds) {
        this.pingTimeInSeconds = pingTimeInSeconds;
    }

    public Application toApplication(SystemTracker systemTracker){
        return new Application(
                this.displayName,
                this.programName,
                systemTracker,
                this.activeColor,
                this.passiveColor,
                this.backgroundColor,
                this.pingTimeInSeconds,
                this
        );
    }
}
