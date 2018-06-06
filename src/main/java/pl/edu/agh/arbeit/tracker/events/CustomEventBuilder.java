package pl.edu.agh.arbeit.tracker.events;

import java.util.Date;

public final class CustomEventBuilder {
    private String topic;
    private EventType type;
    private Date date;

    private CustomEventBuilder() {
    }

    public static CustomEventBuilder aCustomEvent() {
        return new CustomEventBuilder();
    }

    public CustomEventBuilder withTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public CustomEventBuilder withType(EventType type) {
        this.type = type;
        return this;
    }

    public CustomEventBuilder withDate(Date date) {
        this.date = date;
        return this;
    }

    public CustomEvent build() {
        return new CustomEvent(topic, type, date);
    }
}
