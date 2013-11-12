// MethodPanel.java

package org.sf.cafebabe.gadget.bodyeditor.parameter;

import java.util.Vector;

import org.sf.classfile.ConstPool;
import org.sf.classfile.instruction.Instruction;

public class MethodPanel extends MemberPanel {

  public final String METHOD_TEXT = "Method";

  public MethodPanel(Instruction instruction, Vector types, ConstPool constPool) {
    super(instruction, types, constPool);

    setNameForBorder(METHOD_TEXT);
  }

}

