package scheduler.view_controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import scheduler.Scheduler;

public class LoginController {
  
  private Scheduler app;
  private Stage primaryStage;

  @FXML
  private TextField usernameTextField;

  @FXML
  private TextField passwordField;

  @FXML
  private Button loginButton;

  @FXML
  void handleLogin() {
    String username = usernameTextField.getText();
    String password = passwordField.getText();
    System.out.println("username: " + username);
    System.out.println("password: " + password);
    if("test".equals(username) && "test".equals(password)){
      // CallenderController.showDialog();
      System.out.println("You got it right!");
    }else{
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Invalid Credentials");
      alert.setContentText("The username and password did not match.");
      alert.showAndWait();
    }
  }
    
  public void setApp(Scheduler app) {
    this.app = app;
  }
  
  public void setPrimaryStage(Stage primaryStage){
    this.primaryStage = primaryStage;
  }

}
