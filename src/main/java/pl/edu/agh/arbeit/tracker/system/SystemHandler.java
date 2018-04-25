package pl.edu.agh.arbeit.tracker.system;

import java.util.Set;

public interface SystemHandler {

    Set<String> getRunningApplications();
    String getFocusedApplicationName();
}
