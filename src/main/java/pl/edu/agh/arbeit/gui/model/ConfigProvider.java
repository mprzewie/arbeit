package pl.edu.agh.arbeit.gui.model;

import java.time.Duration;
import java.util.List;

public interface ConfigProvider {
    List<ApplicationInfo> getAppsToTrack();
    Duration getSystemPingTime();
    Duration getTimeToBecomePassive();

    void addAppToTrack(ApplicationInfo app);
    void removeAppToTrack(String programName);
    void update();
    void setSystemPingTime(Duration pingTime);
    void setTimeToBecomePassive(Duration timeToBecomePassive);
}
