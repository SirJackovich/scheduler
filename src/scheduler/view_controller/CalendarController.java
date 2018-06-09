package scheduler.view_controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import scheduler.model.AlertDialog;
import scheduler.model.Appointment;
import scheduler.model.DateTime;

public class CalendarController {
  private Stage stage;
  private Connection connection;
  private final ObservableList<Appointment> calendar = FXCollections.observableArrayList();
  private String today;
  private String tomorrow;
  private final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private String reminder = "";
  
  @FXML
  private ComboBox<String> viewComboBox;

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
  private void handleComboBox() throws ParseException {
    Calendar cal = Calendar.getInstance();
    today = DATE_FORMAT.format(cal.getTime());
    String view = viewComboBox.getValue();
    if("Month".equals(view)){
      cal.add(Calendar.MONTH, +1);
    }else{
      cal.add(Calendar.DAY_OF_YEAR, +7);
    }
    tomorrow = DATE_FORMAT.format(cal.getTime());
    updateCalendar();
  }
  
  @FXML
  private void handleCustomerButton() throws IOException, ClassNotFoundException, ParseException{
    CustomerController.showDialog(stage, connection);
    updateCalendar();
  }
  
  @FXML
  private void handleAddButton() throws IOException, ClassNotFoundException, ParseException{
    AppointmentController.showDialog(stage, connection, null, "Add Appointment");
    updateCalendar();
  }
  
  @FXML
  private void handleModifyButton() throws IOException, ClassNotFoundException, ParseException{    
    Appointment appointment = calendarTableView.getSelectionModel().getSelectedItem(); 
    if (appointment != null) {
      AppointmentController.showDialog(stage, connection, appointment, "Modify Appointment");
      updateCalendar();
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
  private void initialize() throws IOException, ClassNotFoundException, ParseException{
    // connect to database
    makeConnection();
    
    // Initialize the appointment table
    timeTableColumn.setCellValueFactory(cellData -> cellData.getValue().startProperty());
    nameTableColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
    typeTableColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
    customerTableColumn.setCellValueFactory(cellData -> cellData.getValue().customerIDProperty().asObject());
    
    // get todays timestamp based on timezone
    Instant now = Instant.now();
    ZoneId timeZone = ZoneId.systemDefault();
    ZonedDateTime todayZonedDateTime = now.atZone(timeZone);
    LocalDateTime todayLocalDateTime = todayZonedDateTime.toLocalDateTime();
    Timestamp todayTimestamp = Timestamp.valueOf(todayLocalDateTime);
    today = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(todayTimestamp);

    // get 1 months from now timestamp based on timezone
    Calendar cal = Calendar.getInstance();
    cal.setTime(todayTimestamp);
    cal.add(Calendar.MONTH, +1);
    tomorrow = DATE_FORMAT.format(cal.getTime());
    
    ResultSet resultSet = getAppointmentsFromDataBase();
    
    try {
      while (resultSet.next()) {
        int appointmentID = resultSet.getInt("appointmentid");
        String start = DateTime.makeDateLocal(resultSet.getString("start"));
        String title = resultSet.getString("title");
        String type = resultSet.getString("type");
        int customerID = resultSet.getInt("customerId");
        int userId = resultSet.getInt("userId");
        String description = resultSet.getString("description");
        String location = resultSet.getString("location");
        String contact = resultSet.getString("contact");
        String URL = resultSet.getString("url");
        String end = DateTime.makeDateLocal(resultSet.getString("end"));
        Appointment appointment = new Appointment(appointmentID, start, title, type, customerID, userId, description, location, contact, URL, end);
        calendar.add(appointment);
        if(reminder.equals("")){         
          if (DateTime.inFifteenMinutes(today, start)) {
            String[] parts = start.split(" ");
            reminder = "You have a meeting at " + parts[1];
          }
        }
      }
      calendarTableView.setItems(calendar);
    } catch (SQLException ex) {
        Logger.getLogger(CalendarController.class.getName()).log(Level.SEVERE, null, ex);
    }
    viewComboBox.getItems().addAll("Month", "Week");
    viewComboBox.getSelectionModel().select(0);
    
    if(!LoginController.showDialog(stage, connection)){
      Platform.exit();
    }else if(!reminder.equals("")){
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Appointment Reminder");
      alert.setContentText(reminder);
      alert.showAndWait();
    }
  }
  
  private ResultSet getAppointmentsFromDataBase(){
    ResultSet resultSet = null;
    Statement statement;
    try {
      statement = connection.createStatement();
      resultSet = statement.executeQuery(
      "SELECT appointmentid, start, title, type, customerId, userId, description, location, contact, url, end " +
      "FROM appointment " +
      "WHERE start >='" + DateTime.makeDateUTC(today) + "' " +
      "AND start <'" + DateTime.makeDateUTC(tomorrow) + "' " +
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
    } catch (ClassNotFoundException | SQLException ex) {
      ex.printStackTrace();
    }
  }
  
  private void updateCalendar() throws ParseException{
    ResultSet resultSet = getAppointmentsFromDataBase();
    calendar.clear();
    try {
      while (resultSet.next()) {
        int appointmentID = resultSet.getInt("appointmentid");
        String start = DateTime.makeDateLocal(resultSet.getString("start"));
        String title = resultSet.getString("title");
        String type = resultSet.getString("type");
        int customerID = resultSet.getInt("customerId");
        int userId = resultSet.getInt("userId");
        String description = resultSet.getString("description");
        String location = resultSet.getString("location");
        String contact = resultSet.getString("contact");
        String URL = resultSet.getString("url");
        String end = DateTime.makeDateLocal(resultSet.getString("end"));
        Appointment appointment = new Appointment(appointmentID, start, title, type, customerID, userId, description, location, contact, URL, end);
        calendar.add(appointment);
      }
      calendarTableView.setItems(calendar);
    } catch (SQLException ex) {
        Logger.getLogger(CalendarController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
}
