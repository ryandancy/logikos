package ca.keal.logikos.ui;

import ca.keal.logikos.field.FieldComponent;

/**
 * Represents a tool, which appears in the ToolPane and can be clicked on by the user and used to interact with the
 * field. Tools in {@link #ALL_TOOLS} are added to the ToolPane at startup. When the user clicks on the tool,
 * {@link #onSelect()} is run; when they click on another tool, {@link #onDeselect()} is run. When the user, with this
 * tool selected, hovers over a spot on the field, {@link #onHover(int, int, FieldComponent)} is called, and when they
 * click using this tool {@link #onClick(int, int, FieldComponent)} is called. Tools also have a name and a tooltip.
 */
public abstract class Tool {
  
  // TODO real tools - these are temporary for testing
  public static final Tool[] ALL_TOOLS = {
      new Tool("TEST 1", "TEMPORARY TEST") {
        @Override
        public void onHover(int paneX, int paneY, FieldComponent hoveredFC) {}
  
        @Override
        public void onClick(int paneX, int paneY, FieldComponent clickedFC) {}
  
        @Override
        public void onSelect() {
          super.onSelect();
          System.out.println("Test 1 on select");
        }
  
        @Override
        public void onDeselect() {
          super.onDeselect();
          System.out.println("Test 1 on deselect");
        }
      },
      new Tool("TEST 2", "TEMPORARY TEST") {
        @Override
        public void onHover(int paneX, int paneY, FieldComponent hoveredFC) {}
  
        @Override
        public void onClick(int paneX, int paneY, FieldComponent clickedFC) {}
  
        @Override
        public void onSelect() {
          super.onSelect();
          System.out.println("Test 2 on select");
        }
  
        @Override
        public void onDeselect() {
          super.onDeselect();
          System.out.println("Test 2 on deselect");
        }
      }
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
   * @param paneX The x-coordinate in the FieldPane at which the mouse is hovering.
   * @param paneY The y-coordinate in the FieldPane at which the mouse is hovering.
   * @param hoveredFC The {@link FieldComponent}, if any, over which the mouse is hovering. This may be null if the
   *  mouse is not hovering over a component.
   */
  public abstract void onHover(int paneX, int paneY, FieldComponent hoveredFC);
  
  /**
   * Called when the user clicks on a position on the field.
   * @param paneX The x-coordinate in the FieldPane where the mouse clicked.
   * @param paneY The y-coordinate in the FieldPane where the mouse clicked.
   * @param hoveredFC The {@link FieldComponent}, if any, on which the mouse clicked. This may be null if the
   *  mouse did not click on a component.
   */
  public abstract void onClick(int paneX, int paneY, FieldComponent clickedFC);
  
}