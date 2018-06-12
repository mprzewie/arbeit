package pl.edu.agh.arbeit.gui.controler;

import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;
import pl.edu.agh.arbeit.data.EventListener;
import pl.edu.agh.arbeit.tracker.events.Event;
import pl.edu.agh.arbeit.tracker.trackers.Tracker;

public class EventListenerDrawer extends EventListener {
    private MainWindowController mainWindowController;

    public EventListenerDrawer(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

    public void subscribe(Tracker tracker){
        tracker.getBus().register(this);
    }

    public void unsubscribe(Tracker tracker){
        tracker.getBus().unregister(this);
    }


    @Subscribe
    public void acceptEvent(Event event){
        Platform.runLater(() -> mainWindowController.acceptEvent(event));
    }
}
