package ca.keal.logikos.ui;

import ca.keal.logikos.logic.Connection;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 * Represents a connection between two {@link UIComponent}s. Note that to place the connection accurately, this class
 * relies upon being placed in the FieldPane. An instance of this class should be placed on the FieldPane such that
 * the output, or "from", node is centred at (0, 0).
 */
public class UIConnection extends Group {
  
  private static final double START_END_RADIUS = 3.0;
  
  private final UIComponent fromComponent;
  private final int fromOutputPort;
  private final UIComponent toComponent;
  private final int toInputPort;
  
  public UIConnection(UIComponent fromComponent, int fromOutputPort, UIComponent toComponent, int toInputPort) {
    this.fromComponent = fromComponent;
    this.fromOutputPort = fromOutputPort;
    this.toComponent = toComponent;
    this.toInputPort = toInputPort;
    buildGraphics();
  }
  
  private void buildGraphics() {
    // For some reason the coords of end node are adjusted by "from" port's center coordinates in parent, so we
    // have to compensate
    Node fromPort = fromComponent.getOutputPorts()[fromOutputPort];
    Bounds fromPortBIP = fromPort.getBoundsInParent();
    Node toPort = toComponent.getInputPorts()[toInputPort];
    Bounds toPortBounds = fromPort.sceneToLocal(toPort.localToScene(toPort.getBoundsInLocal()));
    double toCenterX = center(toPortBounds.getMinX(), toPortBounds.getWidth())
        - center(fromPortBIP.getMinX(), fromPortBIP.getWidth());
    double toCenterY = center(toPortBounds.getMinY(), toPortBounds.getHeight())
        - center(fromPortBIP.getMinY(), fromPortBIP.getHeight());
    
    Circle start = new Circle(0, 0, START_END_RADIUS, UIComponent.FOREGROUND_COLOR);
    Circle end = new Circle(toCenterX, toCenterY, START_END_RADIUS, UIComponent.FOREGROUND_COLOR);
    Line wire = new Line(0, 0, toCenterX, toCenterY);
    wire.setFill(UIComponent.FOREGROUND_COLOR);
    
    // Don't block the mouse from the port on either end
    start.setMouseTransparent(true);
    end.setMouseTransparent(true);
    
    getChildren().addAll(start, end, wire);
  }
  
  private double center(double pos, double length) {
    return pos + (length / 2);
  }
  
  public UIComponent getFromComponent() {
    return fromComponent;
  }
  
  public int getFromOutputPort() {
    return fromOutputPort;
  }
  
  public UIComponent getToComponent() {
    return toComponent;
  }
  
  public int getToInputPort() {
    return toInputPort;
  }
  
  /**
   * @return The {@link Connection} corresponding to this {@link UIConnection}, assuming that it's still on the
   * "to" logic component.
   */
  private Connection getLogicConnection() {
    try {
      return toComponent.getFieldComponent()
          .getLogicComponent()
          .getInput(toInputPort)
          .getConnection();
    } catch (NullPointerException e) {
      return null;
    }
  }
  
  /**
   * Remove this {@link UIConnection} from both the input and output {@link UIComponent}s and unregister the
   * corresponding {@link Connection} in the logic layer. This object should then be garbage collected.
   */
  public void destruct() {
    fromComponent.getOutputConnections(fromOutputPort).remove(this);
    toComponent.getInputConnections()[toInputPort] = null;
    
    Connection connection = getLogicConnection();
    if (connection != null) {
      connection.destruct();
    }
  }
  
}