// IntegerEntry.java

package org.sf.serfile;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

/**
 * Class that represents an entry for holding short value
 *
 * @version 1.0 05/25/2000
 * @author Alexander Shvets
 */
public final class IntegerEntry extends Entry {

  /** int value */
  private int value;

  /**
   * The default constructor that creates "int" entry.
   */
  public IntegerEntry() {}

  /**
   * Reads this entry from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException {
    value = in.readInt();
  }

  /**
   * Writes this entry to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    out.writeInt(value);
  }

  /**
   * Gets the type of this entry.
   *
   * @return  the type of entry
   */
  public String getType() {
    return "int";
  }

  /**
   * Gets the length of this entry in bytes.
   *
   * @return  the length of entry
   */
  public long length() {
    return 4;
  }

  /**
   * Gets the integer value for this entry.
   *
   * @return  the integer value for this entry
   */
  public int getValue() {
    return value;
  }

  /**
   * Compares two int entries. They
   * will be equals if they both have the same values.
   *
   * @param   e  the int entry tor comparison.
   * @return  true if this int entry has the same value; false otherwise.
   */
  public boolean equals(IntegerEntry e) {
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
