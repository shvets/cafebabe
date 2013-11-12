// ControlledTree.java

package org.sf.cafebabe.util;

import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultMutableTreeNode;

public class ControlledTree extends JTree {

  public ControlledTree() {
    super();
  }

  public void expandAllLevels() {
    TreeNode startNode = (TreeNode)getModel().getRoot();

    /*TreePath path = getSelectionPath();
    if(path != null) {
      startNode = (TreeNode)path.getLastPathComponent();
    }*/

    Enumeration e = startNode.children();

    while(e.hasMoreElements()) {
      expandNode((TreeNode)e.nextElement());
    }
  }

  public void expandNode(TreeNode node) {
    if(node.getChildCount() != 0) {
      Enumeration e = node.children();
      while(e.hasMoreElements()) {
        DefaultMutableTreeNode iNode = (DefaultMutableTreeNode)e.nextElement();
        expandPath(new TreePath(iNode.getPath()));
        expandNode(iNode);
      }
    }
  }

  public void expandFirstLevel() {
    TreeNode root = (TreeNode)getModel().getRoot();
    Enumeration e = root.children();
    while(e.hasMoreElements()) {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode)e.nextElement();
      TreePath path = new TreePath(node.getPath());
      expandPath(path);
    }
  }

  public void collapse() {
    TreePath path = getSelectionPath();
    TreeNode root = (TreeNode)getModel().getRoot();

    Enumeration e = root.children();
    while(e.hasMoreElements()) {
      collapse((DefaultMutableTreeNode)e.nextElement());
    }

    if(path == null) return;
    Object[] o = path.getPath();
    if(o.length > 1) {
      setSelectionPath(new TreePath(new Object[] {o[0], o[1]}));
    }
  }

  public void collapse(DefaultMutableTreeNode node) {
    Enumeration e = node.children();
    while(e.hasMoreElements()) {
      TreeNode iNode = (TreeNode)e.nextElement();
      if(iNode.getChildCount() != 0) {
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode)iNode.getParent();
        TreePath path = new TreePath(parent.getPath());
        if(path != null && isExpanded(path)) {
          collapsePath(path);
        }
      }
    }

    TreePath path = new TreePath(node.getPath());
    if(path != null && isExpanded(path)) {
      collapsePath(path);
    }
  }

}
