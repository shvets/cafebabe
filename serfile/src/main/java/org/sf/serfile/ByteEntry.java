// ByteEntry.java

package org.sf.serfile;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

/**
 * Class that represents an entry for holding byte value
 *
 * @version 1.0 05/25/2000
 * @author Alexander Shvets
 */
public final class ByteEntry extends Entry {

  /** byte value */
  private byte value;

  /**
   * The default constructor that creates "byte" entry.
   */
  public ByteEntry() {}

  /**
   * Reads this entry from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException {
    value = in.readByte();
  }

  /**
   * Writes this entry to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    out.writeByte(value);
  }

  /**
   * Gets the type of this entry.
   *
   * @return  the type of entry
   */
  public String getType() {
    return "byte";
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
   * Gets the byte value for this entry.
   *
   * @return  the byte value for this entry
   */
  public byte getValue() {
    return value;
  }

  /**
   * Compares two byte entries. They
   * will be equals if they both have the same values.
   *
   * @param   e  the byte entry tor comparison.
   * @return  true if this byte entry has the same value; false otherwise.
   */
  public boolean equals(ByteEntry e) {
    return (value == e.value);
  }

  /**
   * Returns a string representation of the object.
   *
   * @return  a string representation of the object.
   */
  public String toString() {
    return String.valueOf(value);
  }

}
