// SourceFileAttribute.java

package org.sf.classfile.attribute;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

import org.sf.classfile.AttributeEntry;
import org.sf.classfile.Converter;

/**
 * An interpretator for "Source File" attribute.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public final class SourceFileAttribute extends AttributeInterpretator {

  public static final String TYPE = "SourceFileAttribute";

  /* An index that points to a string const with a name of source file */
  private short index;

  /**
   * Creates interpretator for "Source File" attribute
   *
   * @param attribute  an original attribute
   */
  public SourceFileAttribute(AttributeEntry attribute) throws IOException {
    super(attribute);
  }

  /**
   * Reads this attribute from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException {
    index = in.readShort();
  }

  /**
   * Writes this attributre to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    out.writeShort(index);
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
   * Gets an index that points to a name of source file in constant pool
   *
   * @return  an index
   */
  public short getIndex() {
    return index;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return  a string representation of the object.
   */
  public String toString() {
    return getType() + " { 0x" + Converter.toHexString(index) + " }";
  }

}
