package scheduler.view_controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import scheduler.Scheduler;
import scheduler.model.AlertDialog;
import scheduler.model.Customer;

public class CustomerController {
  
  private Stage stage;
  private Connection connection;
  private final ObservableList<Customer> customers = FXCollections.observableArrayList();

  @FXML
  private TableView<Customer> tableView;

  @FXML
  private TableColumn<Customer, Integer> IDTableColumn;

  @FXML
  private TableColumn<Customer, String> nameTableColumn;

  @FXML
  private TableColumn<Customer, Integer> addressTableColumn;

  @FXML
  void handleAddButton() throws IOException, ClassNotFoundException{
    ModifyCustomerController.showDialog(stage, connection, null, "Add Customer");
    updateCustomers();
  }

  @FXML
  void handleModifyButton() throws IOException, ClassNotFoundException{
    Customer customer = tableView.getSelectionModel().getSelectedItem(); 
    if (customer != null) {
      ModifyCustomerController.showDialog(stage, connection, customer, "Modify Customer");
      updateCustomers();
    } else {
      AlertDialog.noSelectionDialog("customer");
    }
  }
  
  public void setStage(Stage stage) {
    this.stage = stage;
  }
  
  public void setConnection(Connection connection) {
    this.connection = connection;
  }

  public static void showDialog(Stage primaryStage, Connection connection) throws IOException, ClassNotFoundException{
    
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
    customerController.setConnection(connection);
    customerController.updateCustomers();
    
    // open the popup
    stage.showAndWait();
  }
  
  @FXML
  private void initialize() throws IOException, ClassNotFoundException{
    // Initialize the customer table
    IDTableColumn.setCellValueFactory(cellData -> cellData.getValue().IDProperty().asObject());
    nameTableColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
    addressTableColumn.setCellValueFactory(cellData -> cellData.getValue().addressIDProperty().asObject());
    
  }
  
  public ResultSet getCustomersFromDataBase() throws ClassNotFoundException {
    ResultSet resultSet = null;
    Statement statement;
    try {
      statement = connection.createStatement();
      resultSet = statement.executeQuery(
        "SELECT customerid, customerName, addressId " + 
        "FROM customer");

    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    return resultSet;
  }
  
  private void updateCustomers() throws ClassNotFoundException{
    ResultSet resultSet = getCustomersFromDataBase();
    customers.clear();
    try {
      while (resultSet.next()) {
        Integer customerID = resultSet.getInt("customerid");
        String name = resultSet.getString("customerName");
        Integer addressID = resultSet.getInt("addressId");
        
        Customer customer = new Customer(customerID, name, addressID);
        customers.add(customer);
      }
      tableView.setItems(customers);
    } catch (SQLException ex) {
        Logger.getLogger(CalendarController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

}
