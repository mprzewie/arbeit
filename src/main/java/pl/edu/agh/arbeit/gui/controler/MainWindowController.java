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
import pl.edu.agh.arbeit.gui.model.AppConfig;
import pl.edu.agh.arbeit.gui.model.ConfigProvider;
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
    private OverviewController overviewController;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button generateReportButton;

    @FXML
    private ScrollPane appScrollPane;

    private AppAdder appAdder;

    private EventListener eventListener;

    private List<Tracker> trackerList;

    private List<ApplicationTracker> applicationTrackerList;

    @FXML
    private VBox listContent;

    private ConfigProvider appConfig;

    public void init(OverviewController overviewController, DoubleBinding heightProperty) {
        this.trackerList = new LinkedList<>();
        this.applicationTrackerList = new LinkedList<>();
        this.overviewController=overviewController;
        this.appConfig = new AppConfig();

        listContent.getChildren().add(0,new SystemListItem());
        initAppScrollPane();

        this.eventListener = new EventListener();
        Tracker systemTracker = new SystemTracker(appConfig.getSystemPingTime(), 10);
        this.eventListener.subscribe(systemTracker);
        systemTracker.start();
        trackerList.add(systemTracker);

        this.bindSizeProperties();
        this.initDatePicer();
        this.initReportButton();
        this.initAppAdder();
        //scrollAndButtonVBox.prefHeightProperty().bind(heightProperty);
    }

    private void initAppScrollPane(){
        this.appScrollPane.setContent(listContent);
        this.appScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.appScrollPane.setStyle("-fx-background-color:transparent;");
    }

    private void initAppAdder(){
        this.appAdder = new AppAdder(this, applicationTrackerList, eventListener);
        listContent.getChildren().add(this.appAdder);
    }

    public void addNewAppView(Application application){
        AppListItem appListItem = new AppListItem(application, applicationTrackerList, this);
        int listContentIndex = listContent.getChildren().size() - 1;
        if(listContentIndex == 0)
            listContentIndex++;
        this.listContent.getChildren().add(listContentIndex, appListItem);
    }

    public void removeAppView(AppListItem appListItem){
        this.listContent.getChildren().removeIf(view -> view.equals(appListItem));
    }

    public void removeApp(String programName){
        this.appConfig.removeAppToTrack(programName);
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
                    reportsController.init(stage, eventListener, applicationTrackerList, stage.heightProperty());
                    stage.setScene(new Scene(root, 450, 450));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        );
    }

    public void addToTrackerList(Tracker tracker){
        this.trackerList.add(tracker);
    }

    public void stopTrackingAll(){
        this.trackerList.forEach(e -> {e.stop(); System.out.println("STOPPED tracking " + e.toString());});
    }
}
