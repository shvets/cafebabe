// MethodBodyNode.java

package org.sf.cafebabe.gadget.classtree;

import org.sf.classfile.attribute.MethodBody;

/**
 * This class represents method body node
 *
 * @version 1.0 02/06/2002
 * @author Alexander Shvets
 */
public class MethodBodyNode extends FolderNode {
  private MethodBody methodBody;

  /**
   * Creates new method body node
   *
   * @param text the text that will be displayed on the node
   * @param methodBody the method body
   */
  public MethodBodyNode(String text, MethodBody methodBody) {
    super(text);

    this.methodBody = methodBody;
  }

  /**
   * Gets the method body
   *
   * @return the method body
   */
  public MethodBody getMethodBody() {
    return methodBody;
  }

}
