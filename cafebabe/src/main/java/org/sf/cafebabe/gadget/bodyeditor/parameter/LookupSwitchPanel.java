// LookupSwitchPanel.java

package org.sf.cafebabe.gadget.bodyeditor.parameter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import org.sf.cafebabe.gadget.bodyeditor.util.HexField;
import org.sf.classfile.Converter;
import org.sf.classfile.instruction.Instruction;
import org.sf.classfile.instruction.LookupSwitchInstruction;

public class LookupSwitchPanel extends ParameterPanel {
  private HexField defaultField      = new HexField();
  private HexField npairsField       = new HexField();

  final PairTableModel tableModel    = new PairTableModel();
  private JTable pairTable           = new JTable();

  private JButton addButton          = new JButton("Add");
  private JButton removeButton       = new JButton("Remove");

  private short offset;
  private LookupSwitchInstruction instruction;

  public LookupSwitchPanel(short offset, Instruction instruction) {
    this.offset      = offset;
    this.instruction = (LookupSwitchInstruction)instruction;

    showParameter();
  }

  protected void showParameter() {
    final JLabel defaultLabel = new JLabel("Default:");
    final JLabel npairsLabel  = new JLabel("N pairs:");

    Font font1 = new Font("Monospaced", Font.BOLD, 12);

    defaultLabel.setFont(font1);
    npairsLabel.setFont(font1);

    byte[] parameter = instruction.getParameter();

    pairTable.setModel(tableModel);

    for(int i=0; i < parameter.length/4; i+=2) {
      int key    = Converter.getInt(parameter, i) + offset;
      int off = Converter.getInt(parameter, i+1) + offset;
      tableModel.addPair(key, off);
    }

    if(pairTable.getRowCount() > 0)
      pairTable.setRowSelectionInterval(0, 0);

    String defaultText =
           Converter.toHexString(instruction.getDefaultOffset() + offset, "");
    String npairsText  = Converter.toHexString(instruction.getNpairs(), "");

    defaultField.setText(defaultText);
    npairsField.setText(npairsText);

    npairsField.setEnabled(false);

    addButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        tableModel.addPair(0, 0);
        int row = pairTable.getRowCount()-1;
        pairTable.setRowSelectionInterval(row, row);
        Rectangle rect = pairTable.getCellRect(row, 0, false);
        pairTable.scrollRectToVisible(rect);

        removeButton.setEnabled(true);

        npairsField.setText(Converter.toHexString(pairTable.getRowCount(), ""));
      }
    });

    removeButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        int row = pairTable.getSelectedRow();
        if(row != -1) {
          tableModel.removePair(row);

          if(row > pairTable.getRowCount())
            row = pairTable.getRowCount()-1;

          pairTable.setRowSelectionInterval(row, row);
          Rectangle rect = pairTable.getCellRect(row, 0, false);
          pairTable.scrollRectToVisible(rect);

          if(pairTable.getRowCount() == 0)
            removeButton.setEnabled(false);

          npairsField.setText(Converter.toHexString(pairTable.getRowCount(), ""));
        }
      }
    });

    if(pairTable.getRowCount() ==0)
      removeButton.setEnabled(false);

    JPanel panel1 = new JPanel();
    panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
    panel1.add(Box.createRigidArea(new Dimension(3, 0)));
    panel1.add(defaultLabel);
    panel1.add(Box.createRigidArea(new Dimension(5, 0)));
    panel1.add(defaultField);
    panel1.add(Box.createRigidArea(new Dimension(60, 0)));
    panel1.add(npairsLabel);
    panel1.add(Box.createRigidArea(new Dimension(5, 0)));
    panel1.add(npairsField);
    panel1.add(Box.createRigidArea(new Dimension(60, 0)));

    JPanel panel1a = new JPanel();
    panel1a.setLayout(new BoxLayout(panel1a, BoxLayout.Y_AXIS));
    panel1a.add(Box.createRigidArea(new Dimension(3, 0)));
    panel1a.add(addButton);
    panel1a.add(Box.createRigidArea(new Dimension(3, 0)));
    panel1a.add(removeButton);
    panel1a.add(Box.createRigidArea(new Dimension(10, 0)));

    JPanel panel2 = new JPanel();
    panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
    panel2.add(Box.createRigidArea(new Dimension(3, 0)));
    panel2.add(new JScrollPane(pairTable));
    panel2.add(Box.createRigidArea(new Dimension(3, 0)));
    panel2.add(panel1a);
    panel2.add(Box.createRigidArea(new Dimension(3, 0)));

    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.add(panel1);
    this.add(Box.createRigidArea(new Dimension(0, 5)));
    this.add(panel2);
    this.add(Box.createRigidArea(new Dimension(0, 5)));
  }

  public byte[] getBytes() {
    int cnt = pairTable.getRowCount();
    byte[] buffer = new byte[2*4 + cnt*4*2];

    int defaultOffset = Integer.parseInt(defaultField.getText(), 16) - offset;
    int npairs        = Integer.parseInt(npairsField.getText(), 16);

    Converter.setInt(buffer, defaultOffset, 0);
    Converter.setInt(buffer, npairs, 1);

    for(int i=0; i < cnt; i++) {
      int key = Integer.parseInt((String)pairTable.getValueAt(i, 0), 16);
      int off = Integer.parseInt((String)pairTable.getValueAt(i, 1), 16);

      Converter.setInt(buffer, key - offset, i*2+2);
      Converter.setInt(buffer, off - offset, i*2+1+2);
    }

    return buffer;
  }

}

