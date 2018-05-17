package pl.edu.agh.arbeit.gui.model;

import java.time.Duration;

public class AppInfo {
    private String name;
    private String programName;
    private long pingTimeInSeconds;

    public AppInfo() {}


    public AppInfo(String name, String programName, Duration pingTime) {
        this.name = name;
        this.programName = programName;
        this.pingTimeInSeconds = pingTime.getSeconds();
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

    public long getPingTimeInSeconds() {
        return pingTimeInSeconds;
    }

    public void setPingTimeInSeconds(long pingTimeInSeconds) {
        this.pingTimeInSeconds = pingTimeInSeconds;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "name='" + name + '\'' +
                ", programName='" + programName + '\'' +
                ", pingTimeInSeconds=" + pingTimeInSeconds +
                '}';
    }
}
