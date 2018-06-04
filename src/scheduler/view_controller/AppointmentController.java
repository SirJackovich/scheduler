package scheduler.view_controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import scheduler.Scheduler;
import scheduler.model.Appointment;

public class AppointmentController {

  private Stage stage;
  private Appointment appointment;
  
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

  }
  
  public void setStage(Stage stage) {
    this.stage = stage;
  }
  
  public void setAppointment(Appointment appointment) {
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
    this.appointment = appointment;
  }
  
  public static void showDialog(Stage primaryStage, Appointment appointment, String title) throws IOException{
    
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
    
    if(appointment != null){
      appointmentController.setAppointment(appointment);
    }
    
    // open the popup
    stage.showAndWait();
  }

}
