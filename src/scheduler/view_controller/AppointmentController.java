package scheduler.view_controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import scheduler.model.Appointment;
import scheduler.model.DateTime;

public class AppointmentController {

  private Stage stage;
  private Connection connection;
  private String appointmentID;
  private final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  @FXML
  private ComboBox<String> userIDComboBox;

  @FXML
  private ComboBox<String> customerIDComboBox;

  @FXML
  private TextField titleTextField;

  @FXML
  private TextField descriptionTextField;

  @FXML
  private TextField locationTextField;

  @FXML
  private TextField contactTextField;

  @FXML
  private TextField typeTextField;

  @FXML
  private TextField URLTextField;

  @FXML
  private TextField startTextField;

  @FXML
  private TextField endTextField;

  private void fillComboBoxes(Appointment appointment) throws ClassNotFoundException {
    userIDComboBox.getItems().clear();
    userIDComboBox.getItems().addAll(getComboBoxItems("user", "userid"));
    
    customerIDComboBox.getItems().clear();
    customerIDComboBox.getItems().addAll(getComboBoxItems("customer", "customerid"));
    
    if(appointment == null){
      // select the first in each list
      userIDComboBox.getSelectionModel().select(0);
      customerIDComboBox.getSelectionModel().select(0);
    }else{
      //select the user and customer from the appointment
      userIDComboBox.getSelectionModel().select(appointment.getUserID() -1);
      customerIDComboBox.getSelectionModel().select(appointment.getCustomerID() -1);
    }
  }
  
