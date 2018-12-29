package ca.keal.logikos.ui;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * The {@link Tool} used to interact with placed {@link UIComponent}.
 */
// TODO selecting multiple components at once
public class SelectTool extends Tool {
  
  private UIComponent selectedComponent = null;
  
  public SelectTool() {
    super("Select", "Select nodes to interact with them");
  }
  
  @Override
  public void onClick(MousePosition position) {
    if (position.getComponent() == selectedComponent) return;
    
    deselectComponent();
    
    if (position.getComponent() == null) return;
    
    selectedComponent = position.getComponent();
    selectedComponent.setSelected();
  }
  
  @Override
  public void onKeyPress(KeyEvent e) {
    if (e.getCode() == KeyCode.DELETE) {
      deleteComponent();
    }
  }
  
  @Override
  public void onDeselect() {
    deselectComponent();
  }
  
  private void deselectComponent() {
    if (selectedComponent != null) {
      selectedComponent.setDeselected();
      selectedComponent = null;
    }
  }
  
  private void deleteComponent() {
    // TODO when connections are implemented, handle deleting a connected component
    if (selectedComponent == null) return;
    Logikos.getInstance().getField().removeFieldComponent(selectedComponent.getFieldComponent());
    Logikos.getInstance().getFieldPaneController().getFieldPane().getContentChildren().remove(selectedComponent);
    deselectComponent();
  }
  
}