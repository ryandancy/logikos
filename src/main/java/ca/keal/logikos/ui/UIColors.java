package ca.keal.logikos.ui;

import javafx.scene.paint.Color;

/**
 * A util class to encapsulate color constants.
 */
public final class UIColors {
  
  public static final Color GHOST_COLOR = Color.gray(0.8);
  public static final Color FOREGROUND_COLOR = Color.BLACK;
  public static final Color BACKGROUND_COLOR = Color.WHITE;
  
  /**
   * @return The correct foreground colour ({@link #GHOST_COLOR} or {@link #FOREGROUND_COLOR}) for ghost or non-ghost
   * components.
   */
  public static Color foreground(boolean isGhost) {
    return isGhost ? GHOST_COLOR : FOREGROUND_COLOR;
  }
  
  // Can't be instantiated
  private UIColors() {}
  
}