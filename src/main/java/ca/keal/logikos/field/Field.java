package ca.keal.logikos.field;

import ca.keal.logikos.logic.EvaluationListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A Field is the container for the entire logic model. It contains {@link FieldComponent}s and handles saving and
 * layout.
 */
public class Field {
  
  private final List<FieldComponent> fieldComponents = new ArrayList<>();
  
  // These lists exist to provide an order for the inputFCs
  // TODO some way to reorder the inputs/outputs
  private final List<InputFC> inputFCs = new ArrayList<>();
  private final List<OutputFC> outputFCs = new ArrayList<>();
  
  public List<FieldComponent> getFieldComponents() {
    return Collections.unmodifiableList(fieldComponents);
  }
  
  public List<InputFC> getInputFCs() {
    return Collections.unmodifiableList(inputFCs);
  }
  
  public List<OutputFC> getOutputFCs() {
    return Collections.unmodifiableList(outputFCs);
  }
  
  public void addFieldComponent(FieldComponent component) {
    if (component == null) {
      throw new NullPointerException("A null FieldComponent cannot be added to a field");
    }
    
    fieldComponents.add(component);
    
    if (component instanceof InputFC) {
      inputFCs.add((InputFC) component);
    } else if (component instanceof OutputFC) {
      outputFCs.add((OutputFC) component);
    }
  }
  
  public void removeFieldComponent(FieldComponent component) {
    fieldComponents.remove(component);
    if (component instanceof InputFC) {
      inputFCs.remove(component);
    } else if (component instanceof OutputFC) {
      outputFCs.remove(component);
    }
  }
  
  /**
   * Evaluate the entire {@link Field} and return the output as an array.
   * @param listener An {@link EvaluationListener} to listen to each gate's evaluation. May be {@code null}.
   * @return An array containing the output of each {@link OutputFC} in order. To get the {@link OutputFC} associated
   *  with each result, match each element in the returned array with the corresponding element in
   *  {@link #getOutputFCs()}.
   */
  public boolean[] evaluate(EvaluationListener listener) {
    boolean[] output = new boolean[outputFCs.size()];
    for (int i = 0; i < outputFCs.size(); i++) {
      output[i] = outputFCs.get(i).evaluate(listener);
    }
    return output;
  }
  
}