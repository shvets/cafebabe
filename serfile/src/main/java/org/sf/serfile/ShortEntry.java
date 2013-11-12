// ShortEntry.java

package org.sf.serfile;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

/**
 * Class that represents an entry for holding short value
 *
 * @version 05/25/2000
 * @author Alexander Shvets
 */
public final class ShortEntry extends Entry {

  /** short value */
  private short value;

  /**
   * The default constructor that creates "short" entry.
   */
  public ShortEntry() {}

  /**
   * Reads this entry from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException {
    value = in.readShort();
  }

  /**
   * Writes this entry to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    out.writeShort(value);
  }

  /**
   * Gets the type of this entry.
   *
   * @return  the type of entry
   */
  public String getType() {
    return "short";
  }

  /**
   * Gets the length of this entry in bytes.
   *
   * @return  the length of entry
   */
  public long length() {
    return 2;
  }

  /**
   * Get the short value for this entry.
   *
   * @return  the short value for this entry
   */
  public short getValue() {
    return value;
  }

  /**
   * Compares two short entries. They
   * will be equals if they both have the same values.
   *
   * @param   e  the short entry tor comparison.
   * @return  true if this short entry has the same value; false otherwise.
   */
  public boolean equals(ShortEntry e) {
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
