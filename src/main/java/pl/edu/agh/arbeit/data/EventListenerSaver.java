package pl.edu.agh.arbeit.data;

import com.google.common.eventbus.Subscribe;
import pl.edu.agh.arbeit.data.repository.EventRepository;
import pl.edu.agh.arbeit.tracker.events.*;

/**
 * @author marcin on 4/8/18
 **/
public class EventListenerSaver extends EventListener {

    private EventRepository repository;


    public EventListenerSaver(EventRepository repository) {
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


