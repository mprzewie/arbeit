package pl.edu.agh.arbeit.gui.model;

import pl.edu.agh.arbeit.tracker.Application;

import java.util.Map;

public interface ConfigProvider {
    Map<Application, Long> getAppsToTrack();
    Long getSystemPingTime();
}
