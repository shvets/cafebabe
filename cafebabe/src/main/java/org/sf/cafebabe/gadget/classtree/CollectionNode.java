// CollectionNode.java

package org.sf.cafebabe.gadget.classtree;

import org.sf.classfile.EntryCollection;

/**
 * This class represents collection node
 *
 * @version 1.0 02/06/2002
 * @author Alexander Shvets
 */
public class CollectionNode extends FolderNode {
  private EntryCollection entries;

  /**
   * Creates new entries collection node
   *
   * @param text the text that will be displayed on the node
   * @param entries the entries collection
   */
  public CollectionNode(String text, EntryCollection entries) {
    super(text);

    this.entries = entries;
  }

  /**
   * Gets the entries collection
   *
   * @return the entries collection
   */
  public EntryCollection getEntries() {
    return entries;
  }

}
