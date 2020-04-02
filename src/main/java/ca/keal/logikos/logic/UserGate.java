package ca.keal.logikos.logic;

import ca.keal.logikos.field.Field;
import ca.keal.logikos.field.InputFC;
import ca.keal.logikos.ui.Logikos;
import ca.keal.logikos.util.DeserializationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
  public UserGate(Field field) {
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
    
    // Tick
    return field.tick(null);
  }
  
  @Override
  public String getName() {
    return getUserGateName();
  }
  
  @Override
  public String getPortNameByIndex(boolean input, int index) {
    // retrieve from the field
    int numPorts = input ? getNumInputs() : getNumOutputs();
    if (index < 0 || index >= numPorts) {
      throw new IllegalArgumentException("Cannot get port name of index " + index + ", max is " + numPorts);
    }
    
    if (input) {
      return getField().getInputFCs().get(index).getLabel();
    } else {
      return getField().getOutputFCs().get(index).getLabel();
    }
  }
  
  @Override
  public Element toXml(Document doc) {
    Path absPath = Paths.get(field.getFilename()).normalize();
    Path base = Paths.get(Logikos.getInstance().getField().getFilename()).getParent().normalize();
    Path relPath = base.relativize(absPath);
    
    // Overwrite the type and add the filename
    Element elem = super.toXml(doc);
    elem.setAttribute("type", "USER");
    elem.setAttribute("filename", relPath.toString());
    return elem;
  }
  
  public static UserGate fromXml(Element elem, String filename) throws DeserializationException {
    if (!elem.hasAttribute("filename")) {
      throw new DeserializationException("User gate elements must have the 'filename' attribute.");
    }
    
    Path gatePath = Paths.get(elem.getAttribute("filename")).normalize();
    Path base = Paths.get(filename).getParent().normalize();
    Path absGatePath = base.resolve(gatePath);
    
    // Attempt to recover the field from the filename
    Field userField;
    try {
      userField = Field.fromXml(absGatePath.toString());
    } catch (IOException e) {
      throw new DeserializationException("Error while trying to read file '" + absGatePath + "'", e);
    }
    
    return new UserGate(userField);
  }
  
}