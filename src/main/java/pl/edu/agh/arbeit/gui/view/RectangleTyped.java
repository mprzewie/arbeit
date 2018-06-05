package pl.edu.agh.arbeit.gui.view;

import javafx.scene.shape.Rectangle;
import pl.edu.agh.arbeit.tracker.events.EventType;

public class RectangleTyped extends Rectangle {
    private EventType eventType;

    public RectangleTyped(int i, int i1) {
        super(i,i1);
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

}