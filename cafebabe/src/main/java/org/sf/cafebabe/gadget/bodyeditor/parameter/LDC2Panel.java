// LDC2Panel.java

package org.sf.cafebabe.gadget.bodyeditor.parameter;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.sf.classfile.*;
import org.sf.classfile.instruction.*;

public class LDC2Panel extends RefPanel {

  public final String LDC2_TEXT = "LDC2 command";

  private final String DOUBLE   = "Double";
  private final String LONG     = "Long";
  private final String STRING   = "String";

  protected JComboBox groupBox = new JComboBox();

  private Vector doubles;
  private Vector longs;
  private Vector strings;
  private ConstPool constPool;

  public LDC2Panel(Instruction instruction, Vector doubles,
                  Vector longs, Vector strings, ConstPool constPool) {

    this.doubles   = doubles;
    this.longs     = longs;
    this.strings   = strings;
    this.constPool = constPool;

    setNameForBorder(LDC2_TEXT);

    showParameter();

    byte[] parameter = instruction.getParameter();

    short index = (short)Converter.getShort(parameter, 0);

    TypesModel model = null;

    PoolEntry entry = (PoolEntry)constPool.get(index);
    if(index == 0 || entry instanceof DoubleConst) {
      groupBox.setSelectedItem(DOUBLE);
      model = new SinglesModel(doubles, constPool);
      typesBox.setModel(model);
    }
    else if(entry instanceof LongConst) {
      groupBox.setSelectedItem(LONG);
      model = new SinglesModel(longs, constPool);
      typesBox.setModel(model);
    }
    else if(entry instanceof StringConst) {
      groupBox.setSelectedItem(STRING);
      model = new SinglesModel(strings, constPool);
      typesBox.setModel(model);
    }

    model.navigate(index);
  }

  protected void showParameter() {
    groupBox.addItem(DOUBLE);
    groupBox.addItem(LONG);
    groupBox.addItem(STRING);

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

      if(item.equals(DOUBLE)) {
        TypesModel model = new SinglesModel(doubles, constPool);
        typesBox.setModel(model);
      }
      else if(item.equals(LONG)) {
        TypesModel model = new SinglesModel(longs, constPool);
        typesBox.setModel(model);
      }
      else if(item.equals(STRING)) {
        TypesModel model = new SinglesModel(strings, constPool);
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
