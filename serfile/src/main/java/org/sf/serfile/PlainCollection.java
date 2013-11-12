// PlainEntryCollection.java

package org.sf.serfile;

import java.io.DataOutput;
import java.io.IOException;

/**
 * Class-container that can hold entries. Size of collection is unknown at
 * the time of reading.
 *
 * @version 1.0 05/25/2000
 * @author Alexander Shvets
 */
public class PlainCollection extends EntryCollection {

  /** Table that contains all handles to allocated elements of serialized file */
  protected HandlesTable handlesTable;

  /**
   * Creates a collection that can hold only elements of specified type.
   *
   * @param clazz  the type of allowed in this collection class
   * @param handlesTable  table of handles
   */
  public PlainCollection(Class clazz, String type, HandlesTable handlesTable) {
    super(clazz, type);

    this.handlesTable = handlesTable;
  }

  /**
   * Writes this collection to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    short cnt = (short)entries.size();

    for(int i=0; i < cnt; i++) {
      ((Entry)entries.get(i)).write(out);
    }
  }

  /**
   * Gets the length of collection in bytes
   *
   * @return  the length of collection in bytes
   */
  public long length() {
    int len = 0;
    for(int i=0; i < entries.size(); i++) {
      len += ((Entry)entries.get(i)).length();
    }

    return len;
  }

}
