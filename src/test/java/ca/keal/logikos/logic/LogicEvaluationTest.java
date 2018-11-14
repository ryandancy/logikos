package ca.keal.logikos.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LogicEvaluationTest {
  
  @DisplayName("Input directly connected to output: output should mirror input")
  @ParameterizedTest(name = "Input is {0}: output should be {0}")
  @CsvSource({"true", "false"})
  void inputIntoOutput(boolean value) {
    Input input = new Input();
    Output output = new Output();
    
    output.getInput(0).connectTo(input.getOutput(0));
    input.setValue(value);
    
    assertArrayEquals(new boolean[] {value}, output.evaluate());
  }
  
  @DisplayName("Simple NOT gate circuit")
  @ParameterizedTest(name = "NOT {0} should be {1}")
  @CsvSource({"true, false", "false, true"})
  void notGate(boolean inputValue, boolean outputValue) {
    Input input = new Input();
    NotGate not = new NotGate();
    Output output = new Output();
    
    input.getOutput(0).connectTo(not.getInput(0));
    not.getOutput(0).connectTo(output.getInput(0));
    input.setValue(inputValue);
    
    assertArrayEquals(new boolean[] {outputValue}, output.evaluate());
  }
  
  @DisplayName("Simple AND gate circuit")
  @ParameterizedTest(name = "{0} AND {1} should be {2}")
  @CsvSource({"true, true, true", "true, false, false", "false, true, false", "false, false, false"})
  void andGate(boolean inputVal1, boolean inputVal2, boolean outputVal) {
    Input input1 = new Input();
    Input input2 = new Input();
    AndGate and = new AndGate();
    Output output = new Output();
    
    input1.getOutput(0).connectTo(and.getInput(0));
    input2.getOutput(0).connectTo(and.getInput(1));
    and.getOutput(0).connectTo(output.getInput(0));
    
    input1.setValue(inputVal1);
    input2.setValue(inputVal2);
    
    assertArrayEquals(new boolean[] {outputVal}, output.evaluate());
  }
  
  @DisplayName("Simple OR gate circuit")
  @ParameterizedTest(name = "{0} OR {1} should be {2}")
  @CsvSource({"true, true, true", "true, false, true", "false, true, true", "false, false, false"})
  void orGate(boolean inputVal1, boolean inputVal2, boolean outputVal) {
    Input input1 = new Input();
    Input input2 = new Input();
    OrGate or = new OrGate();
    Output output = new Output();
    
    input1.getOutput(0).connectTo(or.getInput(0));
    input2.getOutput(0).connectTo(or.getInput(1));
    or.getOutput(0).connectTo(output.getInput(0));
    
    input1.setValue(inputVal1);
    input2.setValue(inputVal2);
    
    assertArrayEquals(new boolean[] {outputVal}, output.evaluate());
  }
  
  
  @DisplayName("Simple NAND gate circuit")
  @ParameterizedTest(name = "{0} NAND {1} should be {2}")
  @CsvSource({"true, true, false", "true, false, true", "false, true, true", "false, false, true"})
  void nandGate(boolean inputVal1, boolean inputVal2, boolean outputVal) {
    Input input1 = new Input();
    Input input2 = new Input();
    NandGate nand = new NandGate();
    Output output = new Output();
    
    input1.getOutput(0).connectTo(nand.getInput(0));
    input2.getOutput(0).connectTo(nand.getInput(1));
    nand.getOutput(0).connectTo(output.getInput(0));
    
    input1.setValue(inputVal1);
    input2.setValue(inputVal2);
    
    assertArrayEquals(new boolean[] {outputVal}, output.evaluate());
  }
  
  @DisplayName("XOR gate circuit")
  @ParameterizedTest(name = "{0} XOR {1} = ({0} NAND {1}) AND ({0} OR {1}) should be {2}")
  @CsvSource({"true, true, false", "true, false, true", "false, true, true", "false, false, false"})
  void xorGate(boolean inputVal1, boolean inputVal2, boolean outputVal) {
    Input input1 = new Input();
    Input input2 = new Input();
    NandGate nand = new NandGate();
    OrGate or = new OrGate();
    AndGate and = new AndGate();
    Output output = new Output();
    
    input1.getOutput(0).connectTo(nand.getInput(0));
    input2.getOutput(0).connectTo(nand.getInput(1));
    
    input1.getOutput(0).connectTo(or.getInput(0));
    input2.getOutput(0).connectTo(or.getInput(1));
    
    nand.getOutput(0).connectTo(and.getInput(0));
    or.getOutput(0).connectTo(and.getInput(1));
    
    and.getOutput(0).connectTo(output.getInput(0));
    
    input1.setValue(inputVal1);
    input2.setValue(inputVal2);
    
    assertArrayEquals(new boolean[] {outputVal}, output.evaluate());
  }
  
  @DisplayName("Single-digit binary adder (test multiple outputs)")
  @ParameterizedTest(name = "{0} + {1} = {2}, carry {3}")
  @CsvSource({"false, false, false, false", "true, false, true, false", "false, true, true, false",
                 "true, true, false, true"})
  void binaryAddition(boolean addend1, boolean addend2, boolean sum, boolean carry) {
    // Sum is XOR, carry is AND
    Input input1 = new Input();
    Input input2 = new Input();
    NandGate nand = new NandGate();
    OrGate or = new OrGate();
    AndGate andSum = new AndGate();
    AndGate andCarry = new AndGate();
    Output sumOutput = new Output();
    Output carryOutput = new Output();
    
    input1.getOutput(0).connectTo(nand.getInput(0));
    input2.getOutput(0).connectTo(nand.getInput(1));
    
    input1.getOutput(0).connectTo(or.getInput(0));
    input2.getOutput(0).connectTo(or.getInput(1));
    
    nand.getOutput(0).connectTo(andSum.getInput(0));
    or.getOutput(0).connectTo(andSum.getInput(1));
    
    andSum.getOutput(0).connectTo(sumOutput.getInput(0));
    
    input1.getOutput(0).connectTo(andCarry.getInput(0));
    input2.getOutput(0).connectTo(andCarry.getInput(1));
    
    andCarry.getOutput(0).connectTo(carryOutput.getInput(0));
    
    input1.setValue(addend1);
    input2.setValue(addend2);
    
    assertArrayEquals(new boolean[] {sum}, sumOutput.evaluate());
    assertArrayEquals(new boolean[] {carry}, carryOutput.evaluate());
  }
  
  @DisplayName("Two gates in succession, last should memoize: 1 output")
  @Test
  void memoization2Gates1Output() {
    // Sequence: input -> firstGate -> secondGate -> output
    Input input = new Input();
    NotGate firstGate = spy(NotGate.class);
    NotGate secondGate = spy(NotGate.class);
    Output output = new Output();
    
    input.getOutput(0).connectTo(firstGate.getInput(0));
    firstGate.getOutput(0).connectTo(secondGate.getInput(0));
    secondGate.getOutput(0).connectTo(output.getInput(0));
    
    input.setValue(false);
    output.evaluate();
    output.evaluate();
    
    // Second gate should have memoized the output so first gate was only called once
    verify(firstGate, times(1)).evaluate(null); // this version of evaluate() is called
    verify(secondGate, times(2)).evaluate(null);
  }
  
  @DisplayName("Two gates in succession, last should memoize: 2 outputs")
  @Test
  void memoization2Gates2Outputs() {
    // Sequence: input -> firstGate -> secondGate -> output*2
    Input input = new Input();
    NotGate firstGate = spy(NotGate.class);
    NotGate secondGate = spy(NotGate.class);
    Output output1 = new Output();
    Output output2 = new Output();
    
    input.getOutput(0).connectTo(firstGate.getInput(0));
    firstGate.getOutput(0).connectTo(secondGate.getInput(0));
    secondGate.getOutput(0).connectTo(output1.getInput(0));
    secondGate.getOutput(0).connectTo(output2.getInput(0));
    
    input.setValue(false);
    output1.evaluate();
    output2.evaluate();
    
    // Second gate should have memoized the output so first gate was only called once
    verify(firstGate, times(1)).evaluate(null);
    verify(secondGate, times(2)).evaluate(null);
  }
  
  @DisplayName("Resetting input value voids memoization")
  @Test
  void resetInputVoidsMemoization() {
    // Sequence: input -> firstGate -> secondGate -> output
    Input input = new Input();
    NotGate firstGate = spy(NotGate.class);
    NotGate secondGate = spy(NotGate.class);
    Output output = new Output();
    
    input.getOutput(0).connectTo(firstGate.getInput(0));
    firstGate.getOutput(0).connectTo(secondGate.getInput(0));
    secondGate.getOutput(0).connectTo(output.getInput(0));
    
    input.setValue(false);
    output.evaluate();
    input.setValue(false); // even the same value should void the memoization
    output.evaluate();
    
    // Both gates should have been called both times as they were dirtied with the second setValue()
    verify(firstGate, times(2)).evaluate(null);
    verify(secondGate, times(2)).evaluate(null);
  }
  
  @DisplayName("EvaluationListener is called on evaluation for inputs, gates, and outputs")
  @Test
  void evaluationListenerCalled(@Mock EvaluationListener listener) {
    Input input = new Input();
    NotGate not = new NotGate();
    Output output = new Output();
    
    input.getOutput(0).connectTo(not.getInput(0));
    not.getOutput(0).connectTo(output.getInput(0));
    
    input.setValue(true);
    output.evaluate(listener);
    
    boolean[] emptyArray = new boolean[0];
    boolean[] trueArray = new boolean[] {true};
    boolean[] falseArray = new boolean[] {false};
    
    verify(listener).onEvaluation(new EvaluationListener.Event(input, emptyArray, trueArray));
    verify(listener).onEvaluation(new EvaluationListener.Event(not, trueArray, falseArray));
    verify(listener).onEvaluation(new EvaluationListener.Event(output, falseArray, falseArray));
  }
  
  @DisplayName("EvaluationListener is called in order from bottom to top")
  @Test
  void evaluationListenerOrder(@Mock EvaluationListener listener) {
    // Same as before but in order
    Input input = new Input();
    NotGate not = new NotGate();
    Output output = new Output();
    
    input.getOutput(0).connectTo(not.getInput(0));
    not.getOutput(0).connectTo(output.getInput(0));
    
    input.setValue(true);
    output.evaluate(listener);
    
    boolean[] emptyArray = new boolean[0];
    boolean[] trueArray = new boolean[] {true};
    boolean[] falseArray = new boolean[] {false};
    
    InOrder inOrder = inOrder(listener);
    inOrder.verify(listener).onEvaluation(new EvaluationListener.Event(input, emptyArray, trueArray));
    inOrder.verify(listener).onEvaluation(new EvaluationListener.Event(not, trueArray, falseArray));
    inOrder.verify(listener).onEvaluation(new EvaluationListener.Event(output, falseArray, falseArray));
    inOrder.verifyNoMoreInteractions();
  }
  
  @DisplayName("EvaluationListener is not called for memoized values")
  @Test
  void evaluationListenerMemoization(@Mock EvaluationListener listener) {
    // Input connected to 2 outputs, listener should only be called on input once
    Input input = new Input();
    Output output1 = new Output();
    Output output2 = new Output();
    
    input.getOutput(0).connectTo(output1.getInput(0));
    input.getOutput(0).connectTo(output2.getInput(0));
    
    input.setValue(false);
    output1.evaluate(listener);
    output2.evaluate(listener);
    
    boolean[] values = new boolean[] {false};
    
    InOrder inOrder = inOrder(listener);
    inOrder.verify(listener).onEvaluation(new EvaluationListener.Event(input, new boolean[0], values));
    inOrder.verify(listener).onEvaluation(new EvaluationListener.Event(output1, values, values));
    inOrder.verify(listener).onEvaluation(new EvaluationListener.Event(output2, values, values));
    inOrder.verifyNoMoreInteractions();
  }
  
  @DisplayName("Resetting input value causes EvaluationListener to be called again")
  @Test
  void evaluationListenerMemoizationDirtying(@Mock EvaluationListener listener) {
    // Input connected to 2 outputs, input value reset in between, each should be called twice
    Input input = new Input();
    Output output1 = new Output();
    Output output2 = new Output();
    
    input.getOutput(0).connectTo(output1.getInput(0));
    input.getOutput(0).connectTo(output2.getInput(0));
    
    input.setValue(false);
    output1.evaluate(listener);
    output2.evaluate(listener);
    input.setValue(false);
    output2.evaluate(listener);
    output1.evaluate(listener);
    
    boolean[] values = new boolean[] {false};
    EvaluationListener.Event inputEvent = new EvaluationListener.Event(input, new boolean[0], values);
    EvaluationListener.Event output1Event = new EvaluationListener.Event(output1, values, values);
    EvaluationListener.Event output2Event = new EvaluationListener.Event(output2, values, values);
    
    InOrder inOrder = inOrder(listener);
    inOrder.verify(listener).onEvaluation(inputEvent);
    inOrder.verify(listener).onEvaluation(output1Event);
    inOrder.verify(listener).onEvaluation(output2Event);
    inOrder.verify(listener).onEvaluation(inputEvent);
    inOrder.verify(listener).onEvaluation(output2Event);
    inOrder.verify(listener).onEvaluation(output1Event);
    inOrder.verifyNoMoreInteractions();
  }
  
}