package pl.edu.agh.arbeit.gui.view;

import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import pl.edu.agh.arbeit.tracker.events.EventType;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class TimeLine extends Pane {
    private List<RectangleTyped> rectangleList;

    private Color passiveColor = Color.rgb(193, 16, 9);
    private Color activeColor = Color.rgb(10, 128, 4);
    private Color offColor = Color.grayRgb(33);
    private Color backgroundColor = Color.grayRgb(84);
    private BorderPane prevBorderPane;

    public TimeLine() {
        this.setLayoutX(121);
        rectangleList = new LinkedList<>();
        Rectangle startRectangle = new Rectangle(880, 49);
        startRectangle.setFill(backgroundColor);
        //rectangleList.add(startRectangle);
        this.getChildren().add(startRectangle);
        Line downLine = new Line();
        downLine.setStartY(50);
        downLine.setEndY(50);
        downLine.setEndX(1010);
        this.getChildren().add(downLine);

        initEventHandler();
    }

    public void addEvent(EventType eventType, LocalDateTime date) {
        if (!eventType.equals(EventType.START)) {
            if (eventType.equals(getLastElemEventType())) {
                if (getPositionFromDate(date) - rectangleList.get(getLastElemIndex()).getLayoutX() > 0)
                    rectangleList.get(getLastElemIndex()).setWidth(getPositionFromDate(date) - rectangleList.get(getLastElemIndex()).getLayoutX());
            } else {
                if (rectangleList.size() > 0)
                    if (getPositionFromDate(date) - rectangleList.get(getLastElemIndex()).getLayoutX() > 1.5) {
                        rectangleList.get(getLastElemIndex()).setWidth(getPositionFromDate(date) - rectangleList.get(getLastElemIndex()).getLayoutX() - 1);
                        RectangleTyped rec = new RectangleTyped(1, 49);
                        rec.setLayoutX(getPositionFromDate(date));
                        rec.setEventType(eventType);
                        rec.setFill(getColorFromEventType(eventType));
                        rectangleList.add(rec);
                        getChildren().add(rec);
                    } else{
                        rectangleList.get(getLastElemIndex()).setFill(getColorFromEventType(eventType));
                    }
                if (rectangleList.size() == 0) {
                    RectangleTyped rec = new RectangleTyped(1, 49);
                    rec.setLayoutX(getPositionFromDate(date));
                    rec.setEventType(eventType);
                    rec.setFill(getColorFromEventType(eventType));
                    rectangleList.add(rec);
                    getChildren().add(rec);
                }
            }
        }
    }

    private String getDateFromPosition(double position) {
        int seconds = (int) (86400 * position / 880);
        Integer hour = seconds / 3600;
        Integer minuts = (seconds - hour * 3600) / 60;
        Integer sec = (seconds - hour * 3600 - minuts * 60);

        String h = hour.toString();
        if(hour.toString().length() == 1)
            h = "0" + hour.toString();

        String m = minuts.toString();
        if(minuts.toString().length() == 1)
            m = "0" + minuts.toString();

        String s = sec.toString();
        if(sec.toString().length() == 1)
            s = "0" + sec.toString();

        return h + ":" + m + ":" + s;

    }

    private int getPositionFromDate(LocalDateTime date) {
        return (int) (880 * (date.getHour() * 3600 + date.getMinute() * 60 + date.getSecond()) / 86400.0);
    }

    private EventType getLastElemEventType() {
        if (rectangleList.size() > 0)
            return rectangleList.get(getLastElemIndex()).getEventType();
        else
            return EventType.STOP;
    }

    private int getLastElemIndex() {
        if (rectangleList.size() > 0)
            return rectangleList.size() - 1;
        return 0;
    }

    private Color getColorFromEventType(EventType eventType) {
        if (eventType.equals(EventType.ACTIVE))
            return this.activeColor;
        if (eventType.equals(EventType.PASSIVE))
            return this.passiveColor;
        if (eventType.equals(EventType.START))
            return this.activeColor;
        //if(eventType.equals(EventType.STOP))
        return this.backgroundColor;
    }

    private void initEventHandler() {
        addEventHandler(MouseEvent.ANY,
                e -> {

                    getChildren().remove(prevBorderPane);
                    //System.out.println(String.valueOf(e.getX()) + "  " + String.valueOf(e.getY()));

                    BorderPane borderPane = new BorderPane();
                    borderPane.setMinWidth(60);
                    borderPane.setMinHeight(19);
                    borderPane.setBorder(
                            new Border(
                                    new BorderStroke(Paint.valueOf("BLACK"),
                                            BorderStrokeStyle.SOLID,
                                            CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                    borderPane.setBackground(new Background(new BackgroundFill(Paint.valueOf("WHITE"), CornerRadii.EMPTY, Insets.EMPTY)));
                    if (e.getX() > 1 && e.getX() < (880 - borderPane.getWidth()))
                        borderPane.setLayoutX(e.getX());
                    if (e.getY() >= 19 && e.getY() + borderPane.getHeight() <= 49)
                        borderPane.setLayoutY(e.getY() - 19);
                    else
                        borderPane.setLayoutY(0);

                    Text text = new Text(0, 0, getDateFromPosition(e.getX()));
                    text.setLayoutY(16);
                    text.setLayoutX(2);
                    borderPane.getChildren().add(text);
                    getChildren().add(borderPane);

                    prevBorderPane = borderPane;
                    if (e.getEventType().equals(MouseEvent.MOUSE_EXITED)) {
                        getChildren().remove(borderPane);
                    }
                });
    }

}
