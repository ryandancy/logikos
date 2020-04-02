package ca.keal.logikos.field;

import ca.keal.logikos.logic.Input;
import ca.keal.logikos.logic.LogicComponent;
import ca.keal.logikos.logic.Output;
import ca.keal.logikos.util.DeserializationException;
import ca.keal.logikos.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Map;
import java.util.UUID;

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
    this.position = position;
  }

  /**
   * Return the name of the index'th port. If {@code input} is {@code true}, then return the name of the index'th
   * input port; else, return the name of the index'th output port.
   */
  public String getPortNameByIndex(boolean input, int index) {
    return getLogicComponent().getPortNameByIndex(input, index);
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
  
  /**
   * Serialize this FieldComponent to XML.
   */
  public Element toXml(Document doc) {
    Element elem = doc.createElement("fieldComponent");
    elem.appendChild(getPosition().toXml(doc));
    elem.appendChild(getLogicComponent().toXml(doc));
    return elem;
  }
  
  /**
   * Partially deserialize XML returned by {@link #toXml(Document)} into a {@link FieldComponent}.
   * {@link #fillInPortsFromXml(Map, Element)} must be called afterwards with a map of all UUIDs to FieldComponents.
   */
  // TODO put tag names into constants and don't use this magic string crap
  public static FieldComponent instantiateFromXml(Element elem, String filename) throws DeserializationException {
    if (!elem.getTagName().equals("fieldComponent")) {
      throw new DeserializationException("Field component must have tag <fieldComponent>.");
    }
    
    Element posElem = XmlUtil.getDirectChildByTagName(elem, "position");
    Position pos = Position.fromXml(posElem);
    Element logicElem = XmlUtil.getDirectChildByTagName(elem, "logicComponent");
    LogicComponent lc = LogicComponent.fromXml(logicElem, filename);
    
    // Handle the input/output/options attributes set by Input/Output/OptionFC - this is tight coupling! bad!
    if (elem.hasAttribute("input")) {
      return InputFC.fromXml(elem, pos, (Input) lc);
    } else if (elem.hasAttribute("output")) {
      return OutputFC.fromXml(elem, pos, (Output) lc);
    } else {
      return new FieldComponent(lc, pos);
    }
  }
  
  public void fillInPortsFromXml(Map<UUID, LogicComponent> uuidToLC, Element fcElem) throws DeserializationException {
    getLogicComponent().fillInPortsFromXml(uuidToLC, XmlUtil.getDirectChildByTagName(fcElem, "logicComponent"));
  }
  
}