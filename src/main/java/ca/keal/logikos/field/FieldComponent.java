package ca.keal.logikos.field;

import ca.keal.logikos.logic.AndGate;
import ca.keal.logikos.logic.LogicComponent;
import ca.keal.logikos.logic.NandGate;
import ca.keal.logikos.logic.NotGate;
import ca.keal.logikos.logic.OrGate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A FieldComponent is something on the {@link Field} - a gate, input or output. It's essentially a wrapper for
 * {@link LogicComponent} that encapsulates a {@link Position}, which can be changed.
 */
// TODO find a way to save connections - we currently don't
public class FieldComponent implements Saveable {
  
  public static final String XML_TAG = "fieldComponent";
  
  private LogicComponent logicComponent;
  private Position position;
  
  public FieldComponent(LogicComponent logicComponent, Position initialPosition) {
    if (logicComponent == null) {
      throw new NullPointerException("FieldComponent's LogicComponent cannot be null");
    }
    this.logicComponent = logicComponent;
    setPosition(initialPosition);
  }
  
  public FieldComponent() {} // for Saveable
  
  public LogicComponent getLogicComponent() {
    return logicComponent;
  }
  
  public Position getPosition() {
    return position;
  }
  
  public void setPosition(Position position) {
    this.position = position;
  }
  
  @Override
  public void save(Document dom, Element serialized) {
    Element positionElement = dom.createElement(Position.XML_TAG);
    position.save(dom, positionElement);
    serialized.appendChild(positionElement);
    
    Element lcElement = dom.createElement("logicComponent");
    lcElement.setTextContent(logicComponent.toString());
    serialized.appendChild(lcElement);
  }
  
  @Override
  public void populate(Element serialized) {
    position = SavingUtils.generate(Position.class, serialized);
    
    String lcName = serialized.getElementsByTagName("logicComponent").item(0).getTextContent();
    // TODO find a better way of looking up logic component names - this breaks DRY
    switch (lcName) {
      case "AND":
        logicComponent = new AndGate();
        break;
      case "OR":
        logicComponent = new OrGate();
        break;
      case "NOT":
        logicComponent = new NotGate();
        break;
      case "NAND":
        logicComponent = new NandGate();
        break;
      default:
        // all others *should* be handled by their respective subclasses
        throw new MalformedSaveException("LogicComponent name unknown to FieldComponent.populate(): " + lcName);
    }
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