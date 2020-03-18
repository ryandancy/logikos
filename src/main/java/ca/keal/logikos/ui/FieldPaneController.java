package ca.keal.logikos.ui;

import ca.keal.logikos.field.Field;
import ca.keal.logikos.field.FieldComponent;
import ca.keal.logikos.field.InputFC;
import ca.keal.logikos.field.OutputFC;
import ca.keal.logikos.logic.Connection;
import ca.keal.logikos.logic.LogicComponent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
  
  @FXML
  public void onPress(MouseEvent e) {
    if (e.getButton() == MouseButton.PRIMARY) {
      callSelectedToolMouseHook(e, null, Logikos.getInstance().getSelectedTool()::onMousePress);
    }
  }
  
  @FXML
  public void onRelease(MouseEvent e) {
    if (e.getButton() == MouseButton.PRIMARY) {
      callSelectedToolMouseHook(e, null, Logikos.getInstance().getSelectedTool()::onMouseRelease);
    }
  }
  
  public void onMouseMove(MouseEvent e, MousePosition.PortOver portOver) {
    // Call the selected tool's hover hook
    callSelectedToolMouseHook(e, portOver, this::hover);
  }
  
  public void onClick(MouseEvent e, MousePosition.PortOver portOver) {
    // Call the selected tool's click hook for the left mouse button
    if (e.getButton() == MouseButton.PRIMARY) {
      callSelectedToolMouseHook(e, portOver, this::click);
    }
  }
  
  @FXML
  @SuppressWarnings("unused")
  private void onLeavePane(MouseEvent e) {
    // Call the selected tool's leave hook
    Logikos.getInstance().getSelectedTool().onLeavePane();
  }
  
  // Call the selected tool's click hook if click is true or its hover hook if click is false
  private void callSelectedToolMouseHook(
      MouseEvent e, MousePosition.PortOver portOver, Consumer<MousePosition> hook) {
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
    hook.accept(position);
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

  /**
   * Remove all UIComponents from the field and regenerate them from the given Field. 
   */
  // TODO should this go here or be moved to Logikos or somewhere else?
  public void clearAndRegenerateField(Field newField) {
    // Delete all UIComponents - we get a list first so we don't modify the observable list while looping through it.
    List<UIComponent> allUICs = fieldPane.getContentChildren().stream()
        .filter(node -> node instanceof UIComponent)
        .map(node -> (UIComponent) node)
        .collect(Collectors.toList());
    for (UIComponent uic : allUICs) {
      uic.delete();
    }
    
    // Regenerate UIComponents from the new field
    Map<LogicComponent, UIComponent> lcToUIC = new HashMap<>();
    for (FieldComponent fc : newField.getFieldComponents()) {
      UIComponent newUIC;
      if (fc instanceof InputFC) {
        newUIC = new InputUIC((InputFC) fc, false);
      } else if (fc instanceof OutputFC) {
        newUIC = new OutputUIC((OutputFC) fc, false);
      } else {
        newUIC = new UIComponent(fc, fc.getLogicComponent().getName(), false);
      }
      newUIC.setLayoutX(fc.getPosition().getX());
      newUIC.setLayoutY(fc.getPosition().getY());
      fieldPane.getContentChildren().add(newUIC);
      lcToUIC.put(fc.getLogicComponent(), newUIC);
    }
    
    // Regenerate all the UIConnections
    for (FieldComponent fc : newField.getFieldComponents()) {
      LogicComponent lc = fc.getLogicComponent();
      for (int input = 0; input < lc.getNumInputs(); input++) {
        Connection connection = lc.getInput(input).getConnection();
        if (connection != null) {
          int output = connection.getOutput().getPortNumber();
          UIComponent fromUIC = lcToUIC.get(connection.getOutput().getComponent());
          UIComponent toUIC = lcToUIC.get(lc);
          UIConnection uiConnection = new UIConnection(fromUIC, output, toUIC, input);
          fromUIC.addOutputConnection(output, uiConnection);
          toUIC.setInputConnection(input, uiConnection);
          uiConnection.moveToOutputNode(fromUIC.getOutputPorts()[output]);
          fieldPane.getContentChildren().add(uiConnection);
        }
      }
    }
  }
  
}