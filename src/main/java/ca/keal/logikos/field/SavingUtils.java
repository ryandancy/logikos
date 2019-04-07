package ca.keal.logikos.field;

import org.w3c.dom.Element;

/**
 * Contains various utilities for working with {@link Saveable}s.
 */
public final class SavingUtils {
  
  private SavingUtils() {}
  
  /**
   * Generate (i.e. create and populate) a {@link Saveable} of the specific type from XML. This assumes there is only
   * one of the tag under the parent element.
   * @param saveableClass The class of the {@link Saveable} to be generated.
   * @param parent The parent element of the {@link Saveable}'s tag.
   * @param <S> The type of {@link Saveable} to be generated.
   * @return The generated {@link Saveable} object.
   */
  public static <S extends Saveable> S generate(Class<S> saveableClass, Element parent) {
    // Get the tag name from the class' static field
    String tagName;
    try {
      java.lang.reflect.Field tagNameField = saveableClass.getDeclaredField("XML_TAG");
      tagName = (String) tagNameField.get(null);
    } catch (NoSuchFieldException | IllegalAccessException | ClassCastException e) {
      throw new RuntimeException("Saveable class " + saveableClass.getName() + " violated contract: no "
          + "public static final String XML_TAG field!");
    }
    
    // Get the element from the parent
    Element element = (Element) parent.getElementsByTagName(tagName).item(0);
    if (element == null) {
      throw new MalformedSaveException("No <" + tagName + "> tag in <" + parent.getTagName() + "> element.");
    }
    
    // Instantiate the Saveable
    S generated;
    try {
      generated = saveableClass.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Saveable class " + saveableClass.getName() + " violated contract: no "
        + "public zero-argument constructor!");
    }
    
    // Populate it and return
    generated.populate(element);
    return generated;
  }
  
}