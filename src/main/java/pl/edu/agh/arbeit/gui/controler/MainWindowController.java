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
import pl.edu.agh.arbeit.data.EventListenerSaver;
import pl.edu.agh.arbeit.data.repository.DatabaseEventRepository;
import pl.edu.agh.arbeit.data.repository.EventRepository;
import pl.edu.agh.arbeit.gui.Main;
import pl.edu.agh.arbeit.gui.model.AppConfig;
import pl.edu.agh.arbeit.gui.model.ConfigProvider;
import pl.edu.agh.arbeit.gui.model.StyleType;
import pl.edu.agh.arbeit.gui.view.AppAdder;
import pl.edu.agh.arbeit.gui.view.AppListItem;
import pl.edu.agh.arbeit.gui.view.SystemListItem;
import pl.edu.agh.arbeit.gui.view.TimeLine;
import pl.edu.agh.arbeit.tracker.Application;
import pl.edu.agh.arbeit.tracker.events.Event;
import pl.edu.agh.arbeit.tracker.trackers.ApplicationTracker;
import pl.edu.agh.arbeit.tracker.trackers.SystemTracker;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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

    @FXML
    private VBox listContent;

    private EventListenerSaver eventListenerSaver;
    private List<EventListener> eventListeners = new ArrayList<>();

    private SystemTracker systemTracker;

    private List<ApplicationTracker> applicationTrackerList = new LinkedList<>();

    private boolean customEventActive = false;

    private EventRepository eventRepository = DatabaseEventRepository.initializeDBOrConnectToExisting();

    private ConfigProvider appConfig = new AppConfig();

    private List<AppListItem> appListItems;

    private SystemListItem systemListItem;

    public void init(OverviewController overviewController, DoubleBinding heightProperty) {
        this.overviewController= overviewController;
        systemListItem = new SystemListItem();
        listContent.getChildren().add(0,systemListItem);
        systemTracker = new SystemTracker(appConfig.getSystemPingTime(), Duration.ofSeconds(10));


        eventListenerSaver = new EventListenerSaver(eventRepository);

        EventListener eventDrawerListener = new EventListenerDrawer(this);

        eventListeners.add(eventListenerSaver);
        eventListeners.add(eventDrawerListener);

        eventListeners.forEach(listener -> listener.subscribe(systemTracker));


        systemTracker.start();

        appListItems = new ArrayList<>();

        this.initAppScrollPane();
        this.bindSizeProperties();
        this.initDatePicker();
        this.initReportButton();
        this.initAppAdder();
        this.initAddCustomEventButton();
        this.initBeginCustomEventButton();
        this.initStyleChocieBox();
        //scrollAndButtonVBox.prefHeightProperty().bind(heightProperty);

        redrawTimelines(LocalDate.now());
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
        AppAdder appAdder = new AppAdder(this, applicationTrackerList, eventListeners, appConfig);
        listContent.getChildren().add(appAdder);
    }

    public void addNewAppView(Application application){
        AppListItem appListItem = new AppListItem(application, applicationTrackerList, this);
        appListItems.add(appListItem);
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

    private void initDatePicker(){
        datePicker.setValue(LocalDate.now());
        datePicker.valueProperty().addListener((overview, oldValue, newValue) -> {
            System.out.println("redrawing timelines");
            redrawTimelines(newValue);
        });
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
                    reportsController.init(stage, eventListenerSaver, applicationTrackerList, stage.heightProperty());
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

    public void stopTrackingAll(){
        systemTracker.stop();
        this.applicationTrackerList.forEach(tracker -> {
            tracker.stop();
            System.out.println("STOPPED tracking " + tracker.toString());
        });

    }

    public SystemTracker getSystemTracker() {
        return systemTracker;
    }

    public void acceptEvent(Event event){
        if(datePicker.getValue().isEqual(LocalDate.now())){

            appListItems.forEach(appListItem -> {
                if(appListItem.getApplication().getProgramName().equals(event.getTopic()))
                    appListItem.getTimeLine().addEvent(event);
            });
            if(event.getTopic().equals("system"))
                systemListItem.getTimeLine().addEvent(event);
        }
    }

    private void redrawTimelines(LocalDate date){
        Date from = Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date to = Date.from(date.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        appListItems.forEach(appListItem -> {
            String applicationName = appListItem.getApplication().getProgramName();
//            List<Event> relevantEvents = eventRepository.getEventForGivenAppinRange(applicationName, from, to);
            appListItem.setTimeLine(new TimeLine());
        });
        systemListItem.setTimeLine(new TimeLine());
    }
}
