package ca.keal.logikos.logic;

/**
 * A simple {@link LogicComponent} representing an output on a circuit. It is simply an end-node with 1 input and 0
 * outputs; despite this, {@link #evaluate()} still returns a value, mirroring its input value.
 */
public class Output extends LogicComponent {
  
  public Output() {
    super(1, 0); // 1 input, 0 outputs
  }
  
  @Override
  public boolean[] evaluate() {
    // Mirror the input value
    return new boolean[] {getInput(0).getInputValue()};
  }
  
  @Override
  public String getName() {
    return "Output";
  }
  
}