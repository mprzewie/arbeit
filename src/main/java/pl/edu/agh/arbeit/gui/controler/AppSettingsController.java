package pl.edu.agh.arbeit.gui.controler;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pl.edu.agh.arbeit.gui.model.StyleType;
import pl.edu.agh.arbeit.tracker.Application;

public class AppSettingsController {
    private Application application;
    private Stage stage;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField appNameTextField;

    @FXML
    private TextField programNameTextField;

    @FXML
    private ChoiceBox pingTimeChoiceBox;

    public void init(Application application, Stage stage, String styleType) {
        this.application = application;
        this.stage = stage;
        this.stage.setTitle("Settings");
        this.stage.setResizable(false);

        appNameTextField.setText(application.getDisplayName());
        programNameTextField.setText(application.getProgramName());
        pingTimeChoiceBox.setDisable(true);

        initCancelButton();
        initSaveButton();
        anchorPane.getStylesheets().clear();
        anchorPane.getStylesheets().add(styleType);
    }

    private void initSaveButton() {
        saveButton.setOnAction(event ->
            stage.close()
        );
    }

    private void initCancelButton(){
        cancelButton.setOnAction(event ->
                stage.close()
        );
    }
}
