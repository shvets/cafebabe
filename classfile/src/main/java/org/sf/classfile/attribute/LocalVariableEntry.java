// LocalVariableEntry.java

package org.sf.classfile.attribute;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

import org.sf.classfile.Entry;
import org.sf.classfile.Resolvable;
import org.sf.classfile.Pool;
import org.sf.classfile.PoolEntry;
import org.sf.classfile.Signature;
import org.sf.classfile.Converter;

/**
 * Class that represents a local variable entry.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public final class LocalVariableEntry extends Entry implements Resolvable {

  public static final String TYPE = "LocalVariable";

  /* An index that points to start PC index */
  private short startPc;

  /* An index that points to length */
  private short length;

  /* the index value that points to the name of this entry */
  private short nameIndex;

  /* An index that points to description index */
  private short descrIndex;

  /* An index that points to index */
  private short index;

  /**
   * The default constructor that creates local variable entry.
   */
  public LocalVariableEntry() {}

  /**
   * Reads this entry from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException {
    startPc    = in.readShort();
    length     = in.readShort();
    nameIndex  = in.readShort();
    descrIndex = in.readShort();
    index      = in.readShort();
  }

  /**
   * Writes this entry to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    out.writeShort(startPc);
    out.writeShort(length);
    out.writeShort(nameIndex);
    out.writeShort(descrIndex);
    out.writeShort(index);
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
   * Length of this entry in bytes
   * (start PC + length + name index + description index + index).
   *
   * @return  length of entry
   */
  public long length() {
    return 10; // 2 + 2 + 2 + 2 + 2
  }

  /**
   * Gets start PC index
   *
   * @return  start PC index
   */
  public short getStartPc() {
    return startPc;
  }

  /**
   * Gets length value
   *
   * @return  length value
   */
  public short getLength() {
    return length;
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
   * Gets description index
   *
   * @return  description index
   */
  public short getDescriptionIndex() {
    return descrIndex;
  }

  /**
   * Gets index
   *
   * @return  index
   */
  public short getIndex() {
    return index;
  }

  /**
   * Resolves this entry.
   *
   * @param   constPool  the pool
   * @return  the resolved information about this entry
   */
  public String resolve(Pool constPool) {
    PoolEntry ei1 = constPool.getEntry(nameIndex);
    String str1 = ei1.resolve(constPool);

    PoolEntry ei2 = constPool.getEntry(descrIndex);
    String str2 = ei2.resolve(constPool);

    Signature signature = new Signature(str2);

    return "(" + startPc + "-" + (startPc + length) + ", " + index + ") " +
           signature + " " + str1;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return  a string representation of the object.
   */
  public String toString() {
    return getType() + " {" +
                  " 0x" + Converter.toHexString(startPc) +
                  " 0x" + Converter.toHexString(nameIndex) +
                  " 0x" + Converter.toHexString(descrIndex) +
                  " 0x" + Converter.toHexString(index) +
           " }";
  }


}
