package ca.keal.logikos.logic;

import java.util.UUID;
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
  
  private final UUID id = UUID.randomUUID();
  
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
        .mapToObj(portNum -> portConstructor.apply(portNum, this))
        .toArray(arrayConstructor);
  }
  
  public UUID getId() {
    return id;
  }
  
  public int getNumInputs() {
    return inputs.length;
  }
  
  public Port.Input getInput(int num) {
    return inputs[num];
  }
  
  public Port.Input[] getInputs() {
    return inputs;
  }
  
  public int getNumOutputs() {
    return outputs.length;
  }
  
  public Port.Output getOutput(int num) {
    return outputs[num];
  }
  
  public Port.Output[] getOutputs() {
    return outputs;
  }
  
  /**
   * Find the output values for this LogicComponent.
   * @return This {@link LogicComponent}'s output values for its current input values.
   */
  // TODO implement an EvaluationListener
  public abstract boolean[] evaluate();
  
  /** @see Gate#markDirty() */
  public void markDirty() {}
  
  /** @see Gate#isDirty() */
  public boolean isDirty() {
    return false;
  }
  
  @Override
  public String toString() {
    return getName() + "[id=" + getId() + "]";
  }
  
  protected abstract String getName();
  
  @Override
  public boolean equals(Object obj) {
    if (obj == null || !getClass().equals(obj.getClass())) return false;
    LogicComponent component = (LogicComponent) obj;
    return getId().equals(component.getId());
  }
  
  @Override
  public int hashCode() {
    return getId().hashCode();
  }
  
}