package pl.edu.agh.arbeit.gui.model;
import java.time.Duration;
import java.util.List;

public class Info {
    private long systemPingTimeInSeconds;
    private long timeToBecomePassiveInSeconds;
    private List<AppInfo> appsToTrack;

    public Info(){
    }

    public Info(long systemPingTime, List<AppInfo> appsToTrack) {
        this.systemPingTimeInSeconds = systemPingTime;
        this.appsToTrack = appsToTrack;
    }

    public Duration getSystemPingTimeInSeconds() {
        return Duration.ofSeconds(systemPingTimeInSeconds);
    }

    public void setSystemPingTimeInSeconds(long systemPingTimeInSeconds) {
        this.systemPingTimeInSeconds = systemPingTimeInSeconds;
    }

    public Duration getTimeToBecomePassiveInSeconds() {
        return Duration.ofSeconds(timeToBecomePassiveInSeconds);
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
