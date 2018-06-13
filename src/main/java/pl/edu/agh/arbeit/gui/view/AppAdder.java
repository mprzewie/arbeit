package pl.edu.agh.arbeit.gui.view;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import org.controlsfx.control.textfield.TextFields;
import pl.edu.agh.arbeit.data.EventListener;
import pl.edu.agh.arbeit.gui.controler.MainWindowController;
import pl.edu.agh.arbeit.gui.model.ApplicationInfo;
import pl.edu.agh.arbeit.gui.model.ConfigProvider;
import pl.edu.agh.arbeit.tracker.Application;
import pl.edu.agh.arbeit.tracker.system.RunningWindowsCollector;
import pl.edu.agh.arbeit.tracker.trackers.ApplicationTracker;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


public class AppAdder extends Pane {
    private final Duration APP_TRACKER_PING_TIME = Duration.ofSeconds(5);

    private FontAwesomeIconView addCircle;
    private ComboBox<String> appNameComboBox;
    private List<ApplicationTracker> applicationTrackers;
    private List<EventListener> eventListeners;
    private ConfigProvider appConfig;

    public AppAdder(MainWindowController mainWindowController, List<ApplicationTracker> applicationTrackers, List<EventListener> eventListeners, ConfigProvider appConfig) {
        this.appConfig = appConfig;
        this.applicationTrackers = applicationTrackers;
        this.eventListeners = eventListeners;
        addCircle = new FontAwesomeIconView(FontAwesomeIcon.PLUS_CIRCLE);
        addCircle.setSize("45px");
        addCircle.setLayoutX(40);
        addCircle.setLayoutY(40);
        addCircle.setStyleClass("icon-plus");
        this.getChildren().add(addCircle);
        addCircle.setDisable(true);

        Line verticalLine = new Line();
        verticalLine.setStartX(120);
        verticalLine.setEndX(120);
        verticalLine.setStartY(0);
        verticalLine.setEndY(52);

        this.getChildren().add(verticalLine);

        List<String> appNames = new ArrayList<>(new RunningWindowsCollector().getRunningWindowsNames());
        this.appNameComboBox = new ComboBox<>(FXCollections.observableList(appNames));
        this.appNameComboBox.setLayoutX(130);
        this.appNameComboBox.setLayoutY(10);
        if(appNames.size() > 0) {
            this.appNameComboBox.setValue(appNames.get(0));
        }

        this.getChildren().add(appNameComboBox);
        appNameComboBox.setEditable(true);
        TextFields.bindAutoCompletion(appNameComboBox.getEditor(), appNameComboBox.getItems());
        forbidEmptyAppName();
        initTrackingAppsFromConfig(this.appConfig.getAppsToTrack(), mainWindowController);
        appNameComboBox.setOnKeyPressed(keyEvent ->{
            if(keyEvent.getCode().equals(KeyCode.ENTER)) {
                if(!this.appNameComboBox.getEditor().getText().equals("")) {
                    Application newApp = new Application(this.appNameComboBox.getEditor().getText(), this.appNameComboBox.getEditor().getText(), mainWindowController.getSystemTracker());
                    if (isAppNotTracked(newApp))
                        appConfig.addAppToTrack(newApp.getApplicationInfo());
                    addApp(mainWindowController, newApp, APP_TRACKER_PING_TIME);
                }
            }
        });
        initAddButton(mainWindowController);

    }

    private void initTrackingAppsFromConfig(List<ApplicationInfo> apps, MainWindowController mainWindowController){
        apps.forEach(e -> addApp(mainWindowController, e.toApplication(mainWindowController.getSystemTracker()), Duration.ofSeconds(e.getPingTimeInSeconds())
        ));
    }

    private boolean isAppNotTracked(Application application){
        return applicationTrackers.stream()
                .noneMatch(tracker -> tracker.getApplication().equals(application));
    }

    private void forbidEmptyAppName(){
        appNameComboBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(""))

                addCircle.setDisable(true);
            else
                addCircle.setDisable(false);
        });
    }

    private void initAddButton(MainWindowController mainWindowController){
        addCircle.setOnMouseClicked(event ->{
            Application newApp =  new Application(this.appNameComboBox.getEditor().getText(), this.appNameComboBox.getEditor().getText(), mainWindowController.getSystemTracker());
            if(isAppNotTracked(newApp))
                appConfig.addAppToTrack(newApp.getApplicationInfo());
            addApp(mainWindowController,newApp,APP_TRACKER_PING_TIME);
        });
    }

    private void addApp(MainWindowController mainWindowController, Application application, Duration pingTime){
        if(isAppNotTracked(application)) {
            this.applicationTrackers.add(createTracker(pingTime, application, mainWindowController));
            mainWindowController.addNewAppView(application);
        }
    }

    private ApplicationTracker createTracker(Duration pingTime, Application application, MainWindowController mainWindowController){
        ApplicationTracker appTracker = new ApplicationTracker(pingTime, application);
        eventListeners.forEach(listener -> listener.subscribe(appTracker));
        appTracker.start();
        return appTracker;
    }
}
