// LongEntry.java

package org.sf.serfile;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

/**
 * Class that represents an entry for holding long value
 *
 * @version 1.0 05/25/2000
 * @author Alexander Shvets
 */
public final class LongEntry extends Entry {

  /** short value */
  private long value;

  /**
   * The default constructor that creates "long" entry.
   */
  public LongEntry() {}

  /**
   * Reads this entry from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException {
    value = in.readLong();
  }

  /**
   * Writes this entry to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    out.writeLong(value);
  }

  /**
   * Gets the type of this entry.
   *
   * @return  the type of entry
   */
  public String getType() {
    return "long";
  }

  /**
   * Gets the length of this entry in bytes.
   *
   * @return  the length of entry
   */
  public long length() {
    return 8;
  }

  /**
   * Gets the long value for this entry.
   *
   * @return  the long value for this entry
   */
  public long getValue() {
    return value;
  }

  /**
   * Compares two long entries. They
   * will be equals if they both have the same values.
   *
   * @param   e  the long entry tor comparison.
   * @return  true if this long entry has the same value; false otherwise.
   */
  public boolean equals(LongEntry e) {
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
