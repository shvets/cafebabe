// DoubleConst.java

package org.sf.classfile;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

/**
 * Class that represents a "double" const entry.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public final class DoubleConst extends ConstPoolEntry {

  public static final String TYPE = "DoubleConst";

  /* the double value of this entry */
  private double value;

  /**
   * The default constructor that creates double constant.
   */
  public DoubleConst() {}

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
    out.writeByte(getTag());
    out.writeDouble(value);
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
   * Length of this entry in bytes (tag + size of double type).
   *
   * @return  length of entry
   */
  public long length() {
    return 1 + 8;
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
   * Gets the tag of this entry.
   *
   * @return  the tag for entry
   */
  public byte getTag() {
    return CONSTANT_DOUBLE;
  }

  /**
   * Resolves this entry.
   *
   * @param   constPool  the pool
   * @return  the resolved information about this entry
   */
  public String resolve(Pool constPool) {
    return new Double(value).toString();
  }

  /**
   * Compares two Objects for equality. Two double consts
   * will be equals if they both have the same value.
   *
   * @param   object  the reference object with which to compare.
   * @return  true if this object is the same as the obj
   *          argument; false otherwise.
   */
  public boolean equals(Object object) {
    if(object != null && object instanceof DoubleConst) {
      DoubleConst doubleConst = (DoubleConst)object;

      return (value == doubleConst.value);
    }

    return false;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return  a string representation of the object.
   */
  public String toString() {
    return getType() + " { " + value + "}";
  }

}
