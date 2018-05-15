package pl.edu.agh.arbeit.gui.model;

import java.time.Duration;

public class AppInfo {
    private String name;
    private String programName;
    private Duration pingTime;

    public AppInfo() {}


    public AppInfo(String name, String programName, Duration pingTime) {
        this.name = name;
        this.programName = programName;
        this.pingTime = pingTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public Duration getPingTime() {
        return pingTime;
    }

    public void setPingTimeInSeconds(long pingTimeInSeconds) {
        this.pingTime = Duration.ofSeconds(pingTimeInSeconds);
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "name='" + name + '\'' +
                ", programName='" + programName + '\'' +
                ", pingTime=" + pingTime +
                '}';
    }
}
