// ClassDescEntry.java

package org.sf.serfile;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.StreamCorruptedException;

/**
 * Class that represents a class description entry.
 *
 * classDesc: newClassDesc | nullReference | (ClassDesc)prevObject
 *                                        // an object required to be of type ClassDesc
 *
 * @version 1.0 05/25/2000
 * @author Alexander Shvets
 */
public final class ClassDescEntry extends Entry implements SerConstants {

  /** Reference to handle entry for which this entry is a wrapper */
  private HandleSerEntry value;

  /** Table that contains all handles to allocated elements of serialized file */
  private HandlesTable handlesTable;

  /**
   * The constructor that creates class description entry.
   *
   * @param handlesTable  table of handles
   */
  public ClassDescEntry(HandlesTable handlesTable) {
    this.handlesTable = handlesTable;
   }

  /**
   * Reads this entry from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException {
    byte tag = in.readByte();

    switch(tag) {
      case TC_NULL:
        value = new NullSerEntry();
        break;
      case TC_REFERENCE:
        value = new ReferenceSerEntry();
        break;
      case TC_PROXYCLASSDESC:
        value = new ProxyClassDescSerEntry();
        break;
      case TC_CLASSDESC:
        value = new ClassDescSerEntry(handlesTable);
        break;
      default:
        throw new StreamCorruptedException("Unknown tag: " + Converter.toHexString(tag));
    }

    value.read(in);
  }

  /**
   * Writes this entry to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    value.write(out);
  }

  /**
   * Gets the type of this entry.
   *
   * @return  the type of entry
   */
  public String getType() {
    return value.getType();
  }

  /**
   * Gets the length of this entry in bytes.
   *
   * @return  the length of entry
   */
  public long length() {
    return value.length();
  }

  /**
   * Get the value for this entry.
   *
   * @return  the  value for this entry
   */
  public HandleSerEntry getValue() {
    return value;
  }

  /**
   * Gets serialized class description
   *
   * @return serialized class description
   */
  public ClassDescSerEntry getClassDescSerEntry() {
    ClassDescSerEntry entry = null;

    if(value instanceof ReferenceSerEntry) {
      entry = (ClassDescSerEntry)handlesTable.get(value.getHandle());
    }
    else if(value instanceof ClassDescSerEntry) {
      entry = (ClassDescSerEntry)value;
    }

    return entry;
  }

  /**
   * Compares two class description entries. They
   * will be equals if they both have the same values.
   *
   * @param   e  the class description entry tor comparison.
   * @return  true if this class description entry has the same value; false otherwise.
   */
  public boolean equals(ClassDescEntry e) {
    return (value == e.value);
  }

  /**
   * Returns a string representation of the object.
   *
   * @return  a string representation of the object.
   */
  public String toString() {
    return value.toString();
  }

}
