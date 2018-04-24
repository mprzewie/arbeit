package pl.edu.agh.arbeit.data.report;

import pl.edu.agh.arbeit.tracker.events.Event;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

public interface Report {

    void writeCsv(Path path);

    List<Event> getEvents();
}
