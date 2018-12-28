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

/**
 * A custom Group that is the graphical representation of a component. It has a {@link FieldComponent}.
 * {@code UIComponent}s can be ghosts or real; a ghost {@code UIComponent} has a lighter colour, is transparent to
 * mouse events, and is meant to be used only on hover to show where a {@code UIComponent} would be placed if the user
 * clicks.
 */
// TODO resizing?
public class UIComponent extends Group {
  
  // TODO should these be moved to a separate class?
  public static final Color GHOST_COLOR = Color.gray(0.8);
  public static final Color FOREGROUND_COLOR = Color.BLACK;
  public static final Color BACKGROUND_COLOR = Color.WHITE;
  
  // Constants for drawing the component
  private static final double PORT_SPACING = 20.0;
  private static final double MIN_PORT_PADDING = 6.0;
  private static final double MIN_NAME_PADDING = 12.0;
  private static final double PORT_RADIUS = 5.0;
  
  private static final double SELECTED_DROP_SHADOW_RADIUS = 10.0;
  
  private static final Font NAME_FONT = new Font("sans-serif", 15);
  
  private final FieldComponent fieldComponent;
  
  private final List<Node> inputPorts = new ArrayList<>();
  private final List<Node> outputPorts = new ArrayList<>();
  
  private Color fgColor;
  private Rectangle square;
  
  public UIComponent(FieldComponent fieldComponent, String displayName, boolean isGhost) {
    this.fieldComponent = fieldComponent;
  
    // Ghost components are just graphical features
    setMouseTransparent(isGhost);
    
    buildGraphics(displayName, isGhost);
    setupEventHandling();
  }
  
  private void setupEventHandling() {
    // Pass mouse events along to the FieldPaneController
    addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
      Logikos.getInstance().getFieldPaneController().onClick(e);
      e.consume();
    });
    addEventFilter(MouseEvent.MOUSE_MOVED, e -> {
      Logikos.getInstance().getFieldPaneController().onMouseMove(e);
      e.consume();
    });
  }
  
  private void buildGraphics(String displayName, boolean isGhost) {
    // TODO custom graphics for non-UserGate FieldComponents
    
    fgColor = isGhost ? GHOST_COLOR : FOREGROUND_COLOR;
    
    // Construct the name
    Text name = new Text(displayName);
    name.setFont(NAME_FONT);
    name.setFill(fgColor);
    double nameWidth = name.getLayoutBounds().getWidth();
    double nameHeight = name.getLayoutBounds().getHeight() - name.getBaselineOffset();
    name.setX(-nameWidth / 2);
    name.setY(nameHeight);
    
    // Find the proper size for the square with the number of inputs/outputs and name
    int numInputs = fieldComponent.getLogicComponent().getNumInputs();
    int numOutputs = fieldComponent.getLogicComponent().getNumOutputs();
    double inputPortSpace = minPortSpace(numInputs);
    double outputPortSpace = minPortSpace(numOutputs);
    double nameSpace = nameWidth + 2 * MIN_NAME_PADDING;
    double squareSize = Math.max(Math.max(inputPortSpace, outputPortSpace), nameSpace);
    double squareCoord = -squareSize / 2; // the coord to centre it on (0, 0)
    
    // Construct the bounding square
    square = new Rectangle(squareSize, squareSize);
    square.setX(squareCoord);
    square.setY(squareCoord);
    square.setStroke(fgColor);
    square.setFill(BACKGROUND_COLOR);
    
    // Add the square and name here so that the input/output circles go on top of them
    getChildren().add(square);
    getChildren().add(name);
    
    // Add circles for each input/output port
    addPortCircles(numInputs, true, inputPorts, fgColor, squareSize, squareCoord);
    addPortCircles(numOutputs, false, outputPorts, fgColor, squareSize, squareCoord);
  }
  
  private void addPortCircles(int numPorts, boolean left, List<Node> portList, Color fgColor,
                              double squareSize, double squareCoord) {
    // The circles are a set distance PORT_SPACING apart and are centred on each side of the rectangle
    // TODO fill in these circles when connected
    double x = left ? squareCoord : -squareCoord;
    double startY = ((squareSize - ((numPorts - 1) * PORT_SPACING)) / 2) + squareCoord;
    for (int i = 0; i < numPorts; i++) {
      Circle port = new Circle(PORT_RADIUS);
      port.setStroke(fgColor);
      port.setFill(BACKGROUND_COLOR);
      port.setCenterX(x);
      port.setCenterY(startY + (i * PORT_SPACING));
      portList.add(port);
      getChildren().add(port);
    }
  }
  
  private double minPortSpace(int numPorts) {
    return ((numPorts - 1) * PORT_SPACING) + (2 * MIN_PORT_PADDING);
  }
  
  /**
   * Add a visual indication that this {@link UIComponent} is "selected" (currently a drop shadow).
   */
  public void setSelected() {
    square.setEffect(new DropShadow(SELECTED_DROP_SHADOW_RADIUS, fgColor));
  }
  
  /**
   * Remove the visual indication that this {@link UIComponent} is "selected" (currently a drop shadow).
   */
  public void setDeselected() {
    square.setEffect(null);
  }
  
  public FieldComponent getFieldComponent() {
    return fieldComponent;
  }
  
}