// LongStringSerEntry.java

package org.sf.serfile;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

/**
 * Class that represents a serialized long string entry.
 *
 * @version 1.0 05/25/2000
 * @author Alexander Shvets
 */
public class LongStringSerEntry extends HandleSerEntry {

  /** byte array value */
  private byte[] value;

  /**
   * The default constructor that creates serialized long string entry
   *
   * @param handlesTable  table of handles
   */
  public LongStringSerEntry(HandlesTable handlesTable) {
    super(handlesTable);
  }

  /**
   * Reads this entry from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException  {
    long size = in.readLong();

    value = new byte[(int)size];
    in.readFully(value);

    handle = handlesTable.put(this);
  }

  /**
   * Writes this entry to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    out.writeByte(getTag());
    out.writeLong(value.length);
    out.write(value);
  }

  /**
   * Gets the length of this entry in bytes.
   *
   * @return  length of entry
   */
  public long length() {
    return 1 + 8 + value.length;
  }

  /**
   * Gets the byte array that contains this entry.
   *
   * @return  the byte array that contains this entry
   */
  public byte[] getValue() {
    return value;
  }

  /**
   * Gets the type of this entry.
   *
   * @return  the type of entry
   */
  public String getType() {
    return "Long String";
  }

  /**
   * Gets the tag of this entry.
   *
   * @return  the tag for entry
   */
  public byte getTag() {
    return TC_LONGSTRING;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return  a string representation of the object.
   */
  public String toString() {
    return getType() + "(" + handle + ", \"" + new String(value) + "\")";
  }

}
