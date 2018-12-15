package ca.keal.logikos.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * The main JavaFX application.
 */
public class LogikosApplication extends Application {
  
  private Stage primaryStage;
  
  public static void main(String[] args) {
    launch(args);
  }
  
  @Override
  public void start(Stage primaryStage) {
    // TODO update this with save status - SavingManager as util class?
    this.primaryStage = primaryStage;
    primaryStage.setTitle("Logikos");
    
    // Set RootLayout as the scene
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("RootLayout.fxml"));
      primaryStage.setScene(new Scene(loader.load()));
      primaryStage.show();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
  
  public Stage getPrimaryStage() {
    return primaryStage;
  }
  
}
