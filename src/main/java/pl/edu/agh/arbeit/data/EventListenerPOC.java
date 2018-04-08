package pl.edu.agh.arbeit.data;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import pl.edu.agh.arbeit.tracker.trackers.SystemTracker;
import pl.edu.agh.arbeit.tracker.events.*;

/**
 * @author marcin on 4/8/18
 **/
public class EventListenerPOC {

    private final EventBus bus;

    public EventListenerPOC(EventBus bus) {
        this.bus = bus;
        this.bus.register(this);
    }

    @Subscribe
    public void systemEvent(SystemEvent event){
        System.out.println(event.getDate() + " " + event.getType());
    }

    public static void main(String[] args) throws InterruptedException {

        EventBus bus = new EventBus();
        EventListenerPOC poc = new EventListenerPOC(bus);

        Thread t = new Thread(new SystemTracker(bus));
        t.start();

        Thread.sleep(10 * 1000);

        t.interrupt();
    }
}


