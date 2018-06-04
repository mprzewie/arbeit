package pl.edu.agh.arbeit.data;

import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;
import pl.edu.agh.arbeit.data.repository.EventRepository;
import pl.edu.agh.arbeit.gui.controler.MainWindowController;
import pl.edu.agh.arbeit.tracker.events.*;
import pl.edu.agh.arbeit.tracker.trackers.Tracker;

/**
 * @author marcin on 4/8/18
 **/
public class EventListener {

    private EventRepository repository;
    private MainWindowController mainWindowController;

    public void subscribe(Tracker tracker){
        tracker.getBus().register(this);
    }

    public void unsubscribe(Tracker tracker){
        tracker.getBus().unregister(this);
    }

    public EventListener(EventRepository repository, MainWindowController mainWindowController) {
        this.repository = repository;
        this.mainWindowController = mainWindowController;
    }

    public EventRepository getRepository() {
        return repository;
    }

    @Subscribe
    public void acceptEvent(Event event){
        System.out.println(event);
        repository.put(event);
        Platform.runLater(() -> mainWindowController.acceptEvent(event));
    }

}


