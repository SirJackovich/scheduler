package scheduler;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import scheduler.view_controller.LoginController;

public class Scheduler extends Application {
  private Stage primaryStage;
  private AnchorPane loginLayout;
  
  public Scheduler(){
    // setup databse connection stuff
  }
  
  @Override
  public void start(Stage primaryStage) throws Exception {
    this.primaryStage = primaryStage;
    this.primaryStage.setTitle("Scheduler");
    showLogin();
  }
  
  public void showLogin() throws IOException {
    // Load login layout from fxml file
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(Scheduler.class.getResource("view_controller/Login.fxml"));
    loginLayout = (AnchorPane) loader.load();

    // Give the controller access to the main app.
    LoginController loginController = loader.getController();
    loginController.setApp(this);
    loginController.setPrimaryStage(primaryStage);

    // Show the scene containing the login layout
    Scene scene = new Scene(loginLayout);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
  
  public Stage getPrimaryStage() {
    return primaryStage;
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }
  
}
