package ca.keal.logikos.field;

import ca.keal.logikos.logic.LogicComponent;

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
  
}