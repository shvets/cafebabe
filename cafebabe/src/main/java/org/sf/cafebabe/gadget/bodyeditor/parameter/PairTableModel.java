// PairTableModel.java

package org.sf.cafebabe.gadget.bodyeditor.parameter;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;
import org.sf.classfile.Converter;

class PairTableModel extends AbstractTableModel {

class Pair {
  int key, offset;

  Pair(int key, int offset) {
    this.key    = key;
    this.offset = offset;
  }

}

  final String[] names = {"Key", "Offset"};

  private Vector pairs = new Vector();

  public void addPair(int key, int offset) {
    pairs.addElement(new Pair(key, offset));
    fireTableDataChanged();
  }

  public void removePair(int index) {
    if(index >=0 && index < pairs.size()) {
      pairs.removeElementAt(index);
      fireTableDataChanged();
    }
  }

  public int getColumnCount() {
    return names.length;
  }

  public int getRowCount() {
    return pairs.size();
  }

  public Object getValueAt(int row, int col) {
    if(col == 0)
//      return new Integer(((Pair)pairs.elementAt(row)).key);
      return Converter.toHexString(((Pair)pairs.elementAt(row)).key, "");
    else if(col == 1)
//      return new Integer(((Pair)pairs.elementAt(row)).offset);
      return Converter.toHexString(((Pair)pairs.elementAt(row)).offset, "");
    else
      return null;
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
      Pair pair = (Pair)pairs.elementAt(row);
      try {
//        int i = ((Integer)value).intValue();
        int i = Integer.parseInt(value.toString(), 16);

        if(column == 0) {
          pair.key = i;
        }
        else if(column == 1) {
          pair.offset = i;
        }
      }
      catch(NumberFormatException e) {
//        System.out.println(e);
      }
    }
  }

}
