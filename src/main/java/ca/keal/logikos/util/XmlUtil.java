package ca.keal.logikos.util;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Utilities for assisting in parsing XML.
 */
public final class XmlUtil {

  /**
   * Get a list of all of {@code parent}'s direct children that have tag name {@code tagName}.
   */
  public static List<Element> getDirectChildrenByTagName(Element parent, String tagName) {
    List<Element> children = new ArrayList<>();
    NodeList nodes = parent.getChildNodes();
    for (int i = 0; i < nodes.getLength(); i++) {
      Node node = nodes.item(i);
      if (node instanceof Element && ((Element) node).getTagName().equals(tagName)) {
        children.add((Element) node);
      }
    }
    return children;
  }

  /**
   * Get {@code parent}'s single direct child with tag name {@code tagName}, or throw {@link DeserializationException}
   * if there isn't exactly one.
   */
  public static Element getDirectChildByTagName(Element parent, String tagName) throws DeserializationException {
    List<Element> children = getDirectChildrenByTagName(parent, tagName);
    if (children.size() != 1) {
      throw new DeserializationException("Tried to find exactly one <" + tagName + "> tag in <" + parent.getTagName()
          + ">, but there were " + children.size() + "!");
    }
    return children.get(0);
  }
  
}
