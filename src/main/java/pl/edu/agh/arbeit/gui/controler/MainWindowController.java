package pl.edu.agh.arbeit.gui.controler;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import pl.edu.agh.arbeit.data.EventListener;
import pl.edu.agh.arbeit.data.report.CsvReport;
import pl.edu.agh.arbeit.data.report.Report;
import pl.edu.agh.arbeit.data.repository.DatabaseEventRepository;
import pl.edu.agh.arbeit.data.repository.EventRepository;
import pl.edu.agh.arbeit.gui.view.AddCircle;
import pl.edu.agh.arbeit.tracker.Application;
import pl.edu.agh.arbeit.tracker.trackers.ApplicationTracker;
import pl.edu.agh.arbeit.tracker.trackers.SystemTracker;
import pl.edu.agh.arbeit.tracker.trackers.Tracker;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class MainWindowController {
    private OverviewController overviewController;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button generateReportButton;

    @FXML
    private TextField appNameTextField;

    private AddCircle addCircle;

    private EventListener eventListener;

    private List<Tracker> trackerList;

    private Line verticalLine;

    private Line timeLine;

    private EventRepository applicationRepository;
    
    public void init(OverviewController overviewController) {
        this.applicationRepository = new DatabaseEventRepository();
        this.trackerList = new LinkedList<>();
        this.overviewController=overviewController;
        this.addCircle = new AddCircle();
        this.addCircle.setDisable(true);
        this.anchorPane.getChildren().add(this.addCircle);

        this.verticalLine = new Line();
        this.verticalLine.setStartX(120);
        this.verticalLine.setEndX(120);
        this.verticalLine.setStartY(0);
        this.verticalLine.setEndY(150);
        this.anchorPane.getChildren().add(this.verticalLine);

        timeLine = new Line();
        timeLine.setStartX(0);
        timeLine.setEndX(800);
        timeLine.setStartY(50);
        timeLine.setEndY(50);
        this.anchorPane.getChildren().add(timeLine);

        Line f1 = new Line();
        f1.setStartX(0);
        f1.setEndX(120);
        f1.setStartY(100);
        f1.setEndY(100);
        this.anchorPane.getChildren().add(f1);

        Text systemText = new Text("System");
        systemText.setLayoutX(10);
        systemText.setLayoutY(80);
        this.anchorPane.getChildren().add(systemText);

        this.eventListener = new EventListener();
        Tracker systemTracker = new SystemTracker(10);
        this.eventListener.subscribe(systemTracker);
        systemTracker.start();
        trackerList.add(systemTracker);

        generateReportButton.setOnMouseClicked(event -> {
            try {
                Report report = new CsvReport(applicationRepository.getEvents());
                report.writeCsv(Paths.get("."));
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        addCircle.setOnMouseClicked(event ->{
            Application app = new Application(this.appNameTextField.getText(), this.appNameTextField.getText());

            Tracker appTracker = new ApplicationTracker(5, app);
            eventListener.subscribe(appTracker);
            appTracker.start();
            trackerList.add(appTracker);

            this.verticalLine.setEndY(this.verticalLine.getEndY()+50);

            Line ft = new Line();
            ft.setStartX(0);
            ft.setEndX(120);
            ft.setStartY(100+50*(trackerList.size()-1));
            ft.setEndY(100+50*(trackerList.size()-1));
            this.anchorPane.getChildren().add(ft);

            Text appName = new Text(appNameTextField.getText());
            appName.setLayoutX(10);
            appName.setLayoutY(80+50*(trackerList.size()-1));
            this.anchorPane.getChildren().add(appName);

            addCircle.setLayoutY(addCircle.getLayoutY()+50);
            appNameTextField.setLayoutY(appNameTextField.getLayoutY()+50);
        });

        appNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals(""))
                addCircle.setDisable(true);
            else
                addCircle.setDisable(false);
        });
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
