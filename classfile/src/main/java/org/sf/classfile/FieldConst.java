// FieldConst.java

package org.sf.classfile;

/**
 * Class that represents a "field" const entry.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public final class FieldConst extends MemberConst {

  public static final String TYPE = "FieldConst";

  /**
   * The default constructor that creates field constant.
   */
  public FieldConst() {}

  /**
   * Gets the type of this entry.
   *
   * @return  the type of entry
   */
  public String getType() {
    return TYPE;
  }

  /**
   * Get the tag of this entry.
   *
   * @return  the tag for entry
   */
  public byte getTag() {
    return CONSTANT_FIELD;
  }

  /**
   * Compares two Objects for equality. Two field consts
   * will be equals if they both have the same value-array.
   *
   * @param   object  the reference object with which to compare.
   * @return  true if this object is the same as the obj
   *          argument; false otherwise.
   */
  public boolean equals(Object object) {
    if(object != null && object instanceof FieldConst) {
      FieldConst fieldConst = (FieldConst)object;
      return ((nameIndex == fieldConst.nameIndex) &&
              (valueIndex == fieldConst.valueIndex));
    }

    return false;
  }

}
