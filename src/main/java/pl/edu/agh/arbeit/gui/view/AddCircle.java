package pl.edu.agh.arbeit.gui.view;

import javafx.scene.Group;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;


public class AddCircle extends Group {
    private Circle circle;
    private Line horizontalLine;
    private Line verticalLine;
    public AddCircle(){
        this.circle = new Circle();
        this.circle.setFill(Paint.valueOf("WHITE"));
        this.circle.setStroke(Paint.valueOf("BLACK"));
        this.circle.setStrokeType(StrokeType.INSIDE);
        this.circle.setRadius(16);
        this.getChildren().add(circle);

        this.horizontalLine = new Line();
        this.horizontalLine.setStartX(-10);
        this.horizontalLine.setStartY(0);
        this.horizontalLine.setEndX(10);
        this.horizontalLine.setEndY(0);
        this.horizontalLine.setStroke(Paint.valueOf("BLACK"));
        this.horizontalLine.setStrokeLineCap(StrokeLineCap.ROUND);
        this.horizontalLine.setStrokeWidth(4);
        this.getChildren().add(horizontalLine);

        this.verticalLine = new Line();
        this.verticalLine.setStartX(0);
        this.verticalLine.setStartY(-10);
        this.verticalLine.setEndX(0);
        this.verticalLine.setEndY(10);
        this.verticalLine.setStroke(Paint.valueOf("BLACK"));
        this.verticalLine.setStrokeLineCap(StrokeLineCap.ROUND);
        this.verticalLine.setStrokeWidth(4);
        this.getChildren().add(verticalLine);
    }
}
