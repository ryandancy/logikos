package ca.keal.logikos.ui;

import javafx.geometry.Bounds;
import javafx.scene.Node;

// TODO a "ghost" similar to that in PlaceComponentTool
public class ConnectTool extends Tool {
  
  private UIComponent storedComponent = null; // if null: no output port selected
  private int storedPortNumber = -1;
  private boolean isInputStored;
  
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
      // TODO add a ghost here
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
      oldInputConnection.destruct();
      fieldPane.getContentChildren().remove(oldInputConnection);
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
  
  private void clearStoredPortData() {
    storedComponent = null;
    storedPortNumber = -1;
  }
  
}