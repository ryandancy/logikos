package ca.keal.logikos.field;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A Field is the container for the entire logic model. It contains {@link FieldComponent}s and handles saving and
 * layout.
 */
public class Field {
  
  private final List<FieldComponent> fieldComponents = new ArrayList<>();
  
  public List<FieldComponent> getFieldComponents() {
    return Collections.unmodifiableList(fieldComponents);
  }
  
  public List<InputFC> getInputFCs() {
    return fieldComponents.stream()
        .filter(fc -> fc instanceof InputFC)
        .map(fc -> (InputFC) fc)
        .collect(Collectors.toList());
  }
  
  public boolean addFieldComponent(FieldComponent component) {
    if (component == null) {
      throw new NullPointerException("A null FieldComponent cannot be added to a field");
    }
    return fieldComponents.add(component);
  }
  
  public boolean removeFieldComponent(FieldComponent component) {
    return fieldComponents.remove(component);
  }
  
}