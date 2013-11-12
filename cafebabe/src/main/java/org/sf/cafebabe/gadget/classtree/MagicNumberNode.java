// MagicNumberNode.java

package org.sf.cafebabe.gadget.classtree;



/**
 * This class represents magic number node
 *
 * @version 1.0 02/06/2002
 * @author Alexander Shvets
 */
public class MagicNumberNode extends ClassTreeNode {
  private int magicNumber;

  /**
   * Creates new magic number node
   *
   * @param text the text that will be displayed on the node
   * @param magicNumber the magic number
   */
  public MagicNumberNode(String text, int magicNumber) {
    super(text, false);

    this.magicNumber = magicNumber;
  }

  /**
   * Gets the magic number
   *
   * @return the magic number
   */
  public int getMagicNumber() {
    return magicNumber;
  }

}
