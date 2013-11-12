// Values.java

package org.sf.serfile;

import java.io.DataInput;
import java.io.IOException;

/**
 * contents: content | (contents content)
 *
 * @version 1.0 05/25/2000
 * @author Alexander Shvets
 */
public class Values extends PlainCollection {

  private EntryCollection fields;

  /**
   * Creates collection of value entries.
   *
   * @param handlesTable  table of handles
   * @param fields  fields
   */
  public Values(HandlesTable handlesTable, EntryCollection fields) {
    super(ValueEntry.class, "Values", handlesTable);

    this.fields = fields;
  }

  /**
   * Reads this collection from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException {
    for(short i=0; i < fields.size(); i++) {
      FieldEntry fieldEntry = (FieldEntry)fields.get(i);

      byte tag    = fieldEntry.getTag();
      String name = fieldEntry.getName();
      String type = null;

      StringEntry typeEntry = fieldEntry.getTypeEntry();

      if(typeEntry != null) {
        type = typeEntry.getString();
      }

      ValueEntry value = new ValueEntry(tag, type, name, handlesTable);
      value.read(in);

      entries.add(value);
    }
  }

  /**
   * Returns a string representation of the object.
   *
   * @return  a string representation of the object.
   */
  public String toString() {
    StringBuffer sb = new StringBuffer();

    sb.append("{\n");

    for(short i=0; i < entries.size(); i++) {
      sb.append("  " + entries.get(i) + "\n");
    }

    sb.append("}");

    return sb.toString();
  }

}
