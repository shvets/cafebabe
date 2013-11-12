// ContentEntry.java

package org.sf.serfile;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.StreamCorruptedException;

/**
 * Class that represents a content entry.
 *
 * content: object | blockdata
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public final class ContentEntry extends  Entry implements SerConstants {
  /** Reference to the entry for which this entry is a wrapper */
  private Entry value;

  /** a tag */
  private byte tag;

  /** Table that contains all handles to allocated elements of serialized file */
  private HandlesTable handlesTable;

  /**
   * The constructor that creates content entry.
   *
   * @param tag  		a tag
   * @param handlesTable  	table of handles
   */
  public ContentEntry(byte tag, HandlesTable handlesTable) {
    this.tag = tag;

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
      case TC_BLOCKDATA:
        value = new BlockDataShortEntry();
        break;
      case TC_BLOCKDATALONG:
        value = new BlockDataLongEntry();
        break;
      case TC_ENDBLOCKDATA:
        value = new EndBlockDataEntry();
        break;
      case TC_NULL:
        value = new NullSerEntry();
        break;
      case TC_OBJECT:
        value = new ObjectSerEntry(handlesTable);
        break;
      case TC_REFERENCE:
        value = new ReferenceSerEntry();
        break;
      case TC_ARRAY:
        value = new ArraySerEntry(handlesTable);
        break;
      case TC_STRING:
         value = new StringSerEntry(handlesTable);
         break;
      case TC_LONGSTRING:
         value = new LongStringSerEntry(handlesTable);
         break;
      case TC_CLASSDESC:
        value = new ClassDescSerEntry(handlesTable);
        break;
      case TC_CLASS:
        value = new ClassSerEntry(handlesTable);
        break;
      case TC_EXCEPTION:
        value = new ContentEntry(in.readByte(), handlesTable);
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
    return "Content";
  }

  /**
   * Gets the length of this entry in bytes.
   *
   * @return  the length of the entry
   */
  public long length() {
    return value.length();
  }

  /**
   * Gets the entry that contains this entry.
   *
   * @return  the entry that contains this entry
   */
  public Entry getValue() {
    return value;
  }

  /**
   * Compares two content entries. They
   * will be equals if they both have the same values.
   *
   * @param   e  the content entry tor comparison.
   * @return  true if this content entry has the same value; false otherwise.
   */
  public boolean equals(ContentEntry e) {
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
