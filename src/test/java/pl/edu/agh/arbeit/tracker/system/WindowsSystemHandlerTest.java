package pl.edu.agh.arbeit.tracker.system;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WindowsSystemHandlerTest {

    WindowsSystemHandler handler = new WindowsSystemHandler();

    @Test
    void getRunningApplications() {
        assertTrue(handler.getRunningApplications().contains("idea64.exe"));
    }

    @Test
    void getFocusedApplicationName() {
        assertEquals("idea64.exe", handler.getFocusedApplicationName());
    }
}