package pl.edu.agh.arbeit.gui.controler;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class SavedInfoController {
    @FXML
    private Button okButton;

    @FXML
    private Label label;

    @FXML
    private AnchorPane anchorPane;

    public void init(Stage stage, String styleType, String title, String labelText){
        stage.setTitle(title);
        label.setText(labelText);
        okButton.setOnAction(event -> stage.close());
        anchorPane.getStylesheets().clear();
        anchorPane.getStylesheets().add(styleType);
    }
}
