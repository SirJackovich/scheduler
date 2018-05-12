package scheduler.view_controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import scheduler.Scheduler;

public class ReportsController {

  private Stage stage;
  
    @FXML
    private ComboBox<?> typeComboBox;

    @FXML
    private ComboBox<?> consultantComboBox;

    @FXML
    private Button generateButton;

    @FXML
    private TableColumn<?, ?> tableColumn1;

    @FXML
    private TableColumn<?, ?> tableColumn2;

    @FXML
    private TableColumn<?, ?> tableColumn3;

    @FXML
    private TableColumn<?, ?> tableColumn4;

    @FXML
    void handleConsultantComboBox() {

    }
    @FXML
    void handleGenerateButton() {

    }

    @FXML
    void handleTypeComboBox() {

    }

public static void showDialog(Stage primaryStage, String title) throws IOException{
    
    // Load the fxml file and create a new stage for the popup dialog.
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(Scheduler.class.getResource("view_controller/Reports.fxml"));
    BorderPane page = (BorderPane) loader.load();

    // Create the dialog Stage.
    Stage stage = new Stage();
    stage.setTitle(title);
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.initOwner(primaryStage);
    Scene scene = new Scene(page);
    stage.setScene(scene);
    
    // set the stage so we can close it if needed
    ReportsController reportsController = loader.getController();
    reportsController.setStage(stage);
    
    // open the popup
    stage.showAndWait();
  }

public void setStage(Stage stage) {
    this.stage = stage;
  }

}