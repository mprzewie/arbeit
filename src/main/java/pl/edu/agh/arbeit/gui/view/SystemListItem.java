package pl.edu.agh.arbeit.gui.view;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class SystemListItem extends Pane {

    private FontAwesomeIconView settingsIcon;
    private TimeLine timeLine;

    public SystemListItem() {
        Line verticalLine = new Line();
        verticalLine.setStartX(120);
        verticalLine.setEndX(120);
        verticalLine.setStartY(0);
        verticalLine.setEndY(50);
        this.getChildren().add(verticalLine);


        Line f1 = new Line();
        f1.setStartX(0);
        f1.setEndX(120);
        f1.setStartY(50);
        f1.setEndY(50);
        this.getChildren().add(f1);

        Text systemText = new Text("System");
        systemText.setLayoutX(10);
        systemText.setLayoutY(30);
        this.getChildren().add(systemText);

        this.settingsIcon = new FontAwesomeIconView(FontAwesomeIcon.COG);
        this.settingsIcon.setStyleClass("icon");
        this.settingsIcon.setSize("22px");
        this.settingsIcon.setLayoutX(97);
        this.settingsIcon.setLayoutY(22);
        this.getChildren().add(settingsIcon);

        this.timeLine = new TimeLine();
        this.getChildren().add(timeLine);
    }

    public TimeLine getTimeLine() {
        return timeLine;
    }
}
