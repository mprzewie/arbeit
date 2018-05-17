package pl.edu.agh.arbeit.gui.model;
import java.util.LinkedList;
import java.util.List;

public class Info {
    private Long systemPingTime;
    private List<AppInfo> appsToTrack;

    public Info(){
        this.systemPingTime = 5L;
        this.appsToTrack = new LinkedList<>();
    }

    public Info(Long systemPingTime, List<AppInfo> appsToTrack) {
        this.systemPingTime = systemPingTime;
        this.appsToTrack = appsToTrack;
    }

    public Long getSystemPingTime() {
        return systemPingTime;
    }

    public void setSystemPingTime(Long systemPingTime) {
        this.systemPingTime = systemPingTime;
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
                "systemPingTime=" + systemPingTime +
                ", appsToTrack=" + appsToTrack +
                '}';
    }
}
