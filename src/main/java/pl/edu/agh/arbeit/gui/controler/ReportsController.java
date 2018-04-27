package pl.edu.agh.arbeit.gui.controler;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pl.edu.agh.arbeit.data.EventListener;
import pl.edu.agh.arbeit.tracker.trackers.ApplicationTracker;

import java.util.LinkedList;
import java.util.List;

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
    }

    private void initCancelButton(){
        cancelButton.setOnAction(event ->
                reportsStage.close()
        );
    }

    private void initGenerateReportsButton(){
        generateReportButton.setOnAction(event -> {
            try {
                eventListener.getDataCollector().generateCSVFile(eventListener.getDataCollector().getCSVFromHashMap());
                reportsStage.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void initAppList(){
        List<String> apps = new LinkedList<>();
        trackers.forEach(tracker -> apps.add(tracker.getApplication().getName()));
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