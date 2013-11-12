// CodeAttribute.java

package org.sf.classfile.attribute;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

import org.sf.classfile.EntryCollection;
import org.sf.classfile.AttributeEntry;

/**
 * An interpretator for "Code" attribute.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public final class CodeAttribute extends AttributeInterpretator {
  public static final String TYPE = "CodeAttribute";

  public static final String EXCEPTION_TABLE = "Exception Table";
  public static final String ATTRIBUTES      = "Attributes";

  /* Max stack value */
  private short maxStack;

  /* Max locals value */
  private short maxLocals;

  private MethodBody methodBody;

  /* An array of exception handlers entries */
  private EntryCollection exceptionHandlers;

  /* An array of attributes for this code attribute */
  private EntryCollection attributes;

  /**
   * Creates interpretator for "Code" attribute
   *
   * @param attribute  an original attribute
   */
  public CodeAttribute(AttributeEntry attribute) throws IOException {
    super(attribute);
  }

  /**
   * Reads this attribute from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException {
    maxStack  = in.readShort();
    maxLocals = in.readShort();

    int codeSz = in.readInt();

    methodBody = new MethodBody(codeSz);
    methodBody.read(in);

    short excSz = in.readShort();

    exceptionHandlers = new EntryCollection(ExceptionHandlerEntry.class, EXCEPTION_TABLE);

    for(int i=0; i < excSz; i++) {
      ExceptionHandlerEntry entry = new ExceptionHandlerEntry();
      entry.read(in);

      exceptionHandlers.add(entry);
    }

    attributes = new EntryCollection(AttributeEntry.class, ATTRIBUTES);

    short attrSz = in.readShort(); // attributes count

    for(int i=0; i < attrSz; i++) {
      AttributeEntry entry = new AttributeEntry();
      entry.read(in);

      attributes.add(entry);
    }
  }

  /**
   * Writes this attributre to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    out.writeShort(maxStack);
    out.writeShort(maxLocals);

    out.writeInt((int)methodBody.length());
    methodBody.write(out);

    short excSz = (short)exceptionHandlers.size();

    out.writeShort(excSz);

    for(short i=0; i < excSz; i++) {
      exceptionHandlers.get(i).write(out);
    }

    short attrSz = (short)attributes.size();

    out.writeShort(attrSz);

    for(short i=0; i < attrSz; i++) {
      attributes.get(i).write(out);
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
   * Length of this attribute in bytes.
   *
   * @return  length
   */
  public long length() {
    return 2 + 4 +
           2 + 2 + 4 + methodBody.length()*1 +
           2 + exceptionHandlers.size()*2*4 +
           2 + attributes.length();
  }

  /**
   * Removes all attributes for this code attribute
   *
   * @return  true if there some changes in this attribute
   */
  public boolean clearCodeAttributes() {
    boolean changes = attributes.size() > 0;

    if(changes) {
      attributes.clear();

      byte[] buffer = this.getBytes();

      attribute.setBuffer(buffer);
    }

    return changes;
  }

  /**
   * Get max stack value
   *
   * @return  max stack value
   */
  public short getMaxStack() {
    return maxStack;
  }

  /**
   * Get max locals value
   *
   * @return  max locals value
   */
  public short getMaxLocals() {
    return maxLocals;
  }

  /**
   * Get the body for this code attribute
   *
   * @return  the body for this code attribute
   */
  public MethodBody getMethodBody() {
    return methodBody;
  }

  /**
   * Get an array of exception handler entries
   *
   * @return  an array of exception handler entries
   */
  public EntryCollection getExceptionHandlers() {
    return exceptionHandlers;
  }

  /**
   * Get an array of attributes
   *
   * @return  an array of attributes
   */
  public EntryCollection getAttributes() {
    return attributes;
  }

}
