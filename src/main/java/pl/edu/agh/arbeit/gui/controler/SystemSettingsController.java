package pl.edu.agh.arbeit.gui.controler;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pl.edu.agh.arbeit.gui.model.JsonColor;
import pl.edu.agh.arbeit.gui.view.SystemListItem;

import java.time.Duration;
import java.util.stream.IntStream;

public class SystemSettingsController {
    private SystemListItem systemListItem;
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

    public void init(SystemListItem systemListItem, Stage stage, String styleType) {
        this.systemListItem = systemListItem;
        this.stage = stage;
        this.stage.setTitle("Settings");
        this.stage.setResizable(false);

        appNameTextField.setText(systemListItem.getSystemText().getText());
        appNameTextField.setDisable(true);
        programNameTextField.setText(String.valueOf(systemListItem.getAppConfig().getTimeToBecomePassive().getSeconds()));

        activeColorPicker.setValue(systemListItem.getTimeLine().getActiveColor());
        passiveColorPicker.setValue(systemListItem.getTimeLine().getPassiveColor());
        backgroundColorPicker.setValue(systemListItem.getTimeLine().getBackgroundColor());

        initCancelButton();
        initSaveButton();
        initPingTimeChoiceBox();
        anchorPane.getStylesheets().clear();
        anchorPane.getStylesheets().add(styleType);
    }

    private void initSaveButton() {
        saveButton.setOnAction(event -> {
                    if (!systemListItem.getTimeLine().getActiveColor().equals(activeColorPicker.getValue())) {
                        systemListItem.getAppConfig().getInfo().setSystemActiveColor(new JsonColor(activeColorPicker.getValue()));
                        systemListItem.getTimeLine().setActiveColor(activeColorPicker.getValue());
                    }
                    if (!systemListItem.getTimeLine().getPassiveColor().equals(passiveColorPicker.getValue())) {
                        systemListItem.getAppConfig().getInfo().setSystemPassiveColor(new JsonColor(passiveColorPicker.getValue()));
                        systemListItem.getTimeLine().setPassiveColor(passiveColorPicker.getValue());
                    }
                    if (!systemListItem.getTimeLine().getBackgroundColor().equals(backgroundColorPicker.getValue())) {
                        systemListItem.getAppConfig().getInfo().setSystemBackgroundColor(new JsonColor(backgroundColorPicker.getValue()));
                        systemListItem.getTimeLine().setBackgroundColor(backgroundColorPicker.getValue());
                    }

                    if(systemListItem.getAppConfig().getSystemPingTime().getSeconds() != pingTimeChoiceBox.getValue()){
                        systemListItem.getAppConfig().setSystemPingTime(Duration.ofSeconds(pingTimeChoiceBox.getValue()));
                    }
                    if(systemListItem.getAppConfig().getTimeToBecomePassive().getSeconds() != Long.valueOf(programNameTextField.getText())){
                        systemListItem.getAppConfig().setTimeToBecomePassive(Duration.ofSeconds(Long.valueOf(programNameTextField.getText())));
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
        pingTimeChoiceBox.setValue((int) systemListItem.getAppConfig().getSystemPingTime().getSeconds());
    }
}
