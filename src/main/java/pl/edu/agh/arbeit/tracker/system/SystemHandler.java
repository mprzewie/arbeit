package pl.edu.agh.arbeit.tracker.system;

import pl.edu.agh.arbeit.tracker.events.SystemEvent;

import java.util.Set;

public interface SystemHandler {

    Set<String> getRunningApplications();
    String getFocusedApplicationName();
}
