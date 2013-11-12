// ValueEntry.java

package org.sf.serfile;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.StreamCorruptedException;

/**
 * Class that represents a value entry.
 *
 * @version 1.0 05/25/2000
 * @author Alexander Shvets
 */
public final class ValueEntry extends Entry implements SerConstants {
  /** value that this value-entry contains */
  private Entry value;

  /** tag */
  private byte tag;

  /** type of this value-entry */
  private String type;

  /** name of this value-entry */
  private String name;

  /** handles table */
  private HandlesTable handlesTable;

  /**
   * The constructor that creates value entry.
   *
   * @param tag  tag
   * @param handlesTable  table of handles
   */
  public ValueEntry(byte tag, String type, String name, HandlesTable handlesTable) {
    this.tag          = tag;
    this.type         = type;
    this.name         = name;
    this.handlesTable = handlesTable;
  }

  /**
   * Reads this entry from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException {
    switch(tag) {
      case (byte)'I':
        value = new IntegerEntry();
        break;
      case (byte)'B':
        value = new ByteEntry();
        break;
      case (byte)'C':
        value = new CharacterEntry();
        break;
      case (byte)'D':
        value = new DoubleEntry();
        break;
      case (byte)'F':
        value = new FloatEntry();
        break;
      case (byte)'J':
        value = new LongEntry();
        break;
      case (byte)'S':
        value = new ShortEntry();
        break;
      case (byte)'Z':
        value = new BooleanEntry();
        break;
      case (byte)'L':
        value = new ObjectEntry(handlesTable);
        break;
      case (byte) '[':
        value = new ArrayEntry(handlesTable);
        break;
      case TC_BLOCKDATA:
        value = new BlockDataShortEntry();
        break;
      case TC_BLOCKDATALONG:
        value = new BlockDataLongEntry();
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
    if(value.getType().equals(new NullSerEntry().getType())) {
      return new Signature(type)./*getTypeName()*/toString().replace('/', '.');
    }

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
   * Gets the name for this value entry (names for class and fild).
   *
   * @return  the name for this value entry
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the value for this entry.
   *
   * @return  the value for this entry
   */
  public Entry getValue() {
    return value;
  }

  /**
   * Compares two value entries. They
   * will be equals if they both have the same values.
   *
   * @param   e  the value entry tor comparison.
   * @return  true if this value entry has the same value; false otherwise.
   */
  public boolean equals(ValueEntry e) {
    return (value == e.value);
  }

  /**
   * Returns a string representation of the object.
   *
   * @return  a string representation of the object.
   */
  public String toString() {
    return getType() + " " + name + ": " + value.toString();
  }

}
