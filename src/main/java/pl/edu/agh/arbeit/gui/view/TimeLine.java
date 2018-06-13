package pl.edu.agh.arbeit.gui.view;

import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import pl.edu.agh.arbeit.tracker.events.Event;
import pl.edu.agh.arbeit.tracker.events.EventType;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class TimeLine extends Pane {
    private List<RectangleTyped> rectangleList;
    private Rectangle backgroundRectangle;

    private Color passiveColor = Color.rgb(193, 16, 9);
    private Color activeColor = Color.rgb(10, 128, 4);
    private Color backgroundColor = Color.grayRgb(70);

    private BorderPane prevBorderPane;

    private static int TIME_LINE_WIDTH = 870;
    private static int TIME_LINE_HEIGHT = 49;

    private static int TIME_SHOWER_WIDTH = 60;
    private static int TIME_SHOWER_HEIGHT = 19;

    public  TimeLine(Color passiveColor, Color activeColor, Color backgroundColor){
        this.passiveColor = passiveColor;
        this.activeColor = activeColor;
        this.backgroundColor = backgroundColor;
        this.init();
    }

    public TimeLine(){
        this.init();
    }

    private void init() {
        this.setLayoutX(121);
        this.setMaxWidth(TIME_LINE_WIDTH);
        rectangleList = new LinkedList<>();
        backgroundRectangle = new Rectangle(TIME_LINE_WIDTH, TIME_LINE_HEIGHT);
        backgroundRectangle.setFill(backgroundColor);
        this.getChildren().add(backgroundRectangle);
        Line downLine = new Line();
        downLine.setStartY(50);
        downLine.setEndY(50);
        downLine.setEndX(1010);
        this.getChildren().add(downLine);
        initEventHandler();

        Line endLine = new Line();
        endLine.setEndX(TIME_LINE_WIDTH+1);
        endLine.setStartX(TIME_LINE_WIDTH+1);
        endLine.setStartY(0.5);
        endLine.setEndY(TIME_LINE_HEIGHT);
        this.getChildren().add(endLine);
    }

    public void addEvent(Event event) {
        EventType eventType = event.getType();
        LocalDateTime dateTime = event.getLocalDateTime();
        if (!eventType.equals(EventType.START)) {
            if (rectangleList.size() > 0) {
                if (eventType.equals(getLastElemEventType())) {
                    if (getPositionFromDate(dateTime) - rectangleList.get(getLastElemIndex()).getLayoutX() > 0)
                        rectangleList.get(getLastElemIndex()).setWidth(getPositionFromDate(dateTime) - rectangleList.get(getLastElemIndex()).getLayoutX());
                } else if (getPositionFromDate(dateTime) - rectangleList.get(getLastElemIndex()).getLayoutX() > 1.5) {
                    rectangleList.get(getLastElemIndex()).setWidth(getPositionFromDate(dateTime) - rectangleList.get(getLastElemIndex()).getLayoutX() - 1);
                    RectangleTyped rec = new RectangleTyped(1, 49);
                    rec.setLayoutX(getPositionFromDate(dateTime));
                    rec.setEventType(eventType);
                    rec.setFill(getColorFromEventType(eventType));
                    rectangleList.add(rec);
                    getChildren().add(rec);
                } else {
                    rectangleList.get(getLastElemIndex()).setFill(getColorFromEventType(eventType));
                    rectangleList.get(getLastElemIndex()).setEventType(eventType);
                }
            } else {
                RectangleTyped rec = new RectangleTyped(1, 49);
                rec.setLayoutX(getPositionFromDate(dateTime));
                rec.setEventType(eventType);
                rec.setFill(getColorFromEventType(eventType));
                rectangleList.add(rec);
                getChildren().add(rec);
            }
        }
    }


    private String getDateFromPosition(double position) {
        int seconds = (int) (86400 * position / TIME_LINE_WIDTH);
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
        return (int) (TIME_LINE_WIDTH * (date.getHour() * 3600 + date.getMinute() * 60 + date.getSecond()) / 86400.0);
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
        backgroundRectangle.addEventHandler(MouseEvent.ANY,
                e -> {
                    getChildren().remove(prevBorderPane);
                    //System.out.println(String.valueOf(e.getX()) + "  " + String.valueOf(e.getY()));

                    BorderPane borderPane = new BorderPane();
                    borderPane.setMinWidth(TIME_SHOWER_WIDTH);
                    borderPane.setMinHeight(TIME_SHOWER_HEIGHT);
                    borderPane.setBorder(
                            new Border(
                                    new BorderStroke(Paint.valueOf("BLACK"),
                                            BorderStrokeStyle.SOLID,
                                            CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                    borderPane.setBackground(new Background(new BackgroundFill(Paint.valueOf("WHITE"), CornerRadii.EMPTY, Insets.EMPTY)));
                    if (e.getX() > 1 && e.getX() + TIME_SHOWER_WIDTH < TIME_LINE_WIDTH) {
                        borderPane.setLayoutX(e.getX() + 1);
                    } else if(e.getX() + TIME_SHOWER_WIDTH >= TIME_LINE_WIDTH){
                        borderPane.setLayoutX(TIME_LINE_WIDTH - TIME_SHOWER_WIDTH);
                    }

                    if (e.getY() >= 20 && e.getY() + borderPane.getHeight() <= 49)
                        borderPane.setLayoutY(e.getY() - 20);
                    else
                        borderPane.setLayoutY(TIME_LINE_HEIGHT - TIME_SHOWER_HEIGHT);

                    Text text = new Text(0, 0, getDateFromPosition(e.getX()));
                    text.setLayoutY(16);
                    text.setLayoutX(2);
                    borderPane.getChildren().add(text);

                    if (!e.getEventType().equals(MouseEvent.MOUSE_EXITED) && !e.getEventType().equals(MouseEvent.MOUSE_EXITED_TARGET)) {
                        getChildren().add(borderPane);
                        prevBorderPane = borderPane;
                    }else {
                        getChildren().remove(prevBorderPane);
                    }
                });
    }

    public Color getPassiveColor() {
        return passiveColor;
    }

    public Color getActiveColor() {
        return activeColor;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setPassiveColor(Color passiveColor) {
        this.passiveColor = passiveColor;
        rectangleList.forEach(rectangleTyped -> {
            if(rectangleTyped.getEventType().equals(EventType.PASSIVE))
                rectangleTyped.setFill(passiveColor);
        });
    }

    public void setActiveColor(Color activeColor) {
        this.activeColor = activeColor;
        rectangleList.forEach(rectangleTyped -> {
            if(rectangleTyped.getEventType().equals(EventType.ACTIVE))
                rectangleTyped.setFill(activeColor);

            if(rectangleTyped.getEventType().equals(EventType.START))
                rectangleTyped.setFill(activeColor);
        });
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        backgroundRectangle.setFill(backgroundColor);
        rectangleList.forEach(rectangleTyped -> {
            if(rectangleTyped.getEventType().equals(EventType.STOP))
                rectangleTyped.setFill(backgroundColor);
        });
    }
}
