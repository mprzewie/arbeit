package pl.edu.agh.arbeit.gui.model;

import pl.edu.agh.arbeit.tracker.Application;

import java.util.HashMap;
import java.util.Map;

public class AppConfig implements ConfigProvider{

    public AppConfig() {
    }

    @Override
    public Map<Application, Long> getAppsToTrack() {
        Map<Application, Long>  apps = new HashMap<>();
        apps.put(new Application("Chrome","chrome.exe"), (long) 5);
        return apps;

    }

    @Override
    public Long getSystemPingTime() {
        return Long.valueOf(10);
    }
}
