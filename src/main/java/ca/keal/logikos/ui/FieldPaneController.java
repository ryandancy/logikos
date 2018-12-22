package ca.keal.logikos.ui;

import ca.keal.logikos.field.FieldComponent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.input.MouseEvent;

/**
 * The controller class for the FieldPane.
 */
public class FieldPaneController extends MainPaneController {
  
  @FXML private PannablePane fieldPane;
  
  @FXML
  private void onMouseMove(MouseEvent e) {
    // Call the selected tool's hover hook
    callSelectedToolHook(e, false);
  }
  
  @FXML
  private void onClick(MouseEvent e) {
    // Call the selected tool's click hook
    callSelectedToolHook(e, true);
  }
  
  @FXML
  @SuppressWarnings("unused")
  private void onLeavePane(MouseEvent e) {
    // Call the selected tool's leave hook
    getApplication().getSelectedTool().onLeavePane(getApplication());
  }
  
  // Call the selected tool's click hook if click is true or its hover hook if click is false
  private void callSelectedToolHook(MouseEvent e, boolean click) {
    // Get the field component it's over
    FieldComponent hoveredFC = null;
    if (e.getSource() instanceof UIComponent) {
      hoveredFC = ((UIComponent) e.getSource()).getFieldComponent();
    }
    
    // Get the coordinates relative to the FieldPane
    Bounds fieldPaneBounds = fieldPane.localToScene(fieldPane.getBoundsInLocal());
    double relX = e.getSceneX() - fieldPaneBounds.getMinX();
    double relY = e.getSceneY() - fieldPaneBounds.getMinY();
    
    // Call the hook
    if (click) {
      getApplication().getSelectedTool().onClick(getApplication(), relX, relY, hoveredFC);
    } else {
      getApplication().getSelectedTool().onHover(getApplication(), relX, relY, hoveredFC);
    }
  }
  
  public PannablePane getFieldPane() {
    return fieldPane;
  }
  
}