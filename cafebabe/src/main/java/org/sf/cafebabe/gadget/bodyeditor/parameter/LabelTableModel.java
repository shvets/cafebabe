// LabelTableModel.java

package org.sf.cafebabe.gadget.bodyeditor.parameter;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;
import org.sf.classfile.Converter;

class LabelTableModel extends AbstractTableModel {

  final String[] names = {"Label"};

  private Vector labels = new Vector();

  public void addLabel(int label) {
    labels.addElement(new Integer(label));
    fireTableDataChanged();
  }

  public void removeLabel(int index) {
    if(index >=0 && index < labels.size()) {
      labels.removeElementAt(index);
      fireTableDataChanged();
    }
  }

  public int getColumnCount() {
    return names.length;
  }

  public int getRowCount() {
    return labels.size();
  }

  public Object getValueAt(int row, int col) {
    return Converter.toHexString(((Integer)labels.elementAt(row)).intValue(), "");
//    return labels.elementAt(row);
  }

  public String getColumnName(int column) {
    return names[column];
  }

  public Class getColumnClass(int c) {
    return getValueAt(0, c).getClass();
  }

  public boolean isCellEditable(int row, int column) {
    return true;
  }

  public void setValueAt(Object value, int row, int column) {
    if(value != null) {
      try {
        int label = Integer.parseInt(value.toString(), 16);
        labels.setElementAt(new Integer(label), row);
      }
      catch(NumberFormatException e) {
//        System.out.println(e);
      }
    }
  }

}
