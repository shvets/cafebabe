// SinglePanel.java

package org.sf.cafebabe.gadget.bodyeditor.parameter;

import java.awt.*;
import java.util.Vector;

import javax.swing.*;
import org.sf.classfile.ConstPool;
import org.sf.classfile.Converter;
import org.sf.classfile.instruction.Instruction;

public class SinglePanel extends RefPanel {

  public SinglePanel(Instruction instruction, Vector types, ConstPool constPool) {
    TypesModel model = new SinglesModel(types, constPool);
    typesBox.setModel(model);

    showParameter();

    byte[] parameter = instruction.getParameter();
    short index = (short)Converter.getShort(parameter, 0);
    model.navigate(index);
  }

  protected void showParameter() {
    JPanel panel1 = new JPanel();
    panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
    panel1.add(Box.createRigidArea(new Dimension(3, 0)));
    panel1.add(typesBox);
    panel1.add(Box.createRigidArea(new Dimension(3, 0)));

    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.add(Box.createRigidArea(new Dimension(0, 2)));
    this.add(panel1);
    this.add(Box.createRigidArea(new Dimension(0, 2)));
  }

}

