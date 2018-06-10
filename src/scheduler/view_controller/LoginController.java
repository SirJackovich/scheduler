package scheduler.view_controller;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import scheduler.Scheduler;
import scheduler.model.User;

public class LoginController {
  
  private Stage stage;
  private Connection connection;
  private boolean auth = false;
  private final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  
  @FXML
  private TextField usernameTextField;

  @FXML
  private TextField passwordField;

  private ObservableList<User> getUsers(){
    ObservableList<User> users = FXCollections.observableArrayList();
    try (Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("Select userid, userName, password from user");) {
      try {
        while (resultSet.next()) {
          int userID = resultSet.getInt("userid");
          String username = resultSet.getString("userName");
          String password = resultSet.getString("password");
          User user = new User(userID, username, password);
          users.add(user);
        }
      } catch (SQLException ex) {
          Logger.getLogger(CalendarController.class.getName()).log(Level.SEVERE, null, ex);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return users;
  }

  @FXML
  private void handleLogin() throws IOException {
    auth = false;
    // default username and password is "test"
    String username = usernameTextField.getText();
    String password = passwordField.getText();
    ObservableList<User> users = getUsers();
    for (User user : users) {
			if(user.getUsername().equals(username) && user.getPassword().equals(password)){
        auth = true;
      }
		}
    if(auth){
      Calendar cal = Calendar.getInstance();	
      String time = DATE_FORMAT.format(cal.getTime());
      List<String> lines = Arrays.asList(time + " User: " + username + " logged in.");
      Path file = Paths.get("scheduler_logs.txt");
      try {
        Files.createFile(file);
      } catch (FileAlreadyExistsException ex) {
        // file already exists
      }
      Files.write(file, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
      stage.close();
    }else{
      // Language testing
      // Locale.setDefault(new Locale("es", "ES"));
      
      ResourceBundle rb = ResourceBundle.getBundle("locales/scheduler");
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle(rb.getString("title"));
      alert.setContentText(rb.getString("message"));
      alert.showAndWait();
    }
  }
  
  public boolean getAuth(){
    return auth;
  }
  
  public void setConnection(Connection connection) {
    this.connection = connection;
  }
  
  public void setStage(Stage stage) {
    this.stage = stage;
  }
  
  public static boolean showDialog(Stage primaryStage, Connection connection) throws IOException{
    
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