package ca.keal.logikos.field;

import ca.keal.logikos.logic.LogicComponent;

/**
 * A FieldComponent is something on the {@link Field} - a gate, input or output. It's essentially a wrapper for
 * {@link LogicComponent} that encapsulates a {@link Position}, which can be changed.
 */
// TODO handling connections to other FieldComponents?
public class FieldComponent {
  
  private final LogicComponent logicComponent;
  private Position position;
  
  public FieldComponent(LogicComponent logicComponent, Position initialPosition) {
    if (logicComponent == null) {
      throw new NullPointerException("FieldComponent's LogicComponent cannot be null");
    }
    this.logicComponent = logicComponent;
    setPosition(initialPosition);
  }
  
  public LogicComponent getLogicComponent() {
    return logicComponent;
  }
  
  public Position getPosition() {
    return position;
  }
  
  public void setPosition(Position position) {
    if (position == null) {
      throw new NullPointerException("FieldComponent's Position cannot be null");
    }
    this.position = position;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof FieldComponent)) return false;
    FieldComponent fc = (FieldComponent) obj;
    return logicComponent.equals(fc.logicComponent) && position.equals(fc.position);
  }
  
  @Override
  public int hashCode() {
    return 41 * logicComponent.hashCode() * position.hashCode();
  }
  
  @Override
  public String toString() {
    return "FieldComponent[logicComponent=" + logicComponent + ", position=" + position + "]";
  }
  
}