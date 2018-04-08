package pl.edu.agh.arbeit.tracker.events;

import javax.print.attribute.standard.DateTimeAtCreation;
import java.util.Date;

public abstract class Event {
    public abstract String getTopic();
    public abstract EventType getType();

    public Date getDate(){
        return new Date();

    }

}
