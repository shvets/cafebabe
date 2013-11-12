// EntryNode.java

package org.sf.cafebabe.gadget.classtree;

import org.sf.classfile.Entry;

/**
 * This class represents entry node
 *
 * @version 1.0 02/06/2002
 * @author Alexander Shvets
 */
public class EntryNode extends ClassTreeNode {
  private Entry entry;

  /**
   * Creates new entry node
   *
   * @param allowsChildren flag to allow children
   * @param entry the entry
   */
  public EntryNode(boolean allowsChildren, Entry entry) {
    super("", allowsChildren);

    this.entry = entry;
  }

  /**
   * Gets the entry
   *
   * @return the entry
   */
  public Entry getEntry() {
    return entry;
  }

}
