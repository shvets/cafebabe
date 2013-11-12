// SyntheticAttribute.java

package org.sf.classfile.attribute;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

import org.sf.classfile.AttributeEntry;

/**
 * An interpretator for "Synthetic" attribute.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public final class SyntheticAttribute extends AttributeInterpretator {

  public static final String TYPE = "SyntheticAttribute";

  /**
   * Creates interpretator for "Source File" attribute
   *
   * @param attribute  an original attribute
   */
  public SyntheticAttribute(AttributeEntry attribute) throws IOException {
    super(attribute);
  }

  /**
   * Reads this attribute from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException {}

  /**
   * Writes this attributre to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {}

  /**
   * Gets the type of this entry.
   *
   * @return  the type of entry
   */
  public String getType() {
    return TYPE;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return  a string representation of the object.
   */
  public String toString() {
    return getType() + " {}";
  }

}
