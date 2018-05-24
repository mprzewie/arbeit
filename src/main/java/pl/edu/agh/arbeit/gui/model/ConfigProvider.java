package pl.edu.agh.arbeit.gui.model;

import java.time.Duration;
import java.util.List;

public interface ConfigProvider {
    List<AppInfo> getAppsToTrack();
    Duration getSystemPingTime();
    Duration getTimeToBecomePassive();

    void addAppToTrack(AppInfo appInfo);
    void removeAppToTrack(String programName);
    void setSystemPingTime(Duration pingTime);
    void setTimeToBecomePassive(Duration timeToBecomePassive);
}
