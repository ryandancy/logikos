package ca.keal.logikos.logic;

import ca.keal.logikos.field.Field;
import ca.keal.logikos.field.InputFC;
import ca.keal.logikos.util.DeserializationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.IOException;
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
   */
  protected UserGate(Field field) {
    super(field.getInputFCs().size(), field.getOutputFCs().size());
    setField(field);
    setUserGateName(field.getName());
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
  
  @Override
  public Element toXml(Document doc) {
    // Overwrite the type and add the filename
    Element elem = super.toXml(doc);
    elem.setAttribute("type", "USER");
    elem.setAttribute("filename", field.getFilename());
    return elem;
  }
  
  public static UserGate fromXml(Element elem) throws DeserializationException {
    if (!elem.hasAttribute("filename")) {
      throw new DeserializationException("User gate elements must have the 'filename' attribute.");
    }
    
    // Attempt to recover the field from the filename
    String filename = elem.getAttribute("filename");
    Field userField;
    try {
      userField = Field.fromXml(filename);
    } catch (IOException e) {
      throw new DeserializationException("Error while trying to read file '" + filename + "'", e);
    }
    
    return new UserGate(userField);
  }
  
}