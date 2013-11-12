// ShortNode.java

package org.sf.cafebabe.gadget.classtree;

/**
 * This class represents the node with the short value
 *
 * @version 1.0 02/06/2002
 * @author Alexander Shvets
 */
public class ShortNode extends ClassTreeNode {
  private short value;

  /**
   * Creates new attribute node
   *
   * @param text the text that will be displayed on the node
   * @param value the short value
   */
  public ShortNode(String text, short value) {
    super(text, false);

    this.value = value;
  }

  /**
   * Gets the short value
   *
   * @return the short value
   */
  public short getValue() {
    return value;
  }

}
