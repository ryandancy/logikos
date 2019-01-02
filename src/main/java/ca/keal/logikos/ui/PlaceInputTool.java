package ca.keal.logikos.ui;

import ca.keal.logikos.field.InputFC;
import ca.keal.logikos.logic.Input;

public class PlaceInputTool extends PlaceComponentTool {
  
  private static final InputFC.Type DEFAULT_TYPE = InputFC.Type.SWITCH;
  
  public PlaceInputTool() {
    super("Input", "Add an input", position -> new InputFC(new Input(), position, DEFAULT_TYPE),
        (fc, isGhost) -> new InputUIC((InputFC) fc, isGhost));
  }
  
}