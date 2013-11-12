// ExceptionHandlerEntry.java

package org.sf.classfile.attribute;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

import org.sf.classfile.Resolvable;
import org.sf.classfile.Entry;
import org.sf.classfile.Pool;
import org.sf.classfile.PoolEntry;
import org.sf.classfile.Converter;

/**
 * Class that represents an exception handler entry.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public final class ExceptionHandlerEntry extends Entry implements Resolvable {

  public static final String TYPE = "ExceptionHandler";

  /* An index that points to start PC index */
  private short startPc;

  /* An index that points to end PC index */
  private short endPc;

  /* An index that points to handler PC index */
  private short handlerPc;

  /* An index that points to catch type index */
  private short catchType;

  /**
   * The default constructor that creates exception handler entry.
   */
  public ExceptionHandlerEntry() {}

  /**
   * Reads this entry from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException {
    startPc   = in.readShort();
    endPc     = in.readShort();
    handlerPc = in.readShort();
    catchType = in.readShort();
  }

  /**
   * Writes this entry to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    out.writeShort(startPc);
    out.writeShort(endPc);
    out.writeShort(handlerPc);
    out.writeShort(catchType);
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
   * Length of this entry in bytes
   * (start PC + end PC + handler PC + catch type index).
   *
   * @return  length of entry
   */
  public long length() {
    return 2 + 2 + 2 + 2;
  }

  /**
   * Gets start PC index
   *
   * @return  start PC index
   */
  public short getStartPc() {
    return startPc;
  }

  /**
   * Gets end PC index
   *
   * @return  end PC index
   */
  public short getEndPc() {
    return endPc;
  }

  /**
   * Gets handler PC index
   *
   * @return  handler PC index
   */
  public short getHandlerPc() {
    return handlerPc;
  }

  /**
   * Gets catch type index
   *
   * @return  catch type index
   */
  public short getCatchType() {
    return catchType;
  }

  /**
   * Resolves this entry.
   *
   * @param   constPool  the const pool
   * @return  the resolved information about this entry
   */
  public String resolve(Pool constPool) {
    String name = "any";

    if(catchType != 0) {
      PoolEntry entry = constPool.getEntry(catchType);

      name = entry.resolve(constPool).replace('/', '.');
    }

    return "(" + " 0x" + Converter.toHexString(startPc) + "-" +
                 " 0x" + Converter.toHexString(endPc) + "," +
                 " 0x" + Converter.toHexString(handlerPc) +
                 ") " + name;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return  a string representation of the object.
   */
  public String toString() {
    return getType() + " (" + "0x" + Converter.toHexString(startPc) +
                              " 0x" + Converter.toHexString(endPc) +
                              " 0x" + Converter.toHexString(handlerPc) +
                              " 0x" + Converter.toHexString(catchType) +
                       " )";
  }

}

