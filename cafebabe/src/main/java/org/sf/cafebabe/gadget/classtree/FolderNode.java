// FolderNode.java

package org.sf.cafebabe.gadget.classtree;

/**
 * This class represents folders node
 *
 * @version 1.0 02/06/2002
 * @author Alexander Shvets
 */
public class FolderNode extends ClassTreeNode {

  /**
   * Creates new folder node
   *
   * @param text the text that will be displayed on the node
   */
  public FolderNode(String text) {
    super(text, true);
  }

}
