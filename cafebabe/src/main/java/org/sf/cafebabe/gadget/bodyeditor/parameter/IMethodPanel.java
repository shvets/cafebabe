// IMethodPanel.java

package org.sf.cafebabe.gadget.bodyeditor.parameter;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.sf.classfile.*;
import org.sf.classfile.instruction.*;
import org.sf.cafebabe.gadget.bodyeditor.util.*;

public class IMethodPanel extends RefPanel {

  public final String IMETHOD_TEXT = "Interface method";

  private final JTextField signatureName = new JTextField();
  private final JTextField className     = new JTextField();
  private final DigitField argsName      = new DigitField(2);

  public IMethodPanel(Instruction instruction, Vector types, ConstPool constPool) {
    super();

    setNameForBorder(IMETHOD_TEXT);

    TypesModel model = new MembersModel(types, constPool);
    typesBox.setModel(model);

    typesBox.setRenderer(new MembersListRenderer());
    typesBox.addItemListener(new TypesListener());

    showParameter();

    byte[] parameter = instruction.getParameter();
    short index = (short)Converter.getShort(parameter, 0);
    model.navigate(index);

    byte number = (byte)Converter.getByte(parameter, 2);
    argsName.setText("" + (short)number);
  }

  protected void showParameter() {
    final JLabel nameLabel      = new JLabel("Name      :");
    final JLabel signatureLabel = new JLabel("Signature :");
    final JLabel classLabel     = new JLabel("Class     :");
    final JLabel argsLabel      = new JLabel("Args      :");

    Font font1 = new Font("Monospaced", Font.BOLD, 12);

    nameLabel.setFont(font1);
    signatureLabel.setFont(font1);
    classLabel.setFont(font1);
    argsLabel.setFont(font1);

    signatureName.setEditable(false);
    className.setEditable(false);

    JPanel panel1 = new JPanel();
    panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
    panel1.add(Box.createRigidArea(new Dimension(3, 0)));
    panel1.add(nameLabel);
    panel1.add(Box.createRigidArea(new Dimension(5, 0)));
    panel1.add(typesBox);
    panel1.add(Box.createRigidArea(new Dimension(3, 0)));

    JPanel panel2 = new JPanel();
    panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
    panel2.add(Box.createRigidArea(new Dimension(3, 0)));
    panel2.add(signatureLabel);
    panel2.add(Box.createRigidArea(new Dimension(5, 0)));
    panel2.add(signatureName);
    panel2.add(Box.createRigidArea(new Dimension(3, 0)));

    JPanel panel3 = new JPanel();
    panel3.setLayout(new BoxLayout(panel3, BoxLayout.X_AXIS));
    panel3.add(Box.createRigidArea(new Dimension(3, 0)));
    panel3.add(classLabel);
    panel3.add(Box.createRigidArea(new Dimension(5, 0)));
    panel3.add(className);
    panel3.add(Box.createRigidArea(new Dimension(3, 0)));

    JPanel panel4 = new JPanel();
    panel4.setLayout(new BoxLayout(panel4, BoxLayout.X_AXIS));
    panel4.add(Box.createRigidArea(new Dimension(3, 0)));
    panel4.add(argsLabel);
    panel4.add(Box.createRigidArea(new Dimension(5, 0)));
    panel4.add(argsName);
    panel4.add(Box.createRigidArea(new Dimension(280, 0)));

    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.add(panel1);
    this.add(Box.createRigidArea(new Dimension(0, 2)));
    this.add(panel2);
    this.add(Box.createRigidArea(new Dimension(0, 2)));
    this.add(panel3);
    this.add(Box.createRigidArea(new Dimension(0, 2)));
    this.add(panel4);
    this.add(Box.createRigidArea(new Dimension(0, 2)));
  }

  public byte[] getBytes() {
    int index          = typesBox.getSelectedIndex();
    Short indexWrapper = (Short)((TypesModel)typesBox.getModel()).getTypeAt(index);
    short poolIndex    = indexWrapper.shortValue();

    byte[] buffer = new byte[4];

    buffer[0] = (byte)(poolIndex >> 8);
    buffer[1] = (byte)((poolIndex << 8) >> 8);
    buffer[2] = Byte.parseByte(argsName.getText());
    buffer[3] = (byte)0;

    return buffer;
  }

class MembersListRenderer extends DefaultListCellRenderer  {

  public Component getListCellRendererComponent(JList listbox, Object value,
                      int index, boolean isSelected, boolean cellHasFocus) {

    String text = value.toString();

    StringTokenizer st = new StringTokenizer(text);
    st.nextToken();

    String name = st.nextToken();

    return super.getListCellRendererComponent(
                 listbox, name, index, isSelected, cellHasFocus);
  }
}

class TypesListener implements ItemListener {

  public void itemStateChanged(ItemEvent e) {
    if(e.getStateChange() == ItemEvent.SELECTED) {
      int index          = typesBox.getSelectedIndex();
      Short indexWrapper = (Short)((TypesModel)typesBox.getModel()).getTypeAt(index);
      short poolIndex    = indexWrapper.shortValue();

      String text1 = ((MembersModel)typesBox.getModel()).getClass(poolIndex);
      String text2 = ((MembersModel)typesBox.getModel()).getSignature(poolIndex);

      className.setText(text1.replace('/', '.'));
      signatureName.setText(text2);
    }
  }

}

}

