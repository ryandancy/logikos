package ca.keal.logikos.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class LogicEvaluationTest {
  
  @DisplayName("Input directly connected to output: output should mirror input")
  @ParameterizedTest(name = "Input is {0}: output should be {0}")
  @CsvSource({"true", "false"})
  void inputIntoOutput(boolean value) {
    Input input = new Input();
    Output output = new Output();
    
    output.getInput(0).connectTo(input.getOutput(0));
    input.setValue(value);
    
    assertArrayEquals(output.evaluate(), new boolean[] {value});
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
    
    assertArrayEquals(output.evaluate(), new boolean[] {outputValue});
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
    
    assertArrayEquals(output.evaluate(), new boolean[] {outputVal});
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
    
    assertArrayEquals(output.evaluate(), new boolean[] {outputVal});
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
    
    assertArrayEquals(output.evaluate(), new boolean[] {outputVal});
  }
  
}