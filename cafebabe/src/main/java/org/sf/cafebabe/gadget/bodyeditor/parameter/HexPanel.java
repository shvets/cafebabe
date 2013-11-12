// HexPanel.java

package org.sf.cafebabe.gadget.bodyeditor.parameter;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import org.sf.cafebabe.gadget.bodyeditor.util.HexDocument;
import org.sf.cafebabe.gadget.bodyeditor.util.HexField;
import org.sf.classfile.instruction.Instruction;

public class HexPanel extends ParameterPanel {

  private final String HEXVALUE_TEXT = "Hex value";

  private HexField paramField;

  public HexPanel(Instruction instruction, int length) {
    super();

    this.setLayout(null);

    setNameForBorder(HEXVALUE_TEXT);

    paramField = new HexField(instruction.getParameter(), length*2+1);

    paramField.addFocusListener(new FocusAdapter() {
      public void focusGained(FocusEvent e) {
        paramField.selectAll();
      }
    });

    Font font = new Font("Monospaced", Font.BOLD, 12);
    paramField.setFont(font);

    paramField.setBounds(10, 25, 17*length, 20);
    this.add(paramField);
  }

  public byte[] getBytes() {
    HexDocument hexDocument = (HexDocument)paramField.getDocument();

    return hexDocument.getBytes();
  }

}

