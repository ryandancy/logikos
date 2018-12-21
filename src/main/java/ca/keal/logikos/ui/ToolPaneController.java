package ca.keal.logikos.ui;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;

/**
 * The controller class for the ToolPane. It adds the {@link Tool}s to the list in the ToolPane at runtime.
 */
public class ToolPaneController extends MainPaneController {
  
  @FXML private Pane toolPane;
  
  @FXML
  public void initialize() {
    ToggleGroup group = new ToggleGroup();
    for (Tool tool : Tool.ALL_TOOLS) {
      // TODO images
      RadioButton btn = new RadioButton(tool.getName());
      btn.setTooltip(new Tooltip(tool.getTooltip()));
      btn.selectedProperty().addListener((observableValue, previouslySelected, nowSelected) -> {
        if (nowSelected) {
          tool.onSelect();
          getApplication().setSelectedTool(tool);
        } else if (previouslySelected) {
          tool.onDeselect();
        }
      });
      
      btn.setUserData(tool);
      btn.setToggleGroup(group);
      toolPane.getChildren().add(btn);
    }
  }
  
}