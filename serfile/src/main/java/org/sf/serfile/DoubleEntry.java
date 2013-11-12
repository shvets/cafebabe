// DoubleEntry.java

package org.sf.serfile;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

/**
 * Class that represents an entry for holding double value
 *
 * @version 1.0 05/25/2000
 * @author Alexander Shvets
 */
public final class DoubleEntry extends Entry {

  /** double value */
  private double value;

  /**
   * The default constructor that creates "double" entry.
   */
  public DoubleEntry() {}

  /**
   * Reads this entry from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException {
    value = in.readDouble();
  }

  /**
   * Writes this entry to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    out.writeDouble(value);
  }

  /**
   * Gets the type of this entry.
   *
   * @return  type of entry
   */
  public String getType() {
    return "double";
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
   * Gets the double value for this entry.
   *
   * @return  the double value for this entry
   */
  public double getValue() {
    return value;
  }

  /**
   * Compares two double entries. They
   * will be equals if they both have the same values.
   *
   * @param   e  the double entry tor comparison.
   * @return  true if this double entry has the same value; false otherwise.
   */
  public boolean equals(DoubleEntry e) {
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
