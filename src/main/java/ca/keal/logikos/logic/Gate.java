package ca.keal.logikos.logic;

/**
 * A logic gate; i.e., a {@link LogicComponent} that has input and output {@link Port}s and performs a logical
 * operation. This class exists so that {@link Input} and {@link Output} are not weighed down by redundant memoization,
 * as memoization and the more complex {@link #evaluate()} implementation only make sense for gates and not for other
 * types of {@link LogicComponent}.
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
  // TODO memoize, EvaluationListener
  @Override
  public boolean[] evaluate() {
    // Get all the input values
    Port.Input[] inputs = getInputs();
    boolean[] inputValues = new boolean[getNumInputs()];
    for (int i = 0; i < inputValues.length; i++) {
      inputValues[i] = inputs[i].getInputValue();
    }
    
    // Evaluate them
    return logicalEval(inputValues);
  }
  
  /**
   * Perform the logical operation done by this {@link Gate}.
   * @param input The gate's current input. Length is equal to getNumInputs().
   * @return The gate's output for its input. Length must be equal to getNumOutputs().
   */
  protected abstract boolean[] logicalEval(boolean[] input);
  
}