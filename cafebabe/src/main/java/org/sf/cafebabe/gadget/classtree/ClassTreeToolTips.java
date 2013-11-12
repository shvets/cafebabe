// ClassTreeToolTips.java

package org.sf.cafebabe.gadget.classtree;

import javax.swing.tree.TreeNode;

import org.sf.classfile.ConstPool;
import org.sf.classfile.AttributeEntry;
import org.sf.classfile.attribute.MethodBody;
import org.sf.classfile.attribute.CodeAttribute;
import org.sf.cafebabe.Constants;

public class ClassTreeToolTips {

  private static String magicNumberTip  = "Magic number (4b)";
  private static String minorVersionTip = "Minor version (2b)";
  private static String majorVersionTip = "Major version (2b)";

  private static String thisClassTip    = "Name of this class (2b)";
  private static String superClassTip   = "Name of super class (2b)";

  private static String constPoolTip    = "Size of constant pool (2b)";
  private static String accessFlagsTip  = "Access flags for this class (2b)";
  private static String interfacesTip   = "Number of interfaces (2b)";
  private static String fieldsTip       = "Number of fields (2b)";
  private static String methodsTip      = "Number of methods (2b)";
  private static String exTableTip      = "Exception table's size (2b)";
  private static String exHandlerTip    = "startPc, endPc, handlerPc, catchType " +
                                          "(2b, 2b, 2b, 2b)";
  private static String bodyTip         = "code buffer's size (4b)";
  private static String collectionTip   = "Number of attributes (2b)";
  private static String attributeTip    = "Name and buffer's size (2b, 4b)";

  private static String innerClAttrTip  = "Name, buffer's size, inner classes' number " +
                                          "(2b, 4b, 2b)";
  private static String innerClassTip   = "qualified name, embraced class name, " +
                                          "short name and access flags (2b, 2b, 2b, 2b)";
  private static String sourceFileTip   = "Name of source file (2b)";
  private static String memberTip       = "Access flags, name, signature (2b, 2b, 2b)";

  //private static String codeTip         = "Name, buffer's size (2b, 4b)";
//  private static String exceptionsTip   = "Name, buffer's size, number of exceptions " +
//                                          "(2b, 4b, 2b)";
  private static String lineNumbAttrTip = "Name, buffer's size, number of lines " +
                                          "(2b, 4b, 2b)";
  private static String localVarAttrTip = "Name, buffer's size, number of variables " +
                                          "(2b, 4b, 2b)";
  private static String lineNumbTip     = "startPc, lineNumber (2b, 2b)";
  private static String localVarTip     = "startPc, length, nameIndex, descrIndex, index " +
                                          "(2b, 2b, 2b, 2b, 2b)";
  private static String exceptionTip    = "Name of exception (2b)";

  private ClassTreeToolTips() {}

  public static String getToolTipText(TreeNode node) {
    String nodeText   = node.toString();
    String parentText = node.getParent().toString();

    if(nodeText.equals(Constants.MAGIC_NUMBER_TEXT)) {
      return magicNumberTip;
    }
    else if(nodeText.equals(Constants.MINOR_VERSION_TEXT)) {
      return minorVersionTip;
    }
    else if(nodeText.equals(Constants.MAJOR_VERSION_TEXT)) {
      return majorVersionTip;
    }
    else if(nodeText.equals(ConstPool.TYPE)) {
      return constPoolTip;
    }
    else if(nodeText.equals(Constants.ACCESS_FLAGS_TEXT) ||
            parentText.equals(Constants.ACCESS_FLAGS_TEXT)) {
      return accessFlagsTip;
    }
    else if(nodeText.equals(Constants.THIS_CLASS_TEXT) ||
            parentText.equals(Constants.THIS_CLASS_TEXT)) {
      return thisClassTip;
    }
    else if(nodeText.equals(Constants.SUPER_CLASS_TEXT) ||
            parentText.equals(Constants.SUPER_CLASS_TEXT)) {
      return superClassTip;
    }
    else if(nodeText.equals(Constants.INTERFACES_TEXT)) {
      return interfacesTip;
    }
    else if(nodeText.equals(Constants.FIELDS_TEXT)) {
      return fieldsTip;
    }
    else if(nodeText.equals(Constants.METHODS_TEXT)) {
      return methodsTip;
    }
    else if(nodeText.equals(AttributeEntry.INNER_CLASSES)) {
      return innerClAttrTip;
    }
    else if(nodeText.equals(MethodBody.TYPE)) {
      return bodyTip;
    }
    else if(node instanceof AttributeNode) {
      return attributeTip;
    }
    else if(nodeText.equals(CodeAttribute.EXCEPTION_TABLE)) {
      return exTableTip;
    }
    else if(parentText.equals(CodeAttribute.EXCEPTION_TABLE)) {
      return exHandlerTip;
    }
    else if(nodeText.equals(AttributeEntry.LINE_NUMBER_TABLE)) {
      return lineNumbAttrTip;
    }
    else if(nodeText.equals(AttributeEntry.LOCAL_VARIABLE_TABLE)) {
      return localVarAttrTip;
    }
    else if(node instanceof CollectionNode) {
      return collectionTip;
    }
    else {
      if(parentText.equals(AttributeEntry.INNER_CLASSES)) {
        return innerClassTip;
      }
      else if(parentText.equals(AttributeEntry.SOURCE_FILE)) {
        return sourceFileTip;
      }
      else if(parentText.equals(Constants.FIELDS_TEXT) ||
              parentText.equals(Constants.METHODS_TEXT)) {
        return memberTip;
      }
      else if(nodeText.equals(AttributeEntry.EXCEPTIONS)) {
        return exceptionTip;
      }
      else if(parentText.equals(AttributeEntry.LOCAL_VARIABLE_TABLE)) {
        return localVarTip;
      }
      else if(parentText.equals(AttributeEntry.LINE_NUMBER_TABLE)) {
        return lineNumbTip;
      }
    }

    return nodeText;
  }

}
