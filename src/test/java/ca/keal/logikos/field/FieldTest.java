package ca.keal.logikos.field;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class FieldTest {
  
  @Test
  @DisplayName("Field filters zero InputFCs correctly")
  void noInputFCFilter(@Mock FieldComponent fc) {
    Field field = new Field();
    field.addFieldComponent(fc);
    assertEquals(0, field.getInputFCs().size());
  }
  
  @Test
  @DisplayName("Field filters a single InputFC correctly")
  void inputFCFilter(@Mock InputFC inputFC, @Mock FieldComponent fc) {
    Field field = new Field();
    field.addFieldComponent(fc);
    field.addFieldComponent(inputFC);
  
    List<InputFC> filtered = field.getInputFCs();
    assertEquals(1, filtered.size());
    assertEquals(inputFC, filtered.get(0));
  }
  
  @Test
  @DisplayName("Field filters two InputFCs correctly")
  void twoInputFCFilter(@Mock InputFC inputFC1, @Mock InputFC inputFC2, @Mock FieldComponent fc) {
    Field field = new Field();
    field.addFieldComponent(fc);
    field.addFieldComponent(inputFC1);
    field.addFieldComponent(inputFC2);
    
    List<InputFC> filtered = field.getInputFCs();
    assertEquals(2, filtered.size());
    assertTrue(filtered.contains(inputFC1));
    assertTrue(filtered.contains(inputFC2));
  }
  
  @Test
  @DisplayName("Field filters zero OutputFCs correctly")
  void noOutputFCFilter(@Mock FieldComponent fc) {
    Field field = new Field();
    field.addFieldComponent(fc);
    assertEquals(0, field.getOutputFCs().size());
  }
  
  @Test
  @DisplayName("Field filters a single OutputFC correctly")
  void outputFCFilter(@Mock OutputFC outputFC, @Mock FieldComponent fc) {
    Field field = new Field();
    field.addFieldComponent(fc);
    field.addFieldComponent(outputFC);
    
    List<OutputFC> filtered = field.getOutputFCs();
    assertEquals(1, filtered.size());
    assertEquals(outputFC, filtered.get(0));
  }
  
  @Test
  @DisplayName("Field filters two OutputFCs correctly")
  void twoOutputFCFilter(@Mock OutputFC outputFC1, @Mock OutputFC outputFC2, @Mock FieldComponent fc) {
    Field field = new Field();
    field.addFieldComponent(fc);
    field.addFieldComponent(outputFC1);
    field.addFieldComponent(outputFC2);
    
    List<OutputFC> filtered = field.getOutputFCs();
    assertEquals(2, filtered.size());
    assertTrue(filtered.contains(outputFC1));
    assertTrue(filtered.contains(outputFC2));
  }
  
  @Test
  @DisplayName("Empty field gives no filtered InputFCs or OutputFCs")
  void emptyFieldFilter() {
    Field field = new Field();
    assertEquals(0, field.getFieldComponents().size());
    assertEquals(0, field.getInputFCs().size());
    assertEquals(0, field.getOutputFCs().size());
  }
  
  @Test
  @DisplayName("Both InputFCs and OutputFCs are filtered correctly simultaneously")
  void bothFilter(
      @Mock InputFC inputFC1, @Mock InputFC inputFC2,
      @Mock OutputFC outputFC1, @Mock OutputFC outputFC2,
      @Mock FieldComponent fc1, @Mock FieldComponent fc2) {
    Field field = new Field();
    field.addFieldComponent(inputFC1);
    field.addFieldComponent(outputFC1);
    field.addFieldComponent(fc1);
    field.addFieldComponent(inputFC2);
    field.addFieldComponent(outputFC2);
    field.addFieldComponent(fc2);
    
    List<InputFC> inputFiltered = field.getInputFCs();
    List<OutputFC> outputFiltered = field.getOutputFCs();
    
    assertEquals(2, inputFiltered.size());
    assertTrue(inputFiltered.contains(inputFC1));
    assertTrue(inputFiltered.contains(inputFC2));
    
    assertEquals(2, outputFiltered.size());
    assertTrue(outputFiltered.contains(outputFC1));
    assertTrue(outputFiltered.contains(outputFC2));
  }
  
}