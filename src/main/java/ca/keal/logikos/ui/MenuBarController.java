package ca.keal.logikos.ui;

import ca.keal.logikos.field.Field;
import ca.keal.logikos.util.DeserializationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import java.io.IOException;

public class MenuBarController {
  
  @FXML
  private void onNewItemPress(ActionEvent e) {
    // TODO programmatically click "select"
    Field newField = new Field();
    Logikos.getInstance().getFieldPaneController().clearAndRegenerateField(newField);
    Logikos.getInstance().setField(newField);
  }
  
  @FXML
  private void onOpenItemPress(ActionEvent e) {
    Field newField = SaveUtil.open(Logikos.getInstance().getPrimaryStage());
    if (newField == null) return;
    
    // TODO programmatically click "select" before regenerating
    
    Logikos.getInstance().getFieldPaneController().clearAndRegenerateField(newField);
    Logikos.getInstance().setField(newField);
  }
  
  @FXML
  private void onSaveItemPress(ActionEvent e) {
    SaveUtil.save(Logikos.getInstance().getPrimaryStage(), Logikos.getInstance().getField());
  }
  
  @FXML
  private void onSaveAsItemPress(ActionEvent e) {
    SaveUtil.saveAs(Logikos.getInstance().getPrimaryStage(), Logikos.getInstance().getField());
  }
  
  @FXML
  private void onAddUserGateItemPress(ActionEvent e) {
    String filename = SaveUtil.promptForFilenameToOpen(Logikos.getInstance().getPrimaryStage());
    
    // parse the field from the file here so that the user gets an alert fast if parsing fails
    // as a bonus, we get the gate name here
    
    Field userField;
    try {
      userField = Field.fromXml(filename);
    } catch (IOException | DeserializationException ex) {
      Alert alert = new Alert(Alert.AlertType.ERROR, "Error importing gate: " + ex.getMessage());
      alert.show();
      return;
    }
    
    Logikos.getInstance().getToolPaneController().addTool(new PlaceUserGateTool(userField.getName(), filename));
  }
  
}
