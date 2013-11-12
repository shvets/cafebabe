// MemberPanel.java

package org.sf.cafebabe.gadget.bodyeditor.parameter;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.sf.classfile.*;
import org.sf.classfile.instruction.*;

public class MemberPanel extends RefPanel {

  private final JTextField signatureName = new JTextField();
  private final JTextField className     = new JTextField();

  public MemberPanel(Instruction instruction, Vector types, ConstPool constPool) {
    super();

    TypesModel model = new MembersModel(types, constPool);
    typesBox.setModel(model);

    typesBox.setRenderer(new MembersListRenderer());
    typesBox.addItemListener(new TypesListener());

    showParameter();

    byte[] parameter = instruction.getParameter();
    short index = (short)Converter.getShort(parameter, 0);
    model.navigate(index);
    update(typesBox.getSelectedIndex());
  }

  protected void showParameter() {
    final JLabel nameLabel      = new JLabel("Name      :");
    final JLabel signatureLabel = new JLabel("Signature :");
    final JLabel classLabel     = new JLabel("Class     :");

    Font font1 = new Font("Monospaced", Font.BOLD, 12);

    nameLabel.setFont(font1);
    signatureLabel.setFont(font1);
    classLabel.setFont(font1);

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

    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.add(panel1);
    this.add(Box.createRigidArea(new Dimension(0, 2)));
    this.add(panel2);
    this.add(Box.createRigidArea(new Dimension(0, 2)));
    this.add(panel3);
    this.add(Box.createRigidArea(new Dimension(0, 30)));
  }

  private void update(int index) {
    Short indexWrapper = (Short)((TypesModel)typesBox.getModel()).getTypeAt(index);
    short poolIndex    = indexWrapper.shortValue();

    String text1 = ((MembersModel)typesBox.getModel()).getClass(poolIndex);
    String text2 = ((MembersModel)typesBox.getModel()).getSignature(poolIndex);

    className.setText(text1.replace('/', '.'));
    signatureName.setText(text2);
  }

class TypesListener implements ItemListener {

  public void itemStateChanged(ItemEvent e) {
    if(e.getStateChange() == ItemEvent.SELECTED) {
      update(typesBox.getSelectedIndex());
    }
  }

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

}

