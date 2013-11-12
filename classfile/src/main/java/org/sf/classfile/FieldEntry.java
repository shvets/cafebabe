// FieldEntry.java

package org.sf.classfile;

import java.io.IOException;
import java.io.DataOutput;

/**
 * Class that represents a "field" entry.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public final class FieldEntry extends MemberEntry {
  public static final String TYPE = "Field";

  /**
   * The default constructor that creates field entry.
   */
  public FieldEntry() {
    accessFlags = new FieldAccessFlags();
  }

  /**
   * Gets type of this entry.
   *
   * @return  type of entry
   */
  public String getType() {
    return TYPE;
  }

  /**
   * Writes this entry to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    super.write(out);
  }

  /**
   * Resolves this entry.
   *
   * @param   constPool  the pool
   * @return  the resolved information in form of an array
   */
  public String resolve(Pool constPool) {
    Signature signature = new Signature(getSignature(constPool));

    return accessFlags.toString() + " " + signature + " " +
           ((Utf8Const)constPool.getEntry(nameIndex)).getValue();
  }

  /**
   * Compares two Objects for equality. Two field entries
   * will be equals if they both have the same value and name.
   *
   * @param   object  the reference object with which to compare.
   * @return  true if this object is the same as the obj
   *          argument; false otherwise.
   */
  public boolean equals(Object object) {
    if(object != null && object instanceof FieldEntry) {
      FieldEntry fieldEntry = (FieldEntry)object;

      return ((nameIndex == fieldEntry.nameIndex) &&
              (valueIndex == fieldEntry.valueIndex));
    }

    return false;
  }

}
