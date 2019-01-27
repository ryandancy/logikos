package ca.keal.logikos.ui;

import ca.keal.logikos.field.OutputFC;

// TODO selecting type
public class OutputUIC extends BooleanStateImageUIC {
  
  protected OutputUIC(OutputFC outputFC, boolean isGhost) {
    super(outputFC, isGhost);
  }
  
  @Override
  protected String getImageFilenameBase() {
    OutputFC.Type type = ((OutputFC) getFieldComponent()).getType();
    switch (type) {
      case LAMP:
        return "lamp";
      case BINARY:
        return "binary";
      case RED:
        return "red";
      case GREEN:
        return "green";
      case BLUE:
        return "blue";
      default:
        throw new IllegalStateException("Unknown output type");
    }
  }
  
}