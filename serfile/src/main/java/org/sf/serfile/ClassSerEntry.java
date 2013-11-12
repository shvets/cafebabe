// ClassSerEntry.java

package org.sf.serfile;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

/**
 * Class that represents a serialized class entry.
 *
 * ClassSer: TC_CLASS classDescSer newHandle
 *
 * @version 1.0 05/25/2000
 * @author Alexander Shvets
 */
public class ClassSerEntry extends HandleSerEntry {
  /** serialized class description for this class entry */
  private ClassDescSerEntry classDescSerEntry;

  /**
   * The constructor that creates serialized class entry
   *
   * @param handlesTable  table of handles
   */
  public ClassSerEntry(HandlesTable handlesTable) {
    super(handlesTable);
  }

  /**
   * Reads this entry from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException  {
    in.readByte();

    (classDescSerEntry = new ClassDescSerEntry(handlesTable)).read(in);

    handle = handlesTable.put(this);
  }

  /**
   * Writes this entry to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    out.writeByte(getTag());
    classDescSerEntry.write(out);
  }

  /**
   * Gets the length of this entry in bytes.
   *
   * @return  the length of entry
   */
  public long length() {
    return 1 + classDescSerEntry.length();
  }

  /**
   * Gets serialized class description
   */
  public ClassDescSerEntry getClassDescSerEntry() {
    return classDescSerEntry;
  }

  /**
   * Gets the type of this entry.
   *
   * @return  the type of entry
   */
  public String getType() {
    return "Class";
  }

  /**
   * Gets the tag of this entry.
   *
   * @return  the tag for entry
   */
  public byte getTag() {
    return TC_CLASS;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return  a string representation of the object.
   */
  public String toString() {
    return getType() + "(" + handle + ")" + " -> " + classDescSerEntry;
  }

}
