// HexField.java

package org.sf.cafebabe.gadget.bodyeditor.util;

import javax.swing.JTextField;


public class HexField extends JTextField {
  public HexField() {
    this(new byte[8]);
  }

  public HexField(byte[] buffer) {
    this(buffer, buffer.length*2+3);
  }

  public HexField(byte[] buffer, int columns) {
    super();

    this.setDocument(new HexDocument(buffer));
    this.setColumns(columns);
  }

}
