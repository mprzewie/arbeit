package pl.edu.agh.arbeit.tracker.trackers;

import com.google.common.eventbus.EventBus;

public interface Tracker {

    EventBus getBus();
    void start();
    void stop();


}
