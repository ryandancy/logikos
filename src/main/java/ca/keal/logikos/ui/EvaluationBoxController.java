package ca.keal.logikos.ui;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

/**
 * The controller class for the EvaluationBox. It adds the appropriate {@link Tool}s to the box at runtime much like
 * {@link ToolPaneController}.
 */
public class EvaluationBoxController {
  
  @FXML private Pane evaluationBox;
  
  @FXML
  public void initialize() {
    for (Tool tool : Logikos.DEFAULT_TOOLS) {
      if (tool.getLocation() != Tool.Location.EVALUATION_BOX) continue;
      evaluationBox.getChildren().add(tool.createRadioButton());
    }
  }
  
}