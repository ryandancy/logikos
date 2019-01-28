package ca.keal.logikos.ui;

import ca.keal.logikos.logic.Connection;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 * Represents a connection between two {@link UIComponent}s. Note that to place the connection accurately, this class
 * relies upon being placed in the FieldPane. An instance of this class should be placed on the FieldPane such that
 * the output, or "from", node is centred at (0, 0).
 */
public class UIConnection extends Group implements Selectable {
  
  private static final double LINE_WIDTH = 2.0;
  private static final double CLICKABLE_WIDTH = 16.0; // the width within which mouse events are registered
  private static final double START_END_RADIUS = 3.0;
  
  private static final DropShadow SELECTED_DROP_SHADOW = new DropShadow(
      BlurType.THREE_PASS_BOX, UIColors.FOREGROUND_COLOR, 25.0, 0.75, 0, 0);
  
  private final UIComponent fromComponent;
  private final int fromOutputPort;
  private final UIComponent toComponent;
  private final int toInputPort;
  
  private final boolean isGhost;
  
  private DoubleProperty toCenterXProperty = new SimpleDoubleProperty();
  private DoubleProperty toCenterYProperty = new SimpleDoubleProperty();
  
  private Circle start;
  private Circle end;
  private Line wire;
  
  public UIConnection(UIComponent fromComponent, int fromOutputPort, UIComponent toComponent, int toInputPort) {
    this.fromComponent = fromComponent;
    this.fromOutputPort = fromOutputPort;
    this.toComponent = toComponent;
    this.toInputPort = toInputPort;
    isGhost = false;
    
    setToCoordPropertiesFromComponents();
    
    buildGraphics();
    setupEventHandling();
  }
  
  /**
   * @return A ghost UIConnection with no data.
   */
  public static UIConnection ghost() {
    return new UIConnection();
  }
  
  private UIConnection() {
    // Build a ghost connection with no data (don't call any of its methods)
    isGhost = true;
    fromComponent = null;
    fromOutputPort = -1;
    toComponent = null;
    toInputPort = -1;
    setToCenterRelativeCoords(0, 0);
    buildGraphics();
    setVisible(false);
  }
  
  private void setToCoordPropertiesFromComponents() {
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
    
    toCenterXProperty.set(toCenterX);
    toCenterYProperty.set(toCenterY);
  }
  
  private double center(double pos, double length) {
    return pos + (length / 2);
  }
  
  private void buildGraphics() {
    start = new Circle(0, 0, START_END_RADIUS, UIColors.foreground(isGhost));
    start.setMouseTransparent(isGhost);
    
    end = new Circle(START_END_RADIUS, UIColors.foreground(isGhost));
    end.centerXProperty().bind(toCenterXProperty);
    end.centerYProperty().bind(toCenterYProperty);
    end.setMouseTransparent(isGhost);
    
    wire = new Line();
    wire.setStartX(0);
    wire.setStartY(0);
    wire.endXProperty().bind(toCenterXProperty);
    wire.endYProperty().bind(toCenterYProperty);
    wire.setStroke(UIColors.foreground(isGhost));
    wire.setStrokeWidth(LINE_WIDTH);
    wire.setMouseTransparent(isGhost);
    
    // transparent on top to catch the mouse events
    Line clickableArea = new Line();
    clickableArea.setStartX(0);
    clickableArea.setStartY(0);
    clickableArea.endXProperty().bind(toCenterXProperty);
    clickableArea.endYProperty().bind(toCenterYProperty);
    clickableArea.setOpacity(0);
    clickableArea.setStrokeWidth(CLICKABLE_WIDTH);
    clickableArea.setMouseTransparent(isGhost);
    
    getChildren().addAll(start, end, wire, clickableArea);
    
    // The start and end caps should be in front to catch mouse events first to pass to their UIComponent
    clickableArea.toBack();
    start.toFront();
    end.toFront();
  }
  
  private void setupEventHandling() {
    // Intercept the events here so that their source is this and not the FieldPane
    addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
      Logikos.getInstance().getFieldPaneController().onClick(e, null);
      e.consume();
    });
    addEventHandler(MouseEvent.MOUSE_MOVED, e -> {
      Logikos.getInstance().getFieldPaneController().onMouseMove(e, null);
      e.consume();
    });
    
    // Make mouse events from the start/end caps act as though they're from the underlying ports
    start.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
      FieldPaneController controller = Logikos.getInstance().getFieldPaneController();
      controller.click(controller.getMousePosition(e, fromComponent, null,
          new MousePosition.PortOver(false, fromOutputPort)));
      e.consume();
    });
    start.addEventHandler(MouseEvent.MOUSE_MOVED, e -> {
      FieldPaneController controller = Logikos.getInstance().getFieldPaneController();
      controller.hover(controller.getMousePosition(e, fromComponent, null,
          new MousePosition.PortOver(false, fromOutputPort)));
      e.consume();
    });
    end.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
      FieldPaneController controller = Logikos.getInstance().getFieldPaneController();
      controller.click(controller.getMousePosition(e, toComponent, null,
          new MousePosition.PortOver(true, toInputPort)));
      e.consume();
    });
    end.addEventHandler(MouseEvent.MOUSE_MOVED, e -> {
      FieldPaneController controller = Logikos.getInstance().getFieldPaneController();
      controller.hover(controller.getMousePosition(e, toComponent, null,
          new MousePosition.PortOver(true, toInputPort)));
      e.consume();
    });
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
  
  public double getToCenterX() {
    return toCenterXProperty.get();
  }
  
  public double getToCenterY() {
    return toCenterYProperty.get();
  }
  
  public void setToCenterRelativeCoords(double toCenterX, double toCenterY) {
    toCenterXProperty.set(toCenterX);
    toCenterYProperty.set(toCenterY);
  }
  
  public void setToCenter(double paneX, double paneY) {
    PannablePane fieldPane = Logikos.getInstance().getFieldPaneController().getFieldPane();
    Point2D relCoord = start.sceneToLocal(fieldPane.localToScene(paneX, paneY));
    setToCenterRelativeCoords(relCoord.getX(), relCoord.getY());
  }
  
  public void setStateVisual(boolean on) {
    wire.setStroke(UIColors.onOrOff(on));
  }
  
  public void resetStateVisual() {
    wire.setStroke(UIColors.foreground(isGhost));
  }
  
  @Override
  public void select() {
    wire.setEffect(SELECTED_DROP_SHADOW);
  }
  
  @Override
  public void deselect() {
    wire.setEffect(null);
  }
  
  @Override
  public void delete() {
    if (isGhost) {
      throw new UnsupportedOperationException("Cannot delete ghost connection");
    }
    
    fromComponent.getOutputConnections(fromOutputPort).remove(this);
    toComponent.getInputConnections()[toInputPort] = null;
  
    Connection connection = getLogicConnection();
    if (connection != null) {
      connection.destruct();
    }
    
    Logikos.getInstance().getFieldPaneController().getFieldPane().getContentChildren().remove(this);
  }
  
  /**
   * @return The {@link Connection} corresponding to this {@link UIConnection}, assuming that it's still on the
   * "to" logic component.
   */
  private Connection getLogicConnection() {
    if (isGhost) {
      throw new UnsupportedOperationException("A ghost has no real connection");
    }
    
    try {
      return toComponent.getFieldComponent()
          .getLogicComponent()
          .getInput(toInputPort)
          .getConnection();
    } catch (NullPointerException e) {
      return null;
    }
  }
  
}