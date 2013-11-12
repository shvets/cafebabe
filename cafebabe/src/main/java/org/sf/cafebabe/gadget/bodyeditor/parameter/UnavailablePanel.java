// UnavailablePanel.java

package org.sf.cafebabe.gadget.bodyeditor.parameter;

import java.awt.*;

import javax.swing.JLabel;

public class UnavailablePanel extends ParameterPanel {

  private final String UNAVAVAILABLED_TEXT = "Unavailabled";

  public UnavailablePanel() {
    super();

    setNameForBorder(UNAVAVAILABLED_TEXT);

    JLabel unavailableLabel =
           new JLabel("Constant pool doesn't contain info for this command.");

    Font font = new Font("Monospaced", Font.BOLD, 12);
    unavailableLabel.setFont(font);

    this.add(unavailableLabel);
  }

}

