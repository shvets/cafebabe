// ClassTreeNode.java

package org.sf.cafebabe.gadget.classtree;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * This class represents the basic class for node of class tree
 *
 * @version 1.0 02/05/2002
 * @author Alexander Shvets
 */
public class ClassTreeNode extends DefaultMutableTreeNode {
  private boolean isCorrect = true;
  private boolean isChanged = false;

  /**
   * Creates new class tree node
   *
   * @param text the text that will be displayed on the node
   * @param allowsChildren specifies whether children are allowed
   *                        for this node
   */
  public ClassTreeNode(String text, boolean allowsChildren) {
    super(text, allowsChildren);
  }

  /**
   * Checks if this node was changed
   *
   * @return true if this node was changedl false otherwise
   */
  public boolean isChanged() {
    return isChanged;
  }

  /**
   * Sets the flag of changes
   *
   * @param isChanged the flag of changes
   */
  public void setChanged(boolean isChanged) {
    this.isChanged = isChanged;
  }

  /**
   * Checks if this node corresponds to a correct class file entry
   *
   * @return true if this node corresponds to
   *         a correct class file entry;false otherwise
   */
  public boolean isCorrect() {
    return isCorrect;
  }

  /**
   * Marks this node as correct
   *
   * @param isCorrect the flag of correctness for this node
   */
  public void setCorrect(boolean isCorrect) {
    this.isCorrect = isCorrect;
  }

}
