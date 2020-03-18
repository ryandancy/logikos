package ca.keal.logikos.ui;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * The {@link Tool} used to interact with placed {@link UIComponent}.
 */
// TODO selecting multiple components at once
public class SelectTool extends Tool {
  
  private Selectable selected = null;
  
  public SelectTool() {
    super("Select", "Select nodes to interact with them");
  }
  
  @Override
  public void onClick(MousePosition position) {
    Selectable clicked;
    if (position.getComponent() != null) {
      clicked = position.getComponent();
    } else if (position.getConnection() != null) {
      clicked = position.getConnection();
    } else {
      // They didn't click on anything
      clicked = null;
    }
    
    if (clicked == selected) return;
    
    deselect();
    
    if (clicked == null) return;
    
    selected = clicked;
    selected.select();
  }
  
  @Override
  public void onKeyPress(KeyEvent e) {
    if (selected != null && e.getCode() == KeyCode.DELETE) {
      selected.delete();
      deselect();
    }
  }
  
  @Override
  public void onDeselect() {
    deselect();
  }
  
  private void deselect() {
    if (selected != null) {
      selected.deselect();
      selected = null;
    }
  }
  
}