package ca.keal.logikos.ui;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * The controller class for the FieldPane.
 */
public class FieldPaneController {
  
  @FXML private PannablePane fieldPane;
  
  @FXML
  public void onMouseMove(MouseEvent e) {
    // Call the selected tool's hover hook
    callSelectedToolMouseHook(e, false);
  }
  
  @FXML
  public void onClick(MouseEvent e) {
    // Call the selected tool's click hook for the left mouse button
    if (e.getButton() == MouseButton.PRIMARY) {
      callSelectedToolMouseHook(e, true);
    }
  }
  
  @FXML
  @SuppressWarnings("unused")
  private void onLeavePane(MouseEvent e) {
    // Call the selected tool's leave hook
    Logikos.getInstance().getSelectedTool().onLeavePane();
  }
  
  // Call the selected tool's click hook if click is true or its hover hook if click is false
  private void callSelectedToolMouseHook(MouseEvent e, boolean click) {
    // Get the field component it's over
    UIComponent overComponent = null;
    if (e.getSource() instanceof UIComponent) {
      overComponent = (UIComponent) e.getSource();
    }
    
    // Get the coordinates relative to the FieldPane
    Bounds fieldPaneBounds = fieldPane.localToScene(fieldPane.getBoundsInLocal());
    double relX = e.getSceneX() - fieldPaneBounds.getMinX();
    double relY = e.getSceneY() - fieldPaneBounds.getMinY();
    
    // Call the hook
    if (click) {
      Logikos.getInstance().getSelectedTool().onClick(relX, relY, overComponent);
    } else {
      Logikos.getInstance().getSelectedTool().onHover(relX, relY, overComponent);
    }
  }
  
  public PannablePane getFieldPane() {
    return fieldPane;
  }
  
}