package pl.edu.agh.arbeit.gui.view;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import pl.edu.agh.arbeit.tracker.events.EventType;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class TimeLine extends Pane{
    private List<RectangleTyped> rectangleList;

    private Color activeColor = Color.rgb(193, 16, 9);
    private Color passiveColor = Color.rgb(83, 7, 4);;
    private Color offColor = Color.grayRgb(33);
    private Color backgroundColor = Color.grayRgb(84);

    public TimeLine(){
        this.setLayoutX(121);
        rectangleList = new LinkedList<>();
        Rectangle startRectangle = new Rectangle(880,49);
        startRectangle.setFill(backgroundColor);
        //rectangleList.add(startRectangle);
        this.getChildren().add(startRectangle);
        Line downLine = new Line();
        downLine.setStartY(50);
        downLine.setEndY(50);
        downLine.setEndX(1010);
        this.getChildren().add(downLine);
    }

    public void addEvent(EventType eventType, LocalDateTime date){
        if(eventType.equals(getLastElemEventType())){
            rectangleList.get(getLastElemIndex()).setWidth(getPositionFromDate(date) - rectangleList.get(getLastElemIndex()).getLayoutX());
        }
        else {
            if(rectangleList.size()>0)
                rectangleList.get(getLastElemIndex()).setWidth(getPositionFromDate(date) - rectangleList.get(getLastElemIndex()).getLayoutX() -1);
            RectangleTyped rec = new RectangleTyped(1,49);
            rec.setLayoutX(getPositionFromDate(date));
            rec.setEventType(eventType);
            rec.setFill(getColorFromEventType(eventType));
            rectangleList.add(rec);
            this.getChildren().add(rec);
        }
    }

    private int getPositionFromDate(LocalDateTime date){
        return 880*(date.getHour()*3600 + date.getMinute()*60 + date.getSecond()) / 86400;
    }

    private EventType getLastElemEventType(){
        if(rectangleList.size() >0)
            return rectangleList.get(getLastElemIndex()).getEventType();
        else
            return EventType.STOP;
    }

    private int getLastElemIndex(){
        if(rectangleList.size() > 0)
            return rectangleList.size() - 1;
        return 0;
    }

    private Color getColorFromEventType(EventType eventType){
        if(eventType.equals(EventType.ACTIVE))
            return this.activeColor;
        if(eventType.equals(EventType.PASSIVE))
            return this.passiveColor;
        if(eventType.equals(EventType.START))
            return this.backgroundColor;
        //if(eventType.equals(EventType.STOP))
        return this.backgroundColor;
    }

}
