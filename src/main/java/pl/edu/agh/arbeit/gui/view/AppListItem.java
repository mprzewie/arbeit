package pl.edu.agh.arbeit.gui.view;

import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import pl.edu.agh.arbeit.gui.controler.MainWindowController;
import pl.edu.agh.arbeit.tracker.Application;
import pl.edu.agh.arbeit.tracker.trackers.ApplicationTracker;

import java.util.List;

public class AppListItem extends Group {
    private Application application;
    private DeleteAppButton deleteAppButton;
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

        this.deleteAppButton = new DeleteAppButton();
        deleteAppButton.setLayoutX(114);
        deleteAppButton.setLayoutY(6);
        this.getChildren().add(deleteAppButton);

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
