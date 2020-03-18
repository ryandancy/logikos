package ca.keal.logikos.logic;

import ca.keal.logikos.field.Field;
import ca.keal.logikos.field.InputFC;

import java.util.List;

/**
 * A user-created gate based on a {@link Field}. The {@code UserGate} is a way for users to create a custom gate based
 * on a {@link Field} they have previously created. It contains a {@code Field} to which it delegates evaluation. It
 * also has a user-entered name.
 */
public class UserGate extends Gate {
  
  private Field field;
  private String name;
  
  /**
   * Create a {@link UserGate} based on the given {@link Field}. The new {@code UserGate} is functionally identical to
   * the {@code Field} when run.
   * @param field The {@link Field} on which to base this {@code UserGate}.
   * @param name The name of the new {@code UserGate}.
   */
  protected UserGate(Field field, String name) {
    super(field.getInputFCs().size(), field.getOutputFCs().size());
    setField(field);
    setUserGateName(name);
  }
  
  public void setField(Field field) {
    if (field == null) {
      throw new NullPointerException("UserGate cannot have a null Field");
    }
    this.field = field;
  }
  
  public Field getField() {
    return field;
  }
  
  public void setUserGateName(String name) {
    if (name == null) {
      throw new NullPointerException("UserGate cannot have a null name");
    }
    this.name = name;
  }
  
  public String getUserGateName() {
    return name;
  }
  
  @Override
  protected boolean[] logicalEval(boolean[] input) {
    // Set the inputs
    List<InputFC> inputFCs = field.getInputFCs();
    for (int i = 0; i < input.length; i++) {
      inputFCs.get(i).getLogicComponent().setValue(input[i]);
    }
    
    // Evaluate
    return field.evaluate(null);
  }
  
  @Override
  public String getName() {
    return getUserGateName();
  }
  
}