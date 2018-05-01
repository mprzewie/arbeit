package pl.edu.agh.arbeit.gui.controler;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pl.edu.agh.arbeit.data.EventListener;
import pl.edu.agh.arbeit.data.report.CsvReport;
import pl.edu.agh.arbeit.tracker.events.Event;
import pl.edu.agh.arbeit.tracker.trackers.ApplicationTracker;

import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ReportsController {
    private Stage reportsStage;
    private EventListener eventListener;
    private List<ApplicationTracker> trackers;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private DatePicker dateFromPicker;

    @FXML
    private DatePicker dateToPicker;

    @FXML
    private TextField pathTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private Button generateReportButton;

    @FXML
    private Button cancelButton;

    public void init(Stage reportsStage, EventListener eventListener, List<ApplicationTracker> trackers){
        this.reportsStage = reportsStage;
        this.eventListener = eventListener;
        this.trackers = trackers;
        reportsStage.setTitle("Generate report");
        initCancelButton();
        initGenerateReportsButton();
        initAppList();
        makeFieldsDisabledForNow();
        pathTextField.setText("report.csv");
    }

    private void initCancelButton(){
        cancelButton.setOnAction(event ->
                reportsStage.close()
        );
    }

    private void initGenerateReportsButton(){
        generateReportButton.setOnAction(event -> {
            try {
                List<Event> events = eventListener.getRepository().getEvents();
                CsvReport report = new CsvReport(trackers.stream().map(t -> t.getApplication().getName()).collect(Collectors.toList()), events);
                if(!pathTextField.getText().equals("")) report.writeCsv(Paths.get(pathTextField.getText()));
                reportsStage.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void initAppList(){
        List<String> apps = trackers.stream().map(t -> t.getApplication().getName()).collect(Collectors.toList());
        double checkBoxYValue = 199;
        final double distanceBetweenCheckBoxes = 39;
        final double checkBoxXValue = 14;

        for (String appName : apps){
            CheckBox cb = new CheckBox(appName);
            cb.setLayoutY(checkBoxYValue);
            cb.setLayoutX(checkBoxXValue);
            cb.setSelected(true);
            checkBoxYValue += distanceBetweenCheckBoxes;
            anchorPane.getChildren().add(cb);
        }
    }

    private void makeFieldsDisabledForNow(){
        this.dateFromPicker.setDisable(true);
        this.dateToPicker.setDisable(true);
        this.pathTextField.setDisable(true);
        this.nameTextField.setDisable(true);
    }

}
