// ClassPanel.java

package org.sf.cafebabe.gadget.bodyeditor.parameter;

import java.util.Vector;

import org.sf.classfile.ConstPool;
import org.sf.classfile.instruction.Instruction;

public class ClassPanel extends SinglePanel {

  public final String CLASS_TEXT = "Class";

  public ClassPanel(Instruction instruction, Vector types, ConstPool constPool) {
    super(instruction, types, constPool);

    setNameForBorder(CLASS_TEXT);
  }

}

