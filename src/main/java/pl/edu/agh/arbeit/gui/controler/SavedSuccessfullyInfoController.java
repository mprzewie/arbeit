package pl.edu.agh.arbeit.gui.controler;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class SavedSuccessfullyInfoController {
    @FXML
    private Button okButton;

    public void init(Stage stage){
        stage.setTitle("Saved Successfully Info");
        okButton.setOnAction(event -> stage.close());
    }
}
