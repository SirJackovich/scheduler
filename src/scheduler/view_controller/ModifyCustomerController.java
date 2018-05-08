package scheduler.view_controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import scheduler.Scheduler;

public class ModifyCustomerController {

  private Stage stage;
  
  @FXML
  private TextField IDTextField;

  @FXML
  private TextField nameTextField;

  @FXML
  private TextField addressTextField;

  @FXML
  private Button saveButton;

  @FXML
  private Button cancelButton;

  @FXML
  void handleCancelButton() {
    stage.close();
  }

  @FXML
  void handleSaveButton() {

  }
  
  public void setStage(Stage stage) {
    this.stage = stage;
  }
  
  public static void showDialog(Stage primaryStage, String title) throws IOException{
    
    // Load the fxml file and create a new stage for the popup dialog.
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(Scheduler.class.getResource("view_controller/ModifyCustomer.fxml"));
    AnchorPane page = (AnchorPane) loader.load();

    // Create the dialog Stage.
    Stage stage = new Stage();
    stage.setTitle(title);
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.initOwner(primaryStage);
    Scene scene = new Scene(page);
    stage.setScene(scene);
    
    // set the stage so we can close it if needed
    ModifyCustomerController modifyCustomerController = loader.getController();
    modifyCustomerController.setStage(stage);
    
    // open the popup
    stage.showAndWait();
  }

}
