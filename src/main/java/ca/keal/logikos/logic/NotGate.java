package ca.keal.logikos.logic;

public class NotGate extends Gate {
  
  public NotGate() {
    super(1, 1); // 1 input, 1 output
  }
  
  @Override
  protected boolean[] logicalEval(boolean[] input) {
    return new boolean[] {!input[0]};
  }
  
  @Override
  public String getName() {
    return "NOT";
  }
  
}