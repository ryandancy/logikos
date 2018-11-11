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
  
}