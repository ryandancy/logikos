package ca.keal.logikos.logic;

import ca.keal.logikos.util.DeserializationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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
  
  public abstract void connectTo(CONNECTABLE_TO port);
  
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
   * A {@link Port} representing an input port. It is connectable to a single {@link Port.Output}.
   */
  public static class Input extends Port<Output> {
    
    private Connection connection = null;
    
    public Input(int number, LogicComponent component) {
      super(number, component);
    }
    
    public Connection getConnection() {
      return connection;
    }
    
    public void removeConnection() {
      connection = null;
    }
    
    @Override
    public void connectTo(Output port) {
      // Remove the previous connection from the other port if it exists
      if (connection != null) {
        connection.getOutput().getConnections().remove(connection);
      }
      
      // Overwrite the connection and add it to the other port
      connection = new Connection(this, port);
      port.getConnections().add(connection);
    }
  
    /**
     * @return The value of the connection to which this input is connected. Note that there is no corresponding
     * getOutputValue() in {@link Port.Output} because {@link LogicComponent}s are evaluated backwards: it doesn't make
     * sense to query an output port for the value of its connection, as the {@link Connection} will just call
     * the original {@link LogicComponent}.
     */
    public boolean getInputValue(EvaluationListener listener) {
      Connection connection = getConnection();
      if (connection == null) {
        throw new IllegalStateException("Port had no connection when trying to get input value");
      }
      return connection.getValue(listener);
    }

    /**
     * Serialize this input port's connection to XML.
     */
    public Element toXml(Document doc) {
      Element elem = doc.createElement("inputPort");
      elem.setAttribute("fromId", getConnection() == null ? ""
          : getConnection().getOutput().getComponent().getId().toString());
      elem.setAttribute("fromOutputPortNum", getConnection() == null ? ""
          : Integer.toString(getConnection().getOutput().getPortNumber()));
      return elem;
    }

    /**
     * Populate this input port's connection from the element output by {@link #toXml(Document)}.
     */
    public void populateFromXml(Map<UUID, LogicComponent> uuidToLC, Element elem) throws DeserializationException {
      if (!elem.getTagName().equals("inputPort")) {
        throw new DeserializationException("Input port tag name must be <inputPort>.");
      }
      if (!elem.hasAttribute("fromId") || !elem.hasAttribute("fromOutputPortNum")) {
        throw new DeserializationException("Input port tag must have 'fromId' and 'fromOutputPortNum' attributes.");
      }
      
      String fromIdStr = elem.getAttribute("fromId");
      if (fromIdStr.isEmpty()) {
        // empty means no connection
        removeConnection();
      } else {
        // try to connect to the UUID specified
        UUID uuid;
        try {
          uuid = UUID.fromString(fromIdStr);
        } catch (IllegalArgumentException e) {
          throw new DeserializationException("Invalid UUID: " + fromIdStr, e);
        }
        
        if (!uuidToLC.containsKey(uuid)) {
          throw new DeserializationException("Unknown logic component UUID: " + uuid.toString());
        }
        
        int outputPortNum;
        try {
          outputPortNum = Integer.parseInt(elem.getAttribute("fromOutputPortNum"));
        } catch (NumberFormatException e) {
          throw new DeserializationException("Invalid output port number: "
              + elem.getAttribute("fromOutputPortNum"), e);
        }
        
        LogicComponent from = uuidToLC.get(uuid);
        if (outputPortNum < 0 || outputPortNum >= from.getNumOutputs()) {
          throw new DeserializationException("Output port number out of range: " + outputPortNum);
        }
        
        connectTo(from.getOutput(outputPortNum));
      }
    }
    
  }
  
  /**
   * A {@link Port} representing an output port. It is connectable to multiple {@link Port.Input}s.
   */
  public static class Output extends Port<Input> {
    
    private final Set<Connection> connections = new HashSet<>();
    
    public Output(int number, LogicComponent component) {
      super(number, component);
    }
    
    public Set<Connection> getConnections() {
      return connections;
    }
    
    @Override
    public void connectTo(Input port) {
      // Delegate to Input.connectTo() - no reason to duplicate code here
      port.connectTo(this);
    }
    
  }
  
}