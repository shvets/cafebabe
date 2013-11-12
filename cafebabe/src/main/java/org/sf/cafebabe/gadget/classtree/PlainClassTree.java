// ClassTree.java

package org.sf.cafebabe.gadget.classtree;

import org.sf.cafebabe.Constants;
import org.sf.cafebabe.task.classfile.EditUtfDialog;
import org.sf.cafebabe.task.classfile.Integrity;
import org.sf.cafebabe.task.classfile.SearchDialog;
import org.sf.cafebabe.util.ControlledTree;
import org.sf.classfile.*;
import org.sf.classfile.attribute.MethodBody;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.StringTokenizer;

public abstract class PlainClassTree extends ControlledTree {
  private RootNode root;
  private ClassTreeModel dataModel;
  private MethodEntry methodEntry;

  protected JPopupMenu methodNodeMenu = new MethodNodePopupMenu();

  private boolean isTutorialMode = false;

  protected ClassFile classFile;
  protected HexEditor hexEditor;

  public PlainClassTree() {
    super();
  }

  public ClassFile getClassFile() {
    return classFile;
  }

  public void setClassFile(ClassFile classFile) {
    this.classFile = classFile;

    dataModel = new ClassTreeModel(classFile);

    setModel(dataModel);

    setCellRenderer(new ClassTreeRenderer(classFile.getConstPool()));

    root = (RootNode)dataModel.getRoot();

    this.setEditable(false);
    this.setRootVisible(false);
    this.setSelectionPath(new TreePath(root));

    this.addMouseListener(new ModificationListener());
    this.addMouseListener(new CustomMouseAdapter());

    this.setBorder( new BevelBorder(BevelBorder.RAISED));

    ToolTipManager.sharedInstance().registerComponent(this);
    ToolTipManager.sharedInstance().setInitialDelay(250);

    hexEditor = new HexEditor(this);

    this.addTreeSelectionListener(new ClassTreeSelectionListener(hexEditor));
    this.addMouseListener(new ClassMouseListener(hexEditor));

    this.setSelectionInterval(0, 0);

    getSelectionModel().setSelectionPath(new TreePath(root));

    this.requestFocus();
  }

  public HexEditor getHexEditor() {
    return hexEditor;
  }

  public boolean isAnyChange() {
    if(classFile != null && classFile.isChanged()) {
      return true;
    }

    for(int i=0; i < root.getChildCount(); i++) {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode)root.getChildAt(i);
      if(node.getUserObject().equals(Constants.METHODS_TEXT)) {
        for(int j=0; j < node.getChildCount(); j++) {
          Object next = node.getChildAt(j);
          if(next instanceof EntryNode) {
            EntryNode entryNode = (EntryNode)next;
            if(entryNode.isChanged()) {
              classFile.setChanged(true);

              return true;
            }
          }
        }
        break;
      }
    }

    return false;
  }

  public void saveAsClassFile(File file) throws Exception {
    JFrame mainFrame = (JFrame)this.getTopLevelAncestor();

    mainFrame.getGlassPane().setVisible(true);

    classFile.write(new DataOutputStream(new FileOutputStream(file)));

    mainFrame.getGlassPane().setVisible(false);
  }

  public void setTutorialMode(boolean mode) {
    isTutorialMode = mode;
  }

 /**
   * Looks for desired position in ClassFile tree and
   * positions on it
   *
   * @param selMethod selected method name
   */
  public void setMethodPosition(String selMethod) {
    if(search(selMethod, Constants.SEARCH_METHOD, false, true)) {
      expandMethodFolder();
    }
  }

  /**
   * Looks for desired position in ClassFile tree and
   * positions on it
   *
   * @param selField selected field name
   */
  public void setFieldPosition(String selField) {
    boolean found = search(selField, Constants.SEARCH_FIELD, false, true);

    if(found) {
      expandFieldFolder();
    }
  }

  public String getToolTipText(MouseEvent e) {
    String toolTip = null;

    if(isTutorialMode) {
      TreePath path = null;

      if(e != null) {
        path = getPathForRow(getRowForLocation(e.getX(), e.getY()));
      }
      else {
        path = getSelectionPath();
      }

      if(path != null) {
        TreeNode node = (TreeNode)path.getLastPathComponent();

        toolTip = dataModel.getToolTipText(node);

        if(toolTip != null && toolTip.length() == 0) {
          toolTip = null;
        }
      }
    }

    return toolTip;
  }

class ModificationListener extends MouseAdapter {
  public void mouseClicked(MouseEvent event) {
    TreePath path = getSelectionPath();
    if(path == null) return;

    TreeNode node = (TreeNode)path.getLastPathComponent();

    if(event.getClickCount() >= 2) {
      if(node instanceof EntryNode) {
        ClassTreeNode parent = (ClassTreeNode)node.getParent();

        if(parent.toString().equals(ConstPool.TYPE)) {
          modifyUtf8(((EntryNode)node).getEntry());
        }
      }
      else if(node.toString().equals(MethodBody.TYPE)) {
        ClassTreeNode n = (ClassTreeNode)node.getParent().getParent().getParent();
        if(n instanceof EntryNode) {
          Entry entry = ((EntryNode)n).getEntry();
          if(entry instanceof MethodEntry) {
            openMethodBodyEditor((MethodEntry)entry);
          }
        }
      }
    }
  }

  private void modifyUtf8(Entry entry) {
    try {
      if(entry instanceof Utf8Const) {
        Utf8Const c = (Utf8Const)entry;
        String oldValue = c.getValue();
        JFrame mainFrame = (JFrame) PlainClassTree.this.getTopLevelAncestor();

        EditUtfDialog dialog = new EditUtfDialog(mainFrame, oldValue);
        dialog.show();

        String newValue = dialog.getUtfString();
        if(newValue != null && !newValue.equals(oldValue)) {
          mainFrame.getGlassPane().setVisible(true);

          c.setValue(newValue);

          classFile.setChanged(true);

          mainFrame.getGlassPane().setVisible(false);
        }
      }
    }
    catch(NumberFormatException e) {  e.printStackTrace(); }
  }
}

  public void openMethodBodyEditor(MethodEntry methodEntry) {
    openMethodBodyEditor(methodEntry, 0);
  }

  public abstract void openMethodBodyEditor(MethodEntry methodEntry, int pos);
  /*{
    new BodyEditorToolWindow(classFile, methodEntry, pos);
  } */

  public void search(/*JFrame frame*/) {
    JFrame frame = (JFrame) getTopLevelAncestor();
    
    new SearchDialog(frame, PlainClassTree.this, classFile.getConstPool()).show();
  }

  public boolean search(String line, int searchMode, boolean isNext, boolean exact) {
    if(searchMode == Constants.SEARCH_PLAIN) {
      return search(line, root, isNext, exact);
    }

    TreeNode cpNode = null;

    if(searchMode == Constants.SEARCH_CONST_POOL) {
      cpNode = getChild(ConstPool.TYPE);
    }
    else if(searchMode == Constants.SEARCH_FIELD) {
      cpNode = getChild(Constants.FIELDS_TEXT);
    }
    else if(searchMode == Constants.SEARCH_METHOD) {
      cpNode = getChild(Constants.METHODS_TEXT);
    }
    else if(searchMode == Constants.SEARCH_CLASS_ATTRIBUTE) {
      cpNode = getChild(Constants.CLASS_ATTRIBUTES_TEXT);
    }

    if(cpNode != null) {
      return search(line, cpNode, isNext, exact);
    }

    JOptionPane.showMessageDialog(PlainClassTree.this,
                "String \"" + line + "\" does not found!", "Search dialog",
                 JOptionPane.INFORMATION_MESSAGE);
    return false;
  }

  private TreeNode getChild(String childText) {
    Enumeration e = root.children();
    while(e.hasMoreElements()) {
      TreeNode iNode = (TreeNode)e.nextElement();
      if(iNode.toString().equals(childText)) {
        return iNode;
      }
    }

    return null;
  }

  private boolean search(String line, TreeNode node, boolean isNext, boolean exact) {
    Enumeration e = node.children();
    while(e.hasMoreElements()) {
      TreeNode iNode = (TreeNode)e.nextElement();

      String text = iNode.toString();

      if(exact) {
        if(text.equals(line)) {
          changePosition(iNode);
          return true;
        }
      }
      else {
        StringTokenizer st = new StringTokenizer(text);

        while(st.hasMoreTokens()) {
          String token = st.nextToken();

          if(token.toLowerCase().startsWith(line.toLowerCase())) {
            changePosition(iNode);
            return true;
          }
        }
      }

      if(search(line, iNode, isNext, exact)) {
        return true;
      }
    }

    return false;
  }

  public void changePosition(TreeNode iNode) {
    DefaultMutableTreeNode current = (DefaultMutableTreeNode)iNode;
    DefaultMutableTreeNode parent  = (DefaultMutableTreeNode)iNode.getParent();

    TreePath path1 = new TreePath(current.getPath());
    TreePath path2 = new TreePath(parent.getPath());

    this.expandPath(path2);
    this.setSelectionPath(path1);
    this.expandPath(path1);

    repaint();
  }

  public void showPopupMenu(TreeNode node, MouseEvent event) {
    if(node instanceof EntryNode) {
      Entry entry = ((EntryNode)node).getEntry();
      if(entry instanceof MethodEntry) {
        methodEntry = (MethodEntry)entry;
        methodNodeMenu.show(event.getComponent(), event.getX(), event.getY());
      }
    }
    else {
      methodEntry = null;
    }
  }

  public void expandFieldFolder() {
    TreePath path = this.getSelectionPath();

    if(path == null) {
      return;
    }

    DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();

    while(true) {
      FolderNode parent = (FolderNode)node.getParent();
      path = new TreePath(parent.getPath());

      if(node instanceof FolderNode) {
        FolderNode folderNode = (FolderNode)node;
        node = (DefaultMutableTreeNode)folderNode.getChildAt(0);
      }
      else {
        this.expandPath(path);
        setSelectionPath(new TreePath((node).getPath()));
        break;
      }
    }
  }

  public void expandMethodFolder() {
    TreePath path = this.getSelectionPath();

    if(path == null) {
      return;
    }

    DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();

    while(true) {
      DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
      path = new TreePath(parent.getPath());

      if(node instanceof FolderNode) {
        FolderNode folderNode = (FolderNode)node;
        int codePos = -1;

        for(int i=0; i < folderNode.getChildCount(); i++) {
          ClassTreeNode iNode = (ClassTreeNode)folderNode.getChildAt(i);

          if(iNode.toString().equals(AttributeEntry.CODE)) {
            codePos = i;
            break;
          }
        }

        if(codePos == -1) {
          node = (DefaultMutableTreeNode)folderNode.getChildAt(0);
        }
        else {
          node = (DefaultMutableTreeNode)folderNode.getChildAt(codePos);
        }
      }
      else {
        this.expandPath(path);
        setSelectionPath(new TreePath((node).getPath()));
        break;
      }
    }
  }

  public void integrityTest() {
     Integrity integrity = new Integrity(getClassFile());

     final StringBuffer results = new StringBuffer();

     integrity.check();

     if(results.length() > 0) {
       JOptionPane.showMessageDialog(this,
                    results.toString(), "Integrity Test",
                    JOptionPane.INFORMATION_MESSAGE);
     }
     else {
       JOptionPane.showMessageDialog(this,
                    "Test completed successfully.", "Integrity Test",
                    JOptionPane.INFORMATION_MESSAGE);
     }
   }

public class MethodNodePopupMenu extends JPopupMenu {
  final String GOTO_METHOD_BODY_STRING = "Go to method body";

  private JMenuItem gotoMethodBodyItem = new JMenuItem(GOTO_METHOD_BODY_STRING);

  public MethodNodePopupMenu() {
    super();

    gotoMethodBodyItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        AccessFlags accessFlags = methodEntry.getAccessFlags();
        if(accessFlags.isAbstract()) {
          JOptionPane.showMessageDialog(PlainClassTree.this,
               "This method is abstract. It has no body.",
               "Message", JOptionPane.WARNING_MESSAGE);
        }
        else {
          openMethodBodyEditor(methodEntry);
        }
      }
    });

    this.add(gotoMethodBodyItem);
  }

}

class CustomMouseAdapter extends MouseAdapter {

  public void mouseReleased(MouseEvent event) {
    if(event.isPopupTrigger()) {
      TreePath path = getPathForLocation(event.getX(), event.getY());
      if(path == null) return;

      showPopupMenu((MutableTreeNode)path.getLastPathComponent(), event);
    }
  }

}

}