package ca.keal.logikos.logic;

/**
 * Represents a connection between two {@link Port}s. It links a specific {@link Port.Input} on one
 * {@link LogicComponent} to a specific {@link Port.Output} on another {@link LogicComponent}.
 */
public class Connection {
  
  private final Port.Input input;
  private final Port.Output output;
  
  Connection(Port.Input input, Port.Output output) {
    if (input == null || output == null) throw new NullPointerException("Connection's ports cannot be null");
    this.input = input;
    this.output = output;
  }
  
}