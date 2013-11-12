// LeafNode.java

package org.sf.cafebabe.gadget.classtree;

/**
 * This class represents leaf node
 *
 * @version 1.0 02/06/2002
 * @author Alexander Shvets
 */
public class LeafNode extends ClassTreeNode {

  /**
   * Creates new leaf node
   *
   * @param text the text that will be displayed on the node
   */
  public LeafNode(String text) {
    super(text, false);
  }

}
