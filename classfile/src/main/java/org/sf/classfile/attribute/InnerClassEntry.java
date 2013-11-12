// InnerClassEntry.java

package org.sf.classfile.attribute;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

import org.sf.classfile.Entry;
import org.sf.classfile.Resolvable;
import org.sf.classfile.Pool;
import org.sf.classfile.PoolEntry;
import org.sf.classfile.AccessFlags;
import org.sf.classfile.ClassAccessFlags;
import org.sf.classfile.Converter;

/**
 * Class that represents an inner class entry.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public final class InnerClassEntry extends Entry implements Resolvable {

  public static final String TYPE = "InnerClass";

  /* An index that points to inner class in constant pool */
  private short innerClassIndex;

  /* An index that points to  outer class in constant pool */
  private short outerClassIndex;

  /* An index that points to  inner name of inner class in constant pool */
  private short innerNameIndex;

  /* Access flags for this inner class */
  private AccessFlags accessFlags = new ClassAccessFlags();

  /**
   * The default constructor that creates inner class entry.
   */
  public InnerClassEntry() {}

  /**
   * Reads this entry from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException {
    innerClassIndex = in.readShort();
    outerClassIndex = in.readShort();
    innerNameIndex  = in.readShort();

    accessFlags.read(in);
  }

  /**
   * Writes this entry to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    out.writeShort(innerClassIndex);
    out.writeShort(outerClassIndex);
    out.writeShort(innerNameIndex);
    accessFlags.write(out);
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
   * Length of this entry in bytes
   * (inner class + outer class + inner name + access flags).
   *
   * @return  length of entry
   */
  public long length() {
    return 2 + 2 + 2 + accessFlags.length();
  }

  /**
   * Gets an index that points to a class const for inner class in constant pool
   *
   * @return  an inner class index
   */
  public short getInnerClassIndex() {
    return innerClassIndex;
  }

  /**
   * Gets an index that points to a class const for outer class in constant pool
   *
   * @return  an outer class index
   */
  public short getOuterClassIndex() {
    return outerClassIndex;
  }

  /**
   * Gets an index that points to a name for inner class in constant pool
   *
   * @return  an index
   */
  public short getInnerNameIndex() {
    return innerNameIndex;
  }

  /**
   * Gets access flags for inner class entry.
   *
   * @return access flags
   */
  public AccessFlags getAccessFlags() {
    return accessFlags;
  }

  /**
   * Resolves this entry.
   *
   * @param   constPool  the const pool
   * @return  the resolved information about this entry
   */
  public String resolve(Pool constPool) {
    PoolEntry ei1 = constPool.getEntry(innerClassIndex);

    String innerClassFileName = ei1.resolve(constPool).replace('/', '.');

    String outerClassName = "this";

    if(outerClassIndex != 0) {
      PoolEntry ei2 = constPool.getEntry(outerClassIndex);

      outerClassName = ei2.resolve(constPool).replace('/', '.');
    }

    String innerClassName = "this";

    if(innerNameIndex != 0) {
      PoolEntry ei3 = constPool.getEntry(innerNameIndex);

      innerClassName = ei3.resolve(constPool).replace('/', '.');
    }

    return accessFlags.toString() + " " + outerClassName + "." +
           innerClassName + "(" + innerClassFileName + ")";
  }

  /**
   * Returns a string representation of the object.
   *
   * @return  a string representation of the object.
   */
  public String toString() {
    return getType() + " {" +
                  " 0x" + Converter.toHexString(innerClassIndex) +
                  " 0x" + Converter.toHexString(outerClassIndex) +
                  " 0x" + Converter.toHexString(innerNameIndex) +
                  " 0x" + Converter.toHexString(accessFlags.getValue()) +
           " }";
  }

}

