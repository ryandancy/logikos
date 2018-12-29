package ca.keal.logikos.ui;

import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

/**
 * A Pane that can be panned with the right mouse button with a grid in the background. Used for the FieldPane.
 */
// TODO zooming with the scroll wheel
public class PannablePane extends Pane {
  
  // for some reason the thickness of the grid is determined by thickness / cell size
  private static final double GRID_SIZE = 30.0;
  private static final double GRID_THICKNESS_PX = 1;
  private static final double GRID_THICKNESS_REL = GRID_THICKNESS_PX / GRID_SIZE;
  private static final Color GRID_COLOR = Color.gray(0.4);
  
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
      double newX = translateAnchorX + e.getSceneX() - panAnchorX;
      double newY = translateAnchorY + e.getSceneY() - panAnchorY;
      content.setTranslateX(newX);
      content.setTranslateY(newY);
      updateGrid(newX, newY);
      e.consume();
    });
    
    updateGrid(0, 0);
  }
  
  private void updateGrid(double offsetX, double offsetY) {
    // Set the background as a grid offset by the parameters
    setBackground(new Background(
        new BackgroundFill(new LinearGradient(
            offsetX + 0.5, 0.0, offsetX + GRID_SIZE + 0.5, 0.0, false, CycleMethod.REPEAT,
            new Stop(GRID_THICKNESS_REL, GRID_COLOR), new Stop(GRID_THICKNESS_REL, Color.TRANSPARENT)
        ), CornerRadii.EMPTY, Insets.EMPTY),
        new BackgroundFill(new LinearGradient(
            0.0, offsetY + 0.5, 0.0, offsetY + GRID_SIZE + 0.5, false, CycleMethod.REPEAT,
            new Stop(GRID_THICKNESS_REL, GRID_COLOR), new Stop(GRID_THICKNESS_REL, Color.TRANSPARENT)
        ), CornerRadii.EMPTY, Insets.EMPTY)
    ));
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