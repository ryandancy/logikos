package ca.keal.logikos.ui;

import ca.keal.logikos.field.FieldComponent;

/**
 * Represents a tool, which appears in the ToolPane and can be clicked on by the user and used to interact with the
 * field. Tools in {@link Logikos#ALL_TOOLS} are added to the ToolPane at startup. When the user clicks on the tool,
 * {@link #onSelect()} is run; when they click on another tool, {@link #onDeselect()} is run. When the user, with this
 * tool selected, hovers over a spot on the field, {@link #onHover(double, double, UIComponent)} is called, and when
 * they click using this tool {@link #onClick(double, double, UIComponent)} is called. Tools also have a name and a
 * tooltip.
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
  @SuppressWarnings("unused")
  public void onSelect() {}
  
  /**
   * Called when the user clicks on another {@link Tool} from this one, and this tool is no longer active.
   */
  public void onDeselect() {}
  
  /**
   * Called when the user hovers over a position on the field.
   * @param paneX The x-coordinate in the FieldPane at which the mouse is hovering.
   * @param paneY The y-coordinate in the FieldPane at which the mouse is hovering.
   * @param hoveredComponent The {@link UIComponent}, if any, over which the mouse is hovering. This may be null if the
   *  mouse did not hover over a component.
   */
  public abstract void onHover(double paneX, double paneY, UIComponent hoveredComponent);
  
  /**
   * Called when the user clicks on a position on the field.
   * @param paneX The x-coordinate in the FieldPane where the mouse clicked.
   * @param paneY The y-coordinate in the FieldPane where the mouse clicked.
   * @param clickedComponent The {@link FieldComponent}, if any, on which the mouse clicked. This may be null if the
   *  mouse did not click on a component.
   */
  public abstract void onClick(double paneX, double paneY, UIComponent clickedComponent);
  
  /**
   * Called when the mouse leaves the FieldPane.
   */
  public void onLeavePane() {}
  
}