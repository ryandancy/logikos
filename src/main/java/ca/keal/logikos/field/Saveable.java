package ca.keal.logikos.field;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Represents a saveable field object, which may serialize itself to an XML document and populate itself from an XML
 * element. {@code Saveable}s must have zero-argument constructors with which the document opener may initialize an
 * object to be populated. The XML element representing this Saveable will have a tag name equal to that returned by
 * {@link #getElementName()}.
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
  
  /**
   * Return the name of the XML tag that will represent this field object. For backwards compatibility with future
   * class name changes. This must be a valid XML tag name and must be unique. 
   */
  String getElementName();
  
}