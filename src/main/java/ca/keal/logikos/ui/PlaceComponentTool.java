package ca.keal.logikos.ui;

import ca.keal.logikos.field.FieldComponent;
import ca.keal.logikos.field.Position;
import ca.keal.logikos.logic.LogicComponent;
import javafx.scene.Node;

import java.util.function.Supplier;

/**
 * A {@link Tool} which places a {@link UIComponent}. When the user clicks on the FieldPane, a {@link UIComponent}
 * (fully added to the {@code Field}) appears; when they hover over the FieldPane, a "ghost" {@link UIComponent} is
 * shown to show where a {@link UIComponent} would appear if they click.
 */
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
    
    FieldComponent newFC = new FieldComponent(componentSupplier.get(), new Position(realX, realY));
    Logikos.getInstance().getField().addFieldComponent(newFC);
    
    UIComponent newUIC = new UIComponent(newFC, getName(), false);
    newUIC.setLayoutX(realX);
    newUIC.setLayoutY(realY);
    fieldPane.getContentChildren().add(newUIC);
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
  
}