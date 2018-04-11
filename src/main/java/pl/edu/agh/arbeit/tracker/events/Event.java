package pl.edu.agh.arbeit.tracker.events;

import javax.print.attribute.standard.DateTimeAtCreation;
import java.util.Date;

public abstract class Event {
    protected Date date = new Date();
    public abstract String getTopic();
    public abstract EventType getType();

    public Date getDate(){
        return date;

    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || o.getClass() != this.getClass()) return false;
        Event e = (Event) o;
        return e.getType() == getType() && e.getTopic().equals(getTopic());
    }

}
