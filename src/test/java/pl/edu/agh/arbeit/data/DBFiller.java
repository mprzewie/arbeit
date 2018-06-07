package pl.edu.agh.arbeit.data;

import pl.edu.agh.arbeit.data.repository.DatabaseEventRepository;
import pl.edu.agh.arbeit.data.repository.EventRepository;
import pl.edu.agh.arbeit.tracker.Application;
import pl.edu.agh.arbeit.tracker.events.ApplicationEvent;
import pl.edu.agh.arbeit.tracker.events.Event;
import pl.edu.agh.arbeit.tracker.events.EventType;
import pl.edu.agh.arbeit.tracker.trackers.SystemTracker;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DBFiller {
    public static void main(String[] args) {
        EventRepository repository = DatabaseEventRepository.initializeDBOrConnectToExisting();
        SystemTracker tracker = new SystemTracker(Duration.ofSeconds(1), Duration.ofSeconds(1));
        Application application = new Application("chrome.exe", "chrome.exe", tracker);
        LocalDateTime localDate1 = LocalDateTime.of(2018, 6, 1, 16, 0);
        LocalDateTime localDate2 = LocalDateTime.of(2018, 6, 1, 16, 1);
        LocalDateTime localDate3 = LocalDateTime.of(2018, 6, 1, 16, 15);

        Date date1 = Date.from(localDate1.atZone(ZoneId.systemDefault()).toInstant());
        Date date2 = Date.from(localDate2.atZone(ZoneId.systemDefault()).toInstant());
        Date date3 = Date.from(localDate3.atZone(ZoneId.systemDefault()).toInstant());

        Event event1 = new Event(){

            @Override
            public String getTopic() {
                return application.getProgramName();
            }

            @Override
            public EventType getType() {
                return EventType.START;
            }

            @Override
            public Date getDate() {
                return date1;
            }
        };

        Event event2 = new Event(){

            @Override
            public String getTopic() {
                return application.getProgramName();
            }

            @Override
            public EventType getType() {
                return EventType.ACTIVE;
            }

            @Override
            public Date getDate() {
                return date2;
            }
        };

        Event event3 = new Event(){

            @Override
            public String getTopic() {
                return application.getProgramName();
            }

            @Override
            public EventType getType() {
                return EventType.STOP;
            }

            @Override
            public Date getDate() {
                return date3;
            }
        };
        System.out.println(event1);
        repository.put(event1);
        repository.put(event2);
        repository.put(event3);
        System.out.println("put");

    }
}
