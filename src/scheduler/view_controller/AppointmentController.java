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
        createAppointment(
          userIDComboBox.getValue(),
          customerIDComboBox.getValue(),
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
        updateAppointment(
          userIDComboBox.getValue(),
          customerIDComboBox.getValue(),
          titleTextField.getText(),
          descriptionTextField.getText(),
          locationTextField.getText(),
          contactTextField.getText(),
          typeTextField.getText(),
          URLTextField.getText(),
          startTextField.getText(),
          endTextField.getText()
        );
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
    // get the users
    ResultSet resultSet1 = getDataFromDataBase("SELECT userid FROM user");
    ArrayList<String> users = new ArrayList<>();
    try {
      while (resultSet1.next()) {
        String userID = resultSet1.getString("userid");
        users.add(userID);
      }
    } catch (SQLException ex) {
        Logger.getLogger(CalendarController.class.getName()).log(Level.SEVERE, null, ex);
    }
    userIDComboBox.getItems().clear();
    userIDComboBox.getItems().addAll(users);
    
    
    // get the customers
    ResultSet resultSet2 = getDataFromDataBase("SELECT customerid FROM customer");
    ArrayList<String> customers = new ArrayList<>();
    try {
      while (resultSet2.next()) {
        String customerid = resultSet2.getString("customerid");
        customers.add(customerid);
      }
    } catch (SQLException ex) {
        Logger.getLogger(CalendarController.class.getName()).log(Level.SEVERE, null, ex);
    }
    customerIDComboBox.getItems().clear();
    customerIDComboBox.getItems().addAll(customers);
    
    
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
      preparedStatement.setString(9, DateTime.makeDateUTC(start));
      preparedStatement.setString(10, DateTime.makeDateUTC(end));
      preparedStatement.execute();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    stage.close();
  }
  
  private void updateAppointment(
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
        "UPDATE appointment " +
        "SET customerId=?, userId=?, title=?, description=?, location=?, contact=?, type=?, url=?, start=?, end=? " +
        "WHERE appointmentid = ?");
      preparedStatement.setString(1, customerID);
      preparedStatement.setString(2, userID);
      preparedStatement.setString(3, title);
      preparedStatement.setString(4, description);
      preparedStatement.setString(5, location);
      preparedStatement.setString(6, contact);
      preparedStatement.setString(7, type);
      preparedStatement.setString(8, URL);
      preparedStatement.setString(9, DateTime.makeDateUTC(start));
      preparedStatement.setString(10, DateTime.makeDateUTC(end));
      preparedStatement.setString(11, this.appointmentID);
      preparedStatement.execute();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    stage.close();
  }

}
