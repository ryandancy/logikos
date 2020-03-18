package ca.keal.logikos.field;

import ca.keal.logikos.util.DeserializationException;
import ca.keal.logikos.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.text.NumberFormat;

/**
 * A position on the field.
 */
public class Position {
  
  private final double x;
  private final double y;
  
  public Position(double x, double y) {
    this.x = x;
    this.y = y;
  }
  
  public double getX() {
    return x;
  }
  
  public double getY() {
    return y;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Position)) return false;
    Position pos = (Position) obj;
    return x == pos.x && y == pos.y;
  }
  
  @Override
  public int hashCode() {
    return 11 * Double.hashCode(x) * Double.hashCode(y);
  }
  
  @Override
  public String toString() {
    return "Position(" + x + ", " + y + ")";
  }
  
  public Element toXml(Document doc) {
    Element elem = doc.createElement("position");
    elem.setAttribute("x", Double.toString(getX()));
    elem.setAttribute("y", Double.toString(getY()));
    return elem;
  }
  
  public static Position fromXml(Element elem) throws DeserializationException {
    if (!elem.getTagName().equals("position")) {
      throw new DeserializationException("Positions must have tag <position>.");
    }
    
    if (!elem.hasAttribute("x") || !elem.hasAttribute("y")) {
      throw new DeserializationException("Position elements must have 'x' and 'y' attributes.");
    }
    
    double x, y;
    try {
      x = Double.parseDouble(elem.getAttribute("x"));
      y = Double.parseDouble(elem.getAttribute("y"));
    } catch (NumberFormatException e) {
      throw new DeserializationException("Illegal x or y number in position: x = " + elem.getAttribute("x")
          + ", y = " + elem.getAttribute("y"), e);
    }
    
    return new Position(x, y);
  }
  
}