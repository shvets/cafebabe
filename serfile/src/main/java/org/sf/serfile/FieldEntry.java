// FieldEntry.java

package org.sf.serfile;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

/**
 * Class that represents a field entry.
 *
 * @version 1.0 05/25/2000
 * @author Alexander Shvets
 */
public class FieldEntry extends Entry implements SerConstants {
  /** Type of field */
  private byte type;

  /** Name of field */
  private String name;

  /** Type description in case of non-primitive type */
  private StringEntry typeEntry;

  /** Table that contains all handles to allocated elements of serialized file */
  private HandlesTable handlesTable;

  /**
   * The constructor that creates field entry.
   *
   * @param handlesTable  table of handles
   */
  public FieldEntry(HandlesTable handlesTable) {
    this.handlesTable = handlesTable;
  }

  /**
   * Reads this entry from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException  {
    type = in.readByte();
    name = in.readUTF();

    if(type == (byte)'[' || type == (byte)'L') {
      (typeEntry = new StringEntry(handlesTable)).read(in);
    }
  }

  /**
   * Writes this entry to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    out.writeByte(type);
    out.writeUTF(name);

    if(type == (byte)'[' || type == (byte)'L') {
      typeEntry.write(out);
    }
  }

  /**
   * Gets the type of this entry.
   *
   * @return  the type of entry
   */
  public String getType() {
    return "Field";
  }

  /**
   * Gets the length of this entry in bytes.
   *
   * @return  the length of entry
   */
  public long length() {
    if(type == (byte)'[' || type == (byte)'L') {
      return 1 + (2 + name.length()) + typeEntry.length();
    }

    return 1 + (2 + name.length());
  }

  /**
   * Gets the type of field.
   *
   * @return  the type of field
   */
  public byte getTag() {
    return type;
  }

  /**
   * Gets the name of field.
   *
   * @return  the name of field
   */
  public String getName() {
    return name;
  }

  /**
   * Gets type description if this field is non-primitive.
   *
   * @return  type description if this field is non-primitive
   */
  public StringEntry getTypeEntry() {
    return typeEntry;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return  a string representation of the object.
   */
  public String toString() {
    StringBuffer sb = new StringBuffer();

    if(type == (byte)'[' || type == (byte)'L') {
      HandleSerEntry entry = typeEntry.getValue();
      sb.append(entry.getType() + "(" + entry.getHandle() + ")");
    }
    else { // primitive
      sb.append("Primitive(" + (char)type + ")");
    }

    sb.append("\t" + name);

    return sb.toString();
  }

}
