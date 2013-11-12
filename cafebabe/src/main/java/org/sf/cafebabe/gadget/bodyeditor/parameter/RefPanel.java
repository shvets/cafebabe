// RefPanel.java

package org.sf.cafebabe.gadget.bodyeditor.parameter;

import javax.swing.*;

public class RefPanel extends ParameterPanel {

  protected JComboBox typesBox = new JComboBox();

  public RefPanel() {
    super();
  }

  public byte[] getBytes() {
    int index          = typesBox.getSelectedIndex();
    Short indexWrapper = (Short)((TypesModel)typesBox.getModel()).getTypeAt(index);
    short poolIndex    = indexWrapper.shortValue();

    byte[] buffer = new byte[2];

    buffer[0] = (byte)(poolIndex >> 8);
    buffer[1] = (byte)((poolIndex << 8) >> 8);

    return buffer;
  }

}

