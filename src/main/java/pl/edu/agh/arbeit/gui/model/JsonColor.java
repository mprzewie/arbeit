package pl.edu.agh.arbeit.gui.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.scene.paint.Color;

public class JsonColor {
    private String color;
    private Color colorFx;

    public JsonColor() {
    }

    public JsonColor(String color) {
        this.color = color;
        this.colorFx = Color.web(color);
    }

    public JsonColor(Color colorFx) {
        this.colorFx = colorFx;
        this.color = toRGBCode(colorFx);
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        this.colorFx = Color.web(color);
    }

    @JsonIgnore
    public Color getColorFx() {
        return colorFx;
    }

    public void setColorFx(Color colorFx) {
        this.colorFx = colorFx;
        this.color = toRGBCode(colorFx);
    }

    private String toRGBCode( Color color )
    {
        return String.format( "#%02X%02X%02X",
                (int)( color.getRed() * 255 ),
                (int)( color.getGreen() * 255 ),
                (int)( color.getBlue() * 255 ) );
    }
}
