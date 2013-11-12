// ArraySerEntry.java

package org.sf.serfile;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

/**
 * Class that represents a serialized array entry.
 *
 * ArraySer: TC_ARRAY ClassDesc newHandle (int)<size> values[size]
 *
 * newHandle: "The next number in sequence is assigned to the object
 *             being serialized or deserialized"
 *
 * values: "The size and types are described by the
 *         ClassDesc for the current object"
 *
 * @version 1.0 05/25/2000
 * @author Alexander Shvets
 */
public class ArraySerEntry extends HandleSerEntry {
  /** Class description for this object entry */
  private ClassDescEntry classDescEntry;

  /** Array of values for this array entry*/
  private ValueEntry[] arrayEntries;

  /**
   * The default constructor that creates serialized array entry
   *
   * @param handlesTable  table of handles
   */
  public ArraySerEntry(HandlesTable handlesTable) {
    super(handlesTable);
  }

  /**
   * Reads this entry from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException  {
    (classDescEntry = new ClassDescEntry(handlesTable)).read(in);

    handle = handlesTable.put(this);

    int size = in.readInt();

    arrayEntries = new ValueEntry[size];

    String className = classDescEntry.getClassDescSerEntry().getClassName();
    String newClassName = className.substring(1);

    for(int i=0; i < size; i++) {
      byte tag = (byte)newClassName.charAt(0);

      ValueEntry valueEntry = new ValueEntry(tag, newClassName, "arrayComponent", handlesTable);
      valueEntry.read(in);

      arrayEntries[i] = valueEntry;
    }
  }

  /**
   * Writes this entry to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    out.write(getTag());

    classDescEntry.write(out);

    int size = arrayEntries.length;
    out.writeInt(size);

    for(int i=0; i < size; i++) {
      arrayEntries[i].write(out);
    }
  }

  /**
   * Gets the type of this entry.
   *
   * @return  type of entry
   */
  public String getType() {
    return "Array";
  }

  /**
   * Get the length of this entry in bytes.
   *
   * @return  the length of the entry
   */
  public long length() {
    long arrCnt = 0;
    for(int i=0; i < arrayEntries.length; i++) {
      arrCnt += arrayEntries[i].length();
    }

    return 1 + classDescEntry.length() + 4 + arrCnt;
  }

  /**
   * Gets the tag of this entry.
   *
   * @return  the tag for the entry
   */
  public byte getTag() {
    return TC_ARRAY;
  }

  /**
   * Get class description
   *
   * @return   class description
   */
  public ClassDescEntry getClassDescEntry() {
    return classDescEntry;
  }

  /**
   * Get values
   *
   * @return   values
   */
  public ValueEntry[] getArrayEntries() {
    return arrayEntries;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return  a string representation of the object.
   */
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append(super.toString());
    sb.append(" -> ");

    ClassDescSerEntry classDescSerEntry = classDescEntry.getClassDescSerEntry();
    sb.append(classDescSerEntry.getType() + "(" + classDescSerEntry.getHandle() + ")");
    sb.append(" ->\n");

    sb.append("{\n");

    for(int i=0; i < arrayEntries.length; i++) {
      Entry e = arrayEntries[i];
      sb.append("" + e + "\n");
    }

    sb.append("}\n");

    return sb.toString();
  }

}
