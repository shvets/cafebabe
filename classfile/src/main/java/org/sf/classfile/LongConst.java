// LongConst.java

package org.sf.classfile;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

/**
 * Class that represents a "long" const entry.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public final class LongConst extends ConstPoolEntry {

  public static final String TYPE = "LongConst";

  /* The long value of this entry */
  private long value;

  /**
   * The default constructor that creates long constant.
   */
  public LongConst() {}

  /**
   * Reads this entry from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException {
    value = in.readLong();
  }

  /**
   * Writes this entry to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    out.writeByte(getTag());
    out.writeLong(value);
  }

  /**
   * Gets the type of this entry.
   *
   * @return  type of entry
   */
  public String getType() {
    return TYPE;
  }

  /**
   * Length of this entry in bytes (tag + size of long type).
   *
   * @return  length of entry
   */
  public long length() {
    return 9; // 1 + 8
  }

  /**
   * Get the long value for this entry.
   *
   * @return  the long value for this entry
   */
  public long getValue() {
    return value;
  }

  /**
   * Get the tag of this entry.
   *
   * @return the tag for entry
   */
  public byte getTag() {
    return CONSTANT_LONG;
  }

  /**
   * Resolves this entry.
   *
   * @param   constPool  the pool
   * @return  the resolved information about this entry
   */
  public String resolve(Pool constPool) {
    return new Long(value).toString();
  }

  /**
   * Compares two Objects for equality. Two long consts
   * will be equals if they both have the same value.
   *
   * @param   object  the reference object with which to compare.
   * @return  true if this object is the same as the obj
   *          argument; false otherwise.
   */
  public boolean equals(Object object) {
    if(object != null && object instanceof LongConst) {
      LongConst longConst = (LongConst)object;

      return (value == longConst.value);
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
