package pl.edu.agh.arbeit.gui.controler;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import pl.edu.agh.arbeit.gui.model.Tracker;
import pl.edu.agh.arbeit.gui.view.AddCircle;

import java.time.LocalDate;

public class MainWindowController {
    private OverviewController overviewController;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button generateRaportButton;

    @FXML
    private TextField appNameTextField;

    private AddCircle addCircle;
    
    public void init(OverviewController overviewController, Tracker tracker) {
        this.overviewController=overviewController;
        this.addCircle = new AddCircle(this.appNameTextField, tracker);
        this.anchorPane.getChildren().add(this.addCircle);
        this.bindSizeProperties();
        this.initDatePicer();
    }

    private void bindSizeProperties() {
    }

    private void initDatePicer(){
        datePicker.setValue(LocalDate.now());
        datePicker.setDisable(true);
    }
}
