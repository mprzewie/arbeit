package pl.edu.agh.arbeit.gui;

import javafx.application.Application;
import javafx.stage.Stage;
import pl.edu.agh.arbeit.data.DatabaseInitializer;
import pl.edu.agh.arbeit.gui.controler.OverviewController;

public class Main extends Application {
    private Stage primaryStage;
    private OverviewController overviewController;

    @Override
    public void start(Stage primaryStage) {
        overviewController = new OverviewController(primaryStage);
    }
    public static void main(String[] args) {
        DatabaseInitializer.initializeDatabase();
        launch(args);
    }
}