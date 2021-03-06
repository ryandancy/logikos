package ca.keal.logikos.field;

import ca.keal.logikos.logic.Input;
import ca.keal.logikos.util.DeserializationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A {@link FieldComponent} representing specifically an input on the {@link Field}. It encapsulates an {@link Input}
 * and on top of the normal {@link FieldComponent} behaviour, {@code InputFC} also contains a {@link InputFC.Type},
 * which tells what type of input it is, and a key binding (an integer). The {@link InputFC.Type} tells whether the
 * input is a button, a switch, or something else; the key binding is a key code which is bound to this input, and a
 * negative key binding (the default) means that no key is bound to this component.
 */
public class InputFC extends LabelledFC {
  
  private Type type;
  private int keyBinding = -1;
  
  /**
   * Create a new {@link InputFC}.
   * @param logicComponent The {@link Input} to encapsulate.
   * @param initialPosition The initial position.
   * @param type The {@link Type} of input.
   */
  public InputFC(Input logicComponent, Position initialPosition, Type type) {
    super(logicComponent, initialPosition);
    setType(type);
  }
  
  public void setType(Type type) {
    if (type == null) {
      throw new NullPointerException("InputFC cannot have null type");
    }
    this.type = type;
  }
  
  public Type getType() {
    return type;
  }
  
  public void setKeyBinding(int keyBinding) {
    this.keyBinding = keyBinding;
  }
  
  public void removeKeyBinding() {
    setKeyBinding(-1);
  }
  
  public int getKeyBinding() {
    return keyBinding;
  }
  
  public boolean hasKeyBinding() {
    return getKeyBinding() >= 0;
  }
  
  @Override
  public Input getLogicComponent() {
    return (Input) super.getLogicComponent();
  }
  
  /**
   * Toggle the {@link InputFC}'s {@link Input}'s value.
   */
  public void toggle() {
    getLogicComponent().setValue(!getLogicComponent().getValue());
  }
  
  /**
   * @return The value of this {@link InputFC}'s underlying {@link Input}.
   */
  public boolean getValue() {
    return getLogicComponent().getValue();
  }
  
  /**
   * The type of {@link InputFC}. An {@link InputFC} is either a certain colour of button or a switch. Each type may
   * be either a "press" or "toggle" type, which define the behaviour of the type.
   */
  public enum Type {
    BUTTON_RED(Behaviour.PRESS),
    BUTTON_GREEN(Behaviour.PRESS),
    BUTTON_BLUE(Behaviour.PRESS),
    BUTTON_YELLOW(Behaviour.PRESS),
    SWITCH(Behaviour.TOGGLE),
    BINARY(Behaviour.TOGGLE);
    
    private Behaviour behaviour;
    
    Type(Behaviour behaviour) {
      this.behaviour = behaviour;
    }
    
    public Behaviour getBehaviour() {
      return behaviour;
    }
    
    public enum Behaviour {
      PRESS, TOGGLE
    }
  }
  
  @Override
  public String toString() {
    return "InputFC[type=" + getType() + ", logicComponent=" + getLogicComponent()
        + ", position=" + getPosition() + ", keyBinding=" + getKeyBinding() + "]";
  }
  
  @Override
  public Element toXml(Document doc) {
    Element elem = super.toXml(doc);
    elem.setAttribute("input", "input");
    elem.setAttribute("inputType", getType().name());
    elem.setAttribute("keyBinding", Integer.toString(getKeyBinding()));
    return elem;
  }
  
  /** Perform InputFC-specific parsing on the element. */
  public static InputFC fromXml(Element elem, Position pos, Input lc) throws DeserializationException {
    if (!elem.hasAttribute("input")) {
      throw new DeserializationException("Input field component element must have attribute 'input'.");
    }
    if (!elem.hasAttribute("inputType") || !elem.hasAttribute("keyBinding")) {
      throw new DeserializationException("Input field component element must have attributes " +
          "'inputType' and 'keyBinding'");
    }
    
    Type inputType;
    try {
      inputType = Type.valueOf(elem.getAttribute("inputType"));
    } catch (IllegalArgumentException e) {
      throw new DeserializationException("Unknown input type: " + elem.getAttribute("inputType"), e);
    }
    
    int keyBinding;
    try {
      keyBinding = Integer.parseInt(elem.getAttribute("keyBinding"));
    } catch (NumberFormatException e) {
      throw new DeserializationException("Illegal key binding integer: " + elem.getAttribute("keyBinding"), e);
    }
    
    InputFC inputFC = new InputFC(lc, pos, inputType);
    inputFC.setKeyBinding(keyBinding);
    inputFC.fillInOptionsFromXml(elem);
    return inputFC;
  }
  
}