package ca.keal.logikos.ui;

/**
 * Any UI item (i.e. {@code Node} subclass) that is able to be selected by the user when they have the {@link
 * SelectTool} enabled.
 */
public interface Selectable {
  
  /**
   * Display a visual indicator that this {@link Selectable} has been selected. This method will always be called first
   * or after a call to {@link #deselect()}.
   */
  void select();
  
  /**
   * No longer display a visual indicator that this {@link Selectable} has been selected. This method will always be
   * called after a call to {@link #select()}.
   */
  void deselect();
  
  /**
   * Remove this {@link Selectable} from the UI and from all models, allowing it to be garbage collected.
   */
  void delete();
  
}
