package ca.keal.logikos.ui;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * The controller class for the FieldPane.
 */
public class FieldPaneController {
  
  @FXML private PannablePane fieldPane;
  
  @FXML
  private void onMouseMove(MouseEvent e) {
    onMouseMove(e, null);
  }
  
  @FXML
  private void onClick(MouseEvent e) {
    onClick(e, null);
  }
  
  public void onMouseMove(MouseEvent e, MousePosition.PortOver portOver) {
    // Call the selected tool's hover hook
    callSelectedToolMouseHook(e, portOver, false);
  }
  
  public void onClick(MouseEvent e, MousePosition.PortOver portOver) {
    // Call the selected tool's click hook for the left mouse button
    if (e.getButton() == MouseButton.PRIMARY) {
      callSelectedToolMouseHook(e, portOver, true);
    }
  }
  
  @FXML
  @SuppressWarnings("unused")
  private void onLeavePane(MouseEvent e) {
    // Call the selected tool's leave hook
    Logikos.getInstance().getSelectedTool().onLeavePane();
  }
  
  // Call the selected tool's click hook if click is true or its hover hook if click is false
  private void callSelectedToolMouseHook(MouseEvent e, MousePosition.PortOver portOver, boolean click) {
    // Get the component or connection it's over
    UIComponent overComponent = null;
    UIConnection overConnection = null;
    if (e.getSource() instanceof UIComponent) {
      overComponent = (UIComponent) e.getSource();
    } else if (e.getSource() instanceof UIConnection) {
      overConnection = (UIConnection) e.getSource();
    } else if (e.getSource() instanceof Node) {
      // Search for a parent UIComponent - what if the user clicked on a node?
      Node source = (Node) e.getSource();
      Parent current = source.getParent();
      while (current != null) {
        if (current instanceof UIComponent) {
          overComponent = (UIComponent) current;
          break;
        }
        current = current.getParent();
      }
    }
    
    MousePosition position = getMousePosition(e, overComponent, overConnection, portOver);
    
    // Call the hook
    if (click) {
      click(position);
    } else {
      hover(position);
    }
  }
  
  /**
   * @return A {@link MousePosition} from the coordinates in the {@link MouseEvent} and the other parameters.
   */
  public MousePosition getMousePosition(MouseEvent e, UIComponent component, UIConnection connection,
                                        MousePosition.PortOver portOver) {
    // Get the coordinates relative to the FieldPane
    Bounds fieldPaneBounds = fieldPane.localToScene(fieldPane.getBoundsInLocal());
    double relX = e.getSceneX() - fieldPaneBounds.getMinX();
    double relY = e.getSceneY() - fieldPaneBounds.getMinY();
    
    return new MousePosition(relX, relY, component, connection, portOver);
  }
  
  /**
   * Call the click hook.
   */
  public void click(MousePosition position) {
    Logikos.getInstance().getSelectedTool().onClick(position);
  }
  
  /**
   * Call the hover hook.
   */
  public void hover(MousePosition position) {
    Logikos.getInstance().getSelectedTool().onHover(position);
  }
  
  public PannablePane getFieldPane() {
    return fieldPane;
  }
  
}