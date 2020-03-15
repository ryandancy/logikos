package ca.keal.logikos.ui;

import ca.keal.logikos.field.Field;
import ca.keal.logikos.field.InputFC;
import ca.keal.logikos.field.OutputFC;
import ca.keal.logikos.logic.AndGate;
import ca.keal.logikos.logic.Input;
import ca.keal.logikos.logic.NandGate;
import ca.keal.logikos.logic.NotGate;
import ca.keal.logikos.logic.OrGate;
import ca.keal.logikos.logic.Output;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * The main JavaFX application. Also the controller for the RootLayout. It contains some global data, such as what tool
 * is selected and the {@link Field}. This class is a singleton so that every UI class has access to its data - its
 * instance is created automatically when loading and can be retrieved with {@link #getInstance()}.
 */
// TODO this should *probably* not also be the controller for RootLayout
public class Logikos extends Application {
  
  private static Logikos instance = null;
  
  /**
   * @return The {@link Logikos} singleton.
   */
  public static Logikos getInstance() {
    return instance;
  }
  
  // TODO more tools
  public static final Tool[] ALL_TOOLS = {
      new SelectTool(),
      new ConnectTool(),
      new PlaceTypedComponentTool<>("Input", "Add an input",
          type -> position -> new InputFC(new Input(), position, type),
          (fc, isGhost) -> new InputUIC((InputFC) fc, isGhost),
          InputFC.Type.SWITCH, InputFC.Type.values()),
      new PlaceTypedComponentTool<>("Output", "Add an output",
          type -> position -> new OutputFC(new Output(), position, type),
          (fc, isGhost) -> new OutputUIC((OutputFC) fc, isGhost),
          OutputFC.Type.LAMP, OutputFC.Type.values()),
      new PlaceComponentTool("AND", "Add an AND gate", AndGate::new),
      new PlaceComponentTool("OR", "Add an OR gate", OrGate::new),
      new PlaceComponentTool("NOT", "Add a NOT gate", NotGate::new),
      new PlaceComponentTool("NAND", "Add a NAND gate", NandGate::new),
      
      // these go in the evaluation box
      new RunTool()
  };
  
  // These are loaded after this class is set as the controller for the RootLayout
  @FXML @SuppressWarnings("unused") private ToolPaneController toolPaneController;
  @FXML @SuppressWarnings("unused") private FieldPaneController fieldPaneController;
  @FXML @SuppressWarnings("unused") private EvaluationBoxController evaluationBoxController;
  
  private Stage primaryStage;
  
  // The first tool is initially selected
  private Tool selectedTool = ALL_TOOLS[0];
  private final Field field = new Field(); // TODO saving & reloading
  
  public Logikos() {
    super();
    // synchronizing in case multiple threads call this at the same time
    synchronized (Logikos.class) {
      if (instance != null) {
        throw new IllegalStateException("Logikos is a singleton and cannot be instantiated more than once");
      }
      instance = this;
    }
  }
  
  public static void main(String[] args) {
    launch(args);
  }
  
  @Override
  public void start(Stage primaryStage) {
    // TODO update this with save status - SavingManager as util class?
    this.primaryStage = primaryStage;
    primaryStage.setTitle("Logikos");
    
    // Add the save option
    MenuBar menuBar = new MenuBar();
    Menu file = new Menu("File");
    MenuItem save = new MenuItem("Save");
    save.setOnAction(e -> SaveUtil.save(primaryStage, field));
    MenuItem saveAs = new MenuItem("Save as...");
    saveAs.setOnAction(e -> SaveUtil.saveAs(primaryStage, field));
    file.getItems().addAll(save, saveAs);
    menuBar.getMenus().add(file);
    
    // Set RootLayout as the scene
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("RootLayout.fxml"));
      loader.setController(this);
      primaryStage.setScene(new Scene(loader.load()));
      
      // Handle key presses + scrolling globally
      primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, this::onKeyPress);
      primaryStage.addEventFilter(ScrollEvent.ANY, this::onScroll);
      
      primaryStage.requestFocus();
      primaryStage.show();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
    
    // Tell the first tool that it's been selected
    getSelectedTool().onSelect();
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
  
  public EvaluationBoxController getEvaluationBoxController() {
    return evaluationBoxController;
  }
  
  // handle global key presses (just delegate to the selected tool)
  private void onKeyPress(KeyEvent e) {
    getSelectedTool().onKeyPress(e);
  }
  
  // same, for scrolling
  private void onScroll(ScrollEvent e) {
    getSelectedTool().onScroll(e);
  }
  
}
