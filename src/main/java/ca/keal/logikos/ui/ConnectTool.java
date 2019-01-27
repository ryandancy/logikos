package ca.keal.logikos.ui;

import javafx.geometry.Bounds;
import javafx.scene.Node;

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
      ghost.setLayoutX(fieldPane.paneToRealX(portBounds.getMinX() + portBounds.getWidth() / 2));
      ghost.setLayoutY(fieldPane.paneToRealY(portBounds.getMinY() + portBounds.getHeight() / 2));
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
    
    // Get the coordinates of the center of the output node for placing the connection
    Node outputNode = output.getOutputPorts()[outputPort];
    Bounds outputBounds = fieldPane.sceneToLocal(outputNode.localToScene(outputNode.getBoundsInLocal()));
    double outputCenterX = fieldPane.paneToRealX(outputBounds.getMinX()) + (outputBounds.getWidth() / 2);
    double outputCenterY = fieldPane.paneToRealY(outputBounds.getMinY()) + (outputBounds.getHeight() / 2);
    
    UIConnection connection = new UIConnection(output, outputPort, input, inputPort);
    output.getOutputConnections(outputPort).add(connection);
    input.getInputConnections()[inputPort] = connection;
    
    connection.setLayoutX(outputCenterX);
    connection.setLayoutY(outputCenterY);
    fieldPane.getContentChildren().add(connection);
  }
  
  @Override
  public void onHover(MousePosition position) {
    if (!ghost.isVisible()) return;
    
    // Update the 'to' center coordinates of the ghost to make it 'stretch' with the mouse
    ghost.setToCenter(position.getPaneX(), position.getPaneY());
  }
  
  private void clearStoredPortData() {
    storedComponent = null;
    storedPortNumber = -1;
    
    // Clear the ghost
    ghost.setVisible(false);
    Logikos.getInstance().getFieldPaneController().getFieldPane().getContentChildren().remove(ghost);
  }
  
}