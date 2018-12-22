package ca.keal.logikos.ui;

import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * A Pane that can be panned with the right mouse button. Used for the FieldPane.
 */
// TODO zooming with the scroll wheel
public class PannablePane extends Pane {
  
  private Pane content = new Pane();
  
  private double panAnchorX;
  private double panAnchorY;
  private double translateAnchorX;
  private double translateAnchorY;
  
  public PannablePane() {
    getChildren().add(content);
    
    // Panning with the right mouse button
    addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
      if (!e.isSecondaryButtonDown()) return; // right mouse button pans
      panAnchorX = e.getSceneX();
      panAnchorY = e.getSceneY();
      translateAnchorX = content.getTranslateX();
      translateAnchorY = content.getTranslateY();
    });
    addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
      if (!e.isSecondaryButtonDown()) return; // right mouse button pans
      content.setTranslateX(translateAnchorX + e.getSceneX() - panAnchorX);
      content.setTranslateY(translateAnchorY + e.getSceneY() - panAnchorY);
      e.consume();
    });
  }
  
  public ObservableList<Node> getContentChildren() {
    return content.getChildren();
  }
  
  /**
   * Convert an x-coordinate relative to the pane to a coordinate suitable for use as a position in a component or when
   * placing an item. This essentially undoes all the panning.
   * @param paneX The pane-relative x-coordinate to convert.
   * @return The converted "real" x-coordinate.
   */
  public double paneToRealX(double paneX) {
    Point2D paneOrigin = localToScene(0, 0);
    Bounds contentBounds = content.localToScene(content.getBoundsInLocal());
    
    double translateX = content.getTranslateX();
    double negativeX = contentBounds.getMinX() - paneOrigin.getX();
    if (negativeX < 0) {
      // compensate for strange translation when there's anything offscreen
      return paneX - translateX + negativeX;
    } else {
      return paneX - translateX;
    }
  }
  
  /**
   * Convert an y-coordinate relative to the pane to a coordinate suitable for use as a position in a component or when
   * placing an item. This essentially undoes all the panning.
   * @param paneY The pane-relative y-coordinate to convert.
   * @return The converted "real" y-coordinate.
   */
  public double paneToRealY(double paneY) {
    Point2D paneOrigin = localToScene(0, 0);
    Bounds contentBounds = content.localToScene(content.getBoundsInLocal());
    
    double translateY = content.getTranslateY();
    double negativeY = contentBounds.getMinY() - paneOrigin.getY();
    if (negativeY < 0) {
      // compensate for strange translation when there's anything offscreen
      return paneY - translateY + negativeY;
    } else {
      return paneY - translateY;
    }
  }
  
}