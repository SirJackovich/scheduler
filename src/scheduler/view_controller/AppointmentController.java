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
import java.util.Date;
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
import scheduler.model.Appointment;
import scheduler.model.DateTime;

public class AppointmentController {

  private Stage stage;
  private Connection connection;
  private String appointmentID;

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

  @FXML
  void handleCancelButton() {
    stage.close();
  }

  @FXML
  void handleSaveButton() {
    if(isInputValid()){
      if(this.appointmentID == null){
        handleAppointment(true);
      }else{
        handleAppointment(false);
      }
    }
  }
  
  public void setStage(Stage stage) {
    this.stage = stage;
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
  
  private boolean isInputValid() {
    String errorMessage = "";

    DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    try {
      Date start = DATE_FORMAT.parse(startTextField.getText());
      Date end = DATE_FORMAT.parse(endTextField.getText());
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

}
