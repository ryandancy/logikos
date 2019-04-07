package ca.keal.logikos.field;

/**
 * An exception which is thrown by {@link SavingUtils} and others when a save file is malformed or cannot be read.
 */
public class MalformedSaveException extends RuntimeException {
  
  public MalformedSaveException(String message) {
    super(message);
  }
  
}