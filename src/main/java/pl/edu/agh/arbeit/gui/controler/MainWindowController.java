package pl.edu.agh.arbeit.gui.controler;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pl.edu.agh.arbeit.data.EventListener;
import pl.edu.agh.arbeit.data.EventListenerSaver;
import pl.edu.agh.arbeit.data.report.IllegalCustomEventTypeException;
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
import pl.edu.agh.arbeit.tracker.events.CustomEvent;
import pl.edu.agh.arbeit.tracker.events.CustomEventBuilder;
import pl.edu.agh.arbeit.tracker.events.Event;
import pl.edu.agh.arbeit.tracker.events.EventType;
import pl.edu.agh.arbeit.tracker.trackers.ApplicationTracker;
import pl.edu.agh.arbeit.tracker.trackers.SystemTracker;

import java.io.IOException;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;
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
    private Button addCustomEventButton;

    @FXML
    private Button beginCustomEventButton;

    @FXML
    private ScrollPane appScrollPane;

    @FXML
    private ChoiceBox styleChoiceBox;

    @FXML
    private VBox listContent;

    @FXML
    private FontAwesomeIconView leftArrow;

    @FXML
    private FontAwesomeIconView rightArrow;

    private EventListenerSaver eventListenerSaver;
    private List<EventListener> eventListeners = new ArrayList<>();

    private SystemTracker systemTracker;

    private List<ApplicationTracker> applicationTrackerList = new LinkedList<>();

    private boolean customEventActive = false;
    private String currentCustomEventName = "";

    private EventRepository eventRepository = DatabaseEventRepository.initializeDBOrConnectToExisting();

    private ConfigProvider appConfig = new AppConfig();

    private List<AppListItem> appListItems;

    private SystemListItem systemListItem;

    private String styleNow;


    public void init(OverviewController overviewController, DoubleBinding heightProperty) {
        this.overviewController= overviewController;

        systemTracker = new SystemTracker(appConfig.getSystemPingTime(), appConfig.getTimeToBecomePassive());


        eventListenerSaver = new EventListenerSaver(eventRepository);

        EventListener eventDrawerListener = new EventListenerDrawer(this);

        eventListeners.add(eventListenerSaver);
        eventListeners.add(eventDrawerListener);

        eventListeners.forEach(listener -> listener.subscribe(systemTracker));


        systemTracker.start();
        styleNow = AppAdder.class.getResource("Standard.css").toExternalForm();
        systemListItem = new SystemListItem(appConfig, styleNow);
        listContent.getChildren().add(0,systemListItem);

        appListItems = new ArrayList<>();

        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isAfter(LocalDate.now()));
            }
        });

        this.initAppScrollPane();
        this.bindSizeProperties();
        this.initDatePicker();
        this.initReportButton();
        this.initAppAdder();
        this.initAddCustomEventButton();
        this.initBeginCustomEventButton();
        this.initStyleChocieBox();
        this.initArrows();
        //scrollAndButtonVBox.prefHeightProperty().bind(heightProperty);

        redrawTimelines(LocalDate.now());
    }

    private void initArrows() {
        this.leftArrow.setOnMouseClicked(e->{
            datePicker.setValue(datePicker.getValue().minusDays(1));
        });

        this.rightArrow.setOnMouseClicked(e->{
            if(!datePicker.getValue().equals(LocalDate.now()))
                datePicker.setValue(datePicker.getValue().plusDays(1));
        });
    }

    private void initStyleChocieBox() {
        styleChoiceBox.setItems(FXCollections.observableArrayList(
                StyleType.values()));
        styleChoiceBox.getSelectionModel().select(StyleType.STANDARD);

        final String standard = AppAdder.class.getResource("Standard.css").toExternalForm();
        final String dark = AppAdder.class.getResource("Dark.css").toExternalForm();

        styleChoiceBox.getSelectionModel().selectedIndexProperty().addListener(
                (observable, oldValue, newValue) -> {
                    anchorPane.getStylesheets().clear();
                        if(StyleType.values()[newValue.intValue()].equals(StyleType.DARK)) {
                            anchorPane.getStylesheets().add(dark);
                            appListItems.forEach(AppListItem::setTextWhite);
                            systemListItem.setTextWhite();
                            styleNow = dark;
                        } else {
                            anchorPane.getStylesheets().add(standard);
                            appListItems.forEach(AppListItem::setTextBlack);
                            systemListItem.setTextBlack();
                            styleNow = standard;
                        }
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
        AppListItem appListItem = new AppListItem(application, applicationTrackerList, this, styleNow);
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
                    reportsController.init(stage, eventListenerSaver, applicationTrackerList, stage.heightProperty(), styleNow);

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
                        customEventsController.init(stage, styleNow);
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
                beginCustomEventButtonListener
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
            List<Event> relevantEvents = eventRepository.getEventForGivenAppinRange(applicationName, from, to);
            appListItem.setTimeLine(new TimeLine(appListItem.getApplication().getActiveColor().getColorFx(),
                                                appListItem.getApplication().getPassiveColor().getColorFx(),
                                                appListItem.getApplication().getBackgroundColor().getColorFx()));
            eventRepository.getPreviousEventTypeForApp(relevantEvents)
                    .ifPresent(event -> appListItem.getTimeLine().addEvent(new Event() {
                        @Override
                        public String getTopic() {
                            return applicationName;
                        }

                        @Override
                        public EventType getType() {
                            return event.getType();
                        }

                        @Override
                        public Date getDate(){
                            return from;
                        }
                    }));
            relevantEvents.forEach(appListItem.getTimeLine()::addEvent);
        });
        List<Event> relevantSystemEvents = eventRepository.getEventForGivenAppinRange("system", from, to);
        systemListItem.setTimeLine(new TimeLine(appConfig.getInfo().getSystemActiveColor().getColorFx(),
                                                appConfig.getInfo().getSystemPassiveColor().getColorFx(),
                                                appConfig.getInfo().getSystemBackgroundColor().getColorFx()));
        eventRepository.getPreviousEventTypeForApp(relevantSystemEvents)
                .ifPresent(event -> systemListItem.getTimeLine().addEvent(new Event() {
                    @Override
                    public String getTopic() {
                        return "system";
                    }

                    @Override
                    public EventType getType() {
                        return event.getType();
                    }

                    @Override
                    public Date getDate(){
                        return from;
                    }
                }));
        relevantSystemEvents.forEach(systemListItem.getTimeLine()::addEvent);

    }

    public void beginCustomEvent(String eventName){
        beginCustomEventButton.setText("End Custom Event");
        customEventActive = true;
        currentCustomEventName = eventName;
        actuallyBeginCustomEvent(eventName);
        beginCustomEventButton.setOnAction(endCustomEventButtonListener);
    }

    private void actuallyBeginCustomEvent(String eventName) {
        Date date = constructDateNow();
        CustomEvent customEventStart = CustomEventBuilder.aCustomEvent()
                .withDate(date)
                .withTopic(eventName)
                .withType(EventType.START)
                .build();
        try {
            eventRepository.putCustom(customEventStart);
            System.out.println(customEventStart);
        } catch (IllegalCustomEventTypeException e) {
            e.printStackTrace();
        }
    }

    private Date constructDateNow() {
        LocalDateTime now = LocalDateTime.now();
        Instant instant = now.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }


    private EventHandler<ActionEvent> beginCustomEventButtonListener =
            event -> {
                try {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(Main.class.getResource("view/BeginCustomEventPane.fxml"));
                    Parent root = loader.load();
                    Stage stage = new Stage();
                    BeginCustomEventController beginCustomEventController = loader.getController();

                    beginCustomEventController.init(stage, this, styleNow);
                    stage.setScene(new Scene(root, 450, 100));
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            };

    private EventHandler<ActionEvent> endCustomEventButtonListener =
            event -> {
                beginCustomEventButton.setText("Begin Custom Event");
                actuallyEndCustomEvent(currentCustomEventName);
                customEventActive = false;
                currentCustomEventName = "";
                beginCustomEventButton.setOnAction(beginCustomEventButtonListener);
            };

    private void actuallyEndCustomEvent(String eventName) {
        Date date = constructDateNow();
        CustomEvent customEventStop = CustomEventBuilder.aCustomEvent()
                .withDate(date)
                .withTopic(eventName)
                .withType(EventType.STOP)
                .build();
        try {
            eventRepository.putCustom(customEventStop);
            System.out.println(customEventStop);
        } catch (IllegalCustomEventTypeException e) {
            e.printStackTrace();
        }
    }

    public ConfigProvider getAppConfig() {
        return appConfig;
    }
}
