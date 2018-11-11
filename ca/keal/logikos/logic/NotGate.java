package ca.keal.logikos.logic;

public class NotGate extends LogicComponent {
  
  public NotGate() {
    super(1, 1); // 1 input, 1 output
  }
  
  @Override
  protected boolean[] logicalEval(boolean[] input) {
    return new boolean[] {!input[0]};
  }
  
  @Override
  public String toString() {
    return "NOT";
  }
  
}