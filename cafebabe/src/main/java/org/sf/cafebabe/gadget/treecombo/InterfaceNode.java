// InterfaceNode.java

package org.sf.cafebabe.gadget.treecombo;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * This class represents interface node
 *
 * @version 1.0 03/04/2002
 * @author Alexander Shvets
 */
public class InterfaceNode extends DefaultMutableTreeNode {

  /**
   * Creates new interface node
   *
   * @param userObject  the user object
   */
  public InterfaceNode(Object userObject) {
    super(userObject, true);
  }

  /**
   * Sets the text
   *
   * @param text  the text
   */
  public void setText(String text) {
    setUserObject(text);
  }

}
