package ca.keal.logikos.logic;

import java.util.function.BiFunction;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

/**
 * Represents a logical component, a blanket term for a gate, input, or output. This abstract base class contains
 * {@link Connection}s representing inputs and outputs and has logic for evaluating chains of logical components.
 */
// TODO give LogicComponents IDs
public abstract class LogicComponent {
  
  private final Port.Input[] inputs;
  private final Port.Output[] outputs;
  
  /**
   * Initialize the LogicComponent with {@code numInputs} inputs {@link Port.Input}s and {@code numOutputs} output
   * {@link Port.Output}s.
   * @param numInputs The number of inputs this LogicComponent has.
   * @param numOutputs The number of outputs this LogicComponent has.
   */
  protected LogicComponent(int numInputs, int numOutputs) {
    inputs = getPortArray(numInputs, Port.Input::new, Port.Input[]::new);
    outputs = getPortArray(numOutputs, Port.Output::new, Port.Output[]::new);
  }
  
  // Helper to fill an array with Ports with port numbers and array indices aligned
  private <P extends Port> P[] getPortArray(
      int numPorts,
      BiFunction<Integer, LogicComponent, P> portConstructor,
      IntFunction<P[]> arrayConstructor) {
    return IntStream.range(0, numPorts)
        .mapToObj(portNum -> portConstructor.apply(numPorts, this))
        .toArray(arrayConstructor);
  }
  
  public int getNumInputs() {
    return inputs.length;
  }
  
  public int getNumOutputs() {
    return outputs.length;
  }
  
  /**
   * Find the output values for this LogicComponent by evaluating all previous LogicComponents.
   * @return This {@link LogicComponent}'s output values for its current input values.
   */
  // TODO memoize, implement an EvaluationListener
  public boolean[] evaluate() {
    // Get all the inputs
    boolean[] inputValues = new boolean[getNumInputs()];
    for (int i = 0; i < inputValues.length; i++) {
      inputValues[i] = inputs[i].getInputValue();
    }
    
    return logicalEval(inputValues);
  }
  
  protected abstract boolean[] logicalEval(boolean[] input);
  
  @Override
  public abstract String toString();
  
}