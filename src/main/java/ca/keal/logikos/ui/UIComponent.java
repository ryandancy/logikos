package ca.keal.logikos.ui;

import ca.keal.logikos.field.FieldComponent;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * A custom Group that is the graphical representation of a component. It has a {@link FieldComponent}.
 * {@code UIComponent}s can be ghosts or real; a ghost {@code UIComponent} has a lighter colour, is transparent to
 * mouse events, and is meant to be used only on hover to show where a {@code UIComponent} would be placed if the user
 * clicks.
 */
// TODO resizing?
// TODO make this more of a superclass and move the displayName stuff to a subclass
public class UIComponent extends Group implements Selectable {
  
  // Constants for drawing the component
  private static final double PORT_SPACING = 20.0;
  private static final double MIN_PORT_PADDING = 6.0;
  private static final double MIN_NAME_PADDING = 12.0;
  private static final double PORT_RADIUS = 5.0;
  
  private static final DropShadow SELECTED_DROP_SHADOW = new DropShadow(20.0, UIColors.FOREGROUND_COLOR);
  
  private static final Font NAME_FONT = new Font("sans-serif", 15);
  
  private final FieldComponent fieldComponent;
  
  private final String displayName;
  
  private final Node[] inputPorts;
  private final Node[] outputPorts;
  private final UIConnection[] inputConnections;
  private final List<UIConnection>[] outputConnectionLists;
  
  private Rectangle square;
  
  public UIComponent(FieldComponent fieldComponent, String displayName, boolean isGhost) {
    this.fieldComponent = fieldComponent;
    this.displayName = displayName;
  
    // Ghost components are just graphical features
    setMouseTransparent(isGhost);
    
    int numInputs = fieldComponent.getLogicComponent().getNumInputs();
    int numOutputs = fieldComponent.getLogicComponent().getNumOutputs();
    inputPorts = new Node[numInputs];
    outputPorts = new Node[numOutputs];
    inputConnections = new UIConnection[numInputs];
    //noinspection unchecked,SuspiciousToArrayCall
    outputConnectionLists = (List<UIConnection>[]) Stream.generate(ArrayList::new)
        .limit(numOutputs)
        .toArray(List[]::new);
    
    buildGraphics(isGhost);
    setupEventHandling();
  }
  
