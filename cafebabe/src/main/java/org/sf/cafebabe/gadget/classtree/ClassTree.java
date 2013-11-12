// ClassTree.java

package org.sf.cafebabe.gadget.classtree;

import org.sf.cafebabe.gadget.bodyeditor.BodyEditor;
import org.sf.classfile.MethodEntry;

import javax.swing.*;

public class ClassTree extends PlainClassTree {
  private JDesktopPane desktopPane;
  private Integer layer;

  public ClassTree(JDesktopPane desktopPane, Integer layer) {
    super();

    this.desktopPane = desktopPane;
    this.layer       = layer;
  }

  public void openMethodBodyEditor(MethodEntry methodEntry, int pos) {
    new BodyEditor(desktopPane, layer, classFile, methodEntry, pos);
  }

}
