package scheduler.view_controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import scheduler.Scheduler;
import scheduler.model.Appointment;

public class ReportsController {

  private Stage stage;
  private Connection connection;
  
  @FXML
  private ComboBox<String> typeComboBox;

  @FXML
  private ComboBox<String> consultantComboBox;

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
        appointmentsByMonth("SELECT description,COUNT(*) as count FROM appointment GROUP BY description");
        break;
      case "Consultant Schedule":
        consultantSchedule("SELECT appointmentid, start, title, description, customerId FROM appointment where contact='" + consultantComboBox.getValue() + "'");
        break;
      case "Consultant Freetime":
        System.out.println("Consultant Freetime");
        break;
      default:
        System.out.println("no type selected");
    }
  }
  
  public void appointmentsByMonth(String query) throws ClassNotFoundException{
    // ObservableList<Appointment> calendar = FXCollections.observableArrayList();
//    ResultSet resultSet = getDataFromDataBase(query);
//    tableColumn1.setText("Time");
//    tableColumn2.setText("Name");
//    tableColumn3.setText("Type");
//    tableColumn4.setText("Customer");
//    
//    tableColumn1.setCellValueFactory(cellData -> cellData.getValue().startProperty());
//    tableColumn2.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
//    tableColumn3.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
//    tableColumn4.setCellValueFactory(cellData -> cellData.getValue().customerIDProperty().asObject());
//    
//    try {
//      while (resultSet.next()) {
//        Integer appointmentID = resultSet.getInt("appointmentid");
//        String time = resultSet.getString("start");
//        String name = resultSet.getString("title");
//        String description = resultSet.getString("description");
//        Integer customerID = resultSet.getInt("customerId");
//        Appointment appointment = new Appointment(appointmentID, time, name, description, customerID);
//        calendar.add(appointment);
//      }
//      tableView.setItems(calendar);
//    } catch (SQLException ex) {
//        Logger.getLogger(CalendarController.class.getName()).log(Level.SEVERE, null, ex);
//    }
  }
  
  public void consultantSchedule(String query) throws ClassNotFoundException{
    ObservableList<Appointment> calendar = FXCollections.observableArrayList();
    ResultSet resultSet = getDataFromDataBase(query);
    tableColumn1.setText("Time");
    tableColumn2.setText("Name");
    tableColumn3.setText("Type");
    tableColumn4.setText("Customer");
    
    tableColumn1.setCellValueFactory(
      new PropertyValueFactory<>("start")
    );
    tableColumn2.setCellValueFactory(
      new PropertyValueFactory<>("name")
    );
    tableColumn3.setCellValueFactory(
      new PropertyValueFactory<>("type")
    );
    tableColumn4.setCellValueFactory(
      new PropertyValueFactory<>("customerID")
    );
    
    try {
      while (resultSet.next()) {
        Integer appointmentID = resultSet.getInt("appointmentid");
        String time = resultSet.getString("start");
        String name = resultSet.getString("title");
        String description = resultSet.getString("description");
        Integer customerID = resultSet.getInt("customerId");
        Appointment appointment = new Appointment(appointmentID, time, name, description, customerID);
        calendar.add(appointment);
      }
      tableView.setItems(calendar);
    } catch (SQLException ex) {
        Logger.getLogger(CalendarController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @FXML
  void handleTypeComboBox() {
    String type = typeComboBox.getValue();
    if ("Appointments by Month".equals(type)){
      consultantComboBox.setDisable(true);
    } else{
      consultantComboBox.setDisable(false);
    }
    
  }

  public static void showDialog(Stage primaryStage, String title) throws IOException{
    
    // Load the fxml file and create a new stage for the popup dialog.
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(Scheduler.class.getResource("view_controller/Reports.fxml"));
    BorderPane page = (BorderPane) loader.load();

    // Create the dialog Stage.
    Stage stage = new Stage();
    stage.setTitle(title);
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.initOwner(primaryStage);
    Scene scene = new Scene(page);
    stage.setScene(scene);
    
    // set the stage so we can close it if needed
    ReportsController reportsController = loader.getController();
    reportsController.setStage(stage);
    
    // open the popup
    stage.showAndWait();
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }
  
   @FXML
  private void initialize() throws IOException, ClassNotFoundException{
    makeConnection();
    ResultSet resultSet = getDataFromDataBase("SELECT userName FROM user");
    
    ArrayList<String> consultants = new ArrayList<>();
    try {
      while (resultSet.next()) {
        String consultant = resultSet.getString("userName");
        consultants.add(consultant);
      }
    } catch (SQLException ex) {
        Logger.getLogger(CalendarController.class.getName()).log(Level.SEVERE, null, ex);
    }
    typeComboBox.getItems().addAll("Appointments by Month", "Consultant Schedule", "Consultant Freetime");
    typeComboBox.getSelectionModel().select(0);
    consultantComboBox.setDisable(true);
    
    consultantComboBox.getItems().addAll(consultants);
    consultantComboBox.getSelectionModel().select(0);
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
  
  private void makeConnection(){
    String URL = "jdbc:mysql://52.206.157.109/U04bLJ";
    String username = "U04bLJ";
    String password = "53688195100";
    ResultSet resultSet = null;
    Statement statement;
    try {
      Class.forName("com.mysql.jdbc.Driver");
      connection = DriverManager.getConnection(URL, username, password);
      System.out.println("Making connection...");
    } catch (ClassNotFoundException | SQLException ex) {
      ex.printStackTrace();
    }
  }
}