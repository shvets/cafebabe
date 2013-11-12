// AttributeNode.java

package org.sf.cafebabe.gadget.classtree;

import org.sf.classfile.AttributeEntry;

/**
 * This class represents attribute node
 *
 * @version 1.0 02/06/2002
 * @author Alexander Shvets
 */
public class AttributeNode extends FolderNode {
  private AttributeEntry attributeEntry;

  /**
   * Creates new attribute node
   *
   * @param text the text that will be displayed on the node
   * @param attributeEntry the attribute
   */
  public AttributeNode(String text, AttributeEntry attributeEntry) {
    super(text);

    this.attributeEntry = attributeEntry;
  }

  /**
   * Gets the attribute
   *
   * @return the attribute
   */
  public AttributeEntry getAttributeEntry() {
    return attributeEntry;
  }

}
