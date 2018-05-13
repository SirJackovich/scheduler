package scheduler;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import scheduler.view_controller.CalendarController;

public class Scheduler extends Application {
  private Stage stage;
  private BorderPane calendarLayout;
  
  public Scheduler(){
    // setup databse connection stuff
  }
  
  @Override
  public void start(Stage stage) throws Exception {
    this.stage = stage;
    this.stage.setTitle("Scheduler");
    showCalendar();
  }
  
  private void showCalendar() throws IOException {
    // Load calendar layout from fxml file
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(Scheduler.class.getResource("view_controller/Calendar.fxml"));
    calendarLayout = (BorderPane) loader.load();

    // Give the controller access to the main app.
    CalendarController calendarController = loader.getController();
    calendarController.setStage(stage);

    // Show the scene containing the calendar layout
    Scene scene = new Scene(calendarLayout);
    stage.setScene(scene);
    stage.show();
  }
  
  public Stage getStage() {
    return stage;
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }
  
}
