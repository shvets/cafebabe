// FieldAccessFlags.java

package org.sf.classfile;

/**
 * Class that represents access modifiers for class members
 * (fields, methods, class attributes)
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public final class FieldAccessFlags extends AccessFlags {
  public static final String TYPE = "FieldAccessFlags";

  /**
   * The default constructor that creates "access flags" for field entry.
   */
  public FieldAccessFlags() {}

  /**
   * Checks the access flags for valid flags
   *
   * @return  true if all flags are correct; false otherwise
   */
  protected boolean checkAccessFlags() {
    if(isPublic() || isProtected() || isPrivate() || isPackage() ||
       isStatic() || isFinal() || isVolatile() || isTransient()) {
      return true;
    }

    return false;
  }

}
