package ca.keal.logikos.ui;

import ca.keal.logikos.field.FieldComponent;
import ca.keal.logikos.field.Position;
import ca.keal.logikos.logic.LogicComponent;
import javafx.scene.layout.Pane;

import java.util.function.Supplier;

public class PlaceComponentTool extends Tool {
  
  private final Supplier<LogicComponent> componentSupplier;
  
  /** This UIComponent is displayed when the user hovers over the FieldPane. */
  private final UIComponent ghost;
  
  public PlaceComponentTool(String name, String tooltip, Supplier<LogicComponent> componentSupplier) {
    super(name, tooltip);
    
    if (componentSupplier == null) {
      throw new NullPointerException("Component supplier cannot be null!");
    }
    this.componentSupplier = componentSupplier;
    
    // the position of the FieldComponent is null because it's a dummy FieldComponent to make the ghost work
    ghost = new UIComponent(new FieldComponent(componentSupplier.get(), null), name, true);
  }
  
  @Override
  public void onHover(LogikosApplication app, double paneX, double paneY, FieldComponent hoveredFC) {
    Pane fieldPane = app.getFieldPaneController().getFieldPane();
    
    if (hoveredFC != null) {
      // don't display ghost component over another component
      fieldPane.getChildren().remove(ghost);
      return;
    }
    
    // Display the ghost
    ghost.setLayoutX(paneX);
    ghost.setLayoutY(paneY);
    
    if (!fieldPane.getChildren().contains(ghost)) {
      fieldPane.getChildren().add(ghost);
    }
  }
  
  @Override
  public void onClick(LogikosApplication app, double paneX, double paneY, FieldComponent clickedFC) {
    Pane fieldPane = app.getFieldPaneController().getFieldPane();
    
    fieldPane.getChildren().remove(ghost);
    if (clickedFC != null) return; // don't place on another component
    
    // TODO convert from pane x/y to field x/y
    FieldComponent newFC = new FieldComponent(componentSupplier.get(), new Position(paneX, paneY));
    app.getField().addFieldComponent(newFC);
    
    UIComponent newUIC = new UIComponent(newFC, getName(), false);
    newUIC.setLayoutX(paneX);
    newUIC.setLayoutY(paneY);
    fieldPane.getChildren().add(newUIC);
  }
  
  @Override
  public void onDeselect(LogikosApplication app) {
    super.onDeselect(app);
    removeGhost(app);
  }
  
  @Override
  public void onLeavePane(LogikosApplication app) {
    super.onLeavePane(app);
    removeGhost(app);
  }
  
  private void removeGhost(LogikosApplication app) {
    app.getFieldPaneController().getFieldPane().getChildren().remove(ghost);
  }
  
}