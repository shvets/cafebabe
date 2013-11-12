// ClassNode.java

package org.sf.cafebabe.gadget.treecombo;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * This class represents class node
 *
 * @version 1.0 03/04/2002
 * @author Alexander Shvets
 */
public class ClassNode extends DefaultMutableTreeNode {

  /**
   * Creates new class node
   *
   * @param userObject  the user object
   */
  public ClassNode(Object userObject) {
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
