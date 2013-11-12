// ParameterPanel.java

package org.sf.cafebabe.gadget.bodyeditor.parameter;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

public abstract class ParameterPanel extends JPanel {

  public final String PARAMETER_TEXT  = "Parameter";

  protected TitledBorder parameterBorder =
          new TitledBorder(new EtchedBorder(), PARAMETER_TEXT);

  public ParameterPanel() {
    super();

    this.setBorder(parameterBorder);
  }

  public void setNameForBorder(String text) {
    parameterBorder.setTitle(" " + PARAMETER_TEXT + ":  " + text + " ");
  }

  public byte[] getBytes() {
    return null;
  }

  public Dimension getPreferredSize() {
    return new Dimension(400, 130);
  }

  public Dimension getMinimumSize() {
    return getPreferredSize();
  }

  public Dimension getMaximumSize() {
    return getPreferredSize();
  }

}

