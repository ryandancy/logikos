package ca.keal.logikos.ui;

import ca.keal.logikos.field.InputFC;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * A {@link UIComponent} representing an input specifically.
 */
// TODO selecting type, toggling
public class InputUIC extends UIComponent {
  
  private static final double FIT_WIDTH = 60.0;
  
  private ImageView img;
  
  public InputUIC(InputFC inputFC, boolean isGhost) {
    super(inputFC, "", isGhost);
  }
  
  @Override
  protected void buildGraphics(boolean isGhost) {
    InputFC inputFC = (InputFC) getFieldComponent();
    
    img = typeToImageView(inputFC.getType(), false); // TODO for now always off
    img.setPreserveRatio(true);
    img.setFitWidth(FIT_WIDTH);
    if (isGhost) {
      img.setEffect(new ColorAdjust(0, -0.7, 0, 0)); // desaturate, turn mostly greyscale
      img.setOpacity(0.75);
    }
    
    Bounds bounds = img.getLayoutBounds();
    double xCoord = -bounds.getWidth() / 2;
    double yCoord = -bounds.getHeight() / 2;
    
    img.setLayoutX(xCoord);
    img.setLayoutY(yCoord);
    
    getChildren().add(img);
    
    Color portColor = isGhost ? GHOST_COLOR : FOREGROUND_COLOR;
    // We only add the output ports because there are no input ports
    addPortCircles(false, getOutputPorts(), portColor, bounds.getWidth(), xCoord);
  }
  
  @Override
  protected Node getMainBody() {
    return img;
  }
  
  private ImageView typeToImageView(InputFC.Type type, boolean on) {
    String base;
    switch (type) {
      case BUTTON_BLUE:
        base = "button-blue";
        break;
      case BUTTON_GREEN:
        base = "button-green";
        break;
      case BUTTON_RED:
        base = "button-red";
        break;
      case BUTTON_YELLOW:
        base = "button-yellow";
        break;
      case SWITCH:
        base = "switch";
        break;
      default:
        throw new IllegalArgumentException("Unknown input type");
    }
    String filename = base + (on ? "-on.png" : "-off.png");
    return new ImageView(getClass().getResource(filename).toExternalForm());
  }
  
}