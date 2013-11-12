// BodyEditor.java

package org.sf.cafebabe.gadget.bodyeditor;

import java.awt.*;
import java.io.File;

import javax.swing.*;
import org.sf.cafebabe.CustomFrame;
import org.sf.cafebabe.Main;
import org.sf.cafebabe.help.HelpFrame;
import org.sf.classfile.ClassFile;
import org.sf.classfile.MethodEntry;

public class BodyEditor extends CustomFrame implements BodyEditorAware {

  private BodyTable bodyTable;

  private BodyEditorActions actions = new BodyEditorActions(this);

  private JDesktopPane desktopPane;
  private Integer layer;

  public BodyEditor(final JDesktopPane desktopPane, Integer layer,
                    final ClassFile classFile, final MethodEntry methodEntry,
                    int position) {
    this.desktopPane = desktopPane;
    this.layer       = layer;

    enableEvents(AWTEvent.FOCUS_EVENT_MASK);

    String methodName = methodEntry.resolve(classFile.getConstPool());

    setTitle("Method: " + methodName);

    // Set the characteristics for this dialog instance
    Rectangle r = desktopPane.getBounds();
    this.setBounds(r.x + r.width/2 - 650/2, r.y + r.height/2 - 400/2, 650, 300);
//    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    bodyTable = new BodyTable(desktopPane, methodEntry, classFile) {
      public void showTopic() {
        help();
      }
    };

//    if(position < bodyTable.getColumnCount()) {
      bodyTable.setRowSelectionInterval(position, position);
//    }

    JPanel topPanel = new JPanel();
    topPanel.setLayout(new BorderLayout());
    this.getContentPane().add(topPanel);

    topPanel.add(actions.getToolBar(), BorderLayout.NORTH);


    JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                               JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.getViewport().setView(bodyTable);

    topPanel.add(scrollPane, BorderLayout.CENTER);

    desktopPane.add(this, layer);
    open();
  }

  public void editInstruction() {
    bodyTable.editInstruction();
  }

  public void addInstruction() {
    bodyTable.addInstruction(null);
  }

  public void removeInstructions() {
    bodyTable.removeInstructions();
  }

  public void help() {
    String mnemonic = bodyTable.getCurrentMnemonic();

    if(mnemonic == null) {
      JOptionPane.showMessageDialog(this.getParent(),
                  "Please select an instruction to be removed.\n",
                  "Warning", JOptionPane.WARNING_MESSAGE);
    }
    else {
      HelpFrame helpFrame = new HelpFrame();

      desktopPane.add(helpFrame, layer);

//      Rectangle r = this.getDesktopPane().getBounds();
      Rectangle r = desktopPane.getBounds();
      helpFrame.setBounds(r.x + r.width/2 - 650/2, r.y + r.height/2 - 500/2, 650, 450);
      helpFrame.open();

      int index = mnemonic.indexOf('_');
      if(index != -1) {
        mnemonic = mnemonic.substring(0, index+1);
      }
      else if(mnemonic.startsWith("dcmp")) {
        mnemonic = "dcmp";
      }
      else if(mnemonic.startsWith("fcmp")) {
        mnemonic = "fcmp";
      }

      String helpArchive = Main.getHelpArchiveName();

      if(new File(helpArchive).exists()) {
        helpFrame.showTopic(helpArchive, mnemonic + ".html");
      }
    }
  }

}
