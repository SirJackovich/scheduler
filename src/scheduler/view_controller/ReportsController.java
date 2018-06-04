package scheduler.view_controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import scheduler.Scheduler;
import scheduler.model.Appointment;
import scheduler.model.Type;

public class ReportsController {

  private Stage stage;
  private Connection connection;
  
  @FXML
  private ComboBox<String> typeComboBox;

  @FXML
  private ComboBox<String> personComboBox;
  
  @FXML
  private Label personLabel;

  @FXML
  private Button generateButton;

  @FXML
  private TableView tableView;
  
  @FXML
  private TableColumn tableColumn1;

  @FXML
  private TableColumn tableColumn2;

  @FXML
  private TableColumn tableColumn3;

  @FXML
  private TableColumn tableColumn4;

  @FXML
  void handleGenerateButton() throws ClassNotFoundException {
    String type = typeComboBox.getValue();
    switch(type){
      case "Appointments by Month":
        appointmentsByMonth();
        break;
      case "Consultant Schedule":
        consultantSchedule();
        break;
      case "Customer Schedule":
        customerSchedule();
        break;
      default:
        System.out.println("no type selected");
    }
  }
  
  public void appointmentsByMonth() throws ClassNotFoundException{
    ObservableList<Type> table = FXCollections.observableArrayList();
    ResultSet resultSet = getDataFromDataBase("SELECT type,COUNT(*) as count FROM appointment GROUP BY type");
    tableColumn1.setText("Type");
    tableColumn2.setText("Count");
    
    tableColumn1.setCellValueFactory(
      new PropertyValueFactory<>("type")
    );
    tableColumn2.setCellValueFactory(
      new PropertyValueFactory<>("count")
    );
   
    try {
      while (resultSet.next()) {
        String type = resultSet.getString("type");
        Integer count = resultSet.getInt("count");
        
        Type type2 = new Type(count, type);
        table.add(type2);
      }
      tableView.setItems(table);
    } catch (SQLException ex) {
        Logger.getLogger(CalendarController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  public void consultantSchedule() throws ClassNotFoundException{
    fillCallendar(
    "SELECT appointmentid, start, title, type, customerName, appointment.customerId, appointment.userId, userName " +
    "FROM appointment, customer, user " +
    "WHERE appointment.customerId = customer.customerid " +
    "AND appointment.userId = user.userId " + 
    "AND contact='" + personComboBox.getValue() + "'" + 
    "ORDER BY start", true);
  }
  
  public void customerSchedule() throws ClassNotFoundException{
    fillCallendar(
    "SELECT appointmentid, start, title, type, customerName, appointment.customerId, appointment.userId, userName " +
    "FROM appointment, customer, user " +
    "WHERE appointment.customerId = customer.customerid " +
    "AND appointment.userId = user.userId " + 
    "AND customerName= '" + personComboBox.getValue() + "'" +
    "ORDER BY start", false);
  }
    
  public void fillCallendar(String query, Boolean consultant) throws ClassNotFoundException{
    ObservableList<Appointment> calendar = FXCollections.observableArrayList();
    ResultSet resultSet = getDataFromDataBase(query);
    tableColumn1.setText("Time");
    tableColumn2.setText("Name");
    tableColumn3.setText("Type");
    if(consultant){
      tableColumn4.setText("Customer");
    }else{
      tableColumn4.setText("Consultant");
    }
    
    
    tableColumn1.setCellValueFactory(
      new PropertyValueFactory<>("start")
    );
    tableColumn2.setCellValueFactory(
      new PropertyValueFactory<>("name")
    );
    tableColumn3.setCellValueFactory(
      new PropertyValueFactory<>("type")
    );
    if(consultant){
      tableColumn4.setCellValueFactory(
        new PropertyValueFactory<>("customerName")
      );
    }else{
      tableColumn4.setCellValueFactory(
        new PropertyValueFactory<>("userName")
      );
    }
    
    
    try {
      while (resultSet.next()) {
        Integer appointmentID = resultSet.getInt("appointmentid");
        String start = resultSet.getString("start");
        String name = resultSet.getString("title");
        String type = resultSet.getString("type");
        Integer customerID = resultSet.getInt("customerId");
        String customerName = resultSet.getString("customerName");
        Integer userId = resultSet.getInt("userId");
        String userName = resultSet.getString("userName");
        Appointment appointment = new Appointment(appointmentID, start, name, type, customerID, customerName, userId, userName);
        calendar.add(appointment);
      }
      tableView.setItems(calendar);
    } catch (SQLException ex) {
        Logger.getLogger(CalendarController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @FXML
  void handleTypeComboBox() throws ClassNotFoundException {
    String type = typeComboBox.getValue();
    switch(type){
      case "Appointments by Month":
        personComboBox.setDisable(true);
        personLabel.setDisable(true);
        break;
      case "Consultant Schedule":
        fillComboBox("Consultant", "SELECT username FROM user", "username");
        break;
      case "Customer Schedule":
        fillComboBox("Customer", "SELECT customerName FROM customer", "customerName");
        break;
      default:
        System.out.println("no type selected");
    } 
  }

  public static void showDialog(Stage primaryStage, Connection connection) throws IOException{
    
    // Load the fxml file and create a new stage for the popup dialog.
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(Scheduler.class.getResource("view_controller/Reports.fxml"));
    BorderPane page = (BorderPane) loader.load();

    // Create the dialog Stage.
    Stage stage = new Stage();
    stage.setTitle("Generate Reports");
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.initOwner(primaryStage);
    Scene scene = new Scene(page);
    stage.setScene(scene);
    
    // set the stage so we can close it if needed
    ReportsController reportsController = loader.getController();
    reportsController.setStage(stage);
    reportsController.setConnection(connection);
    
    // open the popup
    stage.showAndWait();
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }
  
   @FXML
  private void initialize() throws IOException, ClassNotFoundException{
    typeComboBox.getItems().addAll("Appointments by Month", "Consultant Schedule", "Customer Schedule");
    typeComboBox.getSelectionModel().select(0);
    personComboBox.setDisable(true);
    personLabel.setDisable(true);
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
  
  public void setConnection(Connection connection) {
    this.connection = connection;
  }
  
  private void fillComboBox(String type, String query, String columnName) throws ClassNotFoundException{
    personLabel.setText(type);
    personLabel.setDisable(false);
    personComboBox.setDisable(false);
    ResultSet resultSet = getDataFromDataBase(query);
    ArrayList<String> list = new ArrayList<>();
    try {
      while (resultSet.next()) {
        String item = resultSet.getString(columnName);
        list.add(item);
      }
    } catch (SQLException ex) {
        Logger.getLogger(CalendarController.class.getName()).log(Level.SEVERE, null, ex);
    }
    personComboBox.getItems().clear();
    personComboBox.getItems().addAll(list);
    personComboBox.getSelectionModel().select(0);
  }
}