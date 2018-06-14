package pl.edu.agh.arbeit.gui.view;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pl.edu.agh.arbeit.gui.Main;
import pl.edu.agh.arbeit.gui.controler.AppSettingsController;
import pl.edu.agh.arbeit.gui.controler.MainWindowController;
import pl.edu.agh.arbeit.tracker.Application;
import pl.edu.agh.arbeit.tracker.trackers.ApplicationTracker;
import pl.edu.agh.arbeit.tracker.trackers.AsyncTracker;

import java.io.IOException;
import java.util.List;

public class AppListItem extends Pane {


    private final String standard = AppAdder.class.getResource("Standard.css").toExternalForm();
    private final String dark = AppAdder.class.getResource("Dark.css").toExternalForm();

    private Application application;
    private List<ApplicationTracker> trackers;
    private TimeLine timeLine;
    private Text appNameText;

    private String styleNow;


    public AppListItem(Application application, List<ApplicationTracker> trackers, MainWindowController mainWindowController, String styleNow) {
        this.application = application;
        this.trackers = trackers;

        initApplicationName();
        initHorizontalSmallLine();
        initVerticalLine();
        initSettingsButton();
        initDeleteButton(mainWindowController);

        timeLine = new TimeLine(application.getActiveColor().getColorFx(),
                application.getPassiveColor().getColorFx(),
                application.getBackgroundColor().getColorFx()
                );
        this.getChildren().add(timeLine);
        this.styleNow = styleNow;
    }

    private void initSettingsButton(){
        FontAwesomeIconView settingsIcon = new FontAwesomeIconView(FontAwesomeIcon.COG);
        settingsIcon.setSize("22px");
        settingsIcon.setLayoutX(97);
        settingsIcon.setLayoutY(20);
        settingsIcon.getStyleClass().add("settings-button");

        settingsIcon.setOnMouseClicked(event -> {
                        try {
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(Main.class.getResource("view/AppSettingsPane.fxml"));
                            Parent root = loader.load();
                            Stage stage = new Stage();
                            AppSettingsController appSettingsController = loader.getController();
                            appSettingsController.init(this, stage, styleNow);
                            stage.setScene(new Scene(root, 360.0, 356.0));
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
        });

        this.getChildren().add(settingsIcon);
    }

    private void initDeleteButton(MainWindowController mainWindowController){
        FontAwesomeIconView deleteAppButton = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
        deleteAppButton.setLayoutX(98);
        deleteAppButton.setLayoutY(44);
        deleteAppButton.setSize("22px");
        deleteAppButton.getStyleClass().add("trash-button");

        deleteAppButton.setOnMouseClicked(event -> {
            trackers.stream()
                    .filter(t -> t.getApplication().equals(application))
                    .findFirst()
                    .ifPresent(AsyncTracker::stop);
            trackers.removeIf(tracker -> tracker.getApplication().equals(this.application));

            mainWindowController.removeAppView(this);
            mainWindowController.removeApp(this.application.getProgramName());
        });

        this.getChildren().add(deleteAppButton);
    }

    private void initApplicationName(){
        String applicationName = application.getDisplayName();
        if(application.getDisplayName().length() > 11)
            applicationName = applicationName.substring(0,10) + "...";

        appNameText = new Text(10, 30, applicationName);
        this.getChildren().add(appNameText);
    }

    private void initHorizontalSmallLine(){
        Line horizontalLine = new Line();
        horizontalLine.setStartX(0);
        horizontalLine.setEndX(120);
        horizontalLine.setStartY(50);
        horizontalLine.setEndY(50);
        this.getChildren().add(horizontalLine);
    }

    private void initVerticalLine(){
        Line verticalLine = new Line();
        verticalLine.setStartX(120);
        verticalLine.setEndX(120);
        verticalLine.setStartY(0);
        verticalLine.setEndY(49);
        this.getChildren().add(verticalLine);
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public TimeLine getTimeLine() {
        return timeLine;
    }

    public void setTimeLine(TimeLine timeLine) {
        getChildren().remove(this.timeLine);
        getChildren().add(timeLine);
        this.timeLine = timeLine;
    }

    public void setTextWhite() {
        appNameText.setFill(Color.SNOW);

        styleNow = standard;

    }

    public void setTextBlack() {
        appNameText.setFill(Color.BLACK);

        styleNow = dark;
    }

    public Text getAppNameText() {
        return appNameText;
    }
}
