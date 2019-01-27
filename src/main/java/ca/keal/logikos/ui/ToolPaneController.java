package ca.keal.logikos.ui;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

/**
 * The controller class for the ToolPane. It adds the {@link Tool}s to the list in the ToolPane at runtime.
 */
public class ToolPaneController {
  
  @FXML private Pane toolPane;
  
  @FXML
  public void initialize() {
    for (Tool tool : Logikos.ALL_TOOLS) {
      if (tool.getLocation() != Tool.Location.TOOL_PANE) continue;
      toolPane.getChildren().add(tool.createRadioButton());
    }
  }
  
}