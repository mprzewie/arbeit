package pl.edu.agh.arbeit.gui.controler;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pl.edu.agh.arbeit.data.EventListenerSaver;
import pl.edu.agh.arbeit.data.report.CsvReport;
import pl.edu.agh.arbeit.data.repository.DatabaseEventRepository;
import pl.edu.agh.arbeit.gui.Main;

import pl.edu.agh.arbeit.tracker.events.Event;
import pl.edu.agh.arbeit.tracker.trackers.ApplicationTracker;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import java.nio.file.Paths;
import java.util.stream.Collectors;

public class ReportsController {
    private Stage reportsStage;
    private EventListenerSaver eventListener;
    private List<String> applicationsNames;
    private Map<String, CheckBox> appBoxes = new HashMap<>();


    @FXML
    private AnchorPane anchorPane;

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

    private String styleNow;

    public void init(Stage reportsStage, EventListener eventListener, List<ApplicationTracker> trackers, ReadOnlyDoubleProperty heightProperty, String styleType){

        this.reportsStage = reportsStage;
        this.eventListener = eventListener;

        dateFromPicker.setValue(LocalDate.now());
        dateToPicker.setValue(LocalDate.now().plusDays(1));
        DatabaseEventRepository repository = new DatabaseEventRepository();
        this.applicationsNames = repository.getRecordedAppsNames();
        List<String> applicationsNamesFromTracker = trackers.stream().map(tracker -> tracker.getApplication().getProgramName()).collect(Collectors.toList());
        applicationsNames.addAll(
                applicationsNamesFromTracker.stream()
                        .filter(name -> !applicationsNames.contains(name))
                        .collect(Collectors.toList()));


        reportsStage.setTitle("Generate report");
        initCancelButton();
        initGenerateReportsButton();
        initAppList(heightProperty);
        initPathTextField();
        pathTextField.setText("report.csv");
        anchorPane.getStylesheets().clear();
        anchorPane.getStylesheets().add(styleType);
        styleNow = styleType;
    }

    private void initCancelButton(){
        cancelButton.setOnAction(event ->
                reportsStage.close()
        );
    }

    private void initGenerateReportsButton(){
        generateReportButton.setOnAction(event -> {
            try {
                List<Event> events = new LinkedList<>();
                Date start = Date.from(Instant.from(dateFromPicker.getValue().atStartOfDay(ZoneId.systemDefault())));
                Date end = Date.from(Instant.from(dateToPicker.getValue().atStartOfDay(ZoneId.systemDefault())));

                List<String> appsToReport = applicationsNames.stream()
                        .filter(appName -> appBoxes.get(appName).isSelected()).collect(Collectors.toList());
                appsToReport.forEach(app -> events.addAll(eventListener.getRepository().getEventForGivenAppinRange(app,start,end)));
                if(!events.isEmpty()) {
                    CsvReport report = new CsvReport(appsToReport, events);
                    if(!pathTextField.getText().equals("")) report.writeCsv(Paths.get(pathTextField.getText()));
                    showSavedSuccessfullyInfo();
                }

                reportsStage.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void showSavedSuccessfullyInfo(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/SavedSuccessfullyInfoPane.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            SavedSuccessfullyInfoController savedSuccessfullyInfoController = loader.getController();
            savedSuccessfullyInfoController.init(stage, styleNow);
            stage.setScene(new Scene(root, 450, 100));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void initAppList(ReadOnlyDoubleProperty heightProperty){
        initScrollPane(heightProperty);
        List<String> apps = new LinkedList<>();
        apps.addAll(applicationsNames);
        final String last = apps.remove(apps.size() - 1);
        if(!apps.isEmpty()) {
            for (String appName : apps) {
                CheckBox cb = new CheckBox(appName);
                cb.setSelected(true);
                appListContent.getChildren().add(cb);
                Region region = new Region();
                region.setMinHeight(14.0);
                appListContent.getChildren().add(region);
                appBoxes.put(appName, cb);
            }

            CheckBox cb = new CheckBox(last);
            cb.setSelected(true);
            appListContent.getChildren().add(cb);
            appBoxes.put(last, cb);
        }


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
}
