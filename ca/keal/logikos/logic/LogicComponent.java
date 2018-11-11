package ca.keal.logikos.logic;

/**
 * Represents a logical component, a blanket term for a gate, input, or output. This abstract base class contains
 * {@link Connection}s representing inputs and outputs and has logic for evaluating chains of logical components.
 * TODO: setting Connections, the implementation of evaluate()
 */
public abstract class LogicComponent {
  
  private Connection[] inputs;
  private Connection[] outputs;
  
  /**
   * Initialize the LogicComponent with {@code numInputs} inputs {@link Connection}s and {@code numOutputs} output
   * {@link Connection}s. All {@link Connection}s are initialized to {@code null}.
   * @param numInputs The number of inputs this LogicComponent has.
   * @param numOutputs The number of outputs this LogicComponent has.
   */
  protected LogicComponent(int numInputs, int numOutputs) {
    inputs = new Connection[numInputs];
    outputs = new Connection[numOutputs];
  }
  
  /**
   * Find a value for this LogicComponent by evaluating all previous LogicComponents. This method may be overridden in
   * the case where the input value is constant. TODO memoize 
   */
  public boolean[] evaluate() {
    return null;
  }
  
}