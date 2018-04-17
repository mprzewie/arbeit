package pl.edu.agh.arbeit.gui.model;

public class FakeTracker implements Tracker {
    @Override
    public void track(String appName) {
        System.out.println(appName);
    }
}
