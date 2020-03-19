package ca.keal.logikos.ui;

import ca.keal.logikos.field.Field;
import ca.keal.logikos.logic.UserGate;
import ca.keal.logikos.util.DeserializationException;
import javafx.scene.control.Alert;

import java.io.IOException;

/**
 * A {@link Tool} which places a {@link UserGate}. It handles generating the {@link UserGate}'s field from disk
 * before placing each gate.
 */
public class PlaceUserGateTool extends PlaceComponentTool {
  
  public PlaceUserGateTool(String name, String filename) {
    super(name, "Add a custom gate: " + name, () -> {
      // regenerate the field
      Field gateField;
      try {
        gateField = Field.fromXml(filename);
      } catch (IOException | DeserializationException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Error loading gate: " + e.getMessage());
        alert.show();
        return null;
      }
      
      return new UserGate(gateField);
    });
  }
  
}
