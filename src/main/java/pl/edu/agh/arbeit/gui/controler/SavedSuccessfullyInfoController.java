package pl.edu.agh.arbeit.gui.controler;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class SavedSuccessfullyInfoController {
    @FXML
    private Button okButton;


    @FXML
    private AnchorPane anchorPane;

    public void init(Stage stage, String styleType){
        stage.setTitle("Saved Successfully Info");
        okButton.setOnAction(event -> stage.close());
        anchorPane.getStylesheets().clear();
        anchorPane.getStylesheets().add(styleType);
    }
}
