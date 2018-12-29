package ca.keal.logikos.ui;

import java.util.Objects;

/**
 * A POJO representing the position of the mouse on the FieldPane.
 */
public class MousePosition {
  
  private final double paneX;
  private final double paneY;
  private final UIComponent component;
  private final PortOver portOver;
  
  public MousePosition(double paneX, double paneY, UIComponent component, PortOver portOver) {
    this.paneX = paneX;
    this.paneY = paneY;
    this.component = component;
    this.portOver = portOver;
  }
  
  /**
   * @return The x-coordinate of the mouse's position in the FieldPane.
   */
  public double getPaneX() {
    return paneX;
  }
  
  /**
   * @return The y-coordinate of the mouse's position in the FieldPane.
   */
  public double getPaneY() {
    return paneY;
  }
  
  /**
   * @return The {@link UIComponent} the mouse is over, or {@code null} if the mouse isn't over a component.
   */
  public UIComponent getComponent() {
    return component;
  }
  
  /**
   * @return The {@link PortOver} representing which port the mouse is over, or {@code null} if the mouse isn't over a
   *  port.
   */
  public PortOver getPortOver() {
    return portOver;
  }
  
  /**
   * A POJO representing the port the mouse is over.
   */
  public static class PortOver {
    
    private final boolean isInput;
    private final int portNumber;
    
    public PortOver(boolean isInput, int portNumber) {
      this.isInput = isInput;
      this.portNumber = portNumber;
    }
  
    public boolean isInput() {
      return isInput;
    }
    
    public boolean isOutput() {
      return !isInput;
    }
  
    public int getPortNumber() {
      return portNumber;
    }
  
    @Override
    public boolean equals(Object obj) {
      if (!(obj instanceof PortOver)) return false;
      PortOver portOver = (PortOver) obj;
      return isInput == portOver.isInput && portNumber == portOver.portNumber;
    }
  
    @Override
    public int hashCode() {
      return 5131 * Boolean.hashCode(isInput) * Integer.hashCode(portNumber);
    }
  
    @Override
    public String toString() {
      return "PortOver[isInput=" + isInput + ", portNumber=" + portNumber + "]";
    }
    
  }
  
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof MousePosition)) return false;
    MousePosition pos = (MousePosition) obj;
    return paneX == pos.paneX && paneY == pos.paneY && Objects.equals(component, pos.component)
        && Objects.equals(portOver, pos.portOver);
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(paneX, paneY, component, portOver);
  }
  
  @Override
  public String toString() {
    return "MousePosition[paneX=" + paneX + ", paneY=" + paneY
        + ", component=" + component + ", portOver=" + portOver + "]";
  }
  
}