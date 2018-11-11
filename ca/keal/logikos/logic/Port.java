package ca.keal.logikos.logic;

/**
 * A generic, abstract class representing a port on a {@link LogicComponent}. It has a non-negative port number and a
 * {@link LogicComponent} to which it belongs, as well as possibly a {@link Connection} representing its connection to
 * another {@link Port}. The class is abstract as it should not be instantiated; instead, use {@link Port.Input} and
 * {@link Port.Output}, concrete subclasses of this class. This class is generic so that the {@link #connectTo(Port)}
 * method's parameter is a {@link Port.Output} if this class is a {@link Port.Input} and vice versa for extra
 * compile-time type checking.
 * 
 * <p>The port number and component to which this {@code Port} is connected are immutable.</p>
 * 
 * @param <CONNECTABLE_TO> The type of {@code Port} to which this {@code Port} may be connected.
 */
public abstract class Port<CONNECTABLE_TO extends Port> {
  
  private final int portNumber;
  private final LogicComponent component;
  
  private Connection connection = null;
  
  private Port(int number, LogicComponent component) {
    if (component == null) throw new NullPointerException("Port's component cannot be null");
    if (number < 0) throw new IllegalArgumentException("Port number cannot be negative");
    this.portNumber = number;
    this.component = component;
  }
  
  public int getPortNumber() {
    return portNumber;
  }
  
  public LogicComponent getComponent() {
    return component;
  }
  
  public Connection getConnection() {
    return connection;
  }
  
  /**
   * For internal use. It is recommended to use {@link #connectTo(Port)} to modify this {@link Port}'s
   * {@link Connection}.
   */
  void setConnection(Connection connection) {
    this.connection = connection;
  }
  
  /**
   * Establish a {@link Connection} between {@code port} and this {@link Port}. This method will overwrite the
   * {@link Connection}s of both this {@link Port} and {@code port}.
   * @param port The port of the opposite type to which to connect this {@link Port}.
   */
  public void connectTo(CONNECTABLE_TO port) {
    Connection connection = getNewConnectionTo(port);
    this.connection = connection;
    port.setConnection(connection);
  }
  
  protected abstract Connection getNewConnectionTo(CONNECTABLE_TO port);
  
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Port)) return false;
    Port port = (Port) obj;
    // no null check here because component is never null
    return port.portNumber == portNumber && component.equals(port.component);
  }
  
  @Override
  public int hashCode() {
    return 271 * portNumber * component.hashCode();
  }
  
  @Override
  public String toString() {
    return portNumber + " on " + component;
  }
  
  /**
   * A {@link Port} representing an input port. It is connectable to {@link Port.Output}s.
   */
  public static class Input extends Port<Output> {
    public Input(int number, LogicComponent component) {
      super(number, component);
    }
  
    @Override
    protected Connection getNewConnectionTo(Output port) {
      return new Connection(this, port);
    }
  }
  
  /**
   * A {@link Port} representing an output port. It is connectable to {@link Port.Input}s.
   */
  public static class Output extends Port<Input> {
    public Output(int number, LogicComponent component) {
      super(number, component);
    }
  
    @Override
    protected Connection getNewConnectionTo(Input port) {
      return new Connection(port, this);
    }
  }
  
}