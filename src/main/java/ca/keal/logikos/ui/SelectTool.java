package ca.keal.logikos.ui;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@link Tool} used to interact with placed {@link UIComponent}.
 */
// TODO selecting multiple components at once
// TODO a Selectable interface to facilitate selecting UIConnections
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
    if (selectedComponent == null) return;
    
    Logikos.getInstance().getField().removeFieldComponent(selectedComponent.getFieldComponent());
    Logikos.getInstance().getFieldPaneController().getFieldPane().getContentChildren().remove(selectedComponent);
    
    for (UIConnection inputConnection : selectedComponent.getInputConnections()) {
      deleteConnection(inputConnection);
    }
    for (List<UIConnection> outputConnectionList : selectedComponent.getOutputConnectionLists()) {
      // Make a copy of the list because it's being modified as we loop through it
      List<UIConnection> outputConnListCopy = new ArrayList<>(outputConnectionList);
      for (UIConnection outputConnection : outputConnListCopy) {
        deleteConnection(outputConnection);
      }
    }
    
    deselectComponent();
  }
  
  private void deleteConnection(UIConnection connection) {
    if (connection == null) return;
    connection.destruct();
    Logikos.getInstance().getFieldPaneController().getFieldPane().getContentChildren().remove(connection);
  }
  
}