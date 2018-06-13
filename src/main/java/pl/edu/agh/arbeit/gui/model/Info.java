package pl.edu.agh.arbeit.gui.model;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Info {
    private long systemPingTimeInSeconds=5;
    private JsonColor systemActiveColor = new JsonColor(Color.rgb(10, 128, 4));
    private JsonColor systemPassiveColor = new JsonColor(Color.rgb(193, 16, 9));
    private JsonColor systemBackgroundColor = new JsonColor(Color.grayRgb(70));
    private long timeToBecomePassiveInSeconds=900;
    private List<ApplicationInfo> appsToTrack=new ArrayList<>();

    public Info(){
        this.systemPingTimeInSeconds = 5L;
        this.appsToTrack = new LinkedList<>();
        this.timeToBecomePassiveInSeconds = 900;
    }

    public Info(long systemPingTime, List<ApplicationInfo> appsToTrack) {
        this.systemPingTimeInSeconds = systemPingTime;
        this.appsToTrack = appsToTrack;
    }

    public long getSystemPingTimeInSeconds() {
        return systemPingTimeInSeconds;
    }

    public void setSystemPingTimeInSeconds(long systemPingTimeInSeconds) {
        this.systemPingTimeInSeconds = systemPingTimeInSeconds;
    }

    public long getTimeToBecomePassiveInSeconds() {
        return timeToBecomePassiveInSeconds;
    }

    public void setTimeToBecomePassiveInSeconds(long timeToBecomePassiveInSeconds) {
        this.timeToBecomePassiveInSeconds = timeToBecomePassiveInSeconds;
    }

    public List<ApplicationInfo> getAppsToTrack() {
        return appsToTrack;
    }

    public void setAppsToTrack(List<ApplicationInfo> appsToTrack) {
        this.appsToTrack = appsToTrack;
    }

    public void addAppToTrack(ApplicationInfo app){
        this.appsToTrack.add(app);
    }

    public void removeAppToTrack(String programName){
        this.appsToTrack.removeIf(e->e.getProgramName().equals(programName));
    }

    public JsonColor getSystemActiveColor() {
        return systemActiveColor;
    }

    public void setSystemActiveColor(JsonColor systemActiveColor) {
        this.systemActiveColor = systemActiveColor;
    }

    public JsonColor getSystemPassiveColor() {
        return systemPassiveColor;
    }

    public void setSystemPassiveColor(JsonColor systemPassiveColor) {
        this.systemPassiveColor = systemPassiveColor;
    }

    public JsonColor getSystemBackgroundColor() {
        return systemBackgroundColor;
    }

    public void setSystemBackgroundColor(JsonColor systemBackgroundColor) {
        this.systemBackgroundColor = systemBackgroundColor;
    }
}
