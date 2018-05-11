package pl.edu.agh.arbeit.gui.model;
import java.util.List;

public class Info {
    private Long systemPingTime;
    private int secondsToBecomePassive;
    private List<AppInfo> appsToTrack;

    public Info(){
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

    public int getSecondsToBecomePassive() {
        return this.secondsToBecomePassive;
    }

    public void setSecondsToBecomePassive(int secondsToBecomePassive) {
        this.secondsToBecomePassive=secondsToBecomePassive;
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
