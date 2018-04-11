package pl.edu.agh.arbeit.data;
import pl.edu.agh.arbeit.tracker.events.Event;

import java.util.HashMap;
import java.util.Stack;

public class DataCollector {

    private HashMap<String, Stack<ApplicationInfo>> hashMap = new HashMap<>();

    public void parseEvent(Event event) {
        if(!hashMap.containsKey(event.getTopic())) {
            Stack<ApplicationInfo> stack = new Stack<>();
            hashMap.put(event.getTopic(), stack);
        }
        try {
            modifyApplicationInfo(event);
        } catch (InvalidEventTypeException e) {
            //todo
        }
    }

    private void modifyApplicationInfo(Event event) throws InvalidEventTypeException {
        switch(event.getType()) {
            case START:
                ApplicationInfo applicationInfo = new ApplicationInfo(event.getDate());
                hashMap.get(event.getTopic()).push(applicationInfo);
                break;
            case STOP:
                if(hashMap.get(event.getTopic()).empty()) throw new InvalidEventTypeException(event);
                hashMap.get(event.getTopic()).peek().setEnddate(event.getDate());
                break;
            case ACTIVE:
            case PASSIVE:
                if(hashMap.get(event.getTopic()).empty()) throw new InvalidEventTypeException(event);
                hashMap.get(event.getTopic()).peek().handleActivityEvent(event.getType(), event.getDate());
                break;
        }
    }

}
