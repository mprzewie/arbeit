package pl.edu.agh.arbeit.data.report;

import pl.edu.agh.arbeit.tracker.events.Event;

import java.nio.file.Path;
import java.util.List;

public class CsvReport implements Report {

    private final List<Event> events;

    public CsvReport(List<Event> events) {
        this.events = events;
    }


    @Override
    public void writeCsv(Path path) {

    }

    @Override
    public List<Event> getEvents() {
        return events;
    }
}
