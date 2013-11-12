// InterfaceEntry.java

package org.sf.classfile;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

/**
 * Class that represents an interface entry.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public final class InterfaceEntry extends Entry implements Resolvable {
  public static final String TYPE = "Interface";

  /* The index value that points to the name of this entry */
  private short nameIndex;

  /**
   * The default constructor that creates interface entry.
   */
  public InterfaceEntry() {}

  /**
   * Reads this entry from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException {
    nameIndex = in.readShort();
  }

  /**
   * Writes this entry to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    out.writeShort(nameIndex);
  }

  /**
   * Gets the index that points to the name of this entry
   *
   * @return  the index that points to the name of this entry
   */
  public short getNameIndex() {
    return nameIndex;
  }

  /**
   * Gets the type of this entry.
   *
   * @return  type of entry
   */
  public String getType() {
    return TYPE;
  }

  /**
   * Length of this entry in bytes (name index size).
   *
   * @return  length of entry
   */
  public long length() {
    return 2;
  }

  /**
   * Resolve this entry.
   *
   * @param   constPool  the pool
   * @return  the resolved information about this entry
   */
  public String resolve(Pool constPool) {
    ClassConst entry = (ClassConst)constPool.getEntry(nameIndex);

    short index = entry.getNameIndex();

    String name = ((Utf8Const)constPool.getEntry(index)).getValue();

    return name.replace('/', '.');
  }

  /**
   * Compares two Objects for equality. Two interface entries
   * will be equals if they both have the same value and name.
   *
   * @param   object  the reference object with which to compare.
   * @return  true if this object is the same as the obj
   *          argument; false otherwise.
   */
  public boolean equals(Object object) {
    if(object != null && object instanceof InterfaceEntry) {
      InterfaceEntry interfaceEntry = (InterfaceEntry)object;

      return (nameIndex == interfaceEntry.nameIndex);
    }

    return false;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return  a string representation of the object.
   */
  public String toString() {
    return Converter.toHexString(nameIndex);
  }

}
