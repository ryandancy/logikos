package ca.keal.logikos.ui;

import ca.keal.logikos.field.Field;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;

final class SaveUtil {
  
  static void save(Stage stage, Field field) {
    if (field.getFilename() == null || field.getName() == null) {
      saveAs(stage, field);
    }
    
  }
  
  static void saveAs(Stage stage, Field field) {
    if (field.getName() == null) {
      String name = promptForName();
      if (name == null) return;
      field.setName(name);
    }
    
    if (field.getFilename() == null) {
      String filename = promptForFilename(stage);
      if (filename == null) return;
      field.setFilename(filename);
    }
    
    
  }
  
  private static String promptForFilename(Stage stage) {
    FileChooser chooser = new FileChooser();
    chooser.setTitle("Save as...");
    chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Logikos Files", ".lgk"));
    chooser.setInitialFileName("new-gate.lgk");
    File selected = chooser.showSaveDialog(stage);
    return selected == null ? null : selected.getAbsolutePath();
  }
  
  private static String promptForName() {
    TextInputDialog dialog = new TextInputDialog("New Gate");
    dialog.setTitle("Enter component name...");
    dialog.setContentText("Enter a name for the component which will be shown in its symbol.");
    dialog.getDialogPane().getButtonTypes().add(
        new ButtonType("Done", ButtonBar.ButtonData.OK_DONE));
    Optional<String> result = dialog.showAndWait();
    return result.orElse(null);
  }
  
  // todo saving
  
}
