package pl.edu.agh.arbeit.data;
import pl.edu.agh.arbeit.data.repository.EventRepository;
import pl.edu.agh.arbeit.tracker.Application;
import pl.edu.agh.arbeit.tracker.events.ApplicationEvent;
import pl.edu.agh.arbeit.tracker.events.Event;

import java.io.*;
import java.util.*;

public class DataCollector {

    private HashMap<String, Stack<ApplicationInfo>> hashMap = new HashMap<>();

    private EventRepository eventRepository = new EventRepository();

    public void parseEvent(Event event) {
        this.eventRepository.insertEvent(event);
        System.out.println(this.eventRepository.getAllEvents().toString());

        if(!hashMap.containsKey(event.getTopic())) {
            Stack<ApplicationInfo> stack = new Stack<>();
            hashMap.put(event.getTopic(), stack);
        }
        try {
            modifyApplicationInfo(event);
//            System.out.println("Will be writing CSV");
//            generateCSVFile(getCSVFromHashMap());
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
                // push new applicationInfo to stack and set startDate on it
                applicationInfo = new ApplicationInfo(event.getDate());
                hashMap.get(event.getTopic()).push(applicationInfo);
                break;
            case STOP:
                // set enddate of applicationInfo, mark it as closed, if there is activity end date set do this as event date
                if(hashMap.get(event.getTopic()).empty())
                    throw new InvalidEventTypeException(event);
                synchronized (hashMap) {
                    hashMap.get(event.getTopic()).peek().setEnddate(event.getDate());
                    hashMap.get(event.getTopic()).peek().setClosed(true);
                }
                break;
            case ACTIVE:
                /* if there is nothing on this application's stack or the peeked element is closed
                *     insert new application info to this application's stack
                *     set it's start date
                *     insert new ActivityEvent to newly created ApplicationInfo's stack
                *     set startdate on it
                *  else
                *     insert new ActivityEvent to the stack of the ApplicationInfo's event stack
                *     set startdate of it
                * */
                if (hashMap.get(event.getTopic()).empty() || hashMap.get(event.getTopic()).peek().isClosed()) {
                    applicationInfo = new ApplicationInfo(event.getDate());
                    ActivityEvent newEvent = new ActivityEvent(event.getType(), event.getDate());
                    applicationInfo.getActivityEventStack().push(newEvent);
                    hashMap.get(event.getTopic()).push(applicationInfo);
                } else {
                    ActivityEvent newEvent = new ActivityEvent(event.getType(), event.getDate());
                    hashMap.get(event.getTopic()).peek().getActivityEventStack().push(newEvent);
                }
                /*ActivityEvent activityEvent = new ActivityEvent(event.getType(), event.getDate());
                if (hashMap.get(event.getTopic()).empty()) {
                    hashMap.get(event.getTopic()).push(new ApplicationInfo(event.getDate()));
                }
                hashMap.get(event.getTopic()).peek().getActivityEventStack().push(activityEvent);*/
                break;
            case PASSIVE:
                /* if there is nothing on this application's stack or the peeked element is closed
                 *     insert new application info to this application's stack
                 *     set it's start date
                 *     insert new ActivityEvent to newly created ApplicationInfo's stack
                 *     set startdate and enddate on it
                 *  else
                 *     set enddate of the top of event stack of the top of Info stack
                 * */

                if (hashMap.get(event.getTopic()).empty() || hashMap.get(event.getTopic()).peek().isClosed()) {
                    applicationInfo = new ApplicationInfo(event.getDate());
                    ActivityEvent newEvent = new ActivityEvent(event.getType(), event.getDate());
                    newEvent.setPossibleEndDate(event.getDate());
                    applicationInfo.getActivityEventStack().push(newEvent);
                    hashMap.get(event.getTopic()).push(applicationInfo);
                } else {
                    hashMap.get(event.getTopic()).peek().getActivityEventStack().peek().setPossibleEndDate(event.getDate());
                }
                /*if(hashMap.get(event.getTopic()).empty()) throw new InvalidEventTypeException(event);
                hashMap.get(event.getTopic()).peek().handleActivityEvent(event.getType(), event.getDate());*/
                break;
        }
    }

    public String getCSVFromHashMap() throws Exception {
//        System.out.println("Started CSV");

        Set<String> keys = hashMap.keySet();

        StringBuilder builder = new StringBuilder();

        builder.append(
                "Nazwa programu" + "," +
                        "Data uruchomienia programu" + "," +
                        "Data zakończenia programu" + "," +
                        "Data rozpoczęcia aktywności" + "," +
                        "Data zakończenia aktywności" + ","
        );
        builder.append(System.lineSeparator());

        for (String key :
                keys) {
//            System.out.println("Keys: " + keys);
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

        String result = builder.toString();
        System.out.println(result);

        return result;
    }

    public void generateCSVFile(String sourceCSV) {
        BufferedWriter out = null;
        try {
            Random rand = new Random();
            out = new BufferedWriter(new FileWriter("test" + rand.nextInt() + ".txt"));
            out.write(sourceCSV);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
