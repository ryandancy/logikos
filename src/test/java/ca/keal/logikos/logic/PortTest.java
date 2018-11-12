package ca.keal.logikos.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PortTest {
  
  @DisplayName("Port.Input.connectTo() establishes a Connection")
  @Test
  void inputToOutputGivesConnection() {
    Port.Input inputPort = new Port.Input(0, new Output());
    Port.Output outputPort = new Port.Output(0, new Input());
    
    inputPort.connectTo(outputPort);
    
    assertNotNull(inputPort.getConnection());
    assertEquals(outputPort.getConnections().size(), 1);
    assertTrue(outputPort.getConnections().contains(inputPort.getConnection()));
  }
  
  @DisplayName("Port.Output.connectTo() establishes a Connection")
  @Test
  void outputToInputGivesConnection() {
    Port.Input inputPort = new Port.Input(0, new Output());
    Port.Output outputPort = new Port.Output(0, new Input());
    
    outputPort.connectTo(inputPort);
    
    assertNotNull(inputPort.getConnection());
    assertEquals(outputPort.getConnections().size(), 1);
    assertTrue(outputPort.getConnections().contains(inputPort.getConnection()));
  }
  
  
  @DisplayName("Output port connected to multiple input ports works")
  @Test
  void outputToMultipleInputs() {
    Port.Output outputPort = new Port.Output(0, new Input());
    Port.Input inputPort1 = new Port.Input(0, new Output());
    Port.Input inputPort2 = new Port.Input(0, new Output());
    
    outputPort.connectTo(inputPort1);
    outputPort.connectTo(inputPort2);
    
    assertNotNull(inputPort1.getConnection());
    assertNotNull(inputPort2.getConnection());
    assertEquals(outputPort.getConnections().size(), 2);
    assertTrue(outputPort.getConnections().contains(inputPort1.getConnection()));
    assertTrue(outputPort.getConnections().contains(inputPort2.getConnection()));
  }
  
  @DisplayName("Reassigning ports assigns connections appropriately")
  @Test
  void reassignInputs() {
    Port.Output outputPort1 = new Port.Output(1, new Input());
    Port.Output outputPort2 = new Port.Output(2, new Input());
    Port.Input inputPort1 = new Port.Input(1, new Output());
    Port.Input inputPort2 = new Port.Input(2, new Output());
    
    outputPort1.connectTo(inputPort1);
    outputPort1.connectTo(inputPort2);
    
    outputPort2.connectTo(inputPort1);
    outputPort2.connectTo(inputPort2);
    
    outputPort1.connectTo(inputPort1);
    
    // Final state should be outputPort1 -- inputPort1, outputPort2 -- inputPort2
    assertNotNull(inputPort1.getConnection());
    assertNotNull(inputPort2.getConnection());
    assertEquals(1, outputPort1.getConnections().size());
    assertEquals(1, outputPort2.getConnections().size());
    assertTrue(outputPort1.getConnections().contains(inputPort1.getConnection()));
    assertTrue(outputPort2.getConnections().contains(inputPort2.getConnection()));
  }
  
}