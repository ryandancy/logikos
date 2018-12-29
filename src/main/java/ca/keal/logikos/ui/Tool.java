package ca.keal.logikos.ui;

import javafx.scene.input.KeyEvent;

/**
 * Represents a tool, which appears in the ToolPane and can be clicked on by the user and used to interact with the
 * field.
 * 
 * Tools in {@link Logikos#ALL_TOOLS} are added to the ToolPane at startup.
 * 
 * There are several hooks that subclasses may override. When the user clicks on the tool, {@link #onSelect()} is run;
 * when they click on another tool, {@link #onDeselect()} is run. When the user, with this tool selected, hovers over a
 * spot on the field, {@link #onHover(MousePosition)} is called, and when they click using this tool, {@link
 * #onClick(MousePosition)} is called. When their mouse leaves the pane, {@link #onLeavePane()} is called. When the user
 * presses a key with this tool selected, {@link #onKeyPress(KeyEvent)} is called.
 * 
 * Tools also have a name and a tooltip.
 */
public abstract class Tool {
  
  private final String name;
  private final String tooltip;
  
  // TODO i18n, images
  public Tool(String name, String tooltip) {
    this.name = name;
    this.tooltip = tooltip;
  }
  
  /**
   * @return The name of the tool.
   */
  public String getName() {
    return name;
  }
  
  /**
   * @return The tooltip shown when hovering over the tool.
   */
  public String getTooltip() {
    return tooltip;
  }
  
  /**
   * Called when this {@link Tool} is selected.
   */
  public void onSelect() {}
  
  /**
   * Called when the user clicks on another {@link Tool} from this one, and this tool is no longer active.
   */
  public void onDeselect() {}
  
  /**
   * Called when the user hovers over a position on the field.
   * @param position The {@link MousePosition} containing data about the mouse's position.
   */
  public void onHover(MousePosition position) {}
  
  /**
   * Called when the user clicks on a position on the field.
   * @param position The {@link MousePosition} containing data about the mouse's position.
   */
  public void onClick(MousePosition position) {}
  
  /**
   * Called when the user presses a key.
   * @param e The {@link KeyEvent} that triggered this call.
   */
  public void onKeyPress(KeyEvent e) {}
  
  /**
   * Called when the mouse leaves the FieldPane.
   */
  public void onLeavePane() {}
  
}