package pl.edu.agh.arbeit.gui.controler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import pl.edu.agh.arbeit.data.EventListener;
import javafx.stage.Stage;
import pl.edu.agh.arbeit.gui.Main;
import pl.edu.agh.arbeit.gui.view.AppAdder;
import pl.edu.agh.arbeit.gui.view.AppListItem;
import pl.edu.agh.arbeit.tracker.Application;
import pl.edu.agh.arbeit.tracker.trackers.ApplicationTracker;
import pl.edu.agh.arbeit.tracker.trackers.SystemTracker;
import pl.edu.agh.arbeit.tracker.trackers.Tracker;

import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class MainWindowController {
    private final static long SYSTEM_TRACKER_PING_TIME = 10;
    private final static int SINGLE_APP_VIEW_WIDTH = 50;

    private OverviewController overviewController;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button generateReportButton;

    private AppAdder appAdder;

    private EventListener eventListener;

    private List<Tracker> trackerList;

    private List<ApplicationTracker> applicationTrackerList;

    private Line verticalLine;

    private Line timeLine;


    public void init(OverviewController overviewController) {
        this.trackerList = new LinkedList<>();
        this.applicationTrackerList = new LinkedList<>();
        this.overviewController=overviewController;

        this.verticalLine = new Line();
        this.verticalLine = new Line();
        this.verticalLine.setStartX(120);
        this.verticalLine.setEndX(120);
        this.verticalLine.setStartY(0);
        this.verticalLine.setEndY(100);
        this.anchorPane.getChildren().add(this.verticalLine);

        timeLine = new Line();
        timeLine.setStartX(0);
        timeLine.setEndX(800);
        timeLine.setStartY(50);
        timeLine.setEndY(50);
        this.anchorPane.getChildren().add(timeLine);

        Line f1 = new Line();
        f1.setStartX(0);
        f1.setEndX(120);
        f1.setStartY(100);
        f1.setEndY(100);
        this.anchorPane.getChildren().add(f1);

        Text systemText = new Text("System");
        systemText.setLayoutX(10);
        systemText.setLayoutY(80);
        this.anchorPane.getChildren().add(systemText);

        this.eventListener = new EventListener();
        Tracker systemTracker = new SystemTracker(SYSTEM_TRACKER_PING_TIME);
        this.eventListener.subscribe(systemTracker);
        systemTracker.start();
        trackerList.add(systemTracker);

        this.appAdder = new AppAdder(this, applicationTrackerList, eventListener);
        this.appAdder.setLayoutY(100);
        this.anchorPane.getChildren().add(this.appAdder);

        this.bindSizeProperties();
        this.initDatePicer();
        this.initReportButton();
    }

    public void drawNewAppView(Application application){
        AppListItem appListItem = new AppListItem(application, applicationTrackerList);
        appListItem.setLayoutX(0);
        appListItem.setLayoutY(50 + SINGLE_APP_VIEW_WIDTH * (applicationTrackerList.size()));
        this.anchorPane.getChildren().add(appListItem);
        this.appAdder.setLayoutY(50 + SINGLE_APP_VIEW_WIDTH * (applicationTrackerList.size() + 1));
    }

    private void bindSizeProperties() {
    }

    private void initDatePicer(){
        datePicker.setValue(LocalDate.now());
        datePicker.setDisable(true);
    }

    private void initReportButton() {
        generateReportButton.setOnAction(
            (ActionEvent event) -> {
                try {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(Main.class.getResource("view/ReportsPane.fxml"));
                    Parent root = loader.load();
                    Stage stage = new Stage();
                    ReportsController reportsController = loader.getController();
                    reportsController.init(stage, eventListener, applicationTrackerList);
                    stage.setScene(new Scene(root, 450, 450));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        );
    }
}
