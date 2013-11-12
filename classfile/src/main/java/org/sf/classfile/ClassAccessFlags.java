// ClassAccessFlags.java

package org.sf.classfile;

/**
 * Class that represents access modifiers for class members
 * (fields, methods, class attributes)
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public final class ClassAccessFlags extends AccessFlags {
  public static final String TYPE = "ClassAccessFlags";

  /**
   * The default constructor that creates "access flags" for class file.
   */
  public ClassAccessFlags() {}

  /**
   * Checks the access flags for valid flags
   *
   * @return  true if all flags are correct; false otherwise
   */
  public boolean checkAccessFlags() {
    if(isPublic() || isProtected() || isPrivate() || isPackage() ||
       isFinal() || isSynchronized() || isInterface() || isAbstract()) {
      return true;
    }

    return false;
  }

}
