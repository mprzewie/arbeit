package pl.edu.agh.arbeit.gui.view;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pl.edu.agh.arbeit.gui.Main;
import pl.edu.agh.arbeit.gui.controler.AppSettingsController;
import pl.edu.agh.arbeit.gui.controler.MainWindowController;
import pl.edu.agh.arbeit.gui.controler.ReportsController;
import pl.edu.agh.arbeit.tracker.Application;
import pl.edu.agh.arbeit.tracker.trackers.ApplicationTracker;

import java.io.IOException;
import java.util.List;

public class AppListItem extends Pane {

    private Application application;
    private List<ApplicationTracker> trackers;

    public AppListItem(Application application, List<ApplicationTracker> trackers, MainWindowController mainWindowController) {
        this.application = application;
        this.trackers = trackers;

        initApplicationName();
        initHorizontalSmallLine();
        initVerticalLine();
        initSettingsButton();
        initDeleteButton(mainWindowController);

        this.getChildren().add(new TimeLine());
    }

    private void initSettingsButton(){
        FontAwesomeIconView settingsIcon = new FontAwesomeIconView(FontAwesomeIcon.COG);
        settingsIcon.setSize("22px");
        settingsIcon.setLayoutX(97);
        settingsIcon.setLayoutY(20);

        settingsIcon.setOnMouseClicked(event -> {
                        try {
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(Main.class.getResource("view/AppSettingsPane.fxml"));
                            Parent root = loader.load();
                            Stage stage = new Stage();
                            AppSettingsController appSettingsController = loader.getController();
                            appSettingsController.init(application,stage);
                            stage.setScene(new Scene(root, 359.0, 229.0));
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
        });
    }

    private void initDeleteButton(MainWindowController mainWindowController){
        FontAwesomeIconView deleteAppButton = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
        deleteAppButton.setLayoutX(98);
        deleteAppButton.setLayoutY(44);
        deleteAppButton.setSize("22px");

        deleteAppButton.setOnMouseClicked(event -> {
            trackers.removeIf(tracker -> tracker.getApplication().equals(this.application));
            mainWindowController.removeAppView(this);
            mainWindowController.removeApp(this.application.getProgramName());
        });

        this.getChildren().add(deleteAppButton);
    }

    private void initApplicationName(){
        String applicationName = application.getName();
        if(application.getName().length() > 11)
            applicationName = applicationName.substring(0,10) + "...";

        Text appNameText = new Text(10, 30, applicationName);
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
}
