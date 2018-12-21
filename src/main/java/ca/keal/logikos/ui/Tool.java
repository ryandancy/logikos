package ca.keal.logikos.ui;

import ca.keal.logikos.field.FieldComponent;
import ca.keal.logikos.logic.AndGate;
import ca.keal.logikos.logic.NandGate;
import ca.keal.logikos.logic.NotGate;
import ca.keal.logikos.logic.OrGate;

/**
 * Represents a tool, which appears in the ToolPane and can be clicked on by the user and used to interact with the
 * field. Tools in {@link #ALL_TOOLS} are added to the ToolPane at startup. When the user clicks on the tool,
 * {@link #onSelect()} is run; when they click on another tool, {@link #onDeselect()} is run. When the user, with this
 * tool selected, hovers over a spot on the field, {@link #onHover(LogikosApplication, double, double, FieldComponent)}
 * is called, and when they click using this tool {@link #onClick(LogikosApplication, double, double, FieldComponent)}
 * is called. Tools also have a name and a tooltip.
 */
public abstract class Tool {
  
  // TODO more tools
  // TODO specialized PlaceComponentTool subclasses for placing Inputs/Outputs
  public static final Tool[] ALL_TOOLS = {
      new PlaceComponentTool("AND", "Add an AND gate", AndGate::new),
      new PlaceComponentTool("OR", "Add an OR gate", OrGate::new),
      new PlaceComponentTool("NOT", "Add a NOT gate", NotGate::new),
      new PlaceComponentTool("NAND", "Add a NAND gate", NandGate::new)
  };
  
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
   * @param app The main {@link LogikosApplication} instance.
   * @param paneX The x-coordinate in the FieldPane at which the mouse is hovering.
   * @param paneY The y-coordinate in the FieldPane at which the mouse is hovering.
   * @param hoveredFC The {@link FieldComponent}, if any, over which the mouse is hovering. This may be null if the
   *  mouse is not hovering over a component.
   */
  public abstract void onHover(LogikosApplication app, double paneX, double paneY, FieldComponent hoveredFC);
  
  /**
   * Called when the user clicks on a position on the field.
   * @param app The main {@link LogikosApplication} instance.
   * @param paneX The x-coordinate in the FieldPane where the mouse clicked.
   * @param paneY The y-coordinate in the FieldPane where the mouse clicked.
   * @param clickedFC The {@link FieldComponent}, if any, on which the mouse clicked. This may be null if the
   *  mouse did not click on a component.
   */
  public abstract void onClick(LogikosApplication app, double paneX, double paneY, FieldComponent clickedFC);
  
}