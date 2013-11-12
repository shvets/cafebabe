// EndBlockDataEntry.java

package org.sf.serfile;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

/**
 * Class that represents a serialized end block data entry.
 *
 * @version 1.0 05/25/2000
 * @author Alexander Shvets
 */
public class EndBlockDataEntry extends SerEntry {

  /**
   * Reads this entry from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException  {}

  /**
   * Writes this entry to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    out.writeByte(getTag());
  }

  /**
   * Gets the length of this entry in bytes.
   *
   * @return  the length of entry
   */
  public long length() {
    return 1;
  }

  /**
   * Gets the type of this entry.
   *
   * @return  the type of entry
   */
  public String getType() {
    return "End Block Data";
  }

  /**
   * Gets the tag of this entry.
   *
   * @return  the tag for entry
   */
  public byte getTag() {
    return TC_ENDBLOCKDATA;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return  a string representation of the object.
   */
  public String toString() {
    return getType() + "(" + ")";
  }

}
