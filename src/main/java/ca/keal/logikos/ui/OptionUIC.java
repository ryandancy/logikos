package ca.keal.logikos.ui;

import ca.keal.logikos.field.OptionFC;
import ca.keal.logikos.util.Option;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;

/**
 * A {@link UIComponent} which has a context menu for displaying options.
 */
public class OptionUIC extends UIComponent {
  
  protected OptionUIC(OptionFC optionFC, String displayName, boolean isGhost) {
    super(optionFC, displayName, isGhost);
    setupContextMenu(optionFC);
  }
  
  private void setupContextMenu(OptionFC optionFC) {
    ContextMenu contextMenu = new ContextMenu();
    
    for (Option<?> option : optionFC.getOptions()) {
      // build the menu item
      Label label = new Label(option.getText());
      label.setAlignment(Pos.CENTER_RIGHT);
      label.setStyle("-fx-label-padding: 1em");
      
      TextField textField = new TextField(option.getValue().toString());
      
      textField.setOnKeyTyped(e -> {
        // set the option's value and notify the user if it's invalid
        String text = e.getText();
        boolean valid = option.setValue(text);
        
        if (valid) {
          textField.setStyle("-fx-border-color: transparent");
        } else {
          textField.setStyle("-fx-border-color: red");
        }
      });
      
      HBox hbox = new HBox();
      hbox.setAlignment(Pos.CENTER);
      hbox.getChildren().addAll(label, textField);
      
      CustomMenuItem item = new CustomMenuItem(hbox);
      item.setHideOnClick(false);
      contextMenu.getItems().add(item);
    }
    
    setOnMouseClicked(e -> {
      if (e.getButton() == MouseButton.SECONDARY || e.isShortcutDown()) {
        contextMenu.show(this, Side.BOTTOM, 0, 0);
      }
    });
  }
  
}
