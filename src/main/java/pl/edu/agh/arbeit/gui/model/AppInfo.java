package pl.edu.agh.arbeit.gui.model;

public class AppInfo {
    private String name;
    private String programName;
    private Long pingTime;

    public AppInfo(){

    }

    public AppInfo(String name, String programName, Long pingTime) {
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

    public Long getPingTime() {
        return pingTime;
    }

    public void setPingTime(Long pingTime) {
        this.pingTime = pingTime;
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
