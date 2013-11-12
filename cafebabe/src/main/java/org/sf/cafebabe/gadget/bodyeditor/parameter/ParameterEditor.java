// ParameterEditor.java

package org.sf.cafebabe.gadget.bodyeditor.parameter;

import java.awt.*;
import java.util.Vector;

import javax.swing.*;
import org.sf.classfile.*;
import org.sf.classfile.instruction.*;

public class ParameterEditor extends JPanel implements Opcode {

  private Vector fields   = new Vector();
  private Vector methods  = new Vector();
  private Vector imethods = new Vector();
  private Vector strings  = new Vector();
  private Vector integers = new Vector();
  private Vector floats   = new Vector();
  private Vector doubles  = new Vector();
  private Vector longs    = new Vector();
  private Vector sClasses = new Vector();
  private Vector mClasses = new Vector();

  private RankTable rankTable = ParameterRanking.getInstance();

  private ParameterPanel parameterPanel;

  private boolean isPossible;

  private ConstPool constPool;

  public ParameterEditor(ConstPool constPool) {
    super();

    this.constPool = constPool;

    setLayout(new BorderLayout());

    collectInfo();
  }

  private void collectInfo() {
    for(short i=1; i < constPool.size(); i++) {
      PoolEntry e = (PoolEntry)constPool.get(i);
      if(e == null) continue;

      if(e instanceof FieldConst) {
        fields.addElement(new Short(i));
      }
      else if(e instanceof InterfaceMethodConst) {
        imethods.addElement(new Short(i));
      }
      else if(e instanceof MethodConst) {
        methods.addElement(new Short(i));
      }
      else if(e instanceof StringConst) {
        strings.addElement(new Short(i));
      }
      else if(e instanceof IntegerConst) {
        integers.addElement(new Short(i));
      }
      else if(e instanceof FloatConst) {
        floats.addElement(new Short(i));
      }
      else if(e instanceof DoubleConst) {
        doubles.addElement(new Short(i));
      }
      else if(e instanceof LongConst) {
        longs.addElement(new Short(i));
      }
      else if(e instanceof ClassConst) {
        String str = e.resolve(constPool);

        if(str.charAt(0) == '[')
          mClasses.addElement(new Short(i));
        else
          sClasses.addElement(new Short(i));
      }
    }
  }

  public void setInstruction(short offset, Instruction instruction) {
    this.removeAll();
    this.validate();
    this.repaint();

    parameterPanel = null;
    isPossible     = true;

    byte tag     = instruction.getTag();
    boolean wide = instruction.isWide();

    OpcodeDescription description = OpcodeTable.getDescription(tag);

    int length = description.length-1;

    if(length == 0) {
      parameterPanel = new NonePanel();
    }
    else {
      String group = rankTable.getRank(tag).key;

      if(group.equals(ParameterRanking.PLAIN_RANK)) {
        parameterPanel = new HexPanel(instruction, length * ((wide) ? 2 : 1));
      }
      else if(group.equals(ParameterRanking.SWITCH_RANK)) {
        if(instruction instanceof TableSwitchInstruction) {
          parameterPanel = new TableSwitchPanel(offset, instruction);
        }
        else if(instruction instanceof LookupSwitchInstruction) {
          parameterPanel = new LookupSwitchPanel(offset, instruction);
        }
      }
      else if(description instanceof RefOpcodeDescription) {
        if(group.equals(ParameterRanking.FIELD_RANK)) {
          if(fields.size() == 0) {
            isPossible = false;
            parameterPanel = new UnavailablePanel();
          }
          else {
            parameterPanel = new FieldPanel(instruction, fields, constPool);
          }
        }
        else if(group.equals(ParameterRanking.METHOD_RANK)) {
          if(methods.size() == 0) {
            isPossible = false;
            parameterPanel = new UnavailablePanel();
          }
          else {
            parameterPanel = new MethodPanel(instruction, methods, constPool);
          }
        }
        else if(group.equals(ParameterRanking.IMETHOD_RANK)) {
          if(imethods.size() == 0) {
            isPossible = false;
            parameterPanel = new UnavailablePanel();
          }
          else {
            parameterPanel = new IMethodPanel(instruction, imethods, constPool);
          }
        }
        else if(group.equals(ParameterRanking.LDC_RANK)) {
          if(integers.size() == 0 && floats.size() == 0 &&
             strings.size() == 0) {
            isPossible = false;
            parameterPanel = new UnavailablePanel();
          }
          else {
            parameterPanel = new LDCPanel(instruction, integers, floats, strings, constPool);
          }
        }
        else if(group.equals(ParameterRanking.LDC2_RANK)) {
          if(doubles.size() == 0 && longs.size() == 0) {
            isPossible = false;
            parameterPanel = new UnavailablePanel();
          }
          else {
            parameterPanel = new LDC2Panel(instruction, doubles, longs, strings, constPool);
          }
        }
        else if(group.equals(ParameterRanking.PRIMITIVE_RANK)) {
          parameterPanel = new PrimitivePanel(instruction);
        }
        else if(group.equals(ParameterRanking.CLASS_RANK)) {
          parameterPanel = new ClassPanel(instruction, sClasses, constPool);
        }
        else if(group.equals(ParameterRanking.MULTI_RANK)) {
          if(mClasses.size() == 0) {
            isPossible = false;
            parameterPanel = new UnavailablePanel();
          }
          else {
            parameterPanel = new MultiDimPanel(instruction, mClasses, constPool);
          }
        }
      }
    }

    this.add(parameterPanel, BorderLayout.CENTER);
  }

  public byte[] getBytes() {
    return parameterPanel.getBytes();
  }

  public boolean isPossible() {
    return isPossible;
  }

}