  private void handleAppointment(boolean newAppointment){
    PreparedStatement preparedStatement;
    try {
      if(newAppointment){
        preparedStatement = connection.prepareStatement(
        "INSERT INTO appointment (customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdateBy)" +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURDATE(), 'admin', 'admin')");
      }else{
        preparedStatement = connection.prepareStatement(
        "UPDATE appointment " +
        "SET customerId=?, userId=?, title=?, description=?, location=?, contact=?, type=?, url=?, start=?, end=? " +
        "WHERE appointmentid = ?");
      }
      
      preparedStatement.setString(1, customerIDComboBox.getValue());
      preparedStatement.setString(2, userIDComboBox.getValue());
      preparedStatement.setString(3, titleTextField.getText());
      preparedStatement.setString(4, descriptionTextField.getText());
      preparedStatement.setString(5, locationTextField.getText());
      preparedStatement.setString(6, contactTextField.getText());
      preparedStatement.setString(7, typeTextField.getText());
      preparedStatement.setString(8, URLTextField.getText());
      preparedStatement.setString(9, DateTime.makeDateUTC(startTextField.getText()));
      preparedStatement.setString(10, DateTime.makeDateUTC(endTextField.getText()));
      if(!newAppointment){
        preparedStatement.setString(11, this.appointmentID);
      }
      preparedStatement.execute();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    stage.close();
  }
  
  @FXML
  private void handleCancelButton() {
    stage.close();
  }

  @FXML
  private void handleSaveButton() throws ClassNotFoundException {
    if(isInputValid()){
      if(this.appointmentID == null){
        handleAppointment(true);
      }else{
        handleAppointment(false);
      }
    }
  }
  
  private boolean isInputValid() throws ClassNotFoundException {
    String errorMessage = "";


    
    try {
      Date start = DATE_FORMAT.parse(startTextField.getText());
      Date end = DATE_FORMAT.parse(endTextField.getText());
      if(end.compareTo(start) <= 0){
        errorMessage += "End time must be after start time!\n";
      }
      
      Calendar starttime = Calendar.getInstance();
      starttime.setTime(DATE_FORMAT.parse(startTextField.getText()));
      Calendar morning = Calendar.getInstance();
      morning.setTime(DATE_FORMAT.parse(startTextField.getText()));
      Calendar night = Calendar.getInstance();
      night.setTime(DATE_FORMAT.parse(startTextField.getText()));

      morning.set(Calendar.HOUR, 7);
      morning.set(Calendar.MINUTE, 59);
      morning.set(Calendar.AM_PM, Calendar.AM);

      night.set(Calendar.HOUR, 5);
      night.set(Calendar.MINUTE, 01);
      night.set(Calendar.AM_PM, Calendar.PM);
      
      if(starttime.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || starttime.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || starttime.before(morning) || starttime.after(night)){
        errorMessage += "Meeting must start during buisness hours 8am to 5pm Monday to Friday!\n";
      }
      
      Calendar endtime = Calendar.getInstance();
      endtime.setTime(DATE_FORMAT.parse(endTextField.getText()));
      morning = Calendar.getInstance();
      morning.setTime(DATE_FORMAT.parse(endTextField.getText()));
      night = Calendar.getInstance();
      night.setTime(DATE_FORMAT.parse(endTextField.getText()));

      morning.set(Calendar.HOUR, 7);
      morning.set(Calendar.MINUTE, 59);
      morning.set(Calendar.AM_PM, Calendar.AM);

      night.set(Calendar.HOUR, 5);
      night.set(Calendar.MINUTE, 01);
      night.set(Calendar.AM_PM, Calendar.PM);
      
      if(endtime.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || endtime.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || endtime.before(morning) || endtime.after(night)){
        errorMessage += "Meeting must end during buisness hours 8am to 5pm Monday to Friday!\n";
      }
      
      if(appointmentsOverlap(starttime, endtime)){
        errorMessage += "There is already an appointment scheduled durring this time!\n";
      }
    } catch (ParseException ex) {
      errorMessage += "No valid start or end (must be yyyy-MM-dd HH:mm:ss)!\n";
    }
    
    
    
    if (errorMessage.length() == 0) {
      return true;
    } else {
      AlertDialog.errorDialog(errorMessage);
      return false;
    }
  }
  
  private boolean appointmentsOverlap(Calendar starttime, Calendar endtime) throws ClassNotFoundException, ParseException{
    boolean overlap = false;
    ObservableList<Appointment> calendar = FXCollections.observableArrayList();
    ResultSet resultSet = getDataFromDataBase("SELECT appointmentid, start, title, type, customerId, userId, description, location, contact, url, end " +
              "FROM appointment " +
              "WHERE userId=" + userIDComboBox.getValue() + " " +
              "ORDER BY start");
    try {
      while (resultSet.next()) {
        int apptID = resultSet.getInt("appointmentid");
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
        Appointment appointment = new Appointment(apptID, start, title, type, customerID, userId, description, location, contact, URL, end);
        calendar.add(appointment);
      }
    } catch (SQLException ex) {
        Logger.getLogger(CalendarController.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    resultSet = getDataFromDataBase("SELECT appointmentid, start, title, type, customerId, userId, description, location, contact, url, end " +
              "FROM appointment " +
              "WHERE customerId=" + customerIDComboBox.getValue() + " " +
              "ORDER BY start");
    try {
      while (resultSet.next()) {
        int apptID = resultSet.getInt("appointmentid");
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
        Appointment appointment = new Appointment(apptID, start, title, type, customerID, userId, description, location, contact, URL, end);
        calendar.add(appointment);
      }
    } catch (SQLException ex) {
        Logger.getLogger(CalendarController.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    for (Appointment appointment : calendar) {
      try {
        Calendar morning = Calendar.getInstance();
        morning.setTime(DATE_FORMAT.parse(appointment.getStart()));
        Calendar night = Calendar.getInstance();
        night.setTime(DATE_FORMAT.parse(appointment.getEnd()));
        
        if((starttime.after(morning) && starttime.before(night)) || (endtime.after(morning) && endtime.before(night))){
          overlap = true;
        }
      } catch (ParseException ex) {
        Logger.getLogger(AppointmentController.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    return overlap;
  }
  
  public  ArrayList<String> getComboBoxItems(String table, String column) throws ClassNotFoundException{
    ResultSet resultSet = getDataFromDataBase("SELECT " + column + " FROM " + table);
    ArrayList<String> items = new ArrayList<>();
    try {
      while (resultSet.next()) {
        String item = resultSet.getString(column);
        items.add(item);
      }
    } catch (SQLException ex) {
        Logger.getLogger(CalendarController.class.getName()).log(Level.SEVERE, null, ex);
    }
    return items;
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
  
  public void setAppointment(Appointment appointment) {
    if(appointment == null){
      this.appointmentID = null;
      titleTextField.setText("");
      descriptionTextField.setText("");
      locationTextField.setText("");
      contactTextField.setText("");
      typeTextField.setText("");
      URLTextField.setText("");
      startTextField.setText("");
      endTextField.setText("");
    }else{
      this.appointmentID = Integer.toString(appointment.getID());
      titleTextField.setText(appointment.getTitle());
      descriptionTextField.setText(appointment.getDescription());
      locationTextField.setText(appointment.getLocation());
      contactTextField.setText(appointment.getContact());
      typeTextField.setText(appointment.getType());
      URLTextField.setText(appointment.getURL());
      startTextField.setText(appointment.getStart());
      endTextField.setText(appointment.getEnd());
    }
    
  }
  
  public void setConnection(Connection connection) {
    this.connection = connection;
  }
  
  public void setStage(Stage stage) {
    this.stage = stage;
  }
  
  public static void showDialog(Stage primaryStage, Connection connection, Appointment appointment, String title) throws IOException, ClassNotFoundException{
    
    // Load the fxml file and create a new stage for the popup dialog.
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(Scheduler.class.getResource("view_controller/Appointment.fxml"));
    AnchorPane page = (AnchorPane) loader.load();

    // Create the dialog Stage.
    Stage stage = new Stage();
    stage.setTitle(title);
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.initOwner(primaryStage);
    Scene scene = new Scene(page);
    stage.setScene(scene);
    
    // set the stage so we can close it if needed
    AppointmentController appointmentController = loader.getController();
    appointmentController.setStage(stage);
    appointmentController.setConnection(connection);
    
    if(appointment != null){
      appointmentController.setAppointment(appointment);
      appointmentController.fillComboBoxes(appointment);
    }else{
      appointmentController.setAppointment(null);
      appointmentController.fillComboBoxes(null);
    }
    
    // open the popup
    stage.showAndWait();
  }
}
