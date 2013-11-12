// MethodEntry.java

package org.sf.classfile;

/**
 * Class that represents a "method" entry.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public final class MethodEntry extends MemberEntry {
  public static final String TYPE = "Method";

  /**
   * The default constructor that creates method entry.
   */
  public MethodEntry() {
    accessFlags = new MethodAccessFlags();
  }

  /**
   * Gets the type of this entry.
   *
   * @return  the type of entry
   */
  public String getType() {
    return TYPE;
  }

  /**
   * Gets an attribute with the specified name on base of the information
   * from constant pool.
   * @param name  the name of an attribute
   * @param     constPool constant pool that contains information for
   *            resolution process
   * @return    founded attribute or null if specified attribute doesn't exist.
   */
  public AttributeEntry getAttribute(String name, ConstPool constPool) {
    for(short i=0; i < attributes.size(); i++) {
      AttributeEntry attribute = (AttributeEntry)attributes.get(i);
      String attributeName = attribute.resolve(constPool);

      if(attributeName.equals(name)) {
        return attribute;
      }
    }

    return null;
  }

  /**
   * Resolve this entry.
   *
   * @param   constPool  the pool
   * @return  the resolved information about this entry
   */
  public String resolve(Pool constPool) {
    MethodSignature signature = new MethodSignature(getSignature(constPool));

    Signature returnType   = signature.getReturnType();
    Signature[] parameters = signature.getParameters();

    StringBuffer sb = new StringBuffer();
    if(parameters.length > 0) {
      sb.append(parameters[0].getTypeName());
    }

    for(int i=1; i < parameters.length; i++) {
      sb.append(", " + parameters[i]/*.getTypeName()*/);
    }

    return accessFlags.toString() + " " + returnType/*.getTypeName()*/ + " " +
           ((Utf8Const)constPool.getEntry(nameIndex)).getValue() +
           "(" + sb.toString() + ")";
  }

  /**
   * Compares two Objects for equality. Two method entries
   * will be equals if they both have the same value and name.
   *
   * @param   object  the reference object with which to compare.
   * @return  true if this object is the same as the obj
   *          argument; false otherwise.
   */
  public boolean equals(Object object) {
    if(object != null && object instanceof MethodEntry) {
      MethodEntry methodEntry = (MethodEntry)object;

      return ((nameIndex == methodEntry.nameIndex) &&
              (valueIndex == methodEntry.valueIndex));
    }

    return false;
  }

}
