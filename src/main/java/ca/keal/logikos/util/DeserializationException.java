package ca.keal.logikos.util;

/**
 * An exception thrown when attempts to recover an object (e.g. field, logic component) from XML fails.
 */
public class DeserializationException extends Exception {
  
  public DeserializationException() {}
  
  public DeserializationException(String msg) {
    super(msg);
  }
  
  public DeserializationException(Throwable cause) {
    super(cause);
  }
  
  public DeserializationException(String msg, Throwable cause) {
    super(msg, cause);
  }
  
}