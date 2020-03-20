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
import ca.keal.logikos.util.DeserializationException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
  public static final Tool[] DEFAULT_TOOLS = {
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
  @FXML private ToolPaneController toolPaneController;
  @FXML private FieldPaneController fieldPaneController;
  @FXML private EvaluationBoxController evaluationBoxController;

  @FXML private MenuItem newItem;
  @FXML private MenuItem saveItem;
  @FXML private MenuItem saveAsItem;
  @FXML private MenuItem openItem;
  @FXML private MenuItem addUserGateItem;
  
  private Stage primaryStage;
  
  // The first tool is initially selected
  private Tool selectedTool = DEFAULT_TOOLS[0];
  private Field field = new Field();
  
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
  
  @FXML
  private void initialize() {
    // set up the menu item actions
    newItem.setOnAction(this::onNew);
    saveItem.setOnAction(e -> SaveUtil.save(getPrimaryStage(), getField()));
    saveAsItem.setOnAction(e -> SaveUtil.saveAs(getPrimaryStage(), getField()));
    openItem.setOnAction(this::onOpen);
    addUserGateItem.setOnAction(this::onAddUserGate);
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
  
  // handle presses of the "new" menu item
  private void onNew(ActionEvent event) {
    // TODO programmatically click "select"
    Field newField = new Field();
    getFieldPaneController().clearAndRegenerateField(newField);
    this.field = newField;
  }
  
  // handle presses of the "open" menu item
  private void onOpen(ActionEvent event) {
    Field newField = SaveUtil.open(primaryStage);
    if (newField == null) return;
    
    // TODO programmatically click "select" before regenerating
    
    getFieldPaneController().clearAndRegenerateField(newField);
    this.field = newField;
  }
  
  // handle presses of the "add user gate" menu item
  private void onAddUserGate(ActionEvent event) {
    String filename = SaveUtil.promptForFilenameToOpen(getPrimaryStage());
    
    // parse the field from the file here so that the user gets an alert fast if parsing fails
    // as a bonus, we get the gate name here
    
    Field userField;
    try {
      userField = Field.fromXml(filename);
    } catch (IOException | DeserializationException e) {
      Alert alert = new Alert(Alert.AlertType.ERROR, "Error importing gate: " + e.getMessage());
      alert.show();
      return;
    }
    
    getToolPaneController().addTool(new PlaceUserGateTool(userField.getName(), filename));
  }
  
}
