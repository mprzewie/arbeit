package pl.edu.agh.arbeit.gui.controler;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pl.edu.agh.arbeit.data.EventListener;
import pl.edu.agh.arbeit.data.report.CsvReport;
import pl.edu.agh.arbeit.tracker.events.Event;
import pl.edu.agh.arbeit.tracker.trackers.ApplicationTracker;
import java.io.File;
import java.util.LinkedList;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ReportsController {
    private Stage reportsStage;
    private EventListener eventListener;
    private List<ApplicationTracker> trackers;

    @FXML
    private DatePicker dateFromPicker;

    @FXML
    private DatePicker dateToPicker;

    @FXML
    private TextField pathTextField;

    @FXML
    private Button generateReportButton;

    @FXML
    private Button cancelButton;

    @FXML
    private ScrollPane appListScrollPane;

    @FXML
    private VBox appListContent;


    public void init(Stage reportsStage, EventListener eventListener, List<ApplicationTracker> trackers, ReadOnlyDoubleProperty heightProperty){
        this.reportsStage = reportsStage;
        this.eventListener = eventListener;
        this.trackers = trackers;
        reportsStage.setTitle("Generate report");
        initCancelButton();
        initGenerateReportsButton();
        initAppList(heightProperty);
        initPathTextField();
        makeFieldsDisabledForNow();
        pathTextField.setText("report.csv");
    }

    private void initCancelButton(){
        cancelButton.setOnAction(event ->
                reportsStage.close()
        );
    }

    private void initGenerateReportsButton(){
        generateReportButton.setOnAction(event -> {
            try {
                List<Event> events = eventListener.getRepository().getAllEvents();
                events.forEach(e -> {
                    if(!e.getTopic().equals("system")){
                        System.out.println("relevant -> " + e);
                    }
                });
                CsvReport report = new CsvReport(trackers.stream().map(t -> t.getApplication().getName()).collect(Collectors.toList()), events);
                if(!pathTextField.getText().equals("")) report.writeCsv(Paths.get(pathTextField.getText()));
                reportsStage.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    private void initAppList(ReadOnlyDoubleProperty heightProperty){
        initScrollPane(heightProperty);
        System.out.println(calculateSpaceTakenByOtherViews());
        List<String> apps = new LinkedList<>();
        trackers.forEach(tracker -> apps.add(tracker.getApplication().getName()));
        final String last = apps.remove(apps.size() - 1);

        for (String appName : apps){
            CheckBox cb = new CheckBox(appName);
            cb.setSelected(true);
            appListContent.getChildren().add(cb);
            Region region = new Region();
            region.setMinHeight(14.0);
            appListContent.getChildren().add(region);
        }

        CheckBox cb = new CheckBox(last);
        cb.setSelected(true);
        appListContent.getChildren().add(cb);
    }

    private void initScrollPane(ReadOnlyDoubleProperty heightProperty){
        this.appListScrollPane.setContent(appListContent);
        this.appListScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.appListScrollPane.setStyle("-fx-background-color:transparent;");
        this.appListScrollPane.prefHeightProperty().bind(heightProperty.subtract(calculateSpaceTakenByOtherViews()));
    }

    private double calculateSpaceTakenByOtherViews(){
        return 236;
    }

    private void initPathTextField(){
        pathTextField.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select report directory");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Comma Separated Values (.csv)", "*.csv"));
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Text File (.txt)", "*.txt"));
            File file = fileChooser.showSaveDialog(reportsStage);
            if (file != null) {
                pathTextField.setText(file.getAbsolutePath());
            }
        });
    }

    private void makeFieldsDisabledForNow(){
        this.dateFromPicker.setDisable(true);
        this.dateToPicker.setDisable(true);
    }

}
