package pl.edu.agh.arbeit.data;

import com.google.common.eventbus.EventBus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import pl.edu.agh.arbeit.data.repository.EventRepository;
import pl.edu.agh.arbeit.gui.controler.MainWindowController;
import pl.edu.agh.arbeit.tracker.Application;
import pl.edu.agh.arbeit.tracker.events.ApplicationEvent;
import pl.edu.agh.arbeit.tracker.events.Event;
import org.mockito.MockitoAnnotations;
import pl.edu.agh.arbeit.tracker.events.EventType;
import pl.edu.agh.arbeit.tracker.trackers.Tracker;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;

class EventListenerTest {

    private EventBus bus = new EventBus();
    private List<Event> events;
    private EventListener listener;

    @Mock
    private EventRepository repositoryMock;

    @Mock
    private Tracker trackerMock;

    @Mock
    private Application applicationMock;

    @Mock
    private MainWindowController mainWindowController;

    private Event eventMock;

    EventListenerTest(){
        MockitoAnnotations.initMocks(this);

        Mockito.doAnswer(invocation -> {
            Event event = invocation.getArgumentAt(0, Event.class);
            events.add(event);
            return null;
        }).when(repositoryMock).put(any(Event.class));

        Mockito.doAnswer(invocation -> {
            bus.post(eventMock);
            return null;
        }).when(trackerMock).start();

        Mockito.when(trackerMock.getBus()).thenReturn(bus);
        Mockito.when(applicationMock.getName()).thenReturn("application");
        eventMock = new ApplicationEvent(EventType.ACTIVE, applicationMock);

    }

    @BeforeEach
    void setup(){
        listener = new EventListener(repositoryMock, mainWindowController);
        events = new LinkedList<>();
    }


    @Test
    void subscribedTest(){
        listener.subscribe(trackerMock);
        trackerMock.start();
        assertTrue(events.contains(eventMock));
    }

    @Test
    void unsubscribedTest(){
        bus.post(eventMock);
        assertTrue(events.isEmpty());
    }


}