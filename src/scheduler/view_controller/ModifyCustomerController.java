package scheduler.view_controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import scheduler.Scheduler;
import scheduler.model.AlertDialog;
import scheduler.model.Customer;

public class ModifyCustomerController {

  private Stage stage;
  private Connection connection;
  private String customerID;

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
    if(isInputValid()){
      if(this.customerID == null){
        createCustomer(
          nameTextField.getText(),
          addressTextField.getText()
        );
      }else{
        updateCustomer(
          nameTextField.getText(),
          addressTextField.getText()
        );
      }
    }
  }
  
  public void setStage(Stage stage) {
    this.stage = stage;
  }
  
  public void setCustomer(Customer customer) {
    if(customer == null){
      this.customerID = null;
      nameTextField.setText("");
      addressTextField.setText("");
    }else{
      this.customerID = Integer.toString(customer.getID());
      nameTextField.setText(customer.getName());
      addressTextField.setText(Integer.toString(customer.getAddressID()));
    }
  }
  
  public void setConnection(Connection connection) {
    this.connection = connection;
  }
  
  public static void showDialog(Stage primaryStage, Connection connection, Customer customer, String title) throws IOException{
    
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
    modifyCustomerController.setConnection(connection);
    
    if(customer != null){
      modifyCustomerController.setCustomer(customer);
    }else{
      modifyCustomerController.setCustomer(null);
    }
    
    // open the popup
    stage.showAndWait();
  }
  
  private boolean isInputValid() {
    String errorMessage = "";

    if (nameTextField.getText() == null || nameTextField.getText().length() == 0) {
      errorMessage += "No valid name!\n"; 
    }
    
    if (addressTextField.getText() == null || addressTextField.getText().length() == 0) {
      errorMessage += "No valid address id!\n"; 
    } else {
      try {
        Integer.parseInt(addressTextField.getText());
      } catch (NumberFormatException e) {
        errorMessage += "No valid address id (must be an integer)!\n"; 
      }
    }
    
    if (errorMessage.length() == 0) {
      return true;
    } else {
      AlertDialog.errorDialog(errorMessage);
      return false;
    }
  }
  
  private void createCustomer(String name, String address){
    PreparedStatement preparedStatement;
    try {
      preparedStatement = connection.prepareStatement(
        "INSERT INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdateBy) " +
        "VALUES (?, ?, 1, CURDATE(), 'admin', 'admin')");
      preparedStatement.setString(1, name);
      preparedStatement.setString(2, address);
      preparedStatement.execute();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    stage.close();
  }
  
  private void updateCustomer(String name, String address){
    PreparedStatement preparedStatement;
    try {
      preparedStatement = connection.prepareStatement(
        "UPDATE customer " +
        "SET customerName=?, addressId=? " +
        "WHERE customerid = ?");
      preparedStatement.setString(1, name);
      preparedStatement.setString(2, address);
      preparedStatement.setString(3, customerID);
      preparedStatement.execute();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    stage.close();
  }

}
