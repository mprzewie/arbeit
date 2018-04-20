package pl.edu.agh.arbeit.tracker;


import org.junit.jupiter.api.*;
import pl.edu.agh.arbeit.tracker.events.EventType;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationTest {
    Application intelliJIdea = new Application("IntelliJ Idea", "idea64.exe");
    Application pyCharm = new Application("PyCharm", "pycharm64.exe");

    @Test
    void intelliJRunning() {
        assertTrue(intelliJIdea.isRunning());
    }

    @Test
    void intelliJActive() {
        assertTrue(intelliJIdea.isActive());
    }

    @Test
    void pyCharmRunning() {
        assertFalse(pyCharm.isRunning());
    }

    @Test
    void getCurrentStateEvent() {
        MockedApplication app = new MockedApplication(true,true);
        assertEquals(EventType.ACTIVE, app.getCurrentStateEvent().getType());
        app.setIsActive(false);
        assertEquals(EventType.PASSIVE,app.getCurrentStateEvent().getType());
        app.setIsRunning(false);
        assertEquals(EventType.STOP, app.getCurrentStateEvent().getType());
    }

    private class MockedApplication extends Application{
        private boolean isRunning;
        private boolean isActive;

        public MockedApplication(boolean isRunning, boolean isActive) {
            super("mockedName", "mockedProgramName");
            this.isRunning = isRunning;
            this.isActive = isActive;
        }

        @Override
        public boolean isRunning(){return isRunning;}

        @Override
        public boolean isActive(){return isActive;}

        private void setIsRunning(boolean running) {
            isRunning = running;
        }

        private void setIsActive(boolean active) {
            isActive = active;
        }
    }
}