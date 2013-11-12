// MemberConst.java

package org.sf.classfile;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

/**
 * Basic class that can represent either field or method const entry.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public abstract class MemberConst extends ConstPoolEntry {

  /* The index value that points to the name of this entry */
  protected short nameIndex;

  /* The index value that points to value of this entry */
  protected short valueIndex;

  /**
   * The default constructor that creates constant of field or method type.
   */
  public MemberConst() {}

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
   * @param  out  output stream.
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
   * Length of this entry in bytes.
   * (tag + name index size + value index size)
   *
   * @return  length of entry
   */
  public long length() {
    return 5; // 1 + 2 + 2
  }

  /**
   * Resolves this entry.
   *
   * @param   constPool  the pool
   * @return  the resolved information about this entry
   */
  public String resolve(Pool constPool) {
    ClassConst entry1 = (ClassConst)constPool.getEntry(nameIndex);

    NameAndTypeConst entry2 = (NameAndTypeConst)constPool.getEntry(valueIndex);

    Utf8Const entry3 = (Utf8Const)constPool.getEntry(entry1.getNameIndex());
    Utf8Const entry4 = (Utf8Const)constPool.getEntry(entry2.getNameIndex());
    Utf8Const entry5 = (Utf8Const)constPool.getEntry(entry2.getValueIndex());

    return entry3.getValue() + " " + entry4.getValue() + " " +
           entry5.getValue();
  }

  /**
   * Returns a string representation of the object.
   *
   * @return  a string representation of the object.
   */
  public String toString() {
    return getType() + " { " +
                  " 0x" + Converter.toHexString(nameIndex) +
                  " 0x" + Converter.toHexString(valueIndex) +
           " }";
  }

}
