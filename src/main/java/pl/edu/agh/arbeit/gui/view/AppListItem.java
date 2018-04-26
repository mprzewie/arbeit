package pl.edu.agh.arbeit.gui.view;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import pl.edu.agh.arbeit.gui.controler.MainWindowController;
import pl.edu.agh.arbeit.gui.model.AppInfo;
import pl.edu.agh.arbeit.tracker.Application;
import pl.edu.agh.arbeit.tracker.trackers.ApplicationTracker;

import java.util.List;

public class AppListItem extends Pane {
    private Line verticalLine;
    private Line horizontalLine;
    private Text appNameText;
    private Application application;
    private FontAwesomeIconView deleteAppButton;
    private FontAwesomeIconView settingsIcon;
    private List<ApplicationTracker> trackers;

    public AppListItem(Application application, List<ApplicationTracker> trackers, MainWindowController mainWindowController) {
        this.application = application;
        this.trackers = trackers;

        this.verticalLine = new Line();
        this.verticalLine.setStartX(120);
        this.verticalLine.setEndX(120);
        this.verticalLine.setStartY(0);
        this.verticalLine.setEndY(50);
        this.getChildren().add(verticalLine);

        this.horizontalLine = new Line();
        this.horizontalLine.setStartX(0);
        this.horizontalLine.setEndX(120);
        this.horizontalLine.setStartY(50);
        this.horizontalLine.setEndY(50);
        this.getChildren().add(horizontalLine);

        this.appNameText = new Text(10, 30, application.getName());
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
