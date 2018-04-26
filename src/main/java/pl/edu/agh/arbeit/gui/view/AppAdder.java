package pl.edu.agh.arbeit.gui.view;

import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.shape.Line;
import pl.edu.agh.arbeit.data.EventListener;
import pl.edu.agh.arbeit.gui.controler.MainWindowController;
import pl.edu.agh.arbeit.gui.model.AppConfig;
import pl.edu.agh.arbeit.gui.model.AppInfo;
import pl.edu.agh.arbeit.gui.model.ConfigProvider;
import pl.edu.agh.arbeit.tracker.Application;
import pl.edu.agh.arbeit.tracker.trackers.ApplicationTracker;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class AppAdder extends Group {
    private final static long APP_TRACKER_PING_TIME = 5;

    private AddCircle addCircle;
    private TextField appNameTextField;
    private List<ApplicationTracker> applicationTrackers;
    private EventListener eventListener;
    private ConfigProvider appConfig;

    public AppAdder(MainWindowController mainWindowController, List<ApplicationTracker> applicationTrackers, EventListener eventListener) {
        this.applicationTrackers = applicationTrackers;
        this.eventListener = eventListener;
        this.appConfig = new AppConfig();
        this.addCircle = new AddCircle();
        this.getChildren().add(addCircle);
        addCircle.setDisable(true);

        Line verticalLine = new Line();
        verticalLine.setStartX(120);
        verticalLine.setEndX(120);
        verticalLine.setStartY(0);
        verticalLine.setEndY(50);
        this.getChildren().add(verticalLine);

        this.appNameTextField = new TextField();
        this.appNameTextField.setLayoutX(130);
        this.appNameTextField.setLayoutY(10);
        this.getChildren().add(appNameTextField);

        forbidEmptyAppName();
        initTrackingAppsFromConfig(appConfig.getAppsToTrack(), mainWindowController);
        initAddButton(mainWindowController);
    }

    private void initTrackingAppsFromConfig(List<AppInfo> appInfos, MainWindowController mainWindowController){
        List<AppInfo> tempList= new LinkedList<>();
        tempList.addAll(appInfos);
        tempList.forEach(e -> addApp(mainWindowController, new Application(e.getName(),e.getProgramName()), e.getPingTime()));
    }

    private boolean isAppNotTracked(Application application){
        return applicationTrackers.stream()
                .noneMatch(tracker -> tracker.getApplication().equals(application));
    }

    private void forbidEmptyAppName(){
        appNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals(""))
                addCircle.setDisable(true);
            else
                addCircle.setDisable(false);
        });
    }

    private void initAddButton(MainWindowController mainWindowController){
        addCircle.setOnMouseClicked(event ->{
            Application newApp =  new Application(this.appNameTextField.getText(), this.appNameTextField.getText());
            if(isAppNotTracked(newApp))
                appConfig.addAppToTrack(new AppInfo(newApp.getName(),newApp.getProgramName(),APP_TRACKER_PING_TIME));
            addApp(mainWindowController,newApp,APP_TRACKER_PING_TIME);
        });
    }

    private void addApp(MainWindowController mainWindowController, Application application, Long pingTime){
        if(isAppNotTracked(application)) {
            this.applicationTrackers.add(createTracker(pingTime, application, mainWindowController));
            mainWindowController.addNewAppView(application);
        }
    }

    private ApplicationTracker createTracker(long pingTime,  Application application, MainWindowController mainWindowController){
        ApplicationTracker appTracker = new ApplicationTracker(pingTime, application);
        eventListener.subscribe(appTracker);
        appTracker.start();
        mainWindowController.addToTrackerList(appTracker);
        return appTracker;
    }
}
