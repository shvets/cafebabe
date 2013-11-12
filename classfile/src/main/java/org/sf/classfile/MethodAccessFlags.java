// MethodAccessFlags.java

package org.sf.classfile;

/**
 * Class that represents access modifiers for class members
 * (fields, methods, class attributes)
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public final class MethodAccessFlags extends AccessFlags {
  public static final String TYPE = "MethodAccessFlags";

  /**
   * The default constructor that creates "access flags" for method entry.
   */
  public MethodAccessFlags() {}

  /**
   * Checks the access flags for valid flags
   *
   * @return  true if all flags are correct; false otherwise
   */
  public boolean checkAccessFlags() {
    if(isPackage() || isPublic() || isPrivate() || isProtected() ||
       isStatic() || isFinal() || isSynchronized() || isNative() ||
       isAbstract()) {
      return true;
    }

    return false;
  }

}
