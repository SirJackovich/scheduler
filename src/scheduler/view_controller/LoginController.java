package scheduler.view_controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import scheduler.Scheduler;

public class LoginController {
  
  private Stage stage;
  private Boolean auth = false;
  
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
      System.out.println("You got it right!");
      auth = true;
      stage.close();
    }else{
      auth = false;
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Invalid Credentials");
      alert.setContentText("The username and password did not match.");
      alert.showAndWait();
    }
  }
  
  public void setStage(Stage stage) {
    this.stage = stage;
  }
  
  public Boolean getAuth(){
    return auth;
  }

  public static Boolean showDialog(Stage primaryStage) throws IOException{
    
    // Load the fxml file and create a new stage for the popup dialog.
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(Scheduler.class.getResource("view_controller/Login.fxml"));
    AnchorPane page = (AnchorPane) loader.load();

    // Create the dialog Stage.
    Stage stage = new Stage();
    stage.setTitle("Login");
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.initOwner(primaryStage);
    Scene scene = new Scene(page);
    stage.setScene(scene);
    
    // set the stage so we can close it if needed
    LoginController loginController = loader.getController();
    loginController.setStage(stage);
    
    // open the popup
    stage.showAndWait();
    
    // return if the user is authorized or not
    return loginController.getAuth();
  }
}