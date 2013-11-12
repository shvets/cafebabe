// Contents.java

package org.sf.serfile;

import java.io.IOException;
import java.io.EOFException;
import java.io.DataInput;

/**
 * Class that represents contents of serialized file.
 *
 * contents: content | (contents content)
 *
 * @version 1.0 05/25/2000
 * @author Alexander Shvets
 */
public class Contents extends PlainCollection {

  /**
   * Creates a collection of contents for serialized file.
   *
   * @param handlesTable  table of handles
   */
  public Contents(HandlesTable handlesTable) {
    super(ContentEntry.class, "Contents", handlesTable);
  }

  /**
   * Reads this collection from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException {
    handlesTable.clear();

    while(true) {
      byte tag = -1;

      try {
        tag = in.readByte();
      }
      catch(EOFException e) {
        break;
      }

      if(tag == -1) {
        break;
      }

      ContentEntry entry = new ContentEntry(tag, handlesTable);

      entry.read(in);

      entries.add(entry);
    }
  }

}
