// LineNumberTableAttribute.java

package org.sf.classfile.attribute;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

import org.sf.classfile.AttributeEntry;

/**
 * An interpretator for "Line Number Table" attribute.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public final class LineNumberTableAttribute extends AttributeInterpretator {
  public static final String TYPE = "LineNumberTableAttribute";

  /* an array of line number entries */
  private LineNumberEntry[] lineNumbers;

  /**
   * Creates interpretator for "Line Number Table" attribute
   *
   * @param attribute  an original attribute
   */
  public LineNumberTableAttribute(AttributeEntry attribute) throws IOException {
    super(attribute);
  }

  /**
   * Reads this attribute from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException {
    short size = in.readShort();

    lineNumbers = new LineNumberEntry[size];
    for(int i=0; i < size; i++) {
      (lineNumbers[i] = new LineNumberEntry()).read(in);
    }
  }

  /**
   * Writes this attributre to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    short size = (short)lineNumbers.length;

    out.writeShort(size);

    for(int i=0; i < size; i++) {
      lineNumbers[i].write(out);
    }
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
   * Gets an array of line number entries
   *
   * @return  an array of line number entries
   */
  public LineNumberEntry[] getLineNumbers() {
    return lineNumbers;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return  a string representation of the object.
   */
  public String toString() {
    StringBuffer sb = new StringBuffer();

    sb.append(getType());

    sb.append(" {" + '\n');

    for(int i=0; i < lineNumbers.length; i++) {
      sb.append(lineNumbers[i] + "\n");
    }

    sb.append("}");


    return sb.toString();
  }

}
