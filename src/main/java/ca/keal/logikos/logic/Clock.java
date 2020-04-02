package ca.keal.logikos.logic;

/**
 * An input that toggles every fixed interval.
 */
// TODO reset the clock status when reset is called
public class Clock extends LogicComponent {
  
  // TODO customizable clock speed
  private static final int TICKS_PER_CLOCK_TICK = 200;
  
  private boolean currentValue = false;
  private int ticksInValue = 0;
  
  public Clock() {
    super(0, 1); // 0 inputs, 1 output
  }
  
  @Override
  boolean[] evaluate(EvaluationListener listener, boolean[] inputValues) {
    if (ticksInValue >= TICKS_PER_CLOCK_TICK) {
      ticksInValue = 0;
      currentValue = !currentValue;
    }
    ticksInValue++;
    
    boolean[] output = new boolean[] {currentValue};
    if (listener != null) {
      listener.onEvaluation(new EvaluationListener.Event(this, new boolean[0], output));
    }
    return output;
  }
  
  @Override
  public String getName() {
    return "CLK";
  }
  
}
