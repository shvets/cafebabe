// TableSwitchPanel.java

package org.sf.cafebabe.gadget.bodyeditor.parameter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.sf.cafebabe.gadget.bodyeditor.util.HexField;
import org.sf.classfile.Converter;
import org.sf.classfile.instruction.Instruction;
import org.sf.classfile.instruction.TableSwitchInstruction;

public class TableSwitchPanel extends ParameterPanel {
  private HexField defaultField    = new HexField();
  private HexField lowField        = new HexField();
  private HexField highField       = new HexField();

  final LabelTableModel tableModel = new LabelTableModel();
  private JTable labelTable        = new JTable();

  private JButton addButton        = new JButton("Add");
  private JButton removeButton     = new JButton("Remove");

  private short offset;
  private TableSwitchInstruction instruction;

  public TableSwitchPanel(short offset, Instruction instruction) {
    this.offset      = offset;
    this.instruction = (TableSwitchInstruction)instruction;

    showParameter();
  }

  protected void showParameter() {
    final JLabel defaultLabel = new JLabel("Default:");
    final JLabel lowLabel     = new JLabel("Low    :");
    final JLabel highLabel    = new JLabel("High   :");

    Font font1 = new Font("Monospaced", Font.BOLD, 12);

    defaultLabel.setFont(font1);
    lowLabel.setFont(font1);
    highLabel.setFont(font1);

    byte[] parameter = instruction.getParameter();

    labelTable.setModel(tableModel);

    for(int i=0; i < parameter.length/4; i++) {
      tableModel.addLabel(Converter.getInt(parameter, i) + offset);
    }

    if(labelTable.getRowCount() > 0)
      labelTable.setRowSelectionInterval(0, 0);

    String defaultText =
           Converter.toHexString(instruction.getDefaultOffset() + offset, "");
    String lowText  = Converter.toHexString(instruction.getLow(), "");
    String highText = Converter.toHexString(instruction.getHigh(), "");

    defaultField.setText(defaultText);
    lowField.setText(lowText);
    highField.setText(highText);

    highField.setEnabled(false);

    addButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        tableModel.addLabel(0);
        int row = labelTable.getRowCount()-1;
        labelTable.setRowSelectionInterval(row, row);
        Rectangle rect = labelTable.getCellRect(row, 0, false);
        labelTable.scrollRectToVisible(rect);

        removeButton.setEnabled(true);

        highField.setText(Converter.toHexString(calculateHighValue(), ""));
      }
    });

    removeButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        int row = labelTable.getSelectedRow();
        if(row != -1) {
          tableModel.removeLabel(row);

          if(row > labelTable.getRowCount())
            row = labelTable.getRowCount()-1;

          labelTable.setRowSelectionInterval(row, row);
          Rectangle rect = labelTable.getCellRect(row, 0, false);
          labelTable.scrollRectToVisible(rect);

          if(labelTable.getRowCount() == 0)
            removeButton.setEnabled(false);

          highField.setText(Converter.toHexString(calculateHighValue(), ""));
        }
      }
    });

    if(labelTable.getRowCount() ==0)
      removeButton.setEnabled(false);

    lowField.getDocument().addDocumentListener(new DocumentListener() {
      public void changedUpdate(DocumentEvent e) {
        highField.setText(Converter.toHexString(calculateHighValue(), ""));
      }

      public void insertUpdate(DocumentEvent e) {
        highField.setText(Converter.toHexString(calculateHighValue(), ""));
      }

      public void removeUpdate(DocumentEvent e) {
        highField.setText(Converter.toHexString(calculateHighValue(), ""));
      }

    });

    JPanel panel1 = new JPanel();
    panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
    panel1.add(Box.createRigidArea(new Dimension(3, 0)));
    panel1.add(defaultLabel);
    panel1.add(Box.createRigidArea(new Dimension(5, 0)));
    panel1.add(defaultField);
    panel1.add(Box.createRigidArea(new Dimension(5, 0)));
    panel1.add(lowLabel);
    panel1.add(Box.createRigidArea(new Dimension(5, 0)));
    panel1.add(lowField);
    panel1.add(Box.createRigidArea(new Dimension(5, 0)));
    panel1.add(highLabel);
    panel1.add(Box.createRigidArea(new Dimension(5, 0)));
    panel1.add(highField);
    panel1.add(Box.createRigidArea(new Dimension(3, 0)));

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
    panel2.add(new JScrollPane(labelTable));
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
    int cnt = labelTable.getRowCount();
    byte[] buffer = new byte[3*4 + cnt*4];

    int defaultOffset = Integer.parseInt(defaultField.getText(), 16) - offset;
    int low           = Integer.parseInt(lowField.getText(), 16);
    int high          = Integer.parseInt(highField.getText(), 16);

    Converter.setInt(buffer, defaultOffset, 0);
    Converter.setInt(buffer, low, 1);
    Converter.setInt(buffer, high, 2);

    for(int i=0; i < cnt; i++) {
      int label = Integer.parseInt((String)labelTable.getValueAt(i, 0), 16);
      Converter.setInt(buffer, label - offset, i+3);
    }

    return buffer;
  }

  private int calculateHighValue() {
    return labelTable.getRowCount() + Integer.parseInt(lowField.getText()) - 1;
  }

}

