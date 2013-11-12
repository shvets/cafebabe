// FieldPanel.java

package org.sf.cafebabe.gadget.bodyeditor.parameter;

import java.util.Vector;

import org.sf.classfile.ConstPool;
import org.sf.classfile.instruction.Instruction;

public class FieldPanel extends MemberPanel {

  public final String FIELD_TEXT = "Field";

  public FieldPanel(Instruction instruction, Vector types, ConstPool constPool) {
    super(instruction, types, constPool);

    setNameForBorder(FIELD_TEXT);
  }

}

