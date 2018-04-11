package pl.edu.agh.arbeit.data;

import pl.edu.agh.arbeit.tracker.events.EventType;

import java.util.Date;

public class ActivityEvent {
    private Date startdate;
    private Date possibleEndDate;
    private EventType type;

    public ActivityEvent(EventType type, Date startdate) {
        this.startdate = startdate;
        this.possibleEndDate = startdate;
        this.type = type;
    }

    public void setPossibleEndDate(Date possibleEndDate) {
        this.possibleEndDate = possibleEndDate;
    }

    public EventType getType() {
        return type;
    }
}
