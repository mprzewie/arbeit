package pl.edu.agh.arbeit.gui.model;

import java.util.List;

public interface ConfigProvider {
    List<AppInfo> getAppsToTrack();
    Long getSystemPingTime();
    void addAppToTrack(AppInfo appInfo);
    void removeAppToTrack(String programName);
    void setSystemPingTime(Long pingTime);
}
