// ClassTreeSelectionListener.java

package org.sf.cafebabe.gadget.classtree;

import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import org.sf.classfile.Converter;
import org.sf.classfile.Entry;
import org.sf.classfile.AttributeEntry;
import org.sf.classfile.MemberEntry;
import org.sf.classfile.EntryCollection;
import org.sf.classfile.attribute.MethodBody;

public class ClassTreeSelectionListener implements TreeSelectionListener {
  private HexEditor hexEditor;

  public ClassTreeSelectionListener(HexEditor hexEditor) {
    this.hexEditor = hexEditor;
  }

  public void valueChanged(TreeSelectionEvent event) {
    perform((JTree)event.getSource(), hexEditor);
  }

  public static void perform(JTree tree, HexEditor hexEditor) {
    TreePath path = tree.getSelectionPath();

    if(path == null) return;

    TreeNode node   = (TreeNode)path.getLastPathComponent();
    TreeNode parent = node.getParent();

    if(node instanceof MagicNumberNode) {
      MagicNumberNode magicNumberNode = (MagicNumberNode)node;

      int magicNumber = magicNumberNode.getMagicNumber();

      hexEditor.setText(Converter.toHexString(magicNumber));
    }
    else if(node instanceof VersionNode) {
      short version = ((VersionNode)node).getVersion();

      hexEditor.setText(Converter.toHexString(version));
    }
    else if(node instanceof ShortNode) {
      short value = ((ShortNode)node).getValue();

      hexEditor.setText(Converter.toHexString(value));
    }
    else if(node instanceof MethodBodyNode) {
      MethodBody methodBody = ((MethodBodyNode)node).getMethodBody();
      hexEditor.setText(Converter.toHexString((int)methodBody.length()));
    }
    else if(node instanceof ClassReferenceNode) {
      short classReference = ((ClassReferenceNode)node).getClassReference();

      hexEditor.setText(Converter.toHexString(classReference));
    }
    else if(node instanceof AttributeNode) {
      AttributeEntry attributeEntry = ((AttributeNode)node).getAttributeEntry();

      byte[] buffer = attributeEntry.getBytes();

      byte[] buffer2 = new byte[6];
      System.arraycopy(buffer, 0, buffer2, 0, 6);
      hexEditor.setText(Converter.toHexString(buffer2));
    }
    else if(node instanceof EntryNode) {
      EntryNode entryNode = (EntryNode)node;
      Entry entry = entryNode.getEntry();

      byte[] buffer = entry.getBytes();

      if(entry instanceof MemberEntry) {
        byte[] buffer2 = new byte[6];
        System.arraycopy(buffer, 0, buffer2, 0, 6);
        hexEditor.setText(Converter.toHexString(buffer2));
      }
      else {
        hexEditor.setText(Converter.toHexString(buffer));
      }
    }
    else if(node instanceof CollectionNode) {
      EntryCollection entries = ((CollectionNode)node).getEntries();
      hexEditor.setText(Converter.toHexString((short)(entries.size())));
    }
    else if(parent instanceof AttributeNode) {
      AttributeEntry attributeEntry = ((AttributeNode)parent).getAttributeEntry();

      hexEditor.setText(Converter.toHexString(attributeEntry.getBuffer()));
    }
    else {
      hexEditor.setText("");
    }
  }

}
