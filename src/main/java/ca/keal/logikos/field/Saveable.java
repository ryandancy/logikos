package ca.keal.logikos.field;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Represents a saveable field object, which may serialize itself to an XML document and populate itself from an XML
 * element. {@code Saveable}s must have public zero-argument constructors with which the document opener may initialize
 * an object to be populated. They must also have public, static, final strings called {@code XML_TAG} whose value are
 * the name of the XML tag in which they are saved; this is done for backwards compatibility with class name changes.
 */
public interface Saveable {
  
  /**
   * Serialize this {@link Saveable} to the {@code serialized} element in {@code dom}.
   */
  void save(Document dom, Element serialized);
  
  /**
   * Populate this {@link Saveable} from the data previously serialized in {@code serialized}.
   */
  void populate(Element serialized);
  
}