package ca.keal.logikos.field;

import ca.keal.logikos.logic.LogicComponent;
import ca.keal.logikos.util.Option;

class LabelledFC extends OptionFC {
  
  public LabelledFC(LogicComponent logicComponent, Position initialPosition) {
    super(logicComponent, initialPosition);
    
    // TODO generate default labels based on input/output number
    Option<String> labelOption = new Option<>("Label", s -> true, s -> s, "");
    addOption(labelOption);
  }
  
}
