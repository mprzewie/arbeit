package pl.edu.agh.arbeit.tracker.events;


import pl.edu.agh.arbeit.tracker.Application;

import java.util.Date;

public class ApplicationEvent extends Event {

    private final Application application;
    private final EventType type;

    public ApplicationEvent(EventType type, Application application) {
        this.application = application;
        this.type = type;
    }

    public ApplicationEvent(EventType type, Application application, Date date) {
        this.application = application;
        this.type = type;
        this.date = date;
    }


    @Override
    public String getTopic() {
        return application.getProgramName();
    }

    @Override
    public EventType getType() {
        return type;
    }
}
