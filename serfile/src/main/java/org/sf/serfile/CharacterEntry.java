// CharacterEntry.java

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
public final class CharacterEntry extends Entry {

  /** char value */
  private char value;

  /**
   * The default constructor that creates "char" entry.
   */
  public CharacterEntry() {}

  /**
   * Reads this entry from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException {
    value = in.readChar();
  }

  /**
   * Writes this entry to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    out.writeChar(value);
  }

  /**
   * Gets the type of this entry.
   *
   * @return  the type of entry
   */
  public String getType() {
    return "char";
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
   * Gets the char value for this entry.
   *
   * @return  the char value for this entry
   */
  public char getValue() {
    return value;
  }

  /**
   * Compares two char entries. They
   * will be equals if they both have the same values.
   *
   * @param   e  the char entry tor comparison.
   * @return  true if this char entry has the same value; false otherwise.
   */
  public boolean equals(CharacterEntry e) {
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
