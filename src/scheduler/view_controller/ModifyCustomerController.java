package scheduler.view_controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
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
  private ComboBox<String> addressIDComboBox;

  @FXML
  void handleCancelButton() {
    stage.close();
  }

  @FXML
  void handleSaveButton() {
    if(isInputValid()){
      if(this.customerID == null){
        handleCustomer(true);
      }else{
        handleCustomer(false);
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
    }else{
      this.customerID = Integer.toString(customer.getID());
      nameTextField.setText(customer.getName());
    }
  }
  
  public void setConnection(Connection connection) {
    this.connection = connection;
  }
  
  public static void showDialog(Stage primaryStage, Connection connection, Customer customer, String title) throws IOException, ClassNotFoundException{
    
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
      modifyCustomerController.fillComboBox(customer);
    }else{
      modifyCustomerController.setCustomer(null);
      modifyCustomerController.fillComboBox(null);
    }
    
    // open the popup
    stage.showAndWait();
  }
  
    private void fillComboBox(Customer customer) throws ClassNotFoundException {
    // get the addresses
    ResultSet resultSet1 = getDataFromDataBase("SELECT addressid FROM address");
    ArrayList<String> addresses = new ArrayList<>();
    try {
      while (resultSet1.next()) {
        String addressID = resultSet1.getString("addressid");
        addresses.add(addressID);
      }
    } catch (SQLException ex) {
        Logger.getLogger(CalendarController.class.getName()).log(Level.SEVERE, null, ex);
    }
    addressIDComboBox.getItems().clear();
    addressIDComboBox.getItems().addAll(addresses);
    
    if(customer == null){
      addressIDComboBox.getSelectionModel().select(0);
    }else{
      addressIDComboBox.getSelectionModel().select(customer.getAddressID() -1);
    }
  }
    
  public ResultSet getDataFromDataBase(String query) throws ClassNotFoundException {
    ResultSet resultSet = null;
    Statement statement;
    try {
      statement = connection.createStatement();
      resultSet = statement.executeQuery(query);
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    return resultSet;
  }
  
  private boolean isInputValid() {
    String errorMessage = "";

    if (nameTextField.getText() == null || nameTextField.getText().length() == 0) {
      errorMessage += "No valid name!\n"; 
    }
    
    if (errorMessage.length() == 0) {
      return true;
    } else {
      AlertDialog.errorDialog(errorMessage);
      return false;
    }
  }
  
  private void handleCustomer(boolean newCustomer){
    PreparedStatement preparedStatement;
    try {
      if(newCustomer){
        preparedStatement = connection.prepareStatement(
        "INSERT INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdateBy) " +
        "VALUES (?, ?, 1, CURDATE(), 'admin', 'admin')");
      }else{
        preparedStatement = connection.prepareStatement(
        "UPDATE customer " +
        "SET customerName=?, addressId=? " +
        "WHERE customerid = ?");
      }
      preparedStatement.setString(1, nameTextField.getText());
      preparedStatement.setString(2, addressIDComboBox.getValue());
      if(!newCustomer){
        preparedStatement.setString(3, customerID);
      }
      preparedStatement.execute();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    stage.close();
  }

}
