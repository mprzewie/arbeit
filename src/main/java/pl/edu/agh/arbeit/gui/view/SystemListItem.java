package pl.edu.agh.arbeit.gui.view;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pl.edu.agh.arbeit.gui.Main;
import pl.edu.agh.arbeit.gui.controler.SystemSettingsController;
import pl.edu.agh.arbeit.gui.model.ConfigProvider;

import java.io.IOException;

public class SystemListItem extends Pane {

    private FontAwesomeIconView settingsIcon;
    private Text systemText;

    private TimeLine timeLine;
    private ConfigProvider appConfig;

    private final String standard = AppAdder.class.getResource("Standard.css").toExternalForm();
    private final String dark = AppAdder.class.getResource("Dark.css").toExternalForm();

    private String styleNow;

    public SystemListItem(ConfigProvider appConfig, String styleNow) {
        Line verticalLine = new Line();
        verticalLine.setStartX(120);
        verticalLine.setEndX(120);
        verticalLine.setStartY(0);
        verticalLine.setEndY(50);
        this.getChildren().add(verticalLine);

        this.appConfig = appConfig;
        this.styleNow = styleNow;

        Line f1 = new Line();
        f1.setStartX(0);
        f1.setEndX(120);
        f1.setStartY(50);
        f1.setEndY(50);
        this.getChildren().add(f1);

        systemText = new Text("System");
        systemText.setLayoutX(10);
        systemText.setLayoutY(30);
        this.getChildren().add(systemText);

        this.timeLine = new TimeLine(
                appConfig.getInfo().getSystemActiveColor().getColorFx(),
                appConfig.getInfo().getSystemPassiveColor().getColorFx(),
                appConfig.getInfo().getSystemBackgroundColor().getColorFx()
        );
        this.settingsIcon = new FontAwesomeIconView(FontAwesomeIcon.COG);
        this.settingsIcon.setStyleClass("icon");
        this.settingsIcon.setSize("22px");
        this.settingsIcon.setLayoutX(97);
        this.settingsIcon.setLayoutY(22);
        this.settingsIcon.getStyleClass().add("settings-button");
        this.getChildren().add(settingsIcon);

        this.timeLine = new TimeLine();
        this.getChildren().add(timeLine);

        initSettingsButton();
    }

    public void setTimeLine(TimeLine timeLine) {
        getChildren().remove(this.timeLine);
        getChildren().add(timeLine);
        this.timeLine = timeLine;
    }

    public TimeLine getTimeLine() {
        return timeLine;
    }

    public void setTextWhite() {
        systemText.setFill(Color.SNOW);
    }

    public void setTextBlack() {
        systemText.setFill(Color.BLACK);
    }

    private void initSettingsButton(){
        FontAwesomeIconView settingsIcon = new FontAwesomeIconView(FontAwesomeIcon.COG);
        settingsIcon.setSize("22px");
        settingsIcon.setLayoutX(97);
        settingsIcon.setLayoutY(20);

        settingsIcon.setOnMouseClicked(event -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Main.class.getResource("view/SystemSettingsPane.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                SystemSettingsController appSettingsController = loader.getController();
                appSettingsController.init(this, stage, styleNow);
                stage.setScene(new Scene(root, 360.0, 356.0));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        this.getChildren().add(settingsIcon);
    }

    public Text getSystemText() {
        return systemText;
    }

    public ConfigProvider getAppConfig() {
        return appConfig;
    }
}
