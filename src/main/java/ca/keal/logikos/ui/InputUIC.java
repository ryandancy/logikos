package ca.keal.logikos.ui;

import ca.keal.logikos.field.InputFC;

/**
 * A {@link UIComponent} representing an input specifically.
 */
// TODO selecting type, toggling, key bindings
public class InputUIC extends BooleanStateImageUIC {
  
  public InputUIC(InputFC inputFC, boolean isGhost) {
    super(inputFC, isGhost);
  }
  
  @Override
  protected String getImageFilenameBase() {
    InputFC.Type type = ((InputFC) getFieldComponent()).getType();
    switch (type) {
      case BUTTON_BLUE:
        return "button-blue";
      case BUTTON_GREEN:
        return "button-green";
      case BUTTON_RED:
        return "button-red";
      case BUTTON_YELLOW:
        return "button-yellow";
      case SWITCH:
        return "switch";
      case BINARY:
        return "binary";
      default:
        throw new IllegalStateException("Unknown input type");
    }
  }
  
}