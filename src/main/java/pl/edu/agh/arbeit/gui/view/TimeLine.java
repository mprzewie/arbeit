package pl.edu.agh.arbeit.gui.view;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import pl.edu.agh.arbeit.tracker.events.EventType;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class TimeLine extends Pane{
    private List<Rectangle> rectangleList;

    private Color activeColor = Color.rgb(193, 16, 9);
    private Color passiveColor = Color.rgb(83, 7, 4);;
    private Color offColor = Color.grayRgb(33);
    private Color backgroundColor = Color.grayRgb(84);

    public TimeLine(){
        this.setLayoutX(121);
        rectangleList = new LinkedList<>();
        Rectangle startRectangle = new Rectangle(880,49);
        startRectangle.setFill(backgroundColor);
        rectangleList.add(startRectangle);
        this.getChildren().add(startRectangle);
        Line downLine = new Line();
        downLine.setStartY(50);
        downLine.setEndY(50);
        downLine.setEndX(1010);
        this.getChildren().add(downLine);
    }

    private void addEvent(EventType eventType, Date date){

    }

}
