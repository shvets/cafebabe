// PrimitivePanel.java

package org.sf.cafebabe.gadget.bodyeditor.parameter;

import java.awt.*;

import javax.swing.*;
import org.sf.classfile.Converter;
import org.sf.classfile.instruction.Instruction;

public class PrimitivePanel extends RefPanel {

  public final String ARRAY_TYPE_TEXT = "Array type";

  public PrimitivePanel(Instruction instruction) {

    TypesModel model = new PrimitivesModel();
    typesBox.setModel(model);

    setNameForBorder(ARRAY_TYPE_TEXT);

    showParameter();

    byte[] parameter = instruction.getParameter();
    byte index = (byte)Converter.getByte(parameter, 0);

    model.navigate((short)index);
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

  public byte[] getBytes() {
    int index           = typesBox.getSelectedIndex();
    Short indexWrapper  = (Short)((TypesModel)typesBox.getModel()).getTypeAt(index);
    short primitiveType = indexWrapper.shortValue();

    byte[] buffer = new byte[1];

    buffer[0] = (byte)(primitiveType);

    return buffer;
  }

}

