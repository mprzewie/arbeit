package pl.edu.agh.arbeit.data;
import pl.edu.agh.arbeit.tracker.Application;
import pl.edu.agh.arbeit.tracker.events.ApplicationEvent;
import pl.edu.agh.arbeit.tracker.events.Event;

import java.io.StringWriter;
import java.util.*;

public class DataCollector {

    private HashMap<String, Stack<ApplicationInfo>> hashMap = new HashMap<>();

    public void parseEvent(Event event) {
        if(!hashMap.containsKey(event.getTopic())) {
            Stack<ApplicationInfo> stack = new Stack<>();
            hashMap.put(event.getTopic(), stack);
        }
        try {
            modifyApplicationInfo(event);
            System.out.println("Will be writing CSV");
            writeHashMapToCsv();
        } catch (InvalidEventTypeException e) {
            //todo
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void modifyApplicationInfo(Event event) throws InvalidEventTypeException {
        ApplicationInfo applicationInfo;
        switch(event.getType()) {
            case START:
                applicationInfo = new ApplicationInfo(event.getDate());
                hashMap.get(event.getTopic()).push(applicationInfo);
                break;
            case STOP:
                if(hashMap.get(event.getTopic()).empty()) throw new InvalidEventTypeException(event);
                hashMap.get(event.getTopic()).peek().setEnddate(event.getDate());
                break;
            case ACTIVE:
                ActivityEvent activityEvent = new ActivityEvent(event.getType(), event.getDate());
                if (hashMap.get(event.getTopic()).empty()) {
                    hashMap.get(event.getTopic()).push(new ApplicationInfo(event.getDate()));
                }
                hashMap.get(event.getTopic()).peek().getActivityEventStack().push(activityEvent);
                break;
            case PASSIVE:
                if(hashMap.get(event.getTopic()).empty()) throw new InvalidEventTypeException(event);
                hashMap.get(event.getTopic()).peek().handleActivityEvent(event.getType(), event.getDate());
                break;
        }
    }

    public void writeHashMapToCsv() throws Exception {
        System.out.println("Started CSV");

        Set<String> keys = hashMap.keySet();

        StringBuilder builder = new StringBuilder();

        for (String key :
                keys) {
            System.out.println("Keys: " + keys);
            for (ApplicationInfo tmpInfo :
                    hashMap.get(key)) {
                builder.append(key + "," +
                        tmpInfo.getStartdate() + "," +
                        tmpInfo.getEnddate() +
                        System.lineSeparator());
                for (ActivityEvent tmpEvent :
                        tmpInfo.getActivityEventStack()) {
                    builder.append(key + "," +
                            tmpInfo.getStartdate() + "," +
                            tmpInfo.getEnddate() + "," +
                            tmpEvent.getStartdate() + "," +
                            tmpEvent.getPossibleEndDate() +
                            System.lineSeparator());
                }
            }
        }


        System.out.println(builder.toString());

        System.out.println("Finished CSV");
    }

}
