package pl.edu.agh.arbeit.gui.controler;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pl.edu.agh.arbeit.gui.Main;

import java.io.IOException;

public class OverviewController {
    private Stage primaryStage;
    private Scene scene;
    private final String APP_TITLE = "Arbe.IT";
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
            scene = new Scene(rootLayout);
            mainWindowController.init(this, primaryStage.heightProperty().multiply(0.85));
            this.primaryStage.setResizable(false);
            this.primaryStage.setScene(scene);

            this.primaryStage.setOnCloseRequest(t -> {
                mainWindowController.getAppConfig().update();
                mainWindowController.stopTrackingAll();
                Platform.exit();
                System.exit(0);
            });

            this.primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public Scene getScene() {
        return scene;
    }
}
