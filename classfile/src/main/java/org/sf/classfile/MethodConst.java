// MethodConst.java

package org.sf.classfile;

/**
 * Class that represents a "method" const entry.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public final class MethodConst extends MemberConst {
  public static final String TYPE = "MethodConst";

  /**
   * The default constructor that creates method constant.
   */
  public MethodConst() {}

  /**
   * Gets type of this entry.
   *
   * @return  type of entry
   */
  public String getType() {
    return TYPE;
  }

  /**
   * Gets the tag of this entry.
   *
   * @return the tag for entry
   */
  public byte getTag() {
    return CONSTANT_METHOD;
  }

  /**
   * Compares two Objects for equality. Two method consts
   * will be equals if they both have the same value and name.
   *
   * @param   object  the reference object with which to compare.
   * @return  true if this object is the same as the obj
   *          argument; false otherwise.
   */
  public boolean equals(Object object) {
    if(object != null && object instanceof MethodConst) {
      MethodConst methodConst = (MethodConst)object;
      return ((nameIndex == methodConst.nameIndex) &&
              (valueIndex == methodConst.valueIndex));
    }

    return false;
  }

}
