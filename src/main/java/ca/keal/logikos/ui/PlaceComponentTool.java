package ca.keal.logikos.ui;

import ca.keal.logikos.field.FieldComponent;
import ca.keal.logikos.field.Position;
import ca.keal.logikos.logic.LogicComponent;

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
  public void onHover(double paneX, double paneY, FieldComponent hoveredFC) {
    PannablePane fieldPane = Logikos.getInstance().getFieldPaneController().getFieldPane();
    
    if (hoveredFC != null) {
      // don't display ghost component over another component
      fieldPane.getContentChildren().remove(ghost);
      return;
    }
    
    // Display the ghost
    ghost.setLayoutX(fieldPane.paneToRealX(paneX));
    ghost.setLayoutY(fieldPane.paneToRealY(paneY));
    
    if (!fieldPane.getContentChildren().contains(ghost)) {
      fieldPane.getContentChildren().add(ghost);
    }
  }
  
  @Override
  public void onClick(double paneX, double paneY, FieldComponent clickedFC) {
    PannablePane fieldPane = Logikos.getInstance().getFieldPaneController().getFieldPane();
    
    fieldPane.getContentChildren().remove(ghost);
    if (clickedFC != null) return; // don't place on another component
    
    double realX = fieldPane.paneToRealX(paneX);
    double realY = fieldPane.paneToRealY(paneY);
    
    FieldComponent newFC = new FieldComponent(componentSupplier.get(), new Position(realX, realY));
    Logikos.getInstance().getField().addFieldComponent(newFC);
    
    UIComponent newUIC = new UIComponent(newFC, getName(), false);
    newUIC.setLayoutX(realX);
    newUIC.setLayoutY(realY);
    fieldPane.getContentChildren().add(newUIC);
  }
  
  @Override
  public void onDeselect() {
    super.onDeselect();
    removeGhost();
  }
  
  @Override
  public void onLeavePane() {
    super.onLeavePane();
    removeGhost();
  }
  
  private void removeGhost() {
    // holy method chaining, batman!
    Logikos.getInstance().getFieldPaneController().getFieldPane().getContentChildren().remove(ghost);
  }
  
}