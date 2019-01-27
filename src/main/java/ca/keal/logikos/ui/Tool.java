package ca.keal.logikos.ui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;

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
 * Tools have a name and a tooltip. They also have a {@link Location} value, which describes the location in which the
 * {@link Tool} should be displayed: the ToolPane or the evaluation box on the top-right of the FieldPane.
 */
public abstract class Tool {
  
  private static final ToggleGroup GLOBAL_TOGGLE_GROUP = new ToggleGroup();
  
  private final String name;
  private final String tooltip;
  private final Location location;
  
  private BooleanProperty enabledProperty = new SimpleBooleanProperty();
  
  // TODO i18n, images
  public Tool(String name, String tooltip, Location location, boolean enabled) {
    this.name = name;
    this.tooltip = tooltip;
    this.location = location;
    enabledProperty.set(enabled);
  }
  
  public Tool(String name, String tooltip) {
    this(name, tooltip, Location.TOOL_PANE, true);
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
   * @return The {@link Location} of the tool.
   */
  public Location getLocation() {
    return location;
  }
  
  /**
   * @return Whether the tool is enabled.
   */
  public boolean isEnabled() {
    return enabledProperty.get();
  }
  
  /**
   * Set the tool enabled or disabled.
   */
  public void setEnabled(boolean enabled) {
    enabledProperty.set(enabled);
  }
  
  /**
   * @return The property of whether or not this tool is enabled.
   */
  public BooleanProperty enabledProperty() {
    return enabledProperty;
  }
  
  /**
   * @return A {@link RadioButton} generated based on this {@link Tool}.
   */
  public RadioButton createRadioButton() {
    // TODO images
    RadioButton btn = new RadioButton(getName());
    btn.setTooltip(new Tooltip(getTooltip()));
    btn.setUserData(this);
    btn.setToggleGroup(GLOBAL_TOGGLE_GROUP);
    btn.disableProperty().bind(enabledProperty().not());
    
    if (this == Logikos.ALL_TOOLS[0]) {
      // Make the first tool selected for real (i.e. with a dot in the radio button)
      btn.fire();
    }
    
    btn.selectedProperty().addListener((observableValue, previouslySelected, nowSelected) -> {
      if (nowSelected) {
        onSelect();
        Logikos.getInstance().setSelectedTool(this);
      } else if (previouslySelected) {
        onDeselect();
      }
    });
    
    return btn;
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
   * Called when the user scrolls.
   * @param e The {@link ScrollEvent} that triggered this call.
   */
  public void onScroll(ScrollEvent e) {}
  
  /**
   * Called when the mouse leaves the FieldPane.
   */
  public void onLeavePane() {}
  
  /**
   * Where should the Tool be located: in the ToolPane or in the evaluation box on the top-right of the FieldPane?
   */
  public enum Location {
    TOOL_PANE, EVALUATION_BOX
  }
  
}