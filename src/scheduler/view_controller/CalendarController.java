package scheduler.view_controller;

import java.awt.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import scheduler.model.Appointment;
import scheduler.model.Customer;

public class CalendarController {
  
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
    private void initialize() {
      viewComboBox.getItems().addAll("Week", "Month");
      viewComboBox.getSelectionModel().select(0);
      System.out.println("I love my wife Sheralyn!!!");
    }
}


