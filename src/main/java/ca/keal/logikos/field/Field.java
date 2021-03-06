package ca.keal.logikos.field;

import ca.keal.logikos.logic.EvaluationListener;
import ca.keal.logikos.logic.LogicComponent;
import ca.keal.logikos.util.DeserializationException;
import ca.keal.logikos.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * A Field is the container for the entire logic model. It contains {@link FieldComponent}s and handles saving and
 * layout.
 */
public class Field {
  
  private final List<FieldComponent> fieldComponents = new ArrayList<>();
  
  // These lists exist to provide an order for the inputFCs
  // TODO some way to reorder the inputs/outputs
  private final List<InputFC> inputFCs = new ArrayList<>();
  private final List<OutputFC> outputFCs = new ArrayList<>();
  
  private String filename = null;
  private String name = null;
  
  private boolean modified = false;
  
  public List<FieldComponent> getFieldComponents() {
    return Collections.unmodifiableList(fieldComponents);
  }
  
  public List<InputFC> getInputFCs() {
    return Collections.unmodifiableList(inputFCs);
  }
  
  public List<OutputFC> getOutputFCs() {
    return Collections.unmodifiableList(outputFCs);
  }
  
  public void addFieldComponent(FieldComponent component) {
    if (component == null) {
      throw new NullPointerException("A null FieldComponent cannot be added to a field");
    }
    
    fieldComponents.add(component);
    
    if (component instanceof InputFC) {
      inputFCs.add((InputFC) component);
    } else if (component instanceof OutputFC) {
      outputFCs.add((OutputFC) component);
    }
    
    setModified(true);
  }
  
  public void removeFieldComponent(FieldComponent component) {
    fieldComponents.remove(component);
    
    if (component instanceof InputFC) {
      inputFCs.remove(component);
    } else if (component instanceof OutputFC) {
      outputFCs.remove(component);
    }
    
    setModified(true);
  }
  
  public String getFilename() {
    return filename;
  }
  
  public void setFilename(String filename) {
    this.filename = filename;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public boolean isModified() {
    return modified;
  }
  
  public void setModified(boolean modified) {
    this.modified = modified;
  }

  /**
   * Reset the evaluation states of the entire field to false.
   */
  public void reset() {
    for (FieldComponent fc : fieldComponents) {
      fc.getLogicComponent().reset();
    }
  }
  
  /**
   * Tick each gate in the {@link Field} and return the {@link OutputFC}s' output as an array.
   * @param listener An {@link EvaluationListener} to listen to each gate's evaluation. May be {@code null}.
   * @return An array containing the output of each {@link OutputFC} in order. To get the {@link OutputFC} associated
   *  with each result, match each element in the returned array with the corresponding element in
   *  {@link #getOutputFCs()}.
   */
  public boolean[] tick(EvaluationListener listener) {
    // first load all inputs into the components
    for (FieldComponent fc : fieldComponents) {
      fc.getLogicComponent().updateInputs();
    }
    
    // then tick each of them
    for (FieldComponent fc : fieldComponents) {
      fc.getLogicComponent().tick(listener);
    }
    
    // then load all the outputs
    for (FieldComponent fc : fieldComponents) {
      fc.getLogicComponent().updateOutputs();
    }
    
    // then assemble the array of output FC return values
    boolean[] outputs = new boolean[outputFCs.size()];
    for (int i = 0; i < outputFCs.size(); i++) {
      outputs[i] = outputFCs.get(i).getOutput();
    }
    return outputs;
  }
  
  /**
   * Serialize the entire {@link Field} to an XML element.
   * @return An {@link Element} from which the field can be recovered.
   */
  public Element toXml(Document doc) {
    Element root = doc.createElement("field");
    root.setAttribute("name", getName());
    for (FieldComponent fc : fieldComponents) {
      root.appendChild(fc.toXml(doc));
    }
    return root;
  }

  /**
   * Deserialize an XML element generated by {@link #toXml(Document)} into a {@link Field}.
   */
  public static Field fromXml(Element elem, String filename) throws DeserializationException {
    if (!elem.getTagName().equals("field")) {
      throw new DeserializationException("Root tag must be <field>.");
    }
    if (!elem.hasAttribute("name")) {
      throw new DeserializationException("Field has no name attribute.");
    }
    
    Field field = new Field();
    field.setName(elem.getAttribute("name"));
    
    // first do everything except hooking up the ports, which requires a list of field components
    List<Element> fcElements = XmlUtil.getDirectChildrenByTagName(elem, "fieldComponent");
    List<FieldComponent> fcs = new ArrayList<>(); // keep track on our own to make sure it's ordered
    for (Element fcElem : fcElements) {
      FieldComponent fc = FieldComponent.instantiateFromXml(fcElem, filename);
      fcs.add(fc);
      field.addFieldComponent(fc);
    }
    
    // build the map of UUIDs to logic components
    Map<UUID, LogicComponent> uuidToLC = new HashMap<>();
    for (FieldComponent fc : field.getFieldComponents()) {
      uuidToLC.put(fc.getLogicComponent().getId(), fc.getLogicComponent());
    }
    
    // fill in all the ports
    for (int i = 0; i < fcs.size(); i++) {
      fcs.get(i).fillInPortsFromXml(uuidToLC, fcElements.get(i));
    }
    
    return field;
  }
  
  public static Field fromXml(String filename) throws DeserializationException, IOException {
    try {
      DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = builder.parse(new File(filename));
      Element root = doc.getDocumentElement();
      
      Field field = fromXml(root, filename);
      field.setFilename(filename);
      return field;
    } catch (ParserConfigurationException e) {
      // shouldn't happen
      throw new RuntimeException(e);
    } catch (SAXException e) {
      throw new DeserializationException("Error reading XML", e);
    } catch (FileNotFoundException e) {
      throw new DeserializationException("Could not find file '" + filename + "'", e);
    }
  }
  
}