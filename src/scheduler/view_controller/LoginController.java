package scheduler.view_controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import scheduler.model.User;

public class LoginController {
  
  private Stage stage;
  private Connection connection;
  private Boolean auth = false;
  
  @FXML
  private TextField usernameTextField;

  @FXML
  private TextField passwordField;

  @FXML
  private Button loginButton;

  @FXML
  void handleLogin() {
    auth = false;
    String username = usernameTextField.getText();
    String password = passwordField.getText();
    ObservableList<User> users = getUsers();
    for (User user : users) {
			if(user.getUsername().equals(username) && user.getPassword().equals(password)){
        auth = true;
      }
		}
    if(auth){
      stage.close();
    }else{
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Invalid Credentials");
      alert.setContentText("The username and password did not match.");
      alert.showAndWait();
    }
  }
  
  public void setStage(Stage stage) {
    this.stage = stage;
  }
  
  public void setConnection(Connection connection) {
    this.connection = connection;
  }
  
  public Boolean getAuth(){
    return auth;
  }
  
  private ObservableList<User> getUsers(){
    ResultSet resultSet = null;
    Statement statement;
    try {
      statement = connection.createStatement();
      resultSet = statement.executeQuery("Select userid, userName, password from user");
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    ObservableList<User> users = FXCollections.observableArrayList();
    try {
      while (resultSet.next()) {
        Integer userID = resultSet.getInt("userid");
        String username = resultSet.getString("userName");
        String password = resultSet.getString("password");
        User user = new User(userID, username, password);
        users.add(user);
      }
    } catch (SQLException ex) {
        Logger.getLogger(CalendarController.class.getName()).log(Level.SEVERE, null, ex);
    }
    return users;
  }

  public static Boolean showDialog(Stage primaryStage, Connection connection) throws IOException{
    
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
    loginController.setConnection(connection);
    
    // open the popup
    stage.showAndWait();
    
    // return if the user is authorized or not
    return loginController.getAuth();
  }
}