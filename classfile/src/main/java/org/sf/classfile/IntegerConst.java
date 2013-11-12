// IntegerConst.java

package org.sf.classfile;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

/**
 * Class that represents a "integer" const entry.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public final class IntegerConst extends ConstPoolEntry {

  public static final String TYPE = "IntegerConst";

  /* the int value of this entry */
  private int value;

  /**
   * The default constructor that creates integer constant.
   */
  public IntegerConst() {}

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
    out.writeByte(getTag());
    out.writeInt(value);
  }

  /**
   * Gets the type of this entry.
   *
   * @return the type of entry
   */
  public String getType() {
    return TYPE;
  }

  /**
   * Length of this entry in bytes (tag + size of int type).
   *
   * @return  length of entry
   */
  public long length() {
    return 1 + 4;
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
   * Gets the tag of this entry.
   *
   * @return the tag for entry
   */
  public byte getTag() {
    return CONSTANT_INTEGER;
  }

  /**
   * Resolve this entry.
   *
   * @param   constPool  the pool
   * @return  the resolved information about this entry
   */
  public String resolve(Pool constPool) {
    return Integer.toHexString(value);
  }

  /**
   * Compares two Objects for equality. Two integer consts
   * will be equals if they both have the same value.
   *
   * @param   object  the reference object with which to compare.
   * @return  true if this object is the same as the obj
   *          argument; false otherwise.
   */
  public boolean equals(Object object) {
    if(object != null && object instanceof IntegerConst) {
      IntegerConst integerConst = (IntegerConst)object;

      return (value == integerConst.value);
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
