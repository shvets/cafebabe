// BodyEditorActions.java

package org.sf.cafebabe.gadget.bodyeditor;

import java.awt.event.ActionEvent;

import javax.swing.*;
import org.sf.cafebabe.Constants;
//import org.sf.cafebabe.Main;
import org.sf.cafebabe.util.Actions;
import org.sf.cafebabe.util.IconProducer;

public class BodyEditorActions extends Actions {
  protected Action editAction, addAction, removeAction, helpAction;

  protected JToolBar toolBar = new JToolBar();

  private BodyEditorAware parent;

  public BodyEditorActions(BodyEditorAware parent) {
    this.parent = parent;

    init();
  }

  public JToolBar getToolBar() {
    return toolBar;
  }

  protected void init() {
    //String archive = Main.getArchiveName();

    ImageIcon editIcon = IconProducer.getImageIcon(Constants.ICON_EDIT);
    ImageIcon addIcon = IconProducer.getImageIcon(Constants.ICON_ADD);
    ImageIcon removeIcon = IconProducer.getImageIcon(Constants.ICON_REMOVE);
    ImageIcon helpIcon = IconProducer.getImageIcon(Constants.ICON_TUTORIAL_OFF);

    editAction = createEditAction("Edit instruction", editIcon);
    addAction = createAddAction("Add instruction", addIcon);
    removeAction = createRemoveAction("Remove instructions", removeIcon);
    helpAction = createHelpAction("Help", helpIcon);

    addActionToToolBar(editAction, toolBar, false, "Edit instruction", 0);
    addActionToToolBar(addAction, toolBar, false, "Add instruction", 0);
    addActionToToolBar(removeAction, toolBar, false, "Remove instruction", 0);

    JPanel emptyArea = new JPanel();
    toolBar.add(emptyArea);

    addActionToToolBar(helpAction, toolBar, false, "Help", 0);
  }

  public Action createEditAction(String text, ImageIcon icon) {
    return new AbstractAction(text, icon) {
      public void actionPerformed(ActionEvent e) {
        parent.editInstruction();
      }
    };
  }

  public Action createAddAction(String text, ImageIcon icon) {
    return new AbstractAction(text, icon) {
      public void actionPerformed(ActionEvent e) {
        parent.addInstruction();
      }
    };
  }

  public Action createRemoveAction(String text, ImageIcon icon) {
    return new AbstractAction(text, icon) {
      public void actionPerformed(ActionEvent e) {
        parent.removeInstructions();
      }
    };
  }

  public Action createHelpAction(String text, ImageIcon icon) {
    return new AbstractAction(text, icon) {
      public void actionPerformed(ActionEvent e) {
        parent.help();
      }
    };
  }

}
