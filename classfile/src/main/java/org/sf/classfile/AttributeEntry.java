// AttributeEntry.java

package org.sf.classfile;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

/**
 * Class that represent attribute entry (class, method or field attribute)
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public class AttributeEntry extends Entry implements Resolvable {
  public static final String TYPE = "Attribute";

  /** A constant for presentation the name of "Source File" attribute */
  public static final String SOURCE_FILE = "SourceFile";

  /** A constant for presentation the name of "Code" attribute */
  public static final String CODE = "Code";

  /** A constant for presentation the name of "Exceptions" attribute */
  public static final String EXCEPTIONS = "Exceptions";

  /** A constant for presentation the name of "Line Number Table" attribute */
  public static final String LINE_NUMBER_TABLE = "LineNumberTable";

  /** A constant for presentation the name of "Local Variable Table" attribute */
  public static final String LOCAL_VARIABLE_TABLE = "LocalVariableTable";

  /** A constant for presentation the name of "Inner Classes" attribute */
  public static final String INNER_CLASSES = "InnerClasses";

  /** A constant for presentation the name of "Constant Value" attribute */
  public static final String CONSTANT_VALUE = "ConstantValue";

  /** A constant for presentation the name of "Synthetic" attribute */
  public static final String SYNTHETIC = "Synthetic";

  /** A constant for presentation the name of "Deprecated" attribute */
  public static final String DEPRECATED = "Deprecated";

  /** the index value that points to the name of this entry */
  protected short nameIndex;

  /** An array of bytes that holds all information attached to this attribute entry.
   *  This information should be resolved lately. */
  protected byte buffer[];

  /**
   * The default constructor that creates attribute entry.
   */
  public AttributeEntry() {}

  /**
   * Reads this entry from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException {
    nameIndex = in.readShort();

    int size = in.readInt();

    buffer  = new byte[size];
    in.readFully(buffer);
  }

  /**
   * Writes this entry to output stream.
   *
   * @param  out  the output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    int size = buffer.length;

    out.writeShort(nameIndex);
    out.writeInt(size);
    out.write(buffer, 0, size);
  }

  /**
   * Gets the type of this entry.
   *
   * @return  the type of entry
   */
  public String getType() {
    return TYPE;
  }

  /**
   * Calculates length of this entry in bytes
   * (name index size + size of buffer size + number of elements in the buffer).
   *
   * @return  length of entry
   */
  public long length() {
    return 2 + 4 + buffer.length;
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
   * Gets the byte array with a whole content of this attribute.
   *
   * @return  the byte array with a whole content of this attribute
   */
  public byte[] getBuffer() {
    return buffer;
  }

  /**
   * Sets the byte array for this attribute.
   *
   * @param buffer  the byte array
   */
  public void setBuffer(byte[] buffer) {
    this.buffer = buffer;
  }

  /**
   * Resolves this entry.
   *
   * @param   constPool  the pool
   * @return  the resolved information about this entity
   */
  public String resolve(Pool constPool) {
    return ((Utf8Const)constPool.getEntry(nameIndex)).getValue();
  }

  /**
   * Returns a string representation of the object.
   *
   * @return  a string representation of the object.
   */
  public String toString() {
    int size = buffer.length;

    StringBuffer sb = new StringBuffer("0x");

    for(int i=0; i < size; i++) {
      sb.append(Converter.toHexString(buffer[i]) + " ");
    }

    return getType() + " { 0x" +
           Converter.toHexString(nameIndex) +
           " [(" + size + ") " + sb.toString() + "]>";
  }

}
