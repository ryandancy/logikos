package ca.keal.logikos.field;

import ca.keal.logikos.logic.LogicComponent;
import ca.keal.logikos.util.Option;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a {@link FieldComponent} that has options customizable by the user, such as label or clock speed.
 */
public class OptionFC extends FieldComponent {
  
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
  
}
