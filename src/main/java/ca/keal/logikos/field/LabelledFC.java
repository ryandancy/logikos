package ca.keal.logikos.field;

import ca.keal.logikos.logic.LogicComponent;
import ca.keal.logikos.util.Option;

class LabelledFC extends OptionFC {

  // TODO generate default labels based on input/output number
  private Option<String> labelOption = new Option<>("Label", s -> true, s -> s, "");
  
  LabelledFC(LogicComponent logicComponent, Position initialPosition) {
    super(logicComponent, initialPosition);
    addOption(labelOption);
  }
  
  public String getLabel() {
    return labelOption.getValue();
  }
  
}
