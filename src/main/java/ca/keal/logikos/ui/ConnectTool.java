package ca.keal.logikos.ui;

import javafx.geometry.Bounds;
import javafx.scene.Node;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class ConnectTool extends Tool {
  
  private UIComponent storedComponent = null; // if null: no output port selected
  private int storedPortNumber = -1;
  private boolean isInputStored;
  
  /** The ghost connection displayed when one port has been clicked on and the other has not yet */
  private UIConnection ghost = UIConnection.ghost();
  
  public ConnectTool() {
    super("Connect", "Connect an input and an output port on two components");
  }
  
  @Override
  public void onClick(MousePosition position) {
    MousePosition.PortOver portOver = position.getPortOver();
    if (portOver == null) {
      // Clear it if it's not clicking on a port
      clearStoredPortData();
      return;
    }
    
    if (storedComponent == null) {
      // Store the data for when the user clicks on another port
      storedComponent = position.getComponent();
      storedPortNumber = portOver.getPortNumber();
      isInputStored = portOver.isInput();
      
      // Add the ghost
      PannablePane fieldPane = Logikos.getInstance().getFieldPaneController().getFieldPane();
      Node port = (isInputStored ? storedComponent.getInputPorts()
          : storedComponent.getOutputPorts())[storedPortNumber];
      Bounds portBounds = fieldPane.sceneToLocal(port.localToScene(port.getLayoutBounds()));
      ghost.setLayoutX(fieldPane.paneToRealX(portBounds.getMinX() + Math.max(0, -fieldPane.getOffsetX()))
          + (portBounds.getWidth() / 2));
      ghost.setLayoutY(fieldPane.paneToRealY(portBounds.getMinY() + Math.max(0, -fieldPane.getOffsetY()))
          + (portBounds.getHeight() / 2));
      ghost.setToCenterRelativeCoords(0, 0);
      ghost.setVisible(true);
      fieldPane.getContentChildren().add(ghost);
      ghost.toFront();
    } else {
      if (isInputStored == portOver.isInput()) {
        // The user clicked on the same kind of port: store that port's data
        clearStoredPortData();
        onClick(position);
        return;
      }
      
      // Connect the components
      if (portOver.isInput()) {
        connect(storedComponent, storedPortNumber, position.getComponent(), portOver.getPortNumber());
      } else {
        connect(position.getComponent(), portOver.getPortNumber(), storedComponent, storedPortNumber);
      }
      clearStoredPortData();
    }
  }
  
  private void connect(UIComponent output, int outputPort, UIComponent input, int inputPort) {
    PannablePane fieldPane = Logikos.getInstance().getFieldPaneController().getFieldPane();
    
    UIConnection oldInputConnection = input.getInputConnections()[inputPort];
    if (oldInputConnection != null) {
      // Remove the old connection at the input port, if it existed
      oldInputConnection.delete();
    }
    
    output.getFieldComponent().getLogicComponent().getOutput(outputPort)
        .connectTo(input.getFieldComponent().getLogicComponent().getInput(inputPort));
    
    UIConnection connection = new UIConnection(output, outputPort, input, inputPort);
    output.addOutputConnection(outputPort, connection);
    input.setInputConnection(inputPort, connection);
    
    connection.moveToOutputNode(output.getOutputPorts()[outputPort]);
    fieldPane.getContentChildren().add(connection);
    
    Logikos.getInstance().getField().setModified(true);
    Logikos.getInstance().getWindowTitleManager().update();
  }
  
  @Override
  public void onHover(MousePosition position) {
    if (storedComponent == null) return;
    
    PannablePane fieldPane = Logikos.getInstance().getFieldPaneController().getFieldPane();
    updateGhostToCoords(position);
    
    if (!fieldPane.getContentChildren().contains(ghost)) {
      fieldPane.getContentChildren().add(ghost);
      ghost.setVisible(true);
    }
  }
  
  @Override
  public void onLeavePane() {
    super.onLeavePane();
    removeGhost();
  }
  
  private void updateGhostToCoords(MousePosition position) {
    // Update the 'to' center coordinates of the ghost to make it 'stretch' with the mouse
    PannablePane fieldPane = Logikos.getInstance().getFieldPaneController().getFieldPane();
    ghost.setToCenter(position.getPaneX() - Math.max(0, -fieldPane.getOffsetX()),
        position.getPaneY() - Math.max(0, -fieldPane.getOffsetY()));
  }
  
  private void clearStoredPortData() {
    storedComponent = null;
    storedPortNumber = -1;
    removeGhost();
  }
  
  private void removeGhost() {
    ghost.setVisible(false);
    Logikos.getInstance().getFieldPaneController().getFieldPane().getContentChildren().remove(ghost);
  }
  
}