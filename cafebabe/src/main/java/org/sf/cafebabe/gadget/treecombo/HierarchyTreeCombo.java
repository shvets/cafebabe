// HierarchyTreeCombo.java

package org.sf.cafebabe.gadget.treecombo;

import java.io.File;
import java.io.DataInputStream;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.util.Vector;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.sf.classfile.ClassFile;
import org.sf.classfile.ConstPool;
import org.sf.classfile.Utf8Const;
import org.sf.classfile.ClassConst;
import org.sf.classfile.InterfaceEntry;
import org.sf.classfile.EntryCollection;

/**
 * This class represents combo box for hierarchy tree
 *
 * @version 1.0 03/04/2002
 * @author Alexander Shvets
 */
public class HierarchyTreeCombo extends JComboBox {
  static final int OFFSET = 16;

  private ImageIcon classIcon;
  private ImageIcon interfaceIcon;
  private ImageIcon unknownIcon;

  /**
   * Cteates new hierarchy tree combo
   */
  public HierarchyTreeCombo() {
    super();
  }

  /**
   * Sets the data model that the JComboBox uses to obtain the list of items.
   *
   * @param treeModel - the ComboBoxModel that provides the displayed
   * list of items
  */
  public void setModel(TreeModel treeModel) {
    setModel(new TreeToListModel(treeModel));
    setRenderer(new ListEntryRenderer());
  }

  /**
   * Sets the icon for the class visual representation
   *
   * @param classIcon  the class icon
   */
  public void setClassIcon(ImageIcon classIcon) {
    this.classIcon = classIcon;
  }

  /**
   * Sets the icon for the interface visual representation
   *
   * @param interfaceIcon  the interface icon
   */
  public void setInterfaceIcon(ImageIcon interfaceIcon) {
    this.interfaceIcon = interfaceIcon;
  }

  /**
   * Sets the icon for the unknown type visual representation
   *
   * @param unknownIcon  the unknown type icon
   */
  public void setUnknownIcon(ImageIcon unknownIcon) {
    this.unknownIcon = unknownIcon;
  }

  /**
   * Sets the class for this component. First, it tries to find out
   * the class by using Class.forName() methods. If this try is failed,
   * it tries to read class description from class file
   *
   * @param className  the class name
   * @param entryName  the entry name
   */
  public void setClass(String className, String entryName) {
    DefaultMutableTreeNode root = null;

    Class clazz = null;

    try {
      clazz = Class.forName(className);

      if(clazz.isInterface()) {
        root = new InterfaceNode(className);
      }
      else {
        root = new ClassNode(className);
      }

      fillSuperclasses(clazz, root);
      fillInterfaces(clazz, root);
    }
    catch(Exception e) {
      root = setClass0(entryName, className);
    }

    setModel(new DefaultTreeModel(root));
  }

  /**
   * Creates nodes for all superclasses of given class
   *
   * @param clazz the given class
   * @param parent the parent node wich will keep all superclass nodes
   */
  private void fillSuperclasses(Class clazz, DefaultMutableTreeNode parent) {
    DefaultMutableTreeNode current = parent;

    for(Class c=clazz.getSuperclass(); c != null; c = c.getSuperclass()) {
      ClassNode node = new ClassNode(c.getName());

      current.add(node);

      current = node;
    }
  }

  /**
   * Creates nodes for all interfaces of given class
   *
   * @param clazz the given class
   * @param parent the parent node wich will keep all interface nodes
   */
  private void fillInterfaces(Class clazz, DefaultMutableTreeNode parent) {
    Class[] interfaces = clazz.getInterfaces();

    for (int i=0; i < interfaces.length; i++) {
      InterfaceNode node = new InterfaceNode(interfaces[i].getName());

      parent.add(node);

      fillInterfaces(interfaces[i], node);
    }
  }

  /**
   * Sets the class for this component. Information about the class
   * comes from class file
   *
   * @param className  the class name
   * @param entryName  the entry name
   */
  private DefaultMutableTreeNode setClass0(String className,
                                           String entryName) {
    DefaultMutableTreeNode root = null;

    try {
      ClassFile classFile = readFromClassFile(entryName, className);

      if(classFile.getAccessFlags().isInterface()) {
        root = new InterfaceNode(className);
      }
      else {
        root = new ClassNode(className);
      }

      fillSuperclasses0(classFile, entryName, className, root);
      fillInterfaces0(classFile, entryName, className, root);
    }
    catch(Exception e2) {
      root = new UnknownNode(className);
    }

    return root;
  }

  /**
   * Creates nodes for all superclasses of given class
   *
   * @param classFile the name of the class file
   * @param entryName  the entry name
   * @param className  the class name
   * @param parent the parent node wich will keep all superclass nodes
   */
  private void fillSuperclasses0(ClassFile classFile, String entryName,
               String className, DefaultMutableTreeNode parent) {
    DefaultMutableTreeNode current = parent;

    short superClass = classFile.getSuperClassIndex();

    while(superClass != 0) {
      ConstPool constPool = classFile.getConstPool();

      ClassConst entry = (ClassConst)constPool.getEntry(superClass);
      Utf8Const utf = (Utf8Const)constPool.getEntry(entry.getNameIndex());

      className = utf.getValue().replace('/', '.');

      if(classFile.getAccessFlags().isInterface() &&
         className.equals("java.lang.Object")) {
        break;
      }

      try {
        classFile = readFromClassFile(entryName, className);
      }
      catch(Exception e) {
        try {
          ClassNode node = new ClassNode(className);
          current.add(node);

          Class clazz = Class.forName(className);
          fillSuperclasses(clazz, node);
        }
        catch(Exception e2) {}
        break;
      }

      ClassNode node = new ClassNode(className);
      current.add(node);
      current = node;

      superClass = classFile.getSuperClassIndex();
    }
  }

  /**
   * Creates nodes for all interfaces of given class
   *
   * @param classFile the name of the class file
   * @param entryName  the entry name
   * @param className  the class name
   * @param parent the parent node wich will keep all superclass nodes
   */
  private void fillInterfaces0(ClassFile classFile, String entryName,
               String className, DefaultMutableTreeNode parent) {
    DefaultMutableTreeNode current = parent;

    EntryCollection interfaces = classFile.getInterfaces();
    ConstPool constPool = classFile.getConstPool();

    for(short i=0; i < interfaces.size(); i++) {
      InterfaceEntry entry = (InterfaceEntry)interfaces.get(i);
      ClassConst entry2 = (ClassConst)constPool.getEntry(entry.getNameIndex());
      Utf8Const utf = (Utf8Const)constPool.getEntry(entry2.getNameIndex());

      className = utf.getValue().replace('/', '.');

      try {
        classFile = readFromClassFile(entryName, className);
      }
      catch(Exception e) {
        try {
          InterfaceNode node = new InterfaceNode(className);
          current.add(node);

          Class clazz = Class.forName(className);
          fillInterfaces(clazz, node);
        }
        catch(Exception e2) {}
        continue;
      }

      InterfaceNode node = new InterfaceNode(className);
      current.add(node);

      fillInterfaces0(classFile, entryName, className, node);
    }
  }

  /**
   * Reads class descriprtion from the file
   *
   * @param entryName  the entry name
   * @param className  the class name
   */
  private ClassFile readFromClassFile(String entryName, String className)
               throws Exception {
    ClassFile classFile = new ClassFile();
    boolean isArchive = !(new File(entryName)).isDirectory();

    if(isArchive) {
      ZipFile zipFile = new ZipFile(entryName);
      String name  = className.replace('.', '/') + ".class";
      ZipEntry zipEntry = zipFile.getEntry(name);

      classFile.read(new DataInputStream(zipFile.getInputStream(zipEntry)));
    }
    else {
      String fileName = className.replace('.', File.separatorChar) + ".class";
      classFile.read(entryName + File.separator + fileName);
    }

    return classFile;
  }

/**
 * Inner class - data model
 */
class TreeToListModel extends AbstractListModel
                      implements ComboBoxModel, TreeModelListener {
  TreeModel source;
  boolean invalid = true;
  Object currentValue;
  Vector cache = new Vector();

  /**
   * Creates new model
   */
  public TreeToListModel(TreeModel model) {
    source = model;

    model.addTreeModelListener(this);

    setRenderer(new ListEntryRenderer());
  }

  /**
   * Sets the selected item
   *
   * @param  item  the item value
   */
  public void setSelectedItem(Object item) {
    currentValue = item;
    fireContentsChanged(this, -1, -1);
  }

  /**
   * Gets the selected item
   *
   * @return  the selected item
   */
  public Object getSelectedItem() {
    return currentValue;
  }

  /**
   * Gets the size of this list
   *
   * @return the size of this list
   */
  public int getSize() {
    validate();
    return cache.size();
  }

  /**
   * Gets the element at the specified position
   *
   * @param index  the specified position
   * @return the element
   */
  public Object getElementAt(int index) {
    return cache.elementAt(index);
  }

  // The implementation of TreeModelListener interface

  public void treeNodesChanged(TreeModelEvent e) {
    invalid = true;
  }

  public void treeNodesInserted(TreeModelEvent e) {
    invalid = true;
  }

  public void treeNodesRemoved(TreeModelEvent e) {
    invalid = true;
  }

  public void treeStructureChanged(TreeModelEvent e) {
    invalid = true;
  }

  /**
   * Fixes changes in the data
   */
  private void validate() {
    if(invalid) {
      cache = new Vector();

      cacheTree(source.getRoot(), 0);

      if(cache.size() > 0) {
        currentValue = cache.elementAt(0);
      }

      invalid = false;

      fireContentsChanged(this, 0, 0);
    }
  }

  /**
   * Caches the tree
   *
   * @param object  the object
   * @param level  the level
   */
  private void cacheTree(Object object, int level) {
    if(source.isLeaf(object)) {
      addListEntry(object, level);
    }
    else {
      int c = source.getChildCount(object);
      int i;
      Object child;

      addListEntry(object, level);
      level++;

      for(i=0;i<c;i++) {
        child = source.getChild(object, i);
        cacheTree(child, level);
      }

      level--;
    }
  }

  /**
   * Adds new entry to the list
   *
   * @param object  the object
   * @param level  the level
   */
  private void addListEntry(Object object, int level) {
    cache.addElement(new ListEntry(object, level));
  }

}

/**
 * This class represents single entry of the list
 */
class ListEntry {
  Object object;
  int level;

  /**
   * Creates new list entry
   *
   * @param object  the object
   * @param level  the level
   */
  public ListEntry(Object object, int level) {
    this.object = object;
    this.level  = level;
  }

  /**
   * Gets the object
   *
   * @return the object
   */
  public Object getObject() {
    return object;
  }

  /**
   * Gets the level
   *
   * @return the level
   */
  public int getLevel() {
    return level;
  }

  /**
   * Gets the string representation of this object
   */
  public String toString() {
    return object.toString();
  }

}

/**
 * This class renders separate cell for the list
 */
class ListEntryRenderer extends JLabel implements ListCellRenderer  {
  private final Border emptyBorder = new EmptyBorder(0,0,0,0);

  /**
   * Creates new list renderer
   */
  public ListEntryRenderer() {
    this.setOpaque(true);
  }

  /**
   * Return a component that has been configured to display the
   * specified value. Contains main logic for the renderer,
   */
  public Component getListCellRendererComponent(JList listbox, Object value,
                      int index, boolean isSelected, boolean cellHasFocus) {
    ListEntry listEntry = (ListEntry)value;

    if(listEntry != null) {
      Border border;
      this.setText(listEntry.getObject().toString());

      Object node = listEntry.getObject();
      if(node instanceof ClassNode && classIcon != null) {
        this.setIcon(classIcon);
      }
      else if(node instanceof InterfaceNode && interfaceIcon != null) {
        this.setIcon(interfaceIcon);
      }
      else if(node instanceof UnknownNode && unknownIcon != null) {
        this.setIcon(unknownIcon);
      }

      if(index != -1) {
        border = new EmptyBorder(0, OFFSET*listEntry.getLevel(), 0, 0);
      }
      else {
        border = emptyBorder;
      }

      if(UIManager.getLookAndFeel().getName().equals("CDE/Motif")) {
        if(index == -1 ) {
          this.setOpaque(false);
        }
        else {
          this.setOpaque(true);
        }
      }
      else {
        this.setOpaque(true);
      }

      this.setBorder(border);

      if(isSelected) {
        this.setBackground(UIManager.getColor("ComboBox.selectionBackground"));
        this.setForeground(UIManager.getColor("ComboBox.selectionForeground"));
      }
      else {
        this.setBackground(UIManager.getColor("ComboBox.background"));
        this.setForeground(UIManager.getColor("ComboBox.foreground"));
      }
    }
    else {
      this.setText("");
      this.setIcon(null);
    }

    return this;
  }
}

  public static void main(String args[]) {
    JFrame frame = new JFrame();

    JPanel topPanel = new JPanel();
    topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

    frame.setContentPane(topPanel);

    frame.setBounds(10, 10, 700, 500);
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) { System.exit(0); }
    });

    HierarchyTreeCombo hierarchyCombo = new HierarchyTreeCombo();

    hierarchyCombo.setClass("D:\\swing\\swingall.jar", "javax.swing.JFrame");

    topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    topPanel.add(hierarchyCombo);
    topPanel.add(Box.createRigidArea(new Dimension(0, 10)));

    frame.setVisible(true);
  }

}
