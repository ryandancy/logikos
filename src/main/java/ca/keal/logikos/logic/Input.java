package ca.keal.logikos.logic;

/**
 * A simple {@link LogicComponent} representing an input of a circuit. It has no inputs and only 1 output. It has a
 * fixed output value which may be accessed with {@link #getValue()} and {@link #setValue(boolean)}.
 */
public class Input extends LogicComponent {
  
  /** The value of this Input */
  private boolean value = false;
  
  public Input() {
    super(0, 1); // 0 inputs, 1 output
  }
  
  /**
   * @return The value of this {@link Input}.
   */
  public boolean getValue() {
    return value;
  }
  
  /**
   * Set this {@link Input}'s value. This dirties all {@link LogicComponent}s connected to this {@link Input}.
   * @param value The new value for this {@link Input}.
   */
  public void setValue(boolean value) {
    this.value = value;
    dirtyAhead(this);
  }
  
  private void dirtyAhead(LogicComponent node) {
    // Dirty everything ahead of the node recursively
    // If a component is already dirty, it was set that way by another Input and so we don't need to dirty it or the
    // components after it again
    
    Port.Output[] outputs = node.getOutputs();
    for (Port.Output output : outputs) {
      if (output == null) continue;
      for (Connection connection : output.getConnections()) {
        if (connection == null) continue;
        
        LogicComponent newNode = connection.getInput().getComponent();
        // ignore Outputs, already dirtied nodes
        if ((newNode instanceof Output) || newNode.isDirty()) continue;
        newNode.markDirty();
        dirtyAhead(newNode);
      }
    }
  }
  
  @Override
  public boolean[] evaluate(EvaluationListener listener) {
    // Supply the set value
    boolean[] output = new boolean[] {value};
    if (listener != null) {
      listener.onEvaluation(new EvaluationListener.Event(this, new boolean[0], output));
    }
    return output;
  }
  
  @Override
  public String getName() {
    return "INPUT";
  }
  
}