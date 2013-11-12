// MultiDimPanel.java

package org.sf.cafebabe.gadget.bodyeditor.parameter;

import java.awt.*;
import java.util.Vector;

import javax.swing.*;
import org.sf.cafebabe.gadget.bodyeditor.util.DigitField;
import org.sf.classfile.ConstPool;
import org.sf.classfile.Converter;
import org.sf.classfile.instruction.Instruction;

public class MultiDimPanel extends RefPanel {

  private final String MULTI_DIM_TEXT = "Multi dimensional array";

  private DigitField dimNumber = new DigitField(3);

  public MultiDimPanel(Instruction instruction, Vector types, ConstPool constPool) {
    TypesModel model = new SinglesModel(types, constPool);
    typesBox.setModel(model);

    setNameForBorder(MULTI_DIM_TEXT);

    showParameter();

    byte[] parameter = instruction.getParameter();
    short index = (short)Converter.getShort(parameter, 0);
    model.navigate(index);

    int number = Converter.getByte(parameter, 2);
    dimNumber.setText("" + number);
  }

  protected void showParameter() {
    final JLabel dimsLabel = new JLabel("Dimension:");

    Font font1 = new Font("Monospaced", Font.BOLD, 12);

    dimsLabel.setFont(font1);

    dimNumber.setRange(0, 255);

//    typesBox.addItemListener(new ClassItemListener());

    JPanel panel1 = new JPanel();
    panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
    panel1.add(Box.createRigidArea(new Dimension(3, 0)));
    panel1.add(typesBox);
    panel1.add(Box.createRigidArea(new Dimension(3, 0)));

    JPanel panel2 = new JPanel();
    panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
    panel2.add(Box.createRigidArea(new Dimension(3, 0)));
    panel2.add(dimsLabel);
    panel2.add(Box.createRigidArea(new Dimension(5, 0)));
    panel2.add(dimNumber);
    panel2.add(Box.createRigidArea(new Dimension(270, 0)));

    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.add(Box.createRigidArea(new Dimension(0, 2)));
    this.add(panel1);
    this.add(Box.createRigidArea(new Dimension(0, 2)));
    this.add(panel2);
    this.add(Box.createRigidArea(new Dimension(0, 50)));
  }

  public byte[] getBytes() {
    int index          = typesBox.getSelectedIndex();
    Short indexWrapper = (Short)((TypesModel)typesBox.getModel()).getTypeAt(index);
    short poolIndex    = indexWrapper.shortValue();

    byte[] buffer = new byte[3];

    buffer[0] = (byte)(poolIndex >> 8);
    buffer[1] = (byte)((poolIndex << 8) >> 8);
    buffer[2] = (byte)Integer.parseInt(dimNumber.getText());

    return buffer;
  }

/*class ClassItemListener implements ItemListener {

  public void itemStateChanged(ItemEvent e) {
    if(e.getStateChange() == ItemEvent.SELECTED) {
      setDimension((String)e.getItem());
    }
  }

  protected void setDimension(String pattern) {
    int cnt = 0;
    for(;pattern.charAt(cnt) != '[';cnt++);

    dimNumber.setText("1");
    dimNumber.setRange(1, cnt);
  }

}
*/

}

