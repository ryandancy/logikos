package ca.keal.logikos.ui;

/**
 * A superclass of {@link ToolPaneController} and {@link FieldPaneController} to facilitate the injection of the
 * {@link LogikosApplication} into each controller.
 */
public class MainPaneController {
  
  private LogikosApplication application = null;
  
  /**
   * This method should be called once by {@link LogikosApplication} in the {@code initialize()} method and should
   * never be called after that.
   */
  public void setApplication(LogikosApplication application) {
    if (this.application != null) {
      throw new IllegalStateException("Application being injected into controller more than once");
    }
    this.application = application;
  }
  
  /**
   * @return The main {@link LogikosApplication}.
   */
  protected LogikosApplication getApplication() {
    return application;
  }
  
}