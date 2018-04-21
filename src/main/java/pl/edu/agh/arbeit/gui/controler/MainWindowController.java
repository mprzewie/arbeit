package pl.edu.agh.arbeit.gui.controler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import pl.edu.agh.arbeit.data.EventListener;
import javafx.stage.Stage;
import pl.edu.agh.arbeit.gui.Main;
import pl.edu.agh.arbeit.gui.view.AddCircle;
import pl.edu.agh.arbeit.tracker.Application;
import pl.edu.agh.arbeit.tracker.trackers.ApplicationTracker;
import pl.edu.agh.arbeit.tracker.trackers.SystemTracker;
import pl.edu.agh.arbeit.tracker.trackers.Tracker;

import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class MainWindowController {
    private final static long APP_TRACKER_PING_TIME = 5;
    private final static long SYSTEM_TRACKER_PING_TIME = 10;
    private final static int SINGLE_APP_VIEW_WIDTH = 50;
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

    private Line verticalLine;

    private Line timeLine;


    public void init(OverviewController overviewController) {
        this.trackerList = new LinkedList<>();
        this.overviewController=overviewController;
        this.addCircle = new AddCircle();
        this.addCircle.setDisable(true);
        this.anchorPane.getChildren().add(this.addCircle);

        this.verticalLine = new Line();
        this.verticalLine.setStartX(120);
        this.verticalLine.setEndX(120);
        this.verticalLine.setStartY(0);
        this.verticalLine.setEndY(150);
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

        addCircle.setOnMouseClicked(event ->{
            Application newApp =  new Application(this.appNameTextField.getText(), this.appNameTextField.getText());
            if(isAppNotTracked(newApp)) {
                this.trackerList.add(createTracker(APP_TRACKER_PING_TIME, newApp));
                this.verticalLine.setEndY(this.verticalLine.getEndY() + SINGLE_APP_VIEW_WIDTH);
                drawNewAppView();
                addCircle.setLayoutY(addCircle.getLayoutY() + SINGLE_APP_VIEW_WIDTH);
                appNameTextField.setLayoutY(appNameTextField.getLayoutY() + SINGLE_APP_VIEW_WIDTH);
            }
        });

        appNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals(""))
                addCircle.setDisable(true);
            else
                addCircle.setDisable(false);
        });
        this.bindSizeProperties();
        this.initDatePicer();
        this.initReportButton();
    }

    private Tracker createTracker(long pingTime,  Application application){
        Tracker appTracker = new ApplicationTracker(pingTime, application);
        eventListener.subscribe(appTracker);
        appTracker.start();
        return appTracker;
    }

    private boolean isAppNotTracked(Application application){
        return trackerList.stream()
                .filter(tracker -> tracker instanceof ApplicationTracker)
                .noneMatch(tracker -> ((ApplicationTracker) tracker).getApplication().equals(application));
    }

    private void drawNewAppView(){
        Line ft = new Line();
        ft.setStartX(0);
        ft.setEndX(120);
        ft.setStartY(100 + SINGLE_APP_VIEW_WIDTH * (trackerList.size()-1));
        ft.setEndY(100+ SINGLE_APP_VIEW_WIDTH *(trackerList.size()-1));
        this.anchorPane.getChildren().add(ft);

        Text appName = new Text(appNameTextField.getText());
        appName.setLayoutX(10);
        appName.setLayoutY(80+ SINGLE_APP_VIEW_WIDTH *(trackerList.size()-1));
        this.anchorPane.getChildren().add(appName);
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
                    reportsController.init(stage, eventListener, trackerList);
                    stage.setScene(new Scene(root, 450, 450));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        );
    }
}
