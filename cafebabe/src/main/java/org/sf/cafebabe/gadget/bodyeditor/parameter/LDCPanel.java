// LDCPanel.java

package org.sf.cafebabe.gadget.bodyeditor.parameter;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.sf.classfile.*;
import org.sf.classfile.instruction.*;

public class LDCPanel extends RefPanel {

  public final String LDC_TEXT = "LDC command";

  private final String STRING  = "String";
  private final String INTEGER = "Integer";
  private final String FLOAT   = "Float";

  protected JComboBox groupBox = new JComboBox();

  private Vector integers;
  private Vector floats;
  private Vector strings;
  private ConstPool constPool;

  public LDCPanel(Instruction instruction, Vector integers,
                  Vector floats, Vector strings, ConstPool constPool) {

    this.integers  = integers;
    this.floats    = floats;
    this.strings   = strings;
    this.constPool = constPool;

    setNameForBorder(LDC_TEXT);

    showParameter();

    byte[] parameter = instruction.getParameter();

    short index = 0;

    if((instruction.getTag() & 0xff) == Opcode.LDC ||
       (instruction.getTag() & 0xff) == Opcode.LDC_QUICK) {
      index = (short)Converter.getByte(parameter, 0);
    }
    else {
      index = (short)Converter.getShort(parameter, 0);
    }

    TypesModel model = null;

    PoolEntry entry = (PoolEntry)constPool.get(index);
    if(index == 0 || entry instanceof StringConst) {
      groupBox.setSelectedItem(STRING);
      model = new SinglesModel(strings, constPool);
      typesBox.setModel(model);
    }
    else if(entry instanceof IntegerConst) {
      groupBox.setSelectedItem(INTEGER);
      model = new SinglesModel(integers, constPool);
      typesBox.setModel(model);
    }
    else if(entry instanceof FloatConst) {
      groupBox.setSelectedItem(FLOAT);
      model = new SinglesModel(floats, constPool);
      typesBox.setModel(model);
    }

    model.navigate(index);
  }

  protected void showParameter() {
    groupBox.addItem(STRING);
    groupBox.addItem(INTEGER);
    groupBox.addItem(FLOAT);

    JLabel groupLabel = new JLabel("Group:");
    JLabel valueLabel = new JLabel("Value:");

    Font font = new Font("Monospaced", Font.BOLD, 12);

    groupLabel.setFont(font);
    valueLabel.setFont(font);

    groupBox.addItemListener(new GroupListener());

    JPanel panel1 = new JPanel();
    panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
    panel1.add(Box.createRigidArea(new Dimension(3, 0)));
    panel1.add(groupLabel);
    panel1.add(Box.createRigidArea(new Dimension(3, 0)));
    panel1.add(groupBox);
    panel1.add(Box.createRigidArea(new Dimension(3, 0)));

    JPanel panel2 = new JPanel();
    panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
    panel2.add(Box.createRigidArea(new Dimension(3, 0)));
    panel2.add(valueLabel);
    panel2.add(Box.createRigidArea(new Dimension(3, 0)));
    panel2.add(typesBox);
    panel2.add(Box.createRigidArea(new Dimension(3, 0)));

    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.add(Box.createRigidArea(new Dimension(0, 2)));
    this.add(panel1);
    this.add(Box.createRigidArea(new Dimension(0, 2)));
    this.add(panel2);
    this.add(Box.createRigidArea(new Dimension(0, 2)));
  }

class GroupListener implements ItemListener {

  public void itemStateChanged(ItemEvent e) {
    if(e.getStateChange() == ItemEvent.SELECTED) {
      String item = (String)e.getItem();

      if(item.equals(STRING)) {
        TypesModel model = new SinglesModel(strings, constPool);
        typesBox.setModel(model);
      }
      else if(item.equals(INTEGER)) {
        TypesModel model = new SinglesModel(integers, constPool);
        typesBox.setModel(model);
      }
      else if(item.equals(FLOAT)) {
        TypesModel model = new SinglesModel(floats, constPool);
        typesBox.setModel(model);
      }

      if(typesBox.getItemCount() == 0) {
        typesBox.setEnabled(false);
      }
      else {
        typesBox.setEnabled(true);
      }
    }
  }

}

}
