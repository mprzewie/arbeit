package pl.edu.agh.arbeit.gui.controler;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pl.edu.agh.arbeit.gui.model.StyleType;

import java.util.LinkedList;
import java.util.List;

public class CustomEventsController {
    private Stage eventsStage;

    @FXML
    private AnchorPane anchorPane;

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

    public void init(Stage eventsStage, String styleType) {
        this.eventsStage = eventsStage;
        eventsStage.setTitle("Add Custom Event");
        initAddButton();
        initCancelButton();
        initHourBoxes();
        eventDatePicker.setDisable(true);
        anchorPane.getStylesheets().clear();
        anchorPane.getStylesheets().add(styleType);
    }

    private void initAddButton() {
        addButton.setOnAction(event ->
                eventsStage.close()
        );
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
