package ca.keal.logikos.logic;

/**
 * A simple gate which always outputs either 1 or 0, depending on the {@code value} parameter.
 * Like an input but not modifiable and doesn't show up in the inputs.
 */
public class Constant extends Gate {
  
  private final boolean value;
  
  public Constant(boolean value) {
    super(0, 1); // 0 inputs, 1 output
    this.value = value;
  }
  
  @Override
  protected boolean[] logicalEval(boolean[] input) {
    return new boolean[] {value};
  }
  
  @Override
  public String getName() {
    return value ? "1" : "0";
  }
  
}
