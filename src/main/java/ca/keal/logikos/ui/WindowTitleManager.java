package ca.keal.logikos.ui;

import ca.keal.logikos.field.Field;
import javafx.stage.Stage;

public class WindowTitleManager {
  
  private static final String TITLE_FORMAT = "Logikos - %s%s";
  private static final String DEFAULT_FILENAME = "Untitled.lgk";
  
  private Stage stage;
  
  public WindowTitleManager(Stage stage) {
    this.stage = stage;
    update();
  }
  
  public void update() {
    Field field = Logikos.getInstance().getField();
    
    String filename = field.getFilename();
    if (filename == null) filename = DEFAULT_FILENAME;
    
    String modifiedAsterisk = field.isModified() ? "*" : "";
    
    String title = String.format(TITLE_FORMAT, filename, modifiedAsterisk);
    stage.setTitle(title);
  }
  
}
