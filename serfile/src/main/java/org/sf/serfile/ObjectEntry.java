// ObjectEntry.java

package org.sf.serfile;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.StreamCorruptedException;

/**
 * Class that represents an object entry.
 *
 * Object: NullSer | ObjectSer | ReferenceSer | ArraySer | StringSer | ClassDescSer |
 *         exception | TC_RESET
 *
 * exception: TC_EXCEPTION reset (Throwable)object reset
 *
 * reset: "The set of known objects is discarded so the objects of
 *         the exception do not overlap with the previously sent objects
 *         or with objects that may be sent after the exception"
 *
 * @version 1.0 05/25/2000
 * @author Alexander Shvets
 */
public final class ObjectEntry extends Entry implements SerConstants {
  /** Reference to handle entry for which this entry is a wrapper */
  private HandleSerEntry value;

  /** Table that contains all handles to allocated elements of serialized file */
  private HandlesTable handlesTable;

  /**
   * The constructor that creates string entry.
   *
   * @param handlesTable  table of handles
   */
  public ObjectEntry(HandlesTable handlesTable) {
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
      case TC_STRING:
        value = new StringSerEntry(handlesTable);
        break;
      case TC_LONGSTRING:
         value = new LongStringSerEntry(handlesTable);
         break;
      case TC_ARRAY:
        value = new ArraySerEntry(handlesTable);
        break;
      case TC_REFERENCE:
        value = new ReferenceSerEntry();
        break;
      case TC_CLASSDESC:
        value = new ClassDescSerEntry(handlesTable);
        break;
      case TC_CLASS:
        value = new ClassSerEntry(handlesTable);
        break;
      case TC_OBJECT:
        value = new ObjectSerEntry(handlesTable);
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
   * @return  type of entry
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
   * Gets the serialized entry that contains this entry.
   *
   * @return  the serialized entry that contains this entry
   */
  public HandleSerEntry getValue() {
    return value;
  }

  /**
   * Compares two object entries. They
   * will be equals if they both have the same values.
   *
   * @param   e  the object entry tor comparison.
   * @return  true if this object entry has the same value; false otherwise.
   */
  public boolean equals(ObjectEntry e) {
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
