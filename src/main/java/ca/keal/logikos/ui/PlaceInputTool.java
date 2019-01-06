package ca.keal.logikos.ui;

import ca.keal.logikos.field.InputFC;
import ca.keal.logikos.logic.Input;
import javafx.scene.input.ScrollEvent;

public class PlaceInputTool extends PlaceComponentTool {
  
  private static final InputFC.Type DEFAULT_TYPE = InputFC.Type.SWITCH;
  private InputFC.Type type = DEFAULT_TYPE;
  
  public PlaceInputTool() {
    super("Input", "Add an input", position -> new InputFC(new Input(), position, DEFAULT_TYPE),
        (fc, isGhost) -> new InputUIC((InputFC) fc, isGhost));
  }
  
  @Override
  public void onScroll(ScrollEvent e) {
    // "increment"/"decrement" the type + update the position function and the ghost
    int typeDelta = e.getDeltaY() >= 0 ? -1 : 1;
    int numTypes = InputFC.Type.values().length;
    // we add numTypes here to keep the argument to the modulus positive
    type = InputFC.Type.values()[(type.ordinal() + typeDelta + numTypes) % numTypes];
    setFieldComponentMaker(position -> new InputFC(new Input(), position, type));
    makeGhost();
  }
  
}