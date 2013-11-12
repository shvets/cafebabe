// ExceptionsAttribute.java

package org.sf.classfile.attribute;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

import org.sf.classfile.AttributeEntry;

/**
 * An interpretator for "Exceptions" attribute.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public final class ExceptionsAttribute extends AttributeInterpretator {

  public static final String TYPE = "ExceptionsAttribute";

  /* an array of exceptions entries */
  private ExceptionEntry[] exceptions;

  /**
   * Creates interpretator for "Exceptions" attribute
   *
   * @param attribute  an original attribute
   */
  public ExceptionsAttribute(AttributeEntry attribute) throws IOException {
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

    exceptions = new ExceptionEntry[size];
    for(int i=0; i < size; i++) {
      (exceptions[i] = new ExceptionEntry()).read(in);
    }
  }

  /**
   * Writes this attributre to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    short size = (short)exceptions.length;

    out.writeShort(size);

    for(int i=0; i < size; i++) {
      exceptions[i].write(out);
    }
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
   * Gets an array of exception entries
   *
   * @return  an array of exception entries
   */
  public ExceptionEntry[] getExceptions() {
    return exceptions;
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

    for(int i=0; i < exceptions.length; i++) {
      sb.append(exceptions[i] + "\n");
    }

    sb.append("}");


    return sb.toString();
  }

}
