package ca.keal.logikos.ui;

import ca.keal.logikos.field.Field;
import ca.keal.logikos.field.InputFC;
import javafx.scene.Node;

// TODO: changing input states, showing connection values (green/red), validation of the field before evaluation
public class RunTool extends Tool {
  
  public RunTool() {
    super("Run", "Run the chip", Location.EVALUATION_BOX, true);
  }
  
  @Override
  public void onClick(MousePosition position) {
    // Toggle toggleable InputUICs
    if (!(position.getComponent() instanceof InputUIC)) return;
    
    InputUIC inputUIC = (InputUIC) position.getComponent();
    InputFC inputFC = (InputFC) inputUIC.getFieldComponent();
    if (inputFC.getType().getBehaviour() != InputFC.Type.Behaviour.TOGGLE) return;
    
    boolean newValue = !inputFC.getValue();
    inputFC.getLogicComponent().setValue(newValue);
    inputUIC.setState(newValue);
    
    evaluate();
  }
  
  @Override
  public void onSelect() {
    // Add a red border to the FieldPane
    PannablePane fieldPane = Logikos.getInstance().getFieldPaneController().getFieldPane();
    fieldPane.setStyle("-fx-border-color: red; -fx-border-width: 15px");
    
    evaluate();
  }
  
  @Override
  public void onDeselect() {
    // Remove the red border on the FieldPane
    PannablePane fieldPane = Logikos.getInstance().getFieldPaneController().getFieldPane();
    fieldPane.setStyle("-fx-border: none");
    
    // Reset all InputUIC/OutputUICs to off
    for (Node node : Logikos.getInstance().getFieldPaneController().getFieldPane().getContentChildren()) {
      if (node instanceof BooleanStateImageUIC) {
        ((BooleanStateImageUIC) node).setState(false);
        if (node instanceof InputUIC) {
          ((InputFC) ((InputUIC) node).getFieldComponent()).getLogicComponent().setValue(false);
        }
      }
    }
  }
  
  private void evaluate() {
    Field field = Logikos.getInstance().getField();
    boolean[] outputs = field.evaluate(null);
    
    // Set all OutputUICs' values
    for (Node node : Logikos.getInstance().getFieldPaneController().getFieldPane().getContentChildren()) {
      if (!(node instanceof OutputUIC)) continue;
      OutputUIC output = (OutputUIC) node;
      
      //noinspection SuspiciousMethodCalls
      output.setState(outputs[field.getOutputFCs().indexOf(output.getFieldComponent())]);
    }
  }
  
}