package pl.edu.agh.arbeit.gui.controler;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pl.edu.agh.arbeit.data.report.IllegalCustomEventTypeException;
import pl.edu.agh.arbeit.data.repository.DatabaseEventRepository;
import pl.edu.agh.arbeit.data.repository.EventRepository;
import pl.edu.agh.arbeit.tracker.events.CustomEvent;
import pl.edu.agh.arbeit.tracker.events.CustomEventBuilder;
import pl.edu.agh.arbeit.tracker.events.EventType;

import java.time.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class CustomEventsController {
    public TextField eventName;

    private Stage eventsStage;

    @FXML
    private Button addButton;

    @FXML
    private Button cancelButton;

    @FXML
    private DatePicker eventDatePicker;

    @FXML
    private ChoiceBox<String> hoursFromBox;

    @FXML
    private ChoiceBox<String> minutesFromBox;

    @FXML
    private ChoiceBox<String> hoursToBox;

    @FXML
    private ChoiceBox<String> minutesToBox;

    private EventRepository applicationRepository = DatabaseEventRepository.initializeDBOrConnectToExisting();

    public void init(Stage eventsStage) {
        this.eventsStage = eventsStage;
        eventsStage.setTitle("Add Custom Event");
        initAddButton();
        initCancelButton();
        initHourBoxes();
//        eventDatePicker.setDisable(true);
    }

    private void initAddButton() {
        addButton.setOnAction(event -> {
            boolean success = true;
            Date dateFrom = constructDateFrom();
            Date dateTo = constructDateTo();
            CustomEvent customEventStart = CustomEventBuilder.aCustomEvent()
                    .withDate(dateFrom)
                    .withTopic(eventName.getText())
                    .withType(EventType.START)
                    .build();
            try {
                applicationRepository.putCustom(customEventStart);
            } catch (IllegalCustomEventTypeException e) {
                e.printStackTrace();
                success = false;
            }
            CustomEvent customEventStop = CustomEventBuilder.aCustomEvent()
                    .withDate(dateTo)
                    .withTopic(eventName.getText())
                    .withType(EventType.STOP)
                    .build();
            try {
                applicationRepository.putCustom(customEventStop);
            } catch (IllegalCustomEventTypeException e) {
                e.printStackTrace();
                success = false;
            }
            eventsStage.close();
        });
    }

    private Date constructDateFrom() {
        LocalTime localTime = LocalTime.of(
                Integer.parseInt(hoursFromBox.getValue()),
                Integer.parseInt(minutesFromBox.getValue())
        );
        LocalDateTime localDateTime = eventDatePicker.getValue().atTime(localTime);
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    private Date constructDateTo() {
        LocalTime localTime = LocalTime.of(
                Integer.parseInt(hoursToBox.getValue()),
                Integer.parseInt(minutesToBox.getValue()),
                0
        );
        LocalDateTime localDateTime = eventDatePicker.getValue().atTime(localTime);

        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    private void initCancelButton() {
        cancelButton.setOnAction(event ->
                eventsStage.close()
        );
    }

    private void initHourBoxes() {
        List<String> hours = new LinkedList<>();
        List<String> minutes = new LinkedList<>();
        for(int i = 0; i <= 60; ++i) {
            if(i<10) {
                hours.add("0" + Integer.toString(i));
                minutes.add("0" + Integer.toString(i));
            } else if(i<=24) hours.add(Integer.toString(i));
            else minutes.add(Integer.toString(i));
        }
        hoursFromBox.setItems(FXCollections.observableArrayList(hours));
        hoursFromBox.setValue("00");
        hoursToBox.setItems(FXCollections.observableArrayList(hours));
        hoursToBox.setValue("24");
        minutesFromBox.setItems(FXCollections.observableArrayList(minutes));
        minutesFromBox.setValue("00");
        minutesToBox.setItems(FXCollections.observableArrayList(minutes));
        minutesToBox.setValue("00");
    }

}
