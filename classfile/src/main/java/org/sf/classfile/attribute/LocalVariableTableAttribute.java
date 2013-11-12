// LocalVariableTableAttribute.java

package org.sf.classfile.attribute;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

import org.sf.classfile.AttributeEntry;

/**
 * An interpretator for "Local Variable Table" attribute.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public final class LocalVariableTableAttribute extends AttributeInterpretator {

  public static final String TYPE = "LocalVariableTableAttribute";

  /* an array of local variable entries */
  private LocalVariableEntry[] localVariables;


  /**
   * Creates interpretator for "Local Variable Table" attribute
   *
   * @param attribute  an original attribute
   */
  public LocalVariableTableAttribute(AttributeEntry attribute) throws IOException {
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

    localVariables = new LocalVariableEntry[size];

    for(int i=0; i < size; i++) {
      (localVariables[i] = new LocalVariableEntry()).read(in);
    }
  }

  /**
   * Writes this attributre to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    short size = (short)localVariables.length;

    out.writeShort(size);

    for(int i=0; i < size; i++) {
      localVariables[i].write(out);
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
   * Get an array of local variable entries
   *
   * @return  an array of local variable entries
   */
  public LocalVariableEntry[] getLocalVariables() {
    return localVariables;
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

    for(int i=0; i < localVariables.length; i++) {
      sb.append(localVariables[i] + "\n");
    }

    sb.append("}");


    return sb.toString();
  }

}

