package ca.keal.logikos.logic;

import java.util.function.BiFunction;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

/**
 * Represents a logical component, a blanket term for a gate, input, or output. This abstract base class contains
 * {@link Connection}s representing inputs and outputs and has logic for evaluating chains of logical components.
 */
public abstract class LogicComponent {
  
  private final Port.Input[] inputs;
  private final Port.Output[] outputs;
  
  /**
   * Initialize the LogicComponent with {@code numInputs} inputs {@link Connection}s and {@code numOutputs} output
   * {@link Connection}s. All {@link Connection}s are initialized to {@code null}.
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
   * Find a value for this LogicComponent by evaluating all previous LogicComponents. This method may be overridden in
   * the case where the input value is constant. TODO memoize 
   */
  public boolean[] evaluate() {
    return null;
  }
  
  @Override
  public abstract String toString();
  
}