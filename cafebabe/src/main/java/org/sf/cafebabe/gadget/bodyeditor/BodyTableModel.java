// BodyTableModel.java

package org.sf.cafebabe.gadget.bodyeditor;

import java.io.IOException;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;
import org.sf.classfile.AttributeEntry;
import org.sf.classfile.ConstPool;
import org.sf.classfile.Converter;
import org.sf.classfile.MethodEntry;
import org.sf.classfile.attribute.CodeAttribute;
import org.sf.classfile.attribute.MethodBody;
import org.sf.classfile.instruction.Instruction;

public class BodyTableModel extends AbstractTableModel {

  final static String[] names = {"Offset", "Instruction", "Parameter"};

  private Vector offsets = new Vector();

  private CodeAttribute codeAttribute;
  private MethodBody methodBody;

  private ConstPool constPool;

  public BodyTableModel(MethodEntry methodEntry, ConstPool constPool) {
    this.constPool   = constPool;

    AttributeEntry attribute =
                   methodEntry.getAttribute(AttributeEntry.CODE, constPool);

    if(attribute != null) {
      try {
        codeAttribute = new CodeAttribute(attribute);
      }
      catch(IOException e) {
        e.printStackTrace();
      }

      methodBody = codeAttribute.getMethodBody();

      calculateOffset();
    }
  }

  private void calculateOffset() {
    offsets.removeAllElements();

    Instruction[] instructions = methodBody.getInstructions();

    for(int i=0, pos=0; i < instructions.length; i++) {
      Instruction instruction = instructions[i];

      offsets.addElement(new Short((short)pos));

      pos += instruction.length();
    }

    fireTableDataChanged();
  }

  public void editInstruction(Instruction instruction, int pos) {
    Instruction oldInstruction = methodBody.getInstruction(pos);

    if(!instruction.equals(oldInstruction)) {
      methodBody.setInstruction(instruction, pos);
      calculateOffset();
    }
  }

  public void addInstruction(Instruction instruction, int pos) {
    methodBody.insertInstruction(instruction, pos);
    calculateOffset();
  }

  public void removeInstructions(Instruction[] instructions) {
    for(int i=0; i < instructions.length; i++) {
      methodBody.removeInstruction(instructions[i]);
    }

    calculateOffset();
  }

  public short getOffset(int pos) {
    return ((Short)offsets.elementAt(pos)).shortValue();
  }

  public boolean isBodyChanged() {
    return methodBody.isBodyChanged();
  }

  public CodeAttribute getCodeAttribute() {
    return codeAttribute;
  }

  public Instruction getInstruction(int pos) {
    return methodBody.getInstruction(pos);
  }

  public int getColumnCount() {
    return names.length;
  }

  public int getRowCount() {
    return methodBody.size();
  }

  public Object getValueAt(int row, int column) {
    if(row < 0 || row > methodBody.size()-1)
      return "";

    Instruction instruction = methodBody.getInstruction(row);

    if(column == 0) {
      Short wrapper = (Short)offsets.elementAt(row);
      return Converter.toHexString(wrapper.shortValue());
    }
    else if(column == 1) {
      return instruction.getMnemonic();
    }
    else if(column == 2) {
      return instruction.resolve(constPool);
    }

    return "";
  }

  public String getTooltipAt(int row, int column) {
    if(row < 0 || row > methodBody.size()-1)
      return "";

    Instruction instruction = methodBody.getInstruction(row);

    if(column == 0) {
      Short wrapper = (Short)offsets.elementAt(row);
      return Converter.toHexString(wrapper.shortValue());
    }
    else if(column == 1) {
      return Converter.toHexString(instruction.getTag());
    }
    else if(column == 2) {
      return Converter.toHexString(instruction.getParameter(), " ");
    }

    return "";
  }

  public String getColumnName(int column) {
    return names[column];
  }

  public Class getColumnClass(int c) {
    return getValueAt(0, c).getClass();
  }

  public boolean isCellEditable(int row, int column) {
    return false;
  }

}
