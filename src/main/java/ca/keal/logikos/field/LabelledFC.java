package ca.keal.logikos.field;

import ca.keal.logikos.logic.LogicComponent;
import ca.keal.logikos.util.Option;

class LabelledFC extends OptionFC {
  
  // TODO generate default labels based on input/output number
  protected Option<String> labelOption = new Option<>("Label", s -> true, s -> s, "");
  
  public LabelledFC(LogicComponent logicComponent, Position initialPosition) {
    super(logicComponent, initialPosition);
    addOption(labelOption);
  }
  
}
