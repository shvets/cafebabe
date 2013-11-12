// Utf8Const.java

package org.sf.classfile;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

/**
 * Class that represents an "utf8" const entry.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public final class Utf8Const extends ConstPoolEntry {
  public static final String TYPE = "UTF8Const";

  /* The utf8 string */
  private String value;

  /**
   * A default constructor that creates utf8 constant.
   */
  public Utf8Const() {}

  /**
   * A constructor that creates utf8 constant with the specified value.
   *
   * @param value  value that specifies utf8 string
   */
  public Utf8Const(String value) {
    this.value = value;
  }

  /**
   * Reads this entry from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException {
    value = in.readUTF();
  }

  /**
   * Writes this entry to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    out.writeByte(getTag());
    out.writeUTF(value);
  }

  /**
   * Gets type of this entry.
   *
   * @return  type of entry
   */
  public String getType() {
    return TYPE;
  }

  /**
   * Length of this entry in bytes.
   * (tag + utf8 length + utf chars)
   *
   * @return  length of entry
   */
  public long length() {
    return 1 + 2 + value.length();
  }

  /**
   * Gets the value-string for this entry.
   *
   * @return  the value-string for this entry
   */
  public String getValue() {
    return value;
  }

  /**
   * Gets the value-string for this entry.
   *
   * @param  value the value-string for this entry
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Gets the tag of this entry.
   *
   * @return  the tag for entry
   */
  public byte getTag() {
    return CONSTANT_UTF8;
  }

  /**
   * Resolves this entry.
   *
   * @param   constPool  the const pool
   * @return  the resolved information about this entry
   */
  public String resolve(Pool constPool) {
    return value;
  }

  /**
   * Compares two Objects for equality. Two const pool entries
   * will be equals if they both have the same value.
   *
   * @param   object  the reference object with which to compare.
   * @return  true if this object is the same as the obj
   *          argument; false otherwise.
   */
  public boolean equals(Object object) {
    if(object != null && object instanceof Utf8Const) {
      Utf8Const utfConst = (Utf8Const)object;

      return value.equals(utfConst.value);
    }

    return false;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return  a string representation of the object.
   */
  public String toString() {
    return getType() + " { " + value + " }";
  }

}
