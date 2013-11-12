// Fields.java

package org.sf.serfile;

import java.io.DataInput;
import java.io.IOException;

/*
 * Annotation: TC_ENDBLOCKDATA | (contents endBlockData)
 * contents - "written by annotateClass or writeObject or
               writeExternal PROTOCOL_VERSION_2"
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public class Fields extends EntryCollection {

  /** Table that contains all handles to allocated elements of serialized file */
  private HandlesTable handlesTable;

  /**
   * Creates a collection that can hold field entries.
   *
   * @param handlesTable  table of handles
   */
  public Fields(HandlesTable handlesTable) {
    super(FieldEntry.class, "Fields");

    this.handlesTable = handlesTable;
  }

  /**
   * Creates an entry for this collection with the predefined type.
   *
   * @param  in  input stream.
   * @return an entry with the specified type
   */
  protected Entry createEntry(DataInput in) throws IOException {
    FieldEntry entry = new FieldEntry(handlesTable);
    entry.read(in);

    return entry;
  }

}
