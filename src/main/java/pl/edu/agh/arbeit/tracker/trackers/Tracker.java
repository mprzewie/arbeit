package pl.edu.agh.arbeit.tracker.trackers;

import com.google.common.eventbus.EventBus;

public interface Tracker extends Runnable{

    EventBus getBus();
    Long getPingTime();


}
