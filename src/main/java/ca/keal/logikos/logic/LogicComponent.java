package ca.keal.logikos.logic;

import ca.keal.logikos.util.DeserializationException;
import ca.keal.logikos.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

/**
 * Represents a logical component, a blanket term for a gate, input, or output. This abstract base class contains
 * {@link Connection}s representing inputs and outputs and has logic for evaluating chains of logical components.
 */
public abstract class LogicComponent {
  
  private final Port.Input[] inputs;
  private final Port.Output[] outputs;
  
  private UUID id = UUID.randomUUID();
  
  /**
   * Initialize the LogicComponent with {@code numInputs} inputs {@link Port.Input}s and {@code numOutputs} output
   * {@link Port.Output}s.
   * @param numInputs The number of inputs this LogicComponent has.
   * @param numOutputs The number of outputs this LogicComponent has.
   */
  protected LogicComponent(int numInputs, int numOutputs) {
    inputs = getPortArray(numInputs, Port.Input::new, Port.Input[]::new);
    outputs = getPortArray(numOutputs, Port.Output::new, Port.Output[]::new);
  }
  
  // Helper to fill an array with Ports with port numbers and array indices aligned
  private <P extends Port> P[] getPortArray(
      int numPorts,
      BiFunction<Integer, LogicComponent, P> portConstructor,
      IntFunction<P[]> arrayConstructor) {
    return IntStream.range(0, numPorts)
        .mapToObj(portNum -> portConstructor.apply(portNum, this))
        .toArray(arrayConstructor);
  }
  
  public UUID getId() {
    return id;
  }
  
  public int getNumInputs() {
    return inputs.length;
  }
  
  public Port.Input getInput(int num) {
    return inputs[num];
  }
  
  public Port.Input[] getInputs() {
    return inputs;
  }
  
  public int getNumOutputs() {
    return outputs.length;
  }
  
  public Port.Output getOutput(int num) {
    return outputs[num];
  }
  
  public Port.Output[] getOutputs() {
    return outputs;
  }
  
  /**
   * Find the output values for this LogicComponent.
   * @param listener An {@link EvaluationListener} to be called on each successful evaluation. This may be {@code null}
   *  if no listener is desired. Note that the listener will be called from bottom-to-top (from the earliest- to the
   *  latest-updated component) as that is the order in which evaluations finish.
   * @return This {@link LogicComponent}'s output values for its current input values.
   */
  public abstract boolean[] evaluate(EvaluationListener listener);
  
  /**
   * @see #evaluate(EvaluationListener)
   */
  public boolean[] evaluate() {
    return evaluate(null);
  }
  
  /** @see Gate#markDirty() */
  public void markDirty() {}
  
  /** @see Gate#isDirty() */
  public boolean isDirty() {
    return false;
  }
  
  @Override
  public String toString() {
    return getName() + "[id=" + getId() + "]";
  }
  
  public abstract String getName();
  
  /** To be used when deserializing. */
  void setId(UUID id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || !getClass().equals(obj.getClass())) return false;
    LogicComponent component = (LogicComponent) obj;
    return getId().equals(component.getId());
  }
  
  @Override
  public int hashCode() {
    return getId().hashCode();
  }

  /**
   * Serialize this LogicComponent to XML.
   */
  public Element toXml(Document doc) {
    Element elem = doc.createElement("logicComponent");
    elem.setAttribute("uuid", getId().toString());
    elem.setAttribute("type", getName()); // TODO use a separate field for serialization type than name
    
    // We only serialize the inputs because they're sufficient to rebuild all the connections
    for (Port.Input port : getInputs()) {
      elem.appendChild(port.toXml(doc));
    }
    
    return elem;
  }
  
  /**
   * Deserialize the XML element to a LogicComponent, but do not fill in ports yet. That must be done with a call
   * to {@link #fillInPortsFromXml(Map, Element)} afterwards.
   */
  public static LogicComponent fromXml(Element elem) throws DeserializationException {
    if (!elem.getTagName().equals("logicComponent")) {
      throw new DeserializationException("Logic components must have tag <logicComponent>.");
    }
    if (!elem.hasAttribute("uuid") || !elem.hasAttribute("type")) {
      throw new DeserializationException("Logic component elements must have 'uuid' and 'type' attributes.");
    }

    UUID uuid = UUID.fromString(elem.getAttribute("uuid"));
    String type = elem.getAttribute("type");

    // TODO store types in constants, y'know, like a responsible programmer, or even in a map, or something, please
    LogicComponent lc;
    switch (type) {
      case "AND":
        lc = new AndGate();
        break;
      case "OR":
        lc = new OrGate();
        break;
      case "NOT":
        lc = new NotGate();
        break;
      case "NAND":
        lc = new NandGate();
        break;
      case "INPUT":
        lc = new Input();
        break;
      case "OUTPUT":
        lc = new Output();
        break;
      case "USER":
        lc = UserGate.fromXml(elem);
        break;
      default:
        throw new DeserializationException("Unrecognized logic component type: " + type);
    }

    lc.setId(uuid);
    return lc;
  }

  /**
   * Fill in the ports and connections from XML, using the map to connect to LogicComponents based on UUID.
   */
  public void fillInPortsFromXml(Map<UUID, LogicComponent> uuidToLC, Element elem) throws DeserializationException {
    List<Element> portElements = XmlUtil.getDirectChildrenByTagName(elem, "inputPort");
    if (portElements.size() != getNumInputs()) {
      throw new DeserializationException("Wrong number of inputs specified to LogicComponent with name '" + getName()
          + "': expected " + getNumInputs() + ", found " + portElements.size());
    }
    
    for (int i = 0; i < getNumInputs(); i++) {
      getInput(i).populateFromXml(uuidToLC, portElements.get(i));
    }
  }
  
}