package pl.edu.agh.arbeit.gui.controler;

import javafx.beans.binding.DoubleBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import pl.edu.agh.arbeit.data.EventListener;
import javafx.stage.Stage;
import pl.edu.agh.arbeit.gui.Main;
import pl.edu.agh.arbeit.gui.view.AppAdder;
import pl.edu.agh.arbeit.gui.view.AppListItem;
import pl.edu.agh.arbeit.gui.view.SystemListItem;
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

    @FXML
    private ScrollPane appScrollPane;

    @FXML
    private VBox scrollAndButtonVBox;

    private AppAdder appAdder;

    private EventListener eventListener;

    private List<Tracker> trackerList;

    private List<ApplicationTracker> applicationTrackerList;

    private VBox listContent;

    public void init(OverviewController overviewController, DoubleBinding heightProperty) {
        this.trackerList = new LinkedList<>();
        this.applicationTrackerList = new LinkedList<>();
        this.overviewController=overviewController;

        SystemListItem systemListItem = new SystemListItem();

        listContent = new VBox();
        this.appScrollPane.setContent(listContent);
        listContent.getChildren().add(systemListItem);

        this.eventListener = new EventListener();
        Tracker systemTracker = new SystemTracker(SYSTEM_TRACKER_PING_TIME);
        this.eventListener.subscribe(systemTracker);
        systemTracker.start();
        trackerList.add(systemTracker);

        this.appAdder = new AppAdder(this, applicationTrackerList, eventListener);
        listContent.getChildren().add(this.appAdder);

        this.bindSizeProperties();
        this.initDatePicer();
        this.initReportButton();
        scrollAndButtonVBox.prefHeightProperty().bind(heightProperty);
    }

    public void addNewAppView(Application application){
        AppListItem appListItem = new AppListItem(application, applicationTrackerList, this);
        this.listContent.getChildren().add(listContent.getChildren().size() - 1, appListItem);
    }

    public void removeAppView(AppListItem appListItem){
        this.listContent.getChildren().removeIf(view -> view.equals(appListItem));
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
