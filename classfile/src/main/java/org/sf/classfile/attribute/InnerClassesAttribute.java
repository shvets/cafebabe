// InnerClassesAttribute.java

package org.sf.classfile.attribute;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

import org.sf.classfile.AttributeEntry;

/**
 * An interpretator for "Inner Classes" attribute.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public final class InnerClassesAttribute extends AttributeInterpretator {

  public static final String TYPE = "InnerClassesAttribute";

  /* An array of inner class entries */
  private InnerClassEntry[] innerClasses;

  /**
   * Creates interpretator for "Inner Classes" attribute
   *
   * @param attribute  an original attribute
   */
  public InnerClassesAttribute(AttributeEntry attribute) throws IOException {
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

    innerClasses = new InnerClassEntry[size];

    for(int i=0; i < size; i++) {
      (innerClasses[i] = new InnerClassEntry()).read(in);
    }
  }

  /**
   * Writes this attributre to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    short size = (short)innerClasses.length;

    out.writeShort(size);

    for(int i=0; i < size; i++) {
      innerClasses[i].write(out);
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
   * Gets an array of inner class entries
   *
   * @return  an array of inner class entries
   */
  public InnerClassEntry[] getInnerClasses() {
    return innerClasses;
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

    for(int i=0; i < innerClasses.length; i++) {
      sb.append(innerClasses[i] + "\n");
    }

    sb.append("}");


    return sb.toString();
  }

}

