package pl.edu.agh.arbeit.gui.controler;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import pl.edu.agh.arbeit.data.EventListener;
import pl.edu.agh.arbeit.gui.view.AddCircle;
import pl.edu.agh.arbeit.tracker.Application;
import pl.edu.agh.arbeit.tracker.trackers.ApplicationTracker;
import pl.edu.agh.arbeit.tracker.trackers.SystemTracker;
import pl.edu.agh.arbeit.tracker.trackers.Tracker;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class MainWindowController {
    private OverviewController overviewController;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button generateReportButton;

    @FXML
    private TextField appNameTextField;

    private AddCircle addCircle;

    private EventListener eventListener;

    private List<Tracker> trackerList;
    
    public void init(OverviewController overviewController) {
        this.trackerList = new LinkedList<>();
        this.overviewController=overviewController;
        this.addCircle = new AddCircle();
        this.addCircle.setDisable(true);
        this.anchorPane.getChildren().add(this.addCircle);

        this.eventListener = new EventListener();
        Tracker systemTracker = new SystemTracker(10);
        this.eventListener.subscribe(systemTracker);
        systemTracker.start();
        trackerList.add(systemTracker);

        generateReportButton.setOnMouseClicked(event -> {
            try {
                eventListener.getDataCollector().generateCSVFile(eventListener.getDataCollector().getCSVFromHashMap());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        addCircle.setOnMouseClicked(event ->{
            Application app = new Application(this.appNameTextField.getText(), this.appNameTextField.getText());
            Tracker appTracker = new ApplicationTracker(5, app);
            eventListener.subscribe(appTracker);
            appTracker.start();
            trackerList.add(appTracker);
        });

        appNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals(""))
                addCircle.setDisable(true);
            else
                addCircle.setDisable(false);
        });
        this.bindSizeProperties();
        this.initDatePicer();
    }

    private void bindSizeProperties() {
    }

    private void initDatePicer(){
        datePicker.setValue(LocalDate.now());
        datePicker.setDisable(true);
    }
}
