// BooleanEntry.java

package org.sf.serfile;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

/**
 * Class that represents an entry for holding boolean value
 *
 * @version 1.0 05/25/2000
 * @author Alexander Shvets
 */
public final class BooleanEntry extends Entry {

  /** boolean value */
  private boolean value;

  /**
   * The default constructor that creates "boolean" entry.
   */
  public BooleanEntry() {}

  /**
   * Reads this entry from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException {
    value = in.readBoolean();
  }

  /**
   * Writes this entry to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    out.writeBoolean(value);
  }

  /**
   * Gets the type of this entry.
   *
   * @return  the type of entry
   */
  public String getType() {
    return "boolean";
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
   * Gets the boolean value for this entry.
   *
   * @return  the boolean value for this entry
   */
  public boolean getValue() {
    return value;
  }

  /**
   * Compares two boolean entries. They
   * will be equals if they both have the same values.
   *
   * @param   e  the boolean entry tor comparison.
   * @return  true if this boolean entry has the same value; false otherwise.
   */
  public boolean equals(BooleanEntry e) {
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
