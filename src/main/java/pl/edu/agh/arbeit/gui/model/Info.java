package pl.edu.agh.arbeit.gui.model;
import java.util.LinkedList;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Info {
    private long systemPingTimeInSeconds=5;
    private long timeToBecomePassiveInSeconds=900;
    private List<AppInfo> appsToTrack=new ArrayList<>();

    public Info(){
        this.systemPingTimeInSeconds = 5L;
        this.appsToTrack = new LinkedList<>();
        this.timeToBecomePassiveInSeconds = 900;
    }

    public Info(long systemPingTime, List<AppInfo> appsToTrack) {
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

    public List<AppInfo> getAppsToTrack() {
        return appsToTrack;
    }

    public void setAppsToTrack(List<AppInfo> appsToTrack) {
        this.appsToTrack = appsToTrack;
    }

    public void addAppToTrack(AppInfo appInfo){
        this.appsToTrack.add(appInfo);
    }

    public void removeAppToTrack(String programName){
        this.appsToTrack.removeIf(e->e.getProgramName().equals(programName));
    }

    @Override
    public String toString() {
        return "Info{" +
                "systemPingTimeInSeconds=" + systemPingTimeInSeconds +
                ", appsToTrack=" + appsToTrack +
                '}';
    }
}
