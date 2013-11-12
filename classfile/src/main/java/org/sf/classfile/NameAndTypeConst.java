// NameAndTypeConst.java

package org.sf.classfile;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

/**
 * Class that represents a "name and type" const entry.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public final class NameAndTypeConst extends ConstPoolEntry {
  public static final String TYPE = "NameAndTypeConst";

  /* The index value that points to the name of this entry */
  private short nameIndex;

  /* The index value that points to value of this entry */
  private short valueIndex;

  /**
   * The default constructor that creates "name and type" constant.
   */
  public NameAndTypeConst() {}

  /**
   * Reads this entry from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException {
    nameIndex = in.readShort();
    valueIndex = in.readShort();
  }

  /**
   * Writes this entry to output stream.
   *
   * @param  out  the output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    out.writeByte(getTag());
    out.writeShort(nameIndex);
    out.writeShort(valueIndex);
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
   * Gets the index that points to the value of this entry
   *
   * @return  the index that points to the value of this entry
   */
  public short getValueIndex() {
    return valueIndex;
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
   * (tag + name index size + value index size)
   *
   * @return  length of entry
   */
  public long length() {
    return 1 + 2 + 2;
  }

  /**
   * Gets the tag of this entry.
   *
   * @return  the tag for entry
   */
  public byte getTag() {
    return CONSTANT_NAMEANDTYPE;
  }

  /**
   * Resolves this entry.
   *
   * @param   constPool  the pool
   * @return  the resolved information about this entry
   */
  public String resolve(Pool constPool) {
    Utf8Const entry1 = (Utf8Const)constPool.getEntry(nameIndex);
    Utf8Const entry2 = (Utf8Const)constPool.getEntry(valueIndex);

    return entry1.getValue() + " " + entry2.getValue();
  }

  /**
   * Compares two Objects for equality. Two "name and type" consts
   * will be equals if they both have the same value and name.
   *
   * @param   object  the reference object with which to compare.
   * @return  true if this object is the same as the obj
   *          argument; false otherwise.
   */
  public boolean equals(Object object) {
    if(object != null && object instanceof NameAndTypeConst) {
      NameAndTypeConst nameAndTypeConst = (NameAndTypeConst)object;

      return ((nameIndex == nameAndTypeConst.nameIndex) &&
              (valueIndex == nameAndTypeConst.valueIndex));
    }

    return false;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return  a string representation of the object.
   */
  public String toString() {
   return getType() + " {" +
                 " 0x" + Converter.toHexString(nameIndex) +
                 " 0x" + Converter.toHexString(valueIndex) +
          " }";
  }

}
