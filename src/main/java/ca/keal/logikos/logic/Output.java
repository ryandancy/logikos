package ca.keal.logikos.logic;

/**
 * A simple {@link LogicComponent} representing an output on a circuit. It is simply an end-node with 1 input and 0
 * outputs; despite this, {@link #evaluate(EvaluationListener, boolean[])} still returns a value, mirroring its input
 * value.
 */
public class Output extends LogicComponent {
  
  public Output() {
    super(1, 0); // 1 input, 0 outputs
  }
  
  @Override
  public boolean[] evaluate(EvaluationListener listener, boolean[] inputValues) {
    // Mirror the input value
    if (listener != null) {
      listener.onEvaluation(new EvaluationListener.Event(this, inputValues, inputValues));
    }
    return inputValues;
  }
  
  @Override
  public String getName() {
    return "OUTPUT";
  }
  
}