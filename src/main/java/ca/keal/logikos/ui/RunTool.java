package ca.keal.logikos.ui;

import ca.keal.logikos.field.Field;
import ca.keal.logikos.field.InputFC;
import ca.keal.logikos.logic.EvaluationListener;
import ca.keal.logikos.logic.LogicComponent;
import javafx.scene.Node;

/**
 * A tool used to run/evaluate the chip. This tool is placed in the evaluation box. When active, the chip is evaluated
 * and the user may interact via changing the inputs' values.
 */
// TODO validation of the field before evaluation
// TODO a better lookup system for LogicComponent -> FieldComponent -> UIComponent
public class RunTool extends Tool implements EvaluationListener {
  
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
    
    // Reset all InputUIC/OutputUICs to off, reset all UIConnection colours
    for (Node node : Logikos.getInstance().getFieldPaneController().getFieldPane().getContentChildren()) {
      if (node instanceof BooleanStateImageUIC) {
        ((BooleanStateImageUIC) node).setState(false);
        if (node instanceof InputUIC) {
          ((InputFC) ((InputUIC) node).getFieldComponent()).getLogicComponent().setValue(false);
        }
      } else if (node instanceof UIConnection) {
        ((UIConnection) node).resetStateVisual();
      }
    }
  }
  
  private void evaluate() {
    Field field = Logikos.getInstance().getField();
    boolean[] outputs = field.evaluate(this);
    
    // Set all OutputUICs' values
    for (Node node : Logikos.getInstance().getFieldPaneController().getFieldPane().getContentChildren()) {
      if (!(node instanceof OutputUIC)) continue;
      OutputUIC output = (OutputUIC) node;
      
      //noinspection SuspiciousMethodCalls
      output.setState(outputs[field.getOutputFCs().indexOf(output.getFieldComponent())]);
    }
  }
  
  @Override
  public void onEvaluation(Event e) {
    LogicComponent lc = e.getLogicComponent();
    if (lc.getNumOutputs() == 0) return;
    
    // Find the UIComponent
    UIComponent uic = null;
    for (Node node : Logikos.getInstance().getFieldPaneController().getFieldPane().getContentChildren()) {
      if (!(node instanceof UIComponent)) continue;
      uic = (UIComponent) node;
      if (uic.getFieldComponent().getLogicComponent().equals(lc)) {
        break;
      }
    }
    
    if (uic == null) {
      // The LogicComponent has no associated UIComponent on the field??
      System.err.println("Warning: LogicComponent " + lc + " has no associated UIComponent upon evaluation");
      return;
    }
    
    // Change the colour of the output UIConnections
    for (int i = 0; i < lc.getNumOutputs(); i++) {
      boolean state = e.getOutputs()[i];
      for (UIConnection conn : uic.getOutputConnections(i)) {
        conn.setStateVisual(state);
      }
    }
  }
  
}