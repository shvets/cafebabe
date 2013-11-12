// ClassMouseListener.java

package org.sf.cafebabe.gadget.classtree;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import javax.swing.JTree;

public class ClassMouseListener extends MouseAdapter {
  private HexEditor hexEditor;

  public ClassMouseListener (HexEditor hexEditor) {
    this.hexEditor = hexEditor;
  }

  public void mouseClicked(MouseEvent event) {
    ClassTreeSelectionListener.perform((JTree)event.getSource(), hexEditor);
  }

  /**
   * Invoked when a mouse button has been pressed on a component.
   */
  public void mousePressed(MouseEvent event) {
    ClassTreeSelectionListener.perform((JTree)event.getSource(), hexEditor);
  }

}
