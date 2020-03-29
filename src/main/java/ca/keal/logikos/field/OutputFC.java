package ca.keal.logikos.field;

import ca.keal.logikos.logic.EvaluationListener;
import ca.keal.logikos.logic.Output;
import ca.keal.logikos.util.DeserializationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A {@link FieldComponent} representing specifically an output on the {@link Field}. It encapsulates a {@link Output}
 * and has a {@link OutputFC.Type}, which represents what type of output (e.g. a lamp or a binary "1"/"0") it is.
 */
public class OutputFC extends LabelledFC {
  
  private Type type;
  
  public OutputFC(Output logicComponent, Position initialPosition, Type type) {
    super(logicComponent, initialPosition);
    setType(type);
  }
  
  public void setType(Type type) {
    if (type == null) {
      throw new NullPointerException("OutputFC cannot have null type");
    }
    this.type = type;
  }
  
  public Type getType() {
    return type;
  }
  
  /**
   * Evaluate the underlying {@link Output} using the given {@link EvaluationListener}. This is package-private as it
   * should only ever be called by {@link Field}; the UI layer has no need of it.
   */
  boolean evaluate(EvaluationListener listener) {
    return getLogicComponent().evaluate(listener)[0];
  }
  
  /**
   * The type of output that the corresponding {@link OutputFC} represents. This corresponds to the graphic displayed
   * in the UI: a lit or unlit lamp, a "1" or "0" for "binary", or a red/green/blue pixel.
   */
  public enum Type {
    LAMP, BINARY, RED, GREEN, BLUE
  }
  
  @Override
  public Element toXml(Document doc) {
    Element elem = super.toXml(doc);
    elem.setAttribute("output", "output");
    elem.setAttribute("outputType", getType().name());
    return elem;
  }
  
  /** Perform OutputFC-specific parsing on the element. */
  public static OutputFC fromXml(Element elem, Position pos, Output lc) throws DeserializationException {
    if (!elem.hasAttribute("output")) {
      throw new DeserializationException("Output field component element must have 'output' attribute.");
    }
    if (!elem.hasAttribute("outputType")) {
      throw new DeserializationException("Output field component element must have 'outputType' attribute.");
    }
    
    Type outputType;
    try {
      outputType = Type.valueOf(elem.getAttribute("outputType"));
    } catch (IllegalArgumentException e) {
      throw new DeserializationException("Unknown output type: " + elem.getAttribute("outputType"), e);
    }
    
    OutputFC outputFC = new OutputFC(lc, pos, outputType);
    outputFC.fillInOptionsFromXml(elem);
    return outputFC;
  }
  
}