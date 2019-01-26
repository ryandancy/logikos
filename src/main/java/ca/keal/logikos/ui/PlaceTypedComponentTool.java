package ca.keal.logikos.ui;

import ca.keal.logikos.field.FieldComponent;
import ca.keal.logikos.field.Position;
import javafx.scene.input.ScrollEvent;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A generic {@link PlaceComponentTool} subclass that can place UIComponents with a specific "type" attached to them -
 * i.e. inputs or outputs. The type class must be an enum. Types are scrolled through in enum order, starting with the
 * default type, with the scroll wheel.
 * 
 * @param <T> The type class, an enum.
 */
public class PlaceTypedComponentTool<T extends Enum<?>> extends PlaceComponentTool {
  
  private final Function<T, Function<Position, FieldComponent>> fcMakerMaker;
  private final T[] values;
  
  private T type;
  
  /**
   * Create a new {@link PlaceTypedComponentTool}.
   * 
   * @param name The name of the tool.
   * @param tooltip The tooltip to be shown when hovering.
   * @param fcMakerMaker A curried function which yields a FieldComponent maker function (mapping position to a
   * FieldComponent) for any given type value.
   * @param uicMaker A function which takes arguments (fc, isGhost) and returns a UIComponent with those values as
   * input.
   * @param defaultType The default type to be shown when this {@link PlaceTypedComponentTool} is selected for the first
   * time.
   * @param values The possible type values, obtained by calling {@code .values()} on the type class.
   */
  public PlaceTypedComponentTool(String name, String tooltip,
                                 Function<T, Function<Position, FieldComponent>> fcMakerMaker,
                                 BiFunction<FieldComponent, Boolean, UIComponent> uicMaker,
                                 T defaultType, T[] values) {
    super(name, tooltip, fcMakerMaker.apply(defaultType), uicMaker);
    
    this.fcMakerMaker = fcMakerMaker;
    type = defaultType;
    this.values = values;
  }
  
  @Override
  public void onScroll(ScrollEvent e) {
    // "increment"/"decrement" the type + update the position function and the ghost
    int typeDelta = e.getDeltaY() >= 0 ? -1 : 1;
    int numTypes = values.length;
    // we add numTypes here to keep the argument to the modulus positive
    type = values[(type.ordinal() + typeDelta + numTypes) % numTypes];
    setFieldComponentMaker(fcMakerMaker.apply(type));
    makeGhost();
  }
  
}