package ca.keal.logikos.ui;

import ca.keal.logikos.field.FieldComponent;
import ca.keal.logikos.field.Position;
import ca.keal.logikos.logic.LogicComponent;
import javafx.scene.Node;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A {@link Tool} which places a {@link UIComponent}. When the user clicks on the FieldPane, a {@link UIComponent}
 * (fully added to the {@code Field}) appears; when they hover over the FieldPane, a "ghost" {@link UIComponent} is
 * shown to show where a {@link UIComponent} would appear if they click.
 */
public class PlaceComponentTool extends Tool {
  
  /**
   * Create a FieldComponent at a given Position.
   */
  private Function<Position, FieldComponent> fcMaker;
  
  /**
   * Create a UIComponent from a FieldComponent. The boolean argument is whether the UIComponent is a ghost.
   */
  private BiFunction<FieldComponent, Boolean, UIComponent> uicMaker;
  
  /** This UIComponent is displayed when the user hovers over the FieldPane. */
  private UIComponent ghost;
  
  public PlaceComponentTool(String name, String tooltip, Supplier<LogicComponent> componentSupplier) {
    this(name, tooltip, position -> new FieldComponent(componentSupplier.get(), position),
        (fc, isGhost) -> new UIComponent(fc, fc.getLogicComponent().getName(), isGhost));
  }
  
  protected PlaceComponentTool(String name, String tooltip, Function<Position, FieldComponent> fcMaker,
                               BiFunction<FieldComponent, Boolean, UIComponent> uicMaker) {
    super(name, tooltip);
    
    if (fcMaker == null || uicMaker == null) {
      throw new NullPointerException("FieldComponent and UIComponent makers cannot be null!");
    }
    this.fcMaker = fcMaker;
    this.uicMaker = uicMaker;
    
    makeGhost();
  }
  
  @Override
  public void onHover(MousePosition position) {
    PannablePane fieldPane = Logikos.getInstance().getFieldPaneController().getFieldPane();
    
    // Display the ghost
    ghost.setLayoutX(fieldPane.paneToRealX(position.getPaneX()));
    ghost.setLayoutY(fieldPane.paneToRealY(position.getPaneY()));
    ghost.setVisible(!doesGhostIntersectAnything());
    
    if (!fieldPane.getContentChildren().contains(ghost)) {
      fieldPane.getContentChildren().add(ghost);
    }
  }
  
  @Override
  public void onClick(MousePosition position) {
    PannablePane fieldPane = Logikos.getInstance().getFieldPaneController().getFieldPane();
    
    ghost.setVisible(false);
    
    // Don't place on another component
    // Note that the ghost's position is constantly set by onHover() so we don't need to do it here
    if (position.getComponent() != null || doesGhostIntersectAnything()) return;
    
    double realX = fieldPane.paneToRealX(position.getPaneX());
    double realY = fieldPane.paneToRealY(position.getPaneY());
    
    FieldComponent newFC = fcMaker.apply(new Position(realX, realY));
    if (newFC == null) return;
    Logikos.getInstance().getField().addFieldComponent(newFC);
    
    UIComponent newUIC = uicMaker.apply(newFC, false);
    newUIC.setLayoutX(realX);
    newUIC.setLayoutY(realY);
    fieldPane.getContentChildren().add(newUIC);
    
    Logikos.getInstance().getWindowTitleManager().update();
  }
  
  private boolean doesGhostIntersectAnything() {
    PannablePane fieldPane = Logikos.getInstance().getFieldPaneController().getFieldPane();
    for (Node child : fieldPane.getContentChildren()) {
      if (child != ghost && child instanceof UIComponent
          && ghost.intersects(ghost.sceneToLocal(child.localToScene(child.getBoundsInLocal())))) {
        return true;
      }
    }
    return false;
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
  
  protected void setFieldComponentMaker(Function<Position, FieldComponent> fcMaker) {
    this.fcMaker = fcMaker;
  }
  
  protected void setUIComponentMaker(BiFunction<FieldComponent, Boolean, UIComponent> uicMaker) {
    this.uicMaker = uicMaker;
  }
  
  protected void makeGhost() {
    // delete the old ghost if it exists + save its information
    boolean oldGhostExists = ghost != null;
    double ghostX = -1;
    double ghostY = -1;
    boolean ghostVisible = false;
    if (oldGhostExists) {
      ghostX = ghost.getLayoutX();
      ghostY = ghost.getLayoutY();
      ghostVisible = ghost.isVisible();
      removeGhost();
    }
    
    // the position of the FieldComponent is null because it's a dummy FieldComponent to make the ghost work
    ghost = uicMaker.apply(fcMaker.apply(null), true);
    
    // reset at the old ghost's position
    if (oldGhostExists) {
      ghost.setLayoutX(ghostX);
      ghost.setLayoutY(ghostY);
      ghost.setVisible(ghostVisible);
      Logikos.getInstance().getFieldPaneController().getFieldPane().getContentChildren().add(ghost);
    }
  }
  
}