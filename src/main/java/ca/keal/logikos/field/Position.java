package ca.keal.logikos.field;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A position on the field.
 */
public class Position implements Saveable {
  
  // effectively final, but not officially final for Saveable
  private double x;
  private double y;
  
  public Position(double x, double y) {
    this.x = x;
    this.y = y;
  }
  
  /** Fulfilling the Saveable contract */
  private Position() {
    x = Double.NaN;
    y = Double.NaN;
  }
  
  public double getX() {
    return x;
  }
  
  public double getY() {
    return y;
  }
  
  @Override
  public void save(Document dom, Element serialized) {
    Element xElem = dom.createElement("x");
    xElem.setTextContent(String.valueOf(x));
    serialized.appendChild(xElem);
    
    Element yElem = dom.createElement("y");
    yElem.setTextContent(String.valueOf(y));
    serialized.appendChild(yElem);
  }
  
  @Override
  public void populate(Element serialized) {
    x = Double.parseDouble(serialized.getElementsByTagName("x").item(0).getTextContent());
    y = Double.parseDouble(serialized.getElementsByTagName("y").item(0).getTextContent());
  }
  
  @Override
  public String getElementName() {
    return "position";
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
  
}