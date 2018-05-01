package pl.edu.agh.arbeit.data.report;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import pl.edu.agh.arbeit.tracker.events.Event;
import pl.edu.agh.arbeit.tracker.events.EventType;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.time.*;

public class CsvReport implements Report {

    private final List<Event> events;
    private final CSVFormat format;
    private final List<LocalDate> dates;

    public CsvReport(List<Event> events) {

        this.events = events;
        this.dates = events
                .stream()
                .filter(event -> event.getTopic().equals("system"))
                .map(event -> event.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .collect(Collectors.toList());

        ArrayList<String> headerList = new ArrayList<>();
        headerList.add("topic");
        headerList.add("activity");
        headerList.addAll(dates.stream().map(LocalDate::toString).distinct().collect(Collectors.toList()));
        String[] header = headerList.toArray(new String[0]);
        this.format = CSVFormat.DEFAULT.withHeader(header);
    }



    public void writeCsv(Path path) throws IOException {
        FileWriter writer = new FileWriter(path.toString());
        try(CSVPrinter printer = new CSVPrinter(writer, format)){
            events.stream().map(Event::getTopic).distinct()
                    .forEach(topic -> {
                try {
                    printer.printRecord(topic, "active");
                    printer.printRecord(topic, "passive");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }


    @Override
    public List<Event> getEvents() {
        return events;
    }
}
