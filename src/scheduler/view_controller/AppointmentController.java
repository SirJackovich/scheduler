package scheduler.view_controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import scheduler.model.Appointment;

public class AppointmentController {

  private Stage stage;
  private Connection connection;
  private Integer appointmentID;
  
  @FXML
  private Button saveButton;

  @FXML
  private Button cancelButton;

  @FXML
  private TextField userIDTextField;

  @FXML
  private TextField customerIDTextField;

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
      System.out.println("Valid input!");
      if(this.appointmentID == null){
        createAppointment(
          userIDTextField.getText(),
          customerIDTextField.getText(),
          titleTextField.getText(),
          descriptionTextField.getText(),
          locationTextField.getText(),
          contactTextField.getText(),
          typeTextField.getText(),
          URLTextField.getText(),
          startTextField.getText(),
          endTextField.getText()
        );
      }else{
        // update the appointment
      }
    }
  }
  
  public void setStage(Stage stage) {
    this.stage = stage;
  }
  
  public void setAppointment(Appointment appointment) {
    if(appointment == null){
      this.appointmentID = null;
      userIDTextField.setText("");
      customerIDTextField.setText("");
      titleTextField.setText("");
      descriptionTextField.setText("");
      locationTextField.setText("");
      contactTextField.setText("");
      typeTextField.setText("");
      URLTextField.setText("");
      startTextField.setText("");
      endTextField.setText("");
    }else{
      this.appointmentID = appointment.getID();
      userIDTextField.setText(Integer.toString(appointment.getUserID()));
      customerIDTextField.setText(Integer.toString(appointment.getCustomerID()));
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
  
  public static void showDialog(Stage primaryStage, Connection connection, Appointment appointment, String title) throws IOException{
    
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
    }else{
      appointmentController.setAppointment(null);
    }
    
    // open the popup
    stage.showAndWait();
  }
  
  private boolean isInputValid() {
    String errorMessage = "";

    if (userIDTextField.getText() == null || userIDTextField.getText().length() == 0) {
      errorMessage += "No valid consultant id!\n"; 
    } else {
      try {
        Integer.parseInt(userIDTextField.getText());
      } catch (NumberFormatException e) {
        errorMessage += "No valid consultant id (must be an integer)!\n"; 
      }
    }
    
    if (customerIDTextField.getText() == null || customerIDTextField.getText().length() == 0) {
      errorMessage += "No valid customer id!\n"; 
    } else {
      try {
        Integer.parseInt(customerIDTextField.getText());
      } catch (NumberFormatException e) {
        errorMessage += "No valid customer id (must be an integer)!\n"; 
      }
    }
    
    if(errorMessage.length() == 0){
      errorMessage += checkDates();
    }
    
    if (errorMessage.length() == 0) {
      return true;
    } else {
      AlertDialog.errorDialog(errorMessage);
      return false;
    }
  }
  
  private String checkDates() {
    String errorMessage = "";
    DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    
    try {
      Date start = DATE_FORMAT.parse(startTextField.getText());
      Date end = DATE_FORMAT.parse(endTextField.getText());
    } catch (ParseException ex) {
      errorMessage += "No valid start or end (must be yyyy-MM-dd HH:mm:ss.SSS)!\n";
    }
    return errorMessage;
  }
  
  private void createAppointment(
    String userID,
    String customerID,
    String title,
    String description,
    String location,
    String contact,
    String type,
    String URL,
    String start,
    String end){
    PreparedStatement preparedStatement;
    try {
      preparedStatement = connection.prepareStatement(
        "INSERT INTO appointment (customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdateBy)" +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURDATE(), 'admin', 'admin')");
      preparedStatement.setString(1, customerID);
      preparedStatement.setString(2, userID);
      preparedStatement.setString(3, title);
      preparedStatement.setString(4, description);
      preparedStatement.setString(5, location);
      preparedStatement.setString(6, contact);
      preparedStatement.setString(7, type);
      preparedStatement.setString(8, URL);
      preparedStatement.setString(9, start);
      preparedStatement.setString(10, end);
      preparedStatement.execute();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    stage.close();
  }

}
