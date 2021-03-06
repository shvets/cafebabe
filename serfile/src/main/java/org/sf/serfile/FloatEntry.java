// FloatEntry.java

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
public final class FloatEntry extends Entry {

  /** float value */
  private float value;

  /**
   * The default constructor that creates "float" entry.
   */
  public FloatEntry() {}

  /**
   * Reads this entry from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException {
    value = in.readFloat();
  }

  /**
   * Writes this entry to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    out.writeFloat(value);
  }

  /**
   * Gets the type of this entry.
   *
   * @return  the type of entry
   */
  public String getType() {
    return "float";
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
   * Gets the float value for this entry.
   *
   * @return  the float value for this entry
   */
  public float getValue() {
    return value;
  }

  /**
   * Compares two float entries. They
   * will be equals if they both have the same values.
   *
   * @param   e  the float entry tor comparison.
   * @return  true if this float entry has the same value; false otherwise.
   */
  public boolean equals(FloatEntry e) {
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
