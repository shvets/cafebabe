// SerTree.java

package org.sf.cafebabe.task.serfile;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Enumeration;
import java.util.StringTokenizer;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeNode;
import org.sf.cafebabe.util.ControlledTree;
import org.sf.cafebabe.Constants;
import org.sf.serfile.SerFile;

public class SerTree extends ControlledTree {
  private boolean isTutorialMode = false;

  private SerFile serFile;

  private RootNode root;

  private SerTreeModel treeModel = new SerTreeModel();

  public SerTree() {
    super();

    this.setEditable(false);
    this.setShowsRootHandles(true);
    this.setRootVisible(false);
    this.setBorder(new BevelBorder(BevelBorder.RAISED));

    // Create a new tree control
    this.setModel(treeModel);

    root = (RootNode)treeModel.getRoot();
    this.setSelectionRow(0);
    this.setSelectionPath(new TreePath(root));

    putClientProperty("JTree.lineStyle", "Angled");

    setCellRenderer(new SerTreeRenderer());

    ToolTipManager.sharedInstance().registerComponent(this);
    ToolTipManager.sharedInstance().setInitialDelay(250);
  }

  public void setFile(File file) {
    try {
      serFile = new SerFile();
      serFile.read(new DataInputStream(new FileInputStream(file)));

      treeModel.setSerFile(serFile);

      expandPath(new TreePath(root.getPath()));
    }
    catch(IOException e) {
      JOptionPane.showMessageDialog(this,
           e.toString() + ".",
           "Error", JOptionPane.WARNING_MESSAGE);
      return;
    }
  }

  public void saveAs(File file) throws Exception {
    JFrame mainFrame = (JFrame)this.getTopLevelAncestor();

    mainFrame.getGlassPane().setVisible(true);

    serFile.write(new DataOutputStream(new FileOutputStream(file)));

    mainFrame.getGlassPane().setVisible(false);
  }

  public void clear() {
    serFile   = null;
    treeModel = null;
  }

  public void setTutorialMode(boolean mode) {
    isTutorialMode = mode;
  }

  public boolean isAnyChange() {
    if(serFile != null && serFile.isChanged()) {
      return true;
    }

    for(int i=0; i < root.getChildCount(); i++) {
      /*DefaultMutableTreeNode node = (DefaultMutableTreeNode)root.getChildAt(i);
      if(((String)node.getUserObject()).equals(Constants.METHODS_TEXT)) {
        for(int j=0; j < node.getChildCount(); j++) {
          Object next = node.getChildAt(j);
          if(next instanceof MethodNode) {
            MethodNode methodNode = (MethodNode)next;
            if(methodNode.isChanged()) {
              classFile.setChanged(true);

              return true;
            }
          }
        }
        break;
      }
      */
    }

    return false;
  }

  public void search(JFrame frame) {
    SearchDialog dialog = new SearchDialog(frame);
    dialog.show();

    boolean isOk = dialog.getAnswer();
    if(!isOk) return;

    String searchString = dialog.getSearchString();
    int searchMode   = dialog.getSearchMode();

    if(searchString == null || searchString.length() == 0)
      return;

    search(searchString, searchMode, false);
  }

  public boolean search(String line, int searchMode, boolean exact) {
    boolean ok = false;
    if(searchMode == Constants.SEARCH_PLAIN) {
      ok = search(line, root, exact);
    }
    else if(searchMode == Constants.SEARCH_CONST_POOL) {
      ok = searchChild(line, Constants.CONSTANT_POOL_TEXT, exact);
    }
    else if(searchMode == Constants.SEARCH_FIELD) {
      ok = searchChild(line, Constants.FIELDS_TEXT, exact);
    }
    else if(searchMode == Constants.SEARCH_METHOD) {
      ok = searchChild(line, Constants.METHODS_TEXT, exact);
    }
    else if(searchMode == Constants.SEARCH_CLASS_ATTRIBUTE) {
      ok = searchChild(line, Constants.CLASS_ATTRIBUTES_TEXT, exact);
    }

    if(!ok)
      JOptionPane.showMessageDialog(SerTree.this,
                  "String \"" + line + "\" does not found!", "Search dialog",
                   JOptionPane.INFORMATION_MESSAGE);
    return ok;
  }

  private boolean searchChild(String line, String childText, boolean exact) {
    Enumeration e = root.children();
    TreeNode cpNode = null;
    while(e.hasMoreElements()) {
      TreeNode iNode = (TreeNode)e.nextElement();
      if(iNode.toString().equals(childText)) {
        cpNode = iNode;
        break;
      }
    }

    if(cpNode != null)
      return search(line, cpNode, exact);

    return false;
  }

  public boolean search(String line, TreeNode node, boolean exact) {
    Enumeration e = node.children();
    while(e.hasMoreElements()) {
      TreeNode iNode = (TreeNode)e.nextElement();
      String text = iNode.toString();

      if(exact) {
        if(text.equals(line)) {
          //changePosition(iNode);
          return true;
        }
      }
      else {
        StringTokenizer st = new StringTokenizer(text);
        while(st.hasMoreTokens()) {
          String token = st.nextToken();
          if(token.toLowerCase().startsWith(line.toLowerCase())) {
            //changePosition(iNode);
            return true;
          }
        }
      }

      if(search(line, iNode, exact)) return true;
    }
    return false;
  }

  public String getToolTipText(MouseEvent event) {
    String toolTip = null;

    if(isTutorialMode) {
      int row = getRowForLocation(event.getX(), event.getY());
      if(row == -1) return null;

      TreePath path = getPathForLocation(event.getX(), event.getY());
      DefaultMutableTreeNode node =
             (DefaultMutableTreeNode)path.getLastPathComponent();

      toolTip = treeModel.getToolTipText(node);

      if(toolTip != null && toolTip.length() == 0) {
        toolTip = null;
      }
    }

    return toolTip;
  }


class SerTreeRenderer extends DefaultTreeCellRenderer {

  private Icon openIcon, closedIcon, leafIcon;

  SerTreeRenderer() {
    openIcon   = getOpenIcon();
    closedIcon = getClosedIcon();
    leafIcon   = getLeafIcon();
  }

  public Component getTreeCellRendererComponent(JTree tree,
                   Object value, boolean isSelected, boolean expanded,
                   boolean leaf, int row, boolean hasFocus) {
    Component c = super.getTreeCellRendererComponent(tree, value, isSelected,
                                                     expanded,leaf, row, hasFocus);
    if(value instanceof FolderNode) {
      if(expanded) {
        setIcon(openIcon);
      }
      else {
        setIcon(closedIcon);
      }
    }
    else {
      setIcon(leafIcon);
    }

    return c;
  }
}


}
