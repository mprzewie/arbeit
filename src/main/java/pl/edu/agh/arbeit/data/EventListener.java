package pl.edu.agh.arbeit.data;

import com.google.common.eventbus.Subscribe;
import pl.edu.agh.arbeit.data.repository.EventRepository;
import pl.edu.agh.arbeit.tracker.events.*;
import pl.edu.agh.arbeit.tracker.trackers.Tracker;

/**
 * @author marcin on 4/8/18
 **/
public class EventListener {



    private EventRepository repository;

    public void subscribe(Tracker tracker){
        tracker.getBus().register(this);
    }

    public void unsubscribe(Tracker tracker){
        tracker.getBus().unregister(this);
    }

    public EventListener(EventRepository repository) {
        this.repository = repository;
    }

    public EventRepository getRepository() {
        return repository;
    }

    @Subscribe
    public void acceptEvent(Event event){
        System.out.println(event);
        repository.put(event);
    }

}


