package ca.keal.logikos.ui;

import ca.keal.logikos.field.FieldComponent;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * An abstract {@link UIComponent} subclass which represents a {@code UIComponent} that can be either on or off, and is
 * represented as such by an image - i.e., an input or output.
 */
public abstract class BooleanStateImageUIC extends UIComponent {
  
  private static final double FIT_WIDTH = 60.0;
  
  private ImageView img;
  
  protected BooleanStateImageUIC(FieldComponent fc, boolean isGhost) {
    super(fc, "", isGhost);
  }
  
  @Override
  protected void buildGraphics(boolean isGhost) {
    img = getImageView(false); // TODO for now always off
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
    
    Color portColor = UIColors.foreground(isGhost);
    addPortCircles(true, getInputPorts(), portColor, bounds.getWidth(), xCoord);
    addPortCircles(false, getOutputPorts(), portColor, bounds.getWidth(), xCoord);
  }
  
  @Override
  protected Node getMainBody() {
    return img;
  }
  
  private ImageView getImageView(boolean on) {
    String base = getImageFilenameBase();
    String filename = base + (on ? "-on.png" : "-off.png");
    return new ImageView(getClass().getResource(filename).toExternalForm());
  }
  
  /**
   * @return The "base" filename (i.e. without the "-on.png" or "-off.png" suffix) for this component.
   */
  protected abstract String getImageFilenameBase();
  
}