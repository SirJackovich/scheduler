package scheduler.view_controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import scheduler.Scheduler;
import scheduler.model.Customer;

public class CustomerController {
  
  private Stage stage;

  @FXML
  private Button addButton;

  @FXML
  private Button modifyButton;

  @FXML
  private TableView<Customer> tableView;

  @FXML
  private TableColumn<Customer, Integer> IDTableColumn;

  @FXML
  private TableColumn<Customer, String> nameTableColumn;

  @FXML
  private TableColumn<Customer, String> addressTableColumn;

  @FXML
  void handleAddButton() {

  }

  @FXML
  void handleModifyButton() {

  }
  
  public void setStage(Stage stage) {
    this.stage = stage;
  }

  public static void showDialog(Stage primaryStage) throws IOException{
    
    // Load the fxml file and create a new stage for the popup dialog.
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(Scheduler.class.getResource("view_controller/Customer.fxml"));
    AnchorPane page = (AnchorPane) loader.load();

    // Create the dialog Stage.
    Stage stage = new Stage();
    stage.setTitle("Customer");
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.initOwner(primaryStage);
    Scene scene = new Scene(page);
    stage.setScene(scene);
    
    // set the stage so we can close it if needed
    CustomerController customerController = loader.getController();
    customerController.setStage(stage);
    
    // open the popup
    stage.showAndWait();
  }

}
