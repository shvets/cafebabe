// UnicodeConst.java

package org.sf.classfile;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

/**
 * Class that represents an "unicode" const entry.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public final class UnicodeConst extends ConstPoolEntry {
  public static final String TYPE = "UnicodeConst";

  /* An array of bytes that holds unicode string */
  protected byte[] value;

  /**
   * A default constructor that creates unicode constant.
   */
  public UnicodeConst() {}

  /**
   * Reads this entry from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException {
    short size = in.readShort();

    value = new byte[size];
    in.readFully(value);
  }

  /**
   * Writes this entry to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    short size = (short)value.length;

    out.writeShort(size);
    out.write(value, 0, size);
  }

  /**
   * Gets the type of this entry.
   *
   * @return  the type of entry
   */
  public String getType() {
    return TYPE;
  }

  /**
   * Length of this entry in bytes.
   * (tag + unicode length + unicode chars)
   *
   * @return  length of entry
   */
  public long length() {
    return 1 + 2 + value.length;
  }

  /**
   * Gets the value-array for this entry.
   *
   * @return  the value-array for this entry
   */
  public byte[] getValue() {
    return value;
  }

  /**
   * Gets the tag of this entry.
   *
   * @return  the tag for entry
   */
  public byte getTag() {
    return CONSTANT_UNICODE;
  }

  /**
   * Resolve this entry.
   *
   * @param   constPool  the const pool
   * @return  the resolved information about this entry
   */
  public String resolve(Pool constPool) {
    return new String(value);
  }

  /**
   * Compares two Objects for equality. Two unicode consts
   * will be equals if they both have the same value-array.
   *
   * @param   object  the reference object with which to compare.
   * @return  true if this object is the same as the obj
   *          argument; false otherwise.
   */
  public boolean equals(Object object) {
    if(object != null && object instanceof UnicodeConst) {
      UnicodeConst unicodeConst = (UnicodeConst)object;

      return new String(value).equals(new String(unicodeConst.value));
    }

    return false;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return  a string representation of the object.
   */
  public String toString() {
    return getType() + " { " + new String(value) + " }";
  }

}
