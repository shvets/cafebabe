// LineNumberEntry.java

package org.sf.classfile.attribute;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

import org.sf.classfile.Entry;
import org.sf.classfile.Resolvable;
import org.sf.classfile.Pool;
import org.sf.classfile.Converter;

/**
 * Class that represents a line number entry.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public final class LineNumberEntry extends Entry implements Resolvable {
  public static final String TYPE = "LineNumber";

  /* An index that points to start PC index */
  private short startPc;

  /* An index that points to line number index */
  private short lineNumber;


  /**
   * The default constructor that creates line number entry.
   */
  public LineNumberEntry() {}

  /**
   * Reads this entry from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException {
    startPc    = in.readShort();
    lineNumber = in.readShort();
  }

  /**
   * Writes this entry to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    out.writeShort(startPc);
    out.writeShort(lineNumber);
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
   * Length of this entry in bytes (start PC + line number).
   *
   * @return  length of entry
   */
  public long length() {
    return 2 + 2;
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
   * Gets line number index
   *
   * @return  line number index
   */
  public short getLineNumber() {
    return lineNumber;
  }

  /**
   * Resolves this entry.
   *
   * @param   constPool  the const pool
   * @return  the resolved information about this entry
   */
  public String resolve(Pool constPool) {
    return "(" + startPc + ", " + lineNumber + ")";
  }

  /**
   * Returns a string representation of the object.
   *
   * @return  a string representation of the object.
   */
  public String toString() {
    return getType() + " {" +
                  " 0x" + Converter.toHexString(startPc) +
                  " 0x" + Converter.toHexString(lineNumber) +
           " }";
  }

}

