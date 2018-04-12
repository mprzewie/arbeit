package pl.edu.agh.arbeit.data;

import pl.edu.agh.arbeit.tracker.events.EventType;

import java.util.Date;
import java.util.Stack;

public class ApplicationInfo {
    private Date startdate;
    private Date enddate = null;
    private Stack<ActivityEvent> activityEventStack = new Stack<ActivityEvent>();

    public ApplicationInfo(Date startdate) {

        this.startdate = startdate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public void handleActivityEvent(EventType type, Date date) {
        if(activityEventStack.empty()) {
            activityEventStack.push(new ActivityEvent(type, date));
        } else if(!type.equals(activityEventStack.peek().getType())) {
            activityEventStack.push(new ActivityEvent(type, date));
        } else if(type.equals(activityEventStack.peek().getType())){
            activityEventStack.peek().setPossibleEndDate(date);
        }
        //in case of crash set possible endDate of application to last receive event
        this.enddate = date;
    }

    public Date getStartdate() {
        return startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public Stack<ActivityEvent> getActivityEventStack() {
        return activityEventStack;
    }
}
