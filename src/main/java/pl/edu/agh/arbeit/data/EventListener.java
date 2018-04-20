package pl.edu.agh.arbeit.data;

import com.google.common.eventbus.Subscribe;
import pl.edu.agh.arbeit.tracker.Application;
import pl.edu.agh.arbeit.tracker.trackers.ApplicationTracker;
import pl.edu.agh.arbeit.tracker.trackers.SystemTracker;
import pl.edu.agh.arbeit.tracker.events.*;
import pl.edu.agh.arbeit.tracker.trackers.Tracker;

/**
 * @author marcin on 4/8/18
 **/
public class EventListener {

    private DataCollector dataCollector;

    public void subscribe(Tracker tracker){
        tracker.getBus().register(this);
    }

    public void unsubscribe(Tracker tracker){
        tracker.getBus().unregister(this);
    }

    public EventListener() {
        dataCollector = new DataCollector();
    }

    @Subscribe
    public void event(Event event){
        System.out.println(event.getTopic() + " " + event.getDate() + " " + event.getType());
        dataCollector.parseEvent(event);
    }

    public DataCollector getDataCollector() {
        return dataCollector;
    }
}


