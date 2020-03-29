package ca.keal.logikos.field;

import ca.keal.logikos.logic.LogicComponent;
import ca.keal.logikos.util.DeserializationException;
import ca.keal.logikos.util.Option;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a {@link FieldComponent} that has options customizable by the user, such as label or clock speed.
 */
public abstract class OptionFC extends FieldComponent {
  
  private List<Option<?>> options = new ArrayList<>();
  
  public OptionFC(LogicComponent logicComponent, Position initialPosition) {
    super(logicComponent, initialPosition);
  }
  
  public List<Option<?>> getOptions() {
    return Collections.unmodifiableList(options);
  }
  
  protected void addOption(Option<?> option) {
    options.add(option);
  }
  
  @Override
  public Element toXml(Document doc) {
    Element elem = super.toXml(doc);
    for (int i = 0; i < options.size(); i++) {
      Option option = options.get(i);
      elem.setAttribute("option-" + i, option.getValue().toString());
    }
    return elem;
  }
  
  protected void fillInOptionsFromXml(Element elem) throws DeserializationException {
    for (int i = 0; i < options.size(); i++) {
      String optionAttr = "option-" + i;
      if (!elem.hasAttribute(optionAttr)) {
        throw new DeserializationException("Option field component is missing attribute for option " + i);
      }
      
      Option option = options.get(i);
      boolean valid = option.setValue(elem.getAttribute(optionAttr));
      if (!valid) {
        throw new DeserializationException("Option field component specifies invalid data for option " + i);
      }
    }
  }
  
}
