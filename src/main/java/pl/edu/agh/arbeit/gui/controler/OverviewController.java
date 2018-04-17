package pl.edu.agh.arbeit.gui.controler;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pl.edu.agh.arbeit.gui.Main;
import pl.edu.agh.arbeit.gui.model.FakeTracker;

import java.io.IOException;

public class OverviewController {
    private Stage primaryStage;
    private final String APP_TITLE = "DiceMaster";
    private final String MAIN_PAIN_RPATH = "view/MainWindowPane.fxml";

    public OverviewController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        try {
            this.primaryStage.setTitle(APP_TITLE);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource(MAIN_PAIN_RPATH));
            System.out.print(Main.class.getResource(MAIN_PAIN_RPATH));
            AnchorPane rootLayout = loader.load();
            MainWindowController mainWindowController = loader.getController();
            mainWindowController.init(this, new FakeTracker());
            Scene scene = new Scene(rootLayout);
            this.primaryStage.setScene(scene);
            this.primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
