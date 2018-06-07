package pl.edu.agh.arbeit.tracker.events;

import javax.print.attribute.standard.DateTimeAtCreation;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public abstract class Event {
    protected Date date = new Date();
    public abstract String getTopic();
    public abstract EventType getType();

    public Date getDate(){
        return date;
    }

    public LocalDate getLocalDate(){
        return getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public LocalDateTime getLocalDateTime(){
        return getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || o.getClass() != this.getClass()) return false;
        Event e = (Event) o;
        return e.getType() == getType() && e.getTopic().equals(getTopic());
    }

    @Override
    public String toString(){
        return "Event : " + getTopic() + " : " + getType() + " : " + getDate();
    }

}
