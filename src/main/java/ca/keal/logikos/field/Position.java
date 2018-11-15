package ca.keal.logikos.field;

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
  
}