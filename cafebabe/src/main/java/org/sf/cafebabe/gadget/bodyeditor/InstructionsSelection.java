// InstructionsSelection.java

package org.sf.cafebabe.gadget.bodyeditor;

import  java.io.*;
import  java.awt.datatransfer.*;

import org.sf.classfile.instruction.*;

public class InstructionsSelection implements Transferable {

  public static DataFlavor instructionsFlavor =
                new DataFlavor(Instruction[].class, "Instructions");

  private Instruction[] data;

  public InstructionsSelection(Instruction[] data) {
    this.data = (Instruction[])data.clone();
  }

  // implements Transferable

  // supported flavors
  public synchronized DataFlavor[] getTransferDataFlavors() {
    return new DataFlavor[] { instructionsFlavor };
  }

  public boolean isDataFlavorSupported(DataFlavor flavor) {
    if(flavor.isMimeTypeEqual(instructionsFlavor))
      return true;

    return false;
  }

  public synchronized Object getTransferData (DataFlavor flavor)
                     throws UnsupportedFlavorException, IOException {
    if(flavor.isMimeTypeEqual(instructionsFlavor))
      return data;
    else
      throw new UnsupportedFlavorException(flavor);
  }

}
