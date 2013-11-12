// IMethodConst.java

package org.sf.classfile;

/**
 * Class that represents a "interface method" const entry.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public final class InterfaceMethodConst extends MemberConst {
  public static final String TYPE = "InterfaceMethodConst";

  /**
   * The default constructor that creates interface method constant.
   */
  public InterfaceMethodConst() {}

  /**
   * Gets the type of this entry.
   *
   * @return  the type of entry
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
    return CONSTANT_INTERFACEMETHOD;
  }

  /**
   * Compares two Objects for equality. Two interface method consts
   * will be equals if they both have the same value and name.
   *
   * @param   object  the reference object with which to compare.
   * @return  true if this object is the same as the obj
   *          argument; false otherwise.
   */
  public boolean equals(Object object) {
    if(object != null && object instanceof InterfaceMethodConst) {
      InterfaceMethodConst imethodConst = (InterfaceMethodConst)object;
      return ((nameIndex == imethodConst.nameIndex) &&
              (valueIndex == imethodConst.valueIndex));
    }

    return false;
  }

}
