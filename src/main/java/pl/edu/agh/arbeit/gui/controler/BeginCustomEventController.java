package pl.edu.agh.arbeit.gui.controler;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class BeginCustomEventController {
    private Stage eventsStage;
    private MainWindowController mainWindowController;

    @FXML
    private Button beginButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField eventNameTextField;

    public void init(Stage eventsStage, MainWindowController mainWindowController) {
        this.eventsStage = eventsStage;
        this.mainWindowController = mainWindowController;
        eventsStage.setTitle("Add Custom Event");
        initBeginButton();
        initCancelButton();
    }

    private void initBeginButton(){
        beginButton.setOnAction(event -> {
            mainWindowController.beginCustomEvent(eventNameTextField.getText());
            eventsStage.close();
        });
    }

    private void initCancelButton(){
        cancelButton.setOnAction(event -> eventsStage.close());
    }
}
