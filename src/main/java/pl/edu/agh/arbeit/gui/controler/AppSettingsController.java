package pl.edu.agh.arbeit.gui.controler;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pl.edu.agh.arbeit.gui.model.JsonColor;
import pl.edu.agh.arbeit.gui.view.AppListItem;

import java.util.stream.IntStream;

public class AppSettingsController {
    private AppListItem appListItem;
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
    private ComboBox<Integer> pingTimeChoiceBox;

    @FXML
    private ColorPicker activeColorPicker;

    @FXML
    private ColorPicker passiveColorPicker;

    @FXML
    private ColorPicker backgroundColorPicker;


    public void init(AppListItem appListItem, Stage stage, String styleType) {
        this.appListItem = appListItem;
        this.stage = stage;
        this.stage.setTitle("Settings");
        this.stage.setResizable(false);

        appNameTextField.setText(appListItem.getApplication().getDisplayName());
        programNameTextField.setText(appListItem.getApplication().getProgramName());

        activeColorPicker.setValue(appListItem.getTimeLine().getActiveColor());
        passiveColorPicker.setValue(appListItem.getTimeLine().getPassiveColor());
        backgroundColorPicker.setValue(appListItem.getTimeLine().getBackgroundColor());

        initCancelButton();
        initSaveButton();
        initPingTimeChoiceBox();
        anchorPane.getStylesheets().clear();
        anchorPane.getStylesheets().add(styleType);
    }

    private void initSaveButton() {
        saveButton.setOnAction(event -> {
                    if (!appListItem.getTimeLine().getActiveColor().equals(activeColorPicker.getValue())) {
                        appListItem.getApplication().setActiveColor(new JsonColor(activeColorPicker.getValue()));
                        appListItem.getTimeLine().setActiveColor(activeColorPicker.getValue());

                    }
                    if (!appListItem.getTimeLine().getPassiveColor().equals(passiveColorPicker.getValue())) {
                        appListItem.getApplication().setPassiveColor(new JsonColor(passiveColorPicker.getValue()));
                        appListItem.getTimeLine().setPassiveColor(passiveColorPicker.getValue());
                    }
                    if (!appListItem.getTimeLine().getBackgroundColor().equals(backgroundColorPicker.getValue())) {
                        appListItem.getApplication().setBackgroundColor(new JsonColor(backgroundColorPicker.getValue()));
                        appListItem.getTimeLine().setBackgroundColor(backgroundColorPicker.getValue());
                    }

                    if (!appListItem.getApplication().getDisplayName().equals(appNameTextField.getText())){
                        appListItem.getApplication().setDisplayName(appNameTextField.getText());
                        appListItem.getAppNameText().setText(appNameTextField.getText());
                    }

                    if (!appListItem.getApplication().getProgramName().equals(programNameTextField.getText())){
                        appListItem.getApplication().setProgramName(programNameTextField.getText());
                    }
                    if(appListItem.getApplication().getPingTimeInSeconds() != pingTimeChoiceBox.getValue()){
                        appListItem.getApplication().setPingTimeInSeconds(pingTimeChoiceBox.getValue());
                    }
                    stage.close();
                }
        );
    }

    private void initCancelButton() {
        cancelButton.setOnAction(event ->
                stage.close()
        );
    }

    private void initPingTimeChoiceBox(){
        IntStream.rangeClosed(1,100).boxed().forEach(pingTimeChoiceBox.getItems()::add);
        pingTimeChoiceBox.setValue(appListItem.getApplication().getPingTimeInSeconds());
    }
}