  private void setupEventHandling() {
    // Pass mouse events along to the FieldPaneController; the source is then this instead of the FieldPane
    addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
      Logikos.getInstance().getFieldPaneController().onClick(e, null);
      e.consume();
    });
    addEventHandler(MouseEvent.MOUSE_MOVED, e -> {
      Logikos.getInstance().getFieldPaneController().onMouseMove(e, null);
      e.consume();
    });
    addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
      Logikos.getInstance().getFieldPaneController().onPress(e);
      e.consume();
    });
    addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
      Logikos.getInstance().getFieldPaneController().onRelease(e);
      e.consume();
    });
    
    for (int i = 0; i < inputPorts.length; i++) {
      setupPortEventHandling(inputPorts[i], i, true);
    }
    for (int i = 0; i < outputPorts.length; i++) {
      setupPortEventHandling(outputPorts[i], i, false);
    }
  }
  
  private void setupPortEventHandling(Node port, int portNumber, boolean input) {
    port.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
      Logikos.getInstance().getFieldPaneController().onClick(e, new MousePosition.PortOver(input, portNumber));
      e.consume();
    });
    port.addEventHandler(MouseEvent.MOUSE_MOVED, e -> {
      Logikos.getInstance().getFieldPaneController().onMouseMove(e, new MousePosition.PortOver(input, portNumber));
      e.consume();
    });
    // TODO if port detection is needed on press/release events, add that here
  }
  
  protected void buildGraphics(boolean isGhost) {
    // TODO custom graphics for non-UserGate FieldComponents
    
    Color fgColor = UIColors.foreground(isGhost);
    
    // Construct the name
    Text name = new Text(displayName);
    name.setFont(NAME_FONT);
    name.setFill(fgColor);
    double nameWidth = name.getLayoutBounds().getWidth();
    double nameHeight = name.getLayoutBounds().getHeight() - name.getBaselineOffset();
    name.setX(-nameWidth / 2);
    name.setY(nameHeight);
    
    // Find the proper size for the square with the number of inputs/outputs and name
    double inputPortSpace = minPortSpace(inputPorts.length);
    double outputPortSpace = minPortSpace(outputPorts.length);
    double nameSpace = nameWidth + 2 * MIN_NAME_PADDING;
    double squareSize = Math.max(Math.max(inputPortSpace, outputPortSpace), nameSpace);
    double squareCoord = -squareSize / 2; // the coord to centre it on (0, 0)
    
    // Construct the bounding square
    square = new Rectangle(squareSize, squareSize);
    square.setX(squareCoord);
    square.setY(squareCoord);
    square.setStroke(fgColor);
    square.setFill(UIColors.BACKGROUND_COLOR);
    
    // Add the mainBody and name here so that the input/output circles go on top of them
    getChildren().add(square);
    getChildren().add(name);
    
    // Add circles for each input/output port
    addPortCircles(true, inputPorts, fgColor, squareSize, squareCoord);
    addPortCircles(false, outputPorts, fgColor, squareSize, squareCoord);
  }
  
  protected void addPortCircles(boolean left, Node[] portArr, Color fgColor, double squareSize, double squareCoord) {
    // The circles are a set distance PORT_SPACING apart and are centred on each side of the rectangle
    // TODO fill in these circles when connected
    double x = left ? squareCoord : -squareCoord; // note: squareCoord is negative
    double startY = ((squareSize - ((portArr.length - 1) * PORT_SPACING)) / 2) + squareCoord;
    for (int i = 0; i < portArr.length; i++) {
      Circle port = new Circle(PORT_RADIUS);
      port.setStroke(fgColor);
      port.setFill(UIColors.BACKGROUND_COLOR);
      port.setCenterX(x);
      port.setCenterY(startY + (i * PORT_SPACING));
      portArr[i] = port;
      getChildren().add(port);
    }
  }
  
  private double minPortSpace(int numPorts) {
    return ((numPorts - 1) * PORT_SPACING) + (2 * MIN_PORT_PADDING);
  }
  
  /**
   * @return The main body of the component to which a drop shadow should be added when selected.
   */
  protected Node getMainBody() {
    return square;
  }
  
  /**
   * Add a visual indication that this {@link UIComponent} is selected (currently a drop shadow).
   */
  @Override
  public void select() {
    getMainBody().setEffect(SELECTED_DROP_SHADOW);
  }
  
  /**
   * Remove the visual indication that this {@link UIComponent} is selected (currently a drop shadow).
   */
  @Override
  public void deselect() {
    getMainBody().setEffect(null);
  }
  
  /**
   * Delete this {@link UIComponent} from the UI and remove all references to it, allowing it to be garbage collected.
   */
  @Override
  public void delete() {
    Logikos.getInstance().getField().removeFieldComponent(fieldComponent);
    Logikos.getInstance().getFieldPaneController().getFieldPane().getContentChildren().remove(this);
    
    // Delete all the connections connected to this
    for (UIConnection inputConnection : inputConnections) {
      if (inputConnection != null) {
        inputConnection.delete();
      }
    }
    for (List<UIConnection> outputConnectionList : outputConnectionLists) {
      // Make a copy of the list because it's being modified as we loop through it
      List<UIConnection> outputConnListCopy = new ArrayList<>(outputConnectionList);
      for (UIConnection outputConnection : outputConnListCopy) {
        if (outputConnection != null) {
          outputConnection.delete();
        }
      }
    }
  }
  
  public FieldComponent getFieldComponent() {
    return fieldComponent;
  }
  
  public Node[] getInputPorts() {
    return inputPorts;
  }
  
  public Node[] getOutputPorts() {
    return outputPorts;
  }
  
  public UIConnection[] getInputConnections() {
    return inputConnections;
  }
  
  public List<UIConnection>[] getOutputConnectionLists() {
    return outputConnectionLists;
  }
  
  public List<UIConnection> getOutputConnections(int outputPort) {
    return outputConnectionLists[outputPort];
  }
  
  public void setInputConnection(int inputPort, UIConnection connection) {
    inputConnections[inputPort] = connection;
  }
  
  public void addOutputConnection(int outputPort, UIConnection connection) {
    outputConnectionLists[outputPort].add(connection);
  }
  
}