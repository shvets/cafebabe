// MethodSignature.java

package org.sf.classfile;

import java.util.List;
import java.util.ArrayList;

/**
 * Class that represents a signature of a method.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public class MethodSignature {
  /* Signature that represents result type returned by a method */
  private Signature returnType;

  /* Array of signatures that represents types of input parameters
   * for a method */
  private Signature[] parameters;

  /**
   * Constructs a method signature.
   *
   * @param   methodSignature  the string representation of a method signature
   */
  public MethodSignature(String methodSignature) {
    List params = getParamList(methodSignature);

    parameters = new Signature[params.size()-1];

    for(int i=0; i < params.size()-1; i++) {
      parameters[i] = new Signature((String)params.get(i));
    }

    returnType = new Signature((String)params.get(params.size()-1));
  }

  /**
   * Gets the signature for return value.
   *
   * @return  the signature for return value
   */
  public Signature getReturnType() {
    return returnType;
  }

  /**
   * Gets the array of signatures for input parameters.
   *
   * @return  the array of signatures for input parameters
   */
  public Signature[] getParameters() {
    return parameters;
  }

  /** Converts the method signature into a list of strings.
   *
   * @param  methodSignature the string representation of the method signature
   * @return  the list of strings that represent this method signature
   */
  private static List getParamList(String methodSignature) {
    List params = new ArrayList();

    int pos = 0;

    if(methodSignature.charAt(pos) == '(') { // delete '('
      pos++;
    }

    while(pos < methodSignature.length()-1) {
      String param = null;

      if(methodSignature.charAt(pos) == ')') {
        pos++; // delete ')'
      }

      param = getParam(methodSignature, pos);
      params.add(param);

      pos += param.length();
    }

    return params;
  }

  /**
   * Gets single parameter from the string - list of parameters.
   *
   * @param methodSignature  the string-list with parameters
   * @param  pos  the position of selected parameter
   */
  private static String getParam(String methodSignature, int pos) {
    int pos1 = pos;

    while(methodSignature.charAt(pos1) == '[') {
      pos1++;
    }

    char ch = methodSignature.charAt(pos1);

    if(Signature.isPrimitiveType(String.valueOf(ch))) {
      pos1++;
    }
    else if(ch == 'L') {
      while(methodSignature.charAt(pos1) != ';') {
        pos1++;
      }

      pos1++;
    }

    return methodSignature.substring(pos, pos1);
  }

  /**
   * Returns a string representation of the object.
   *
   * @return  a string representation of the object.
   */
  public String toString() {
    StringBuffer sb = new StringBuffer();

    sb.append("(");

    for(int i=0; i < parameters.length; i++) {
      sb.append(parameters[i].getSignature());
    }

    sb.append(")");

    sb.append(returnType.getSignature());

    return sb.toString();
  }

}
