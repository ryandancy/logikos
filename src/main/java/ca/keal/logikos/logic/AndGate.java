package ca.keal.logikos.logic;

public class AndGate extends LogicComponent {
  
  public AndGate() {
    super(2, 1); // 2 inputs, 1 output
  }
  
  @Override
  protected boolean[] logicalEval(boolean[] input) {
    return new boolean[] {input[0] && input[1]};
  }
  
  @Override
  public String getName() {
    return "AND";
  }
  
}