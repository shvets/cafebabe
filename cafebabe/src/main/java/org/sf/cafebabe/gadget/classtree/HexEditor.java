// HexEditor.java

package org.sf.cafebabe.gadget.classtree;

import java.awt.event.MouseEvent;

import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ToolTipManager;

public class HexEditor extends JTextField {

  private JTree tree;

  public HexEditor(JTree tree) {
    super();

    this.tree = tree;

    ToolTipManager.sharedInstance().registerComponent(this);

    setEditable(false);
  }

  public String getToolTipText(MouseEvent event) {
    return tree.getToolTipText(null);
  }

}
