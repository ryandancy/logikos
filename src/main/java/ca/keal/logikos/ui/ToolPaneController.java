package ca.keal.logikos.ui;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;

/**
 * The controller class for the ToolPane. It adds the {@link Tool}s to the list in the ToolPane at runtime.
 */
public class ToolPaneController {
  
  @FXML private Pane toolPane;
  
  @FXML
  public void initialize() {
    ToggleGroup group = new ToggleGroup();
    
    for (Tool tool : Logikos.ALL_TOOLS) {
      // TODO images
      RadioButton btn = new RadioButton(tool.getName());
      btn.setTooltip(new Tooltip(tool.getTooltip()));
      btn.setUserData(tool);
      btn.setToggleGroup(group);
      
      if (tool == Logikos.ALL_TOOLS[0]) {
        // Make the first tool selected for real (i.e. with a dot in the radio button)
        btn.fire();
      }
      
      btn.selectedProperty().addListener((observableValue, previouslySelected, nowSelected) -> {
        if (nowSelected) {
          tool.onSelect();
          Logikos.getInstance().setSelectedTool(tool);
        } else if (previouslySelected) {
          tool.onDeselect();
        }
      });
      
      toolPane.getChildren().add(btn);
    }
  }
  
}