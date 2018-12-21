package ca.keal.logikos.ui;

import ca.keal.logikos.field.Field;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * The main JavaFX application. Also the controller for the RootLayout. It contains some global data, such as what tool
 * is selected and the {@link Field}.
 */
public class LogikosApplication extends Application {
  
  // These are loaded after this class is set as the controller for the RootLayout
  @FXML private ToolPaneController toolPaneController;
  @FXML private FieldPaneController fieldPaneController;
  
  private Stage primaryStage;
  
  // The first tool is initially selected
  private Tool selectedTool = Tool.ALL_TOOLS[0];
  private final Field field = new Field(); // TODO saving & reloading
  
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
      loader.setController(this);
      primaryStage.setScene(new Scene(loader.load()));
      primaryStage.show();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
    
    // Tell the first tool that it's been selected
    getSelectedTool().onSelect();
  }
  
  @FXML
  private void initialize() {
    // Inject this Application into each controller
    toolPaneController.setApplication(this);
    fieldPaneController.setApplication(this);
  }
  
  public Stage getPrimaryStage() {
    return primaryStage;
  }
  
  public void setSelectedTool(Tool selectedTool) {
    if (selectedTool == null) {
      throw new NullPointerException("Cannot set a null tool!");
    }
    this.selectedTool = selectedTool;
  }
  
  public Tool getSelectedTool() {
    return selectedTool;
  }
  
  public Field getField() {
    return field;
  }
  
  public FieldPaneController getFieldPaneController() {
    return fieldPaneController;
  }
  
  public ToolPaneController getToolPaneController() {
    return toolPaneController;
  }
  
}
