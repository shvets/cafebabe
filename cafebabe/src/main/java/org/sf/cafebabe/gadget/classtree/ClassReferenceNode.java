// ClassReferenceNode.java

package org.sf.cafebabe.gadget.classtree;

/**
 * This class represents class reference node
 *
 * @version 1.0 02/06/2002
 * @author Alexander Shvets
 */
public class ClassReferenceNode extends ClassTreeNode {
  private short classReference;

  /**
   * Creates new class reference node
   *
   * @param classReference the class reference
   */
  public ClassReferenceNode(short classReference) {
    super("", false);

    this.classReference = classReference;
  }

  /**
   * Gets the class reference
   *
   * @return the class reference
   */
  public short getClassReference() {
    return classReference;
  }

}
