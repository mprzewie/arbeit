package pl.edu.agh.arbeit.tracker;


import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationTest {
    Application intelliJIdea = new Application("IntelliJ Idea", "idea64.exe");
    Application pyCharm = new Application("PyCharm", "pycharm64.exe");


    @Test
    void intelliJRunning() {
        assertTrue(intelliJIdea.isRunning());
    }

    @Test
    void pyCharmRunning() {
        assertFalse(pyCharm.isRunning());
    }
}