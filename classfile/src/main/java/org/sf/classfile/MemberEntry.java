// MemberEntry.java

package org.sf.classfile;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

/**
 * Basic class that can represent either field or method entry.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public abstract class MemberEntry extends Entry implements Resolvable {
  /* The index value that points to the name of this entry */
  protected short nameIndex;

  /* The index value that points to value of this entry */
  protected short valueIndex;

  /** access attributes for this member entry */
  protected AccessFlags accessFlags;

  /** Attributes for this member entry */
  protected EntryCollection attributes =
            new EntryCollection(AttributeEntry.class, "Attributes");

  /**
   * The default constructor that creates field or method entry.
   */
  public MemberEntry() {}

  /**
   * Reads this entry from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException  {
    accessFlags.read(in);

    nameIndex = in.readShort();
    valueIndex = in.readShort();

    attributes.read(in);
  }

  /**
   * Writes this entry to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    accessFlags.write(out);

    out.writeShort(nameIndex);
    out.writeShort(valueIndex);

    attributes.write(out);
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
   * Length of this entry in bytes
   * (size of access flag + name index size + value index size +
   *  size of attributes).
   *
   * @return  length of entry
   */
  public long length() {
    return accessFlags.length() + 2 + 2 + attributes.length();
  }

  /**
   * Gets access flags for this member entry.
   *
   * @return access flags for this member entry
   */
  public AccessFlags getAccessFlags() {
    return accessFlags;
  }

  /**
   * Sets access flags for this member entry.
   *
   * @param flags  access flags for this member entry
   */
/*  public void setAccessFlags(short flags) {
    this.accessFlags.setValue(flags);
  }
*/
  /**
   * Gets the attributes for this member entry
   *
   * @return  the attributes for this member entry
   */
  public EntryCollection getAttributes() {
    return attributes;
  }

  /**
   * Gets the signature of this member entry
   *
   * @param constPool   the constant pool
   * @return  the signature of this member entry
   */
  public String getSignature(Pool constPool) {
    return ((Utf8Const)constPool.getEntry(valueIndex)).getValue();
  }

  /**
   * Returns a string representation of the object.
   *
   * @return  a string representation of the object.
   */
  public String toString() {
    return "{" + accessFlags.getValue() + " " + nameIndex + " " +
           valueIndex + " " + attributes + "}";
  }

}
