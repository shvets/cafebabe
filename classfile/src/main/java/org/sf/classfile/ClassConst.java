// ClassConst.java

package org.sf.classfile;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

/**
 * Class that represents a "class" const entry.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public final class ClassConst extends ConstPoolEntry {
  public static final String TYPE = "ClassConst";

  /* The index value that points to the name of this entry */
  private short nameIndex;

  /**
   * The default constructor that creates "name and type" constant.
   */
  public ClassConst() {}

  /**
   * Reads this entry from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException {
    nameIndex = in.readShort();
  }

  /**
   * Writes this entry to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    out.writeByte(CONSTANT_CLASS);
    out.writeShort(nameIndex);
  }

  /**
   * Gets the index that points to the name of this entry
   *
   * @return  the index that points to the name of this entry
   */
  public short getNameIndex() {
    return nameIndex;
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
   * Calculates length of this entry in bytes (tag + size of name index)
   *
   * @return  length of entry
   */
  public long length() {
    return 1 + 2;
  }

  /**
   * Gets the tag of this entry.
   *
   * @return  the tag for entry
   */
  public byte getTag() {
    return CONSTANT_CLASS;
  }

  /**
   * Resolves this entry.
   *
   * @param   constPool  the pool
   * @return  the resolved information about this entry
   */
  public String resolve(Pool constPool) {
    return ((Utf8Const)constPool.getEntry(nameIndex)).getValue();
  }

  /**
   * Compares two Objects for equality. Two class consts
   * will be equals if they both have the same value-array.
   *
   * @param   object  the reference object with which to compare.
   * @return  true if this object is the same as the obj
   *          argument; false otherwise.
   */
  public boolean equals(Object object) {
    if(object != null && object instanceof ClassConst) {
      ClassConst classConst = (ClassConst)object;

      return (nameIndex == classConst.nameIndex);
    }

    return false;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return  a string representation of the object.
   */
  public String toString() {
    return getType() + " { 0x" + Converter.toHexString(nameIndex) + " }";
  }

}

