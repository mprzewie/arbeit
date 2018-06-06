package pl.edu.agh.arbeit.tracker.events;

import java.util.Date;


public class CustomEvent extends Event{
    private String topic;
    private EventType type;
    private Date date;

    public CustomEvent(String topic, EventType type, Date date) {
        this.topic = topic;
        this.type = type;
        this.date = date;
    }

    @Override
    public String getTopic() {
        return this.topic;
    }

    @Override
    public EventType getType() {
        return this.type;
    }

    @Override
    public Date getDate() {
        return this.date;
    }
}
