// ConstantValueAttribute.java

package org.sf.classfile.attribute;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

import org.sf.classfile.AttributeEntry;

/**
 * An interpretator for "Constant Value" attribute.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public final class ConstantValueAttribute extends AttributeInterpretator {

  public static final String TYPE = "ConstantValueAttribute";

  /* an index that points to a string const with a content of constant value */
  private short index;

  /**
   * Creates interpretator for "Constant Value" attribute
   *
   * @param attribute  an original attribute
   */
  public ConstantValueAttribute(AttributeEntry attribute) throws IOException {
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
   * @return the type of entry
   */
  public String getType() {
    return TYPE;
  }

  /**
   * Gets an index that points to a content of constant value in constant pool
   *
   * @return  an index
   */
  public short getIndex() {
    return index;
  }

}

