package pl.edu.agh.arbeit.gui.view;

import javafx.scene.Group;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import pl.edu.agh.arbeit.gui.controler.MainWindowController;
import pl.edu.agh.arbeit.tracker.Application;
import pl.edu.agh.arbeit.tracker.trackers.ApplicationTracker;

import java.util.List;

public class AppListItem extends Group {
    private Line verticalLine;
    private Line horizontalLine;
    private Text appNameText;
    private Application application;
    private DeleteAppButton deleteAppButton;
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
        });
    }
}
