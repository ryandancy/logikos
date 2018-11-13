package ca.keal.logikos.logic;

/**
 * A simple {@link LogicComponent} representing an input of a circuit. It has no inputs and only 1 output. It has a
 * fixed output value which may be accessed with {@link #getValue()} and {@link #setValue(boolean)}.
 */
public class Input extends LogicComponent {
  
  /** The value of this Input */
  private boolean value = false;
  
  public Input() {
    super(0, 1); // 0 inputs, 1 output
  }
  
  /**
   * @return The value of this {@link Input}.
   */
  public boolean getValue() {
    return value;
  }
  
  /**
   * Set this {@link Input}'s value.
   * @param value The new value for this {@link Input}.
   */
  public void setValue(boolean value) {
    this.value = value;
  }
  
  @Override
  public boolean[] evaluate() {
    // Supply the set value
    return new boolean[] {value};
  }
  
  @Override
  public String getName() {
    return "Input[value=" + value + "]";
  }
  
}