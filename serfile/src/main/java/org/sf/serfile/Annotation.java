// Annotation.java

package org.sf.serfile;

import java.io.IOException;
import java.io.DataInput;

/**
 * This class is used for holding class or object annotation.
 *
 * Annotation: TC_ENDBLOCKDATA | (contents endBlockData)
 * contents - "written by annotateClass or writeObject or
 *             writeExternal PROTOCOL_VERSION_2"
 *
 * @version 1.0 05/25/2000
 * @author Alexander Shvets
 */
public class Annotation extends PlainCollection {

  /**
   * Creates an annotation that can hold only elements of generic type.
   *
   * @param handlesTable  table of handles
   */
  public Annotation(String type, HandlesTable handlesTable) {
    super(Entry.class, type, handlesTable);
  }

  /**
   * Reads the annotation from input stream.
   *
   * @param  in  the input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException {
    while(true) {
      byte tag = in.readByte();

      ContentEntry entry = new ContentEntry(tag, handlesTable);
      entry.read(in);

      entries.add(entry);

      if(tag == SerConstants.TC_ENDBLOCKDATA) {
        break;
      }
    }
  }

}
