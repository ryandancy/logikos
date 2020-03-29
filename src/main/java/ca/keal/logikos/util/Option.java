package ca.keal.logikos.util;

import java.util.function.Function;

/**
 * A user-settable option.
 */
public class Option<T> {
  
  private final String text;
  private final Function<String, Boolean> validator;
  private final Function<String, T> transformer;
  
  private T value;
  
  /**
   * Create a new option. Note that the value of the option is displayed as its representation via {@link #toString}.
   * @param text The text displayed beside the option.
   * @param validator A function which returns whether the user's input is valid.
   * @param transformer The function used to transform the user's input into a useful type.
   * @param defaultValue The default value of the option.
   */
  public Option(String text, Function<String, Boolean> validator, Function<String, T> transformer, T defaultValue) {
    this.text = text;
    this.validator = validator;
    this.transformer = transformer;
    this.value = defaultValue;
  }

  /**
   * Attempt to set the value of this option to {@code input} and return whether it was successful.
   */
  public boolean setValue(String input) {
    if (!validator.apply(input)) return false;
    value = transformer.apply(input);
    return true;
  }
  
  public T getValue() {
    return value;
  }
  
  public String getText() {
    return text;
  }
  
}
