// Signature.java

package org.sf.classfile;

import java.util.Map;
import java.util.HashMap;

/**
 * Class that represents the signature of the field.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public class Signature {
  /* A map of correspondences for primitive types */
  private static Map primitives = new HashMap();

  static {
    primitives.put("V", "void");
    primitives.put("B", "byte");
    primitives.put("C", "char");
    primitives.put("D", "double");
    primitives.put("F", "float");
    primitives.put("I", "int");
    primitives.put("J", "long");
    primitives.put("S", "short");
    primitives.put("Z", "boolean");
  }

  /* The string representation of the signature for the field */
  private String signature;

  /**
   * Constructs a signature with the specified key.
   *
   * @param   signature   the string representation of a signature
   */
  public Signature(String signature) {
    setSignature(signature);
  }

  /**
   * Gets the signature.
   *
   * @return  the signature
   */
  public String getSignature() {
    return signature;
  }

  /**
   * Sets the signature.
   *
   * @param  signature the signature
   */
  public void setSignature(String signature) {
    if(!isPrimitiveType(signature) && !signature.startsWith("[")) {
      if(!signature.startsWith("L") && !signature.endsWith(";")) {
        this.signature = "L" + signature + ";";
        return;
      }
    }

    this.signature = signature;
  }

  /**
   * Determines if the signature is of primitive type.
   *
   * @return  true if the signature is of primitive type; false otherwise
   */
  public boolean isPrimitiveType() {
    return isPrimitiveType(signature);
  }
  /**
   * Determines if the signature is of primitive type.
   *
   * @return  true if the signature is of primitive type; false otherwise
   */
  public static boolean isPrimitiveType(String signature) {
    if(signature.length() == 1) {
      char ch = signature.charAt(0);

      if(primitives.keySet().contains(String.valueOf(ch))) {
        return true;
      }
    }

    return false;
  }

  /**
   * Determines if the signature is of array type.
   *
   * @return  true if the signature is of array type
   */
  public boolean isArrayType() {
    return signature.startsWith("[");
  }

  /**
   * Determines if the signature is of primitive array type.
   *
   * @return  true if the signature is of primitive array type
   */
  public boolean isPrimitiveArrayType() {
    if(isArrayType()) {
      int pos = signature.lastIndexOf("[");

      String type = signature.substring(pos+1);

      return isPrimitiveType(type);
    }

    return false;
  }

  /**
   * Determines if the signature is of reference type.
   *
   * @return  true if the signature is of reference type; false otherwise
   */
  public boolean isReferenceType() {
    return signature.startsWith("L");
  }

  /**
   * Calculates the type inside signature.
   *
   * @return  the type that this signature contains
   */
  public String getTypeName() {
    return getTypeName0(signature);
  }

  /**
   * This is recursive procedure for determining the name of the type.
   *
   * @param  signature the key the type that this signature contains
   * @return  the type that this signature contains
   */
  private static String getTypeName0(String signature) {
    String typeName = (String)primitives.get(signature);

    if(typeName != null) { // base types
      return typeName;
    }
    else if(signature.startsWith("[")) { // array type
      typeName = getTypeName0(signature.substring(1));
    }
    else if(signature.startsWith("L")) { // object type
      return signature.substring(1, signature.length()-1);
    }

    return typeName;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return  a string representation of the object.
   */
  public String toString() {
    return getTypeName1(signature);
  }

  /**
   * This is recursive procedure for determining the name of the type.
   *
   * @param  signature the key the type that this signature contains
   * @return  the type that this signature contains
   */
  private static String getTypeName1(String signature) {
    String typeName = (String)primitives.get(signature);

    if(typeName != null) { // base types
      return typeName;
    }
    else if(signature.startsWith("[")) { // array type
      typeName = getTypeName0(signature.substring(1)) + "[]";
    }
    else if(signature.startsWith("L")) { // object type
      return signature.substring(1, signature.length()-1);
    }

    return typeName;
  }

}
