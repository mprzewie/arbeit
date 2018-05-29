package pl.edu.agh.arbeit.gui.controler;

import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pl.edu.agh.arbeit.data.EventListener;
import pl.edu.agh.arbeit.data.repository.DatabaseEventRepository;
import pl.edu.agh.arbeit.data.repository.EventRepository;
import pl.edu.agh.arbeit.gui.Main;
import pl.edu.agh.arbeit.gui.model.AppConfig;
import pl.edu.agh.arbeit.gui.model.ConfigProvider;
import pl.edu.agh.arbeit.gui.model.StyleType;
import pl.edu.agh.arbeit.gui.view.AppAdder;
import pl.edu.agh.arbeit.gui.view.AppListItem;
import pl.edu.agh.arbeit.gui.view.SystemListItem;
import pl.edu.agh.arbeit.tracker.Application;
import pl.edu.agh.arbeit.tracker.trackers.ApplicationTracker;
import pl.edu.agh.arbeit.tracker.trackers.SystemTracker;
import pl.edu.agh.arbeit.tracker.trackers.Tracker;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

//import pl.edu.agh.arbeit.gui.view.AddCircle;

public class MainWindowController {
    private OverviewController overviewController;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button generateReportButton;

    @FXML
    private Button addCustomEventButton;

    @FXML
    private Button beginCustomEventButton;

    @FXML
    private ScrollPane appScrollPane;

    @FXML
    private ChoiceBox styleChoiceBox;

    private AppAdder appAdder;

    private EventListener eventListener;

    private List<Tracker> trackerList;

    private SystemTracker systemTracker;

    private List<ApplicationTracker> applicationTrackerList;


    private boolean customEventActive;

    private EventRepository applicationRepository = DatabaseEventRepository.initializeDBOrConnectToExisting();

    @FXML
    private VBox listContent;

    private ConfigProvider appConfig;

    private Scene scene;

    public void init(OverviewController overviewController, DoubleBinding heightProperty, Scene scene) {
        customEventActive = false;
        this.trackerList = new LinkedList<>();
        this.scene = scene;
        this.applicationTrackerList = new LinkedList<>();
        this.overviewController=overviewController;
        this.appConfig = new AppConfig();

        listContent.getChildren().add(0,new SystemListItem());
        initAppScrollPane();

        this.eventListener = new EventListener(applicationRepository);

        systemTracker = new SystemTracker(appConfig.getSystemPingTime(), Duration.ofSeconds(10));
        this.eventListener.subscribe(systemTracker);
        systemTracker.start();
        trackerList.add(systemTracker);

        this.bindSizeProperties();
        this.initDatePicer();
        this.initReportButton();
        this.initAppAdder();
        this.initAddCustomEventButton();
        this.initBeginCustomEventButton();
        this.initStyleChocieBox();
        //scrollAndButtonVBox.prefHeightProperty().bind(heightProperty);
    }

    private void initStyleChocieBox() {
        styleChoiceBox.setItems(FXCollections.observableArrayList(
                StyleType.values()));
        styleChoiceBox.getSelectionModel().select(StyleType.STANDARD);

        String standard = AppAdder.class.getResource("Standard.css").toExternalForm();
        String dark = AppAdder.class.getResource("Dark.css").toExternalForm();

        styleChoiceBox.getSelectionModel().selectedIndexProperty().addListener(
                (observable, oldValue, newValue) -> {
                    anchorPane.getStylesheets().clear();
                        if(StyleType.values()[newValue.intValue()].equals(StyleType.DARK))
                            anchorPane.getStylesheets().add(dark);
                        else
                            anchorPane.getStylesheets().add(standard);
                });
    }

    private void initAppScrollPane(){
        this.appScrollPane.setContent(listContent);
        this.appScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.appScrollPane.setStyle("-fx-background-color:transparent;");
    }

    private void initAppAdder(){
        this.appAdder = new AppAdder(this, applicationTrackerList, eventListener, appConfig);
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

    private void initAddCustomEventButton() {
        addCustomEventButton.setOnAction(
                event -> {
                    try {
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(Main.class.getResource("view/CustomEventsPane.fxml"));
                        Parent root = loader.load();
                        Stage stage = new Stage();
                        CustomEventsController customEventsController = loader.getController();
                        customEventsController.init(stage);
                        stage.setScene(new Scene(root, 450, 450));
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    private void initBeginCustomEventButton() {
        beginCustomEventButton.setOnAction(
                event -> {
                    if(customEventActive){
                        beginCustomEventButton.setText("Begin Custom Event");
                        customEventActive = false;
                    } else {
                        beginCustomEventButton.setText("End Custom Event");
                        customEventActive = true;
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

    public SystemTracker getSystemTracker() {
        return systemTracker;
    }
}
