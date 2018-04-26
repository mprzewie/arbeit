package pl.edu.agh.arbeit.gui.view;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import pl.edu.agh.arbeit.gui.controler.MainWindowController;
import pl.edu.agh.arbeit.tracker.Application;
import pl.edu.agh.arbeit.tracker.trackers.ApplicationTracker;

import java.util.List;

public class AppListItem extends Pane {

    private Application application;
    private FontAwesomeIconView deleteAppButton;
    private FontAwesomeIconView settingsIcon;
    private List<ApplicationTracker> trackers;

    public AppListItem(Application application, List<ApplicationTracker> trackers, MainWindowController mainWindowController) {
        this.application = application;
        this.trackers = trackers;

        Line verticalLine = new Line();
        verticalLine.setStartX(120);
        verticalLine.setEndX(120);
        verticalLine.setStartY(0);
        verticalLine.setEndY(49);
        this.getChildren().add(verticalLine);

        Line horizontalLine = new Line();
        horizontalLine.setStartX(0);
        horizontalLine.setEndX(120);
        horizontalLine.setStartY(50);
        horizontalLine.setEndY(50);
        this.getChildren().add(horizontalLine);

        Text appNameText = new Text(10, 30, application.getName());
        this.getChildren().add(appNameText);

        this.deleteAppButton = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
        deleteAppButton.setLayoutX(98);
        deleteAppButton.setLayoutY(44);
        deleteAppButton.setSize("22px");
        this.getChildren().add(deleteAppButton);

        this.settingsIcon = new FontAwesomeIconView(FontAwesomeIcon.COG);
        this.settingsIcon.setSize("22px");
        this.settingsIcon.setLayoutX(97);
        this.settingsIcon.setLayoutY(20);
        this.getChildren().add(settingsIcon);

        initDeleteButton(mainWindowController);
    }

    private void initDeleteButton(MainWindowController mainWindowController){
        deleteAppButton.setOnMouseClicked(event -> {
            trackers.removeIf(tracker -> tracker.getApplication().equals(this.application));
            mainWindowController.removeAppView(this);
            mainWindowController.removeApp(this.application.getProgramName());
        });
    }
}
