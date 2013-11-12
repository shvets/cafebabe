// FloatConst.java

package org.sf.classfile;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

/**
 * Class that represents a "float" const entry.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public final class FloatConst extends ConstPoolEntry {

  public static final String TYPE = "FloatConst";

  /* the float value of this entry */
  private float value;

  /**
   * The default constructor that creates float constant.
   */
  public FloatConst() {}

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
    out.writeByte(getTag());
    out.writeFloat(value);
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
   * Length of this entry in bytes (tag + size of float type).
   *
   * @return  length of entry
   */
  public long length() {
    return 1 + 4;
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
   * Gets the tag of this entry.
   *
   * @return  the tag for entry
   */
  public byte getTag() {
    return CONSTANT_FLOAT;
  }

  /**
   * Resolves this entry.
   *
   * @param   constPool  the pool
   * @return  the resolved information about this entry
   */
  public String resolve(Pool constPool) {
    return new Float(value).toString();
  }

  /**
   * Compares two Objects for equality. Two float consts
   * will be equals if they both have the same value.
   *
   * @param   object  the reference object with which to compare.
   * @return  true if this object is the same as the obj
   *          argument; false otherwise.
   */
  public boolean equals(Object object) {
    if(object != null && object instanceof FloatConst) {
      FloatConst floatConst = (FloatConst)object;
      return (value == floatConst.value);
    }

    return false;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return  a string representation of the object.
   */
  public String toString() {
    return getType() + "{ " + value + " }";
  }

}
