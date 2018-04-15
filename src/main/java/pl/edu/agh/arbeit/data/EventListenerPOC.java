package pl.edu.agh.arbeit.data;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import pl.edu.agh.arbeit.tracker.Application;
import pl.edu.agh.arbeit.tracker.trackers.ApplicationTracker;
import pl.edu.agh.arbeit.tracker.trackers.SystemTracker;
import pl.edu.agh.arbeit.tracker.events.*;
import pl.edu.agh.arbeit.tracker.trackers.Tracker;

/**
 * @author marcin on 4/8/18
 **/
public class EventListenerPOC {

    public void subscribe(Tracker tracker){
        tracker.getBus().register(this);
    }

    public void unsubscribe(Tracker tracker){
        tracker.getBus().unregister(this);
    }

    @Subscribe
    public void event(Event event){
        System.out.println(event.getTopic() + " " + event.getDate() + " " + event.getType());
    }



    public static void main(String[] args) throws InterruptedException {

        Application pyCharm = new Application("PyCharm", "pycharm64.exe");

        Tracker pyCharmTracker = new ApplicationTracker(5, pyCharm);
        Tracker systemTracker = new SystemTracker(10);
        EventListenerPOC poc = new EventListenerPOC();

        poc.subscribe(systemTracker);
        poc.subscribe(pyCharmTracker);

        systemTracker.start();
        pyCharmTracker.start();

        Thread.sleep(120 * 1000);

        systemTracker.stop();
        pyCharmTracker.stop();


    }
}


