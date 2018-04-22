package pl.edu.agh.arbeit.gui.view;

import javafx.scene.Group;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

public class DeleteAppButton extends Group{
    private Line line1;
    private Line line2;

    public DeleteAppButton(){
        this.line1 = new Line();
        this.line1.setStartX(-5);
        this.line1.setStartY(-5);
        this.line1.setEndX(5);
        this.line1.setEndY(5);
        this.line1.setStroke(Paint.valueOf("GREY"));
        this.line1.setStrokeLineCap(StrokeLineCap.ROUND);
        this.line1.setStrokeWidth(4);
        this.getChildren().add(line1);

        this.line2 = new Line();
        this.line2.setStartX(-5);
        this.line2.setStartY(5);
        this.line2.setEndX(5);
        this.line2.setEndY(-5);
        this.line2.setStroke(Paint.valueOf("GREY"));
        this.line2.setStrokeLineCap(StrokeLineCap.ROUND);
        this.line2.setStrokeWidth(4);
        this.getChildren().add(line2);
    }
}
