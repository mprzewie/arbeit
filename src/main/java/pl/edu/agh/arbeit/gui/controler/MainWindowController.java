package pl.edu.agh.arbeit.gui.controler;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class MainWindowController {
    private OverviewController overviewController;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private DatePicker datePicker;
    
    public void init(OverviewController overviewController) {
        this.overviewController=overviewController;
        this.bindSizeProperties();
    }

    private void bindSizeProperties() {
    }
}
