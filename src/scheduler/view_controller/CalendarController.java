package scheduler.view_controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import scheduler.model.AlertDialog;
import scheduler.model.Appointment;

public class CalendarController {
  private Stage stage;
  private Connection connection;
  private final ObservableList<Appointment> calendar = FXCollections.observableArrayList();
  
  
  @FXML
  private ComboBox viewComboBox;

  @FXML
  private TableView<Appointment> calendarTableView;

  @FXML
  private TableColumn<Appointment, String> timeTableColumn;

  @FXML
  private TableColumn<Appointment, String> nameTableColumn;

  @FXML
  private TableColumn<Appointment, String> typeTableColumn;

  @FXML
  private TableColumn<Appointment, Integer> customerTableColumn;

  @FXML
  private Button customerButton;

  @FXML
  private Button appointmentButton;

  @FXML
  private Button reportButton;


  @FXML
  private void handleComboBox() {
    // TODO: display weekly or monthly
  }
  
  @FXML
  private void handleCustomerButton() throws IOException{
    CustomerController.showDialog(stage);
  }
  
  @FXML
  private void handleAddButton() throws IOException, ClassNotFoundException{
    AppointmentController.showDialog(stage, connection, null, "Add Appointment");
    updateCalander();
  }
  
  @FXML
  private void handleModifyButton() throws IOException, ClassNotFoundException{    
    Appointment appointment = calendarTableView.getSelectionModel().getSelectedItem(); 
    if (appointment != null) {
      AppointmentController.showDialog(stage, connection, appointment, "Modify Appointment");
      updateCalander();
    } else {
      AlertDialog.noSelectionDialog("appointment");
    }
  }
  
  @FXML
  private void handleReportButton() throws IOException{
    ReportsController.showDialog(stage, connection);
  }
  
  public void setStage(Stage stage){
    this.stage = stage;
  }

  @FXML
  private void initialize() throws IOException, ClassNotFoundException{
    makeConnection();
    ResultSet resultSet = getAppointmentsFromDataBase();
    
    // Initialize the appointment table
    timeTableColumn.setCellValueFactory(cellData -> cellData.getValue().startProperty());
    nameTableColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
    typeTableColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
    customerTableColumn.setCellValueFactory(cellData -> cellData.getValue().customerIDProperty().asObject());
    
    try {
      while (resultSet.next()) {
        Integer appointmentID = resultSet.getInt("appointmentid");
        String start = resultSet.getString("start");
        String title = resultSet.getString("title");
        String type = resultSet.getString("type");
        Integer customerID = resultSet.getInt("customerId");
        Integer userId = resultSet.getInt("userId");
        String description = resultSet.getString("description");
        String location = resultSet.getString("location");
        String contact = resultSet.getString("contact");
        String URL = resultSet.getString("url");
        String end = resultSet.getString("end");
        Appointment appointment = new Appointment(appointmentID, start, title, type, customerID, userId, description, location, contact, URL, end);
        calendar.add(appointment);
      }
      calendarTableView.setItems(calendar);
    } catch (SQLException ex) {
        Logger.getLogger(CalendarController.class.getName()).log(Level.SEVERE, null, ex);
    }
    viewComboBox.getItems().addAll("Month", "Week");
    viewComboBox.getSelectionModel().select(0);
    if(!LoginController.showDialog(stage, connection)){
      Platform.exit();
    }
  }
  
  public ResultSet getAppointmentsFromDataBase(){
    ResultSet resultSet = null;
    Statement statement;
    try {
      statement = connection.createStatement();
      resultSet = statement.executeQuery(
      "SELECT appointmentid, start, title, type, customerId, userId, description, location, contact, url, end " +
      "FROM appointment " +
      "ORDER BY start");
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    return resultSet;
  }
  
  private void makeConnection(){
    String URL = "jdbc:mysql://52.206.157.109/U04bLJ";
    String username = "U04bLJ";
    String password = "53688195100";
    try {
      Class.forName("com.mysql.jdbc.Driver");
      connection = DriverManager.getConnection(URL, username, password);
      System.out.println("Making connection...");
    } catch (ClassNotFoundException | SQLException ex) {
      ex.printStackTrace();
    }
  }
  
  private void updateCalander(){
    ResultSet resultSet = getAppointmentsFromDataBase();
    calendar.clear();
    try {
      while (resultSet.next()) {
        Integer appointmentID = resultSet.getInt("appointmentid");
        String start = resultSet.getString("start");
        String title = resultSet.getString("title");
        String type = resultSet.getString("type");
        Integer customerID = resultSet.getInt("customerId");
        Integer userId = resultSet.getInt("userId");
        String description = resultSet.getString("description");
        String location = resultSet.getString("location");
        String contact = resultSet.getString("contact");
        String URL = resultSet.getString("url");
        String end = resultSet.getString("end");
        Appointment appointment = new Appointment(appointmentID, start, title, type, customerID, userId, description, location, contact, URL, end);
        calendar.add(appointment);
      }
      calendarTableView.setItems(calendar);
    } catch (SQLException ex) {
        Logger.getLogger(CalendarController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
}


