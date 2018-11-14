package ca.keal.logikos.logic;

import java.util.Arrays;

/**
 * A listener that listens to each evaluation in {@link LogicComponent#evaluate(EvaluationListener)}.
 * {@link #onEvaluation(Event)} is called each time {@link LogicComponent#evaluate(EvaluationListener)} completes.
 */
@FunctionalInterface
public interface EvaluationListener {
  
  /**
   * Called each time {@link LogicComponent#evaluate(EvaluationListener)} is called with this listener registered. Note
   * that this is called only when a (probably) new result is generated and not when a memoized value is recalled.
   * @param e The {@link Event} encapsulating the necessary information about the successful evaluation.
   */
  void onEvaluation(Event e);
  
  /**
   * A POJO encapsulating the information about a successful completion of
   * {@link LogicComponent#evaluate(EvaluationListener)}.
   */
  class Event {
    
    private final LogicComponent logicComponent;
    private final boolean[] inputs;
    private final boolean[] outputs;
    
    Event(LogicComponent logicComponent, boolean[] inputs, boolean[] outputs) {
      if (logicComponent == null || inputs == null || outputs == null) {
        throw new NullPointerException("EvaluationListener.Event cannot have null component/input/output");
      }
      this.logicComponent = logicComponent;
      this.inputs = inputs;
      this.outputs = outputs;
    }
    
    public LogicComponent getLogicComponent() {
      return logicComponent;
    }
    
    public boolean[] getInputs() {
      return inputs;
    }
    
    public boolean[] getOutputs() {
      return outputs;
    }
    
    @Override
    public String toString() {
      return "EvaluationListener.Event[component=" + logicComponent + ", inputs=" + Arrays.toString(inputs)
          + ", outputs=" + Arrays.toString(outputs) + "]";
    }
    
    @Override
    public boolean equals(Object obj) {
      if (!(obj instanceof Event)) return false;
      Event e = (Event) obj;
      return logicComponent.equals(e.logicComponent) && Arrays.equals(inputs, e.inputs)
          && Arrays.equals(outputs, e.outputs);
    }
    
    @Override
    public int hashCode() {
      return 37 * logicComponent.hashCode() * Arrays.hashCode(inputs) * Arrays.hashCode(outputs);
    }
    
  }
  
}
