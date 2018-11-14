package ca.keal.logikos.logic;

/**
 * Represents a connection between two {@link Port}s. It links a specific {@link Port.Input} on one
 * {@link LogicComponent} to a specific {@link Port.Output} on another {@link LogicComponent}. It is immutable.
 */
public class Connection {
  
  private final Port.Input input;
  private final Port.Output output;
  
  Connection(Port.Input input, Port.Output output) {
    if (input == null || output == null) throw new NullPointerException("Connection's ports cannot be null");
    this.input = input;
    this.output = output;
  }
  
  public Port.Input getInput() {
    return input;
  }
  
  public Port.Output getOutput() {
    return output;
  }
  
  /**
   * @return The "value" of this connection - the value of {@link Connection}'s output, or whether the wire represented
   * by this {@link Connection} is active or not.
   */
  public boolean getValue(EvaluationListener listener) {
    return output.getComponent().evaluate(listener)[output.getPortNumber()];
  }
  
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Connection)) return false;
    Connection connection = (Connection) obj;
    return input.equals(connection.input) && output.equals(connection.output);
  }
  
  @Override
  public int hashCode() {
    return 5 * input.hashCode() * output.hashCode();
  }
  
  @Override
  public String toString() {
    return "Connection between output port " + output + " and input port " + input;
  }
  
}