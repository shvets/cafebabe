// MnemonicsModel.java

package org.sf.cafebabe.gadget.bodyeditor;

import javax.swing.*;
import org.sf.classfile.instruction.OpcodeTable;

public class MnemonicsModel extends AbstractListModel {

  private byte[] opcodes = new byte[0];

  public MnemonicsModel() {}

  public void setOpcodes(byte[] opcodes) {
    this.opcodes = opcodes;

    fireContentsChanged(this, 0, opcodes.length-1);
  }

  public int getSize() {
    return opcodes.length;
  }

  public Object getElementAt(int index) {
    if(index >= 0 && index < opcodes.length)
      return getOperation(index);
    else
      return null;
  }

  private String getOperation(int index) {
    return OpcodeTable.getDescription(opcodes[index]).mnemonic;
  }

}
