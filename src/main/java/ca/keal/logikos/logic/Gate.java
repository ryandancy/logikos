package ca.keal.logikos.logic;

/**
 * A logic gate; i.e., a {@link LogicComponent} that has input and output {@link Port}s and performs a logical
 * operation.
 */
public abstract class Gate extends LogicComponent {

  /**
   * Initialize the Gate with {@code numInputs} inputs {@link Port.Input}s and {@code numOutputs} output
   * {@link Port.Output}s.
   * @param numInputs The number of inputs this Gate has.
   * @param numOutputs The number of outputs this Gate has.
   */
  protected Gate(int numInputs, int numOutputs) {
    super(numInputs, numOutputs);
  }
  
  /**
   * Find this {@link Gate}'s output values by evaluating all previous {@link LogicComponent}s.
   * @return This {@link Gate}'s output values for its current input values.
   */
  @Override
  public boolean[] evaluate(EvaluationListener listener, boolean[] inputValues) {
    // Evaluate them
    boolean[] output = logicalEval(inputValues);
    if (listener != null) {
      listener.onEvaluation(new EvaluationListener.Event(this, inputValues, output));
    }
    return output;
  }
  
  /**
   * Perform the logical operation done by this {@link Gate}.
   * @param input The gate's current input. Length is equal to getNumInputs().
   * @return The gate's output for its input. Length must be equal to getNumOutputs().
   */
  protected abstract boolean[] logicalEval(boolean[] input);
  
}