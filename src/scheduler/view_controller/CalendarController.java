package scheduler.view_controller;

import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import scheduler.Scheduler;
import scheduler.model.Appointment;
import scheduler.model.Customer;

public class CalendarController {
  
  private Scheduler app;
  private Stage primaryStage;
  
  @FXML
  private ComboBox viewComboBox;

  @FXML
  private TableView<Appointment> calendarTableView;

  @FXML
  private TableColumn<Appointment, ?> timeTableColumn;

  @FXML
  private TableColumn<Appointment, String> nameTableColumn;

  @FXML
  private TableColumn<Appointment, String> typeTableColumn;

  @FXML
  private TableColumn<Appointment, Customer> customerTableColumn;

  @FXML
  private Button customerButton;

  @FXML
  private Button appointmentButton;

  @FXML
  private Button reportButton;


  @FXML
  private void handleComboBox() {
    System.out.println(viewComboBox.getValue());
  }
  
  @FXML
  private void handleCustomerButton() throws IOException{
    CustomerController.showDialog(primaryStage);
  }
  
  @FXML
  private void handleAddButton() throws IOException{
    AppointmentController.showDialog(primaryStage, "Add Appointment");
  }
  
  @FXML
  private void handleModifyButton() throws IOException{
    AppointmentController.showDialog(primaryStage, "Modify Appointment");
  }

  @FXML
  private void initialize() throws IOException{
    viewComboBox.getItems().addAll("Week", "Month");
    viewComboBox.getSelectionModel().select(0);
    if(LoginController.showDialog(primaryStage)){
      System.out.println("I love my wife Sheralyn!!!");
    }else{
      System.out.println("I dont love my wife Sheralyn!!!");
      Platform.exit();
    }
  }
  
  public void setApp(Scheduler app) {
    this.app = app;
  }
  
  public void setPrimaryStage(Stage primaryStage){
    this.primaryStage = primaryStage;
  }
}


