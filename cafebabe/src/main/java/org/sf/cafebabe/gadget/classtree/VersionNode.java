// VersionNode.java

package org.sf.cafebabe.gadget.classtree;

/**
 * This class represents version node
 *
 * @version 1.0 02/06/2002
 * @author Alexander Shvets
 */
public class VersionNode extends ClassTreeNode {
  private short version;

  /**
   * Creates new version node
   *
   * @param text the text that will be displayed on the node
   * @param version the version number
   */
  public VersionNode(String text, short version) {
    super(text, false);

    this.version = version;
  }

  /**
   * Gets the version number
   *
   * @return the version number
   */
  public short getVersion() {
    return version;
  }

}
