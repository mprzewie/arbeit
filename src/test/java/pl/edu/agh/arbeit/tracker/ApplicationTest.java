package pl.edu.agh.arbeit.tracker;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import pl.edu.agh.arbeit.tracker.events.EventType;
import pl.edu.agh.arbeit.tracker.system.SystemHandler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationTest {
    @Mock
    private SystemHandler handlerMock;

    private Application activeApp;
    private Application passiveApp;
    private Application stoppedApp;


    public ApplicationTest(){
        String activeAppName = "activeApp.exe";
        String passiveAppName = "passiveApp.exe";
        String stoppedAppName = "stoppedApp.exe";
        Set<String> mockedRunningApplications = new HashSet<>(Arrays.asList(activeAppName, passiveAppName));

        MockitoAnnotations.initMocks(this);
        Mockito.when(handlerMock.getFocusedApplicationName()).thenReturn(activeAppName);
        Mockito.when(handlerMock.getRunningApplications()).thenReturn(mockedRunningApplications);

        activeApp = new Application("Active App", activeAppName, handlerMock);
        passiveApp = new Application("Passive App", passiveAppName, handlerMock);
        stoppedApp = new Application("Stopped App", stoppedAppName, handlerMock);
    }
    @Test
    void isRunningActive() {
        assertTrue(activeApp.isRunning());
    }

    @Test
    void isActiveActive() {
        assertTrue(activeApp.isActive());
    }


    @Test
    void isRunningPassive() {
        assertTrue(passiveApp.isRunning());
    }

    @Test
    void isActivePassive() {
        assertFalse(passiveApp.isActive());
    }

    @Test
    void isRunningStopped() {
        assertFalse(stoppedApp.isRunning());
    }

    @Test
    void isActiveStopped() {
        assertFalse(stoppedApp.isActive());
    }


    @Test
    void getCurrentStateEventActive() {
        assertEquals(EventType.ACTIVE, activeApp.getCurrentStateEvent().getType());
    }

    @Test
    void getCurrentStateEventPassive() {
        assertEquals(EventType.PASSIVE, passiveApp.getCurrentStateEvent().getType());
    }

    @Test
    void getCurrentStateEventStopped() {
        assertEquals(EventType.STOP, stoppedApp.getCurrentStateEvent().getType());
    }



}