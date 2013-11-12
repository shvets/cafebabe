// ClassHoundFrame.java

package org.sf.cafebabe.task.classhound;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipFile;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.activation.DataSource;
import org.sf.cafebabe.Constants;
import org.sf.cafebabe.activation.ArchivedDataSource;
import org.sf.cafebabe.gadget.treecombo.HierarchyTreeCombo;
import org.sf.cafebabe.util.IconProducer;

/**
 * Internal frame for displaying class-hound service
 *
 * @version 1.0 01/30/2002
 * @author Alexander Shvets
 */
public class ClassHoundPanel extends JPanel {
  private static String TITLE = "Class-Hound Service";

  private static String UP_STRING = "Up";
  private static String DOWN_STRING = "Down";
  private static String ADD_STRING = "Add";
  private static String REMOVE_STRING = "Remove";
  private static String SET_DEFAULTS_STRING = "Set defaults";
  private static String SELECT_STRING  = "Select";

  private static String UP_COMMAND = "up";
  private static String DOWN_COMMAND = "down";
  private static String ADD_COMMAND = "add";
  private static String REMOVE_COMMAND = "remove";
  private static String SET_DEFAULTS_COMMAND = "defaults";

  private ImageIcon classIcon = IconProducer.getImageIcon(Constants.ICON_HIERARCHY_CLASS);
  private ImageIcon interfaceIcon = IconProducer.getImageIcon(Constants.ICON_HIERARCHY_INTERFACE);
  private ImageIcon unknownIcon   = IconProducer.getImageIcon(Constants.ICON_HIERARCHY_UNKNOWN);

  private final JComboBox entriesCombo = new JComboBox();
  private final JList packagesList = new JList();
  private final JList classesList = new JList();
  private final HierarchyTreeCombo hierarchyCombo = new HierarchyTreeCombo();
  private final JList fieldsList = new JList();
  private final JList methodsList = new JList();

  private final JButton upButton = new JButton(UP_STRING);
  private final JButton downButton = new JButton(DOWN_STRING);
  private final JButton addButton = new JButton(ADD_STRING);
  private final JButton removeButton = new JButton(REMOVE_STRING);
  private final JButton defaultsButton = new JButton(SET_DEFAULTS_STRING);

  private ItemListener combosListener = new CombosListener();

  private final JButton selectButton = new JButton(SELECT_STRING);

  private ClassHound service;

  private String selectedEntry;
  private String selectedPackage;
  private String selectedClass;
  private String selectedMethod;
  private String selectedField;

  private CafeBabeParent parent;

  /**
   * Creates new window for Class-Hound service.
   *
   * @param parent the parent
   */
  public ClassHoundPanel(CafeBabeParent parent) {
    this.parent = parent;

    hierarchyCombo.setClassIcon(classIcon);
    hierarchyCombo.setInterfaceIcon(interfaceIcon);
    hierarchyCombo.setUnknownIcon(unknownIcon);

    entriesCombo.addItemListener(combosListener);
    hierarchyCombo.addItemListener(combosListener);

    ListSelectionListener listsListener = new ListsListener();

    packagesList.addListSelectionListener(listsListener);
    classesList.addListSelectionListener(listsListener);
    fieldsList.addListSelectionListener(listsListener);
    methodsList.addListSelectionListener(listsListener);

    fieldsList.setCellRenderer(new FieldCellRenderer());
    methodsList.setCellRenderer(new MethodCellRenderer());

    upButton.setActionCommand(UP_COMMAND);
    downButton.setActionCommand(DOWN_COMMAND);
    addButton.setActionCommand(ADD_COMMAND);
    removeButton.setActionCommand(REMOVE_COMMAND);
    defaultsButton.setActionCommand(SET_DEFAULTS_COMMAND);

    EntriesListener entriesListener = new EntriesListener();

    upButton.addActionListener(entriesListener);
    downButton.addActionListener(entriesListener);
    addButton.addActionListener(entriesListener);
    removeButton.addActionListener(entriesListener);
    defaultsButton.addActionListener(entriesListener);

    MouseListener dcListener = new DoubleClickListener();

    entriesCombo.addMouseListener(dcListener);
    packagesList.addMouseListener(dcListener);
    classesList.addMouseListener(dcListener);
    fieldsList.addMouseListener(dcListener);
    methodsList.addMouseListener(dcListener);

    selectButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        select(selectedEntry, selectedPackage, selectedClass,
               selectedMethod, selectedField);
      }
    });

    Dimension dim = new Dimension(350, 125);

    JPanel panel11 = new JPanel();
    panel11.setLayout(new BoxLayout(panel11, BoxLayout.Y_AXIS));

    JScrollPane sp111 = new JScrollPane(packagesList);
    sp111.setBorder(new TitledBorder(new EtchedBorder(), "Packages"));
    sp111.setMaximumSize(dim);
    sp111.setMinimumSize(dim);
    sp111.setPreferredSize(dim);

    JScrollPane sp112 = new JScrollPane(classesList);
    sp112.setBorder(new TitledBorder(new EtchedBorder(), "Classes"));
    sp112.setMaximumSize(dim);
    sp112.setMinimumSize(dim);
    sp112.setPreferredSize(dim);

    panel11.add(sp111);
    panel11.add(sp112);

    JPanel panel12 = new JPanel();
    panel12.setLayout(new BoxLayout(panel12, BoxLayout.Y_AXIS));
    JScrollPane sp121 = new JScrollPane(fieldsList);
    sp121.setBorder(new TitledBorder(new EtchedBorder(), "Fields"));
    sp121.setMaximumSize(dim);
    sp121.setMinimumSize(dim);
    sp121.setPreferredSize(dim);
    JScrollPane sp122 = new JScrollPane(methodsList);
    sp122.setBorder(new TitledBorder(new EtchedBorder(), "Methods"));
    sp122.setMaximumSize(dim);
    sp122.setMinimumSize(dim);
    sp122.setPreferredSize(dim);

    panel12.add(sp121);
    panel12.add(sp122);

    JPanel panel1 = new JPanel();
    panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
    panel1.setBorder(new TitledBorder(new EtchedBorder(), "Entries"));

    panel1.add(entriesCombo);
    panel1.add(Box.createRigidArea(new Dimension(3, 0)));
    panel1.add(upButton);
    panel1.add(Box.createRigidArea(new Dimension(3, 0)));
    panel1.add(downButton);
    panel1.add(Box.createRigidArea(new Dimension(3, 0)));
    panel1.add(addButton);
    panel1.add(Box.createRigidArea(new Dimension(3, 0)));
    panel1.add(removeButton);
    panel1.add(Box.createRigidArea(new Dimension(3, 0)));
    panel1.add(defaultsButton);

    JPanel panel2 = new JPanel();
    panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
    panel2.add(Box.createRigidArea(new Dimension(3, 0)));
    panel2.add(hierarchyCombo);
    panel2.add(Box.createRigidArea(new Dimension(3, 0)));

    JPanel panel3 = new JPanel();
    JSplitPane splitPane =
               new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel11, panel12);

    panel3.setLayout(new BoxLayout(panel3, BoxLayout.X_AXIS));
    panel3.add(Box.createRigidArea(new Dimension(3, 0)));
    panel3.add(splitPane);
    panel3.add(Box.createRigidArea(new Dimension(3, 0)));

    JPanel panel4 = new JPanel();
    panel4.setLayout(new BoxLayout(panel4, BoxLayout.X_AXIS));
    panel4.add(Box.createRigidArea(new Dimension(20, 0)));
    panel4.add(selectButton);
    panel4.add(Box.createRigidArea(new Dimension(20, 0)));

    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    this.add(panel1);
    this.add(panel2);
    this.add(panel3);
    this.add(Box.createRigidArea(new Dimension(0, 10)));
    this.add(panel4);
    this.add(Box.createRigidArea(new Dimension(0, 10)));

    selectButton.requestFocus();
  }


  /**
   * Starts thread for filling out GUI components with actual data
   * after displaying the window
   */
  public void startThread() {
    Thread t = new Thread() {
      public void run() {
        //JFrame mainFrame = getJFrame();
        JFrame mainFrame = (JFrame)ClassHoundPanel.this.getTopLevelAncestor();

        //mainFrame.getGlassPane().setVisible(true);

        service = ClassHound.getInstance();

        entriesCombo.setModel(new DefaultComboBoxModel(
                                  service.getEntries().toArray()));
        entriesCombo.setSelectedIndex(0);

        combosListener.itemStateChanged(
          new ItemEvent(entriesCombo, ItemEvent.ITEM_STATE_CHANGED,
                        entriesCombo.getSelectedItem(), ItemEvent.SELECTED)
        );

        //mainFrame.getGlassPane().setVisible(false);
      }
    };

    t.setPriority(Thread.NORM_PRIORITY-3);
    t.start();
  }

  /**
   * Looks for packages in specified CLASSPATH entry
   *
   * @param cpEntry  CLASSPATH entry
   */
  private void fillPackagesList(String cpEntry) {
    selectedEntry   = cpEntry;
    selectedPackage = null;
    selectedClass   = null;
    selectedMethod  = null;
    selectedField   = null;

    service.fillPackagesList(selectedEntry);

    packagesList.setListData(service.getPackages().toArray());
    classesList.setListData(service.getClasses().toArray());
    hierarchyCombo.setModel(new DefaultComboBoxModel(new Object[0]));
    fieldsList.setListData(service.getFields().toArray());
    methodsList.setListData(service.getMethods().toArray());
  }

  /**
   * Looks for classes from specified package
   *
   * @param packageName package name
   */
  private void fillClassesList(String packageName) {
    selectedPackage = packageName;
    selectedClass   = null;
    selectedMethod  = null;
    selectedField   = null;

    service.fillClassesList(selectedEntry, selectedPackage);

    classesList.setListData(service.getClasses().toArray());
    hierarchyCombo.setModel(new DefaultComboBoxModel(new Object[0]));
    fieldsList.setListData(service.getFields().toArray());
    methodsList.setListData(service.getMethods().toArray());
  }

  /**
   * Looks for members (methods and fields) of specified class
   *
   * @param className class name
   */
  private void fillMembersLists(String className) {
    selectedClass  = className;
    selectedMethod = null;
    selectedField  = null;

    service.fillMembersLists(selectedEntry, selectedPackage, selectedClass);

    boolean isAnonymous = (selectedPackage.equals(ClassHound.ANONYMOUS_PACKAGE));

    String qualifiedClassName = null;

    if(isAnonymous) {
      qualifiedClassName = selectedClass;
    }
    else {
      qualifiedClassName = selectedPackage + "." + selectedClass;
    }

    hierarchyCombo.setClass(qualifiedClassName, selectedEntry);
    hierarchyCombo.setSelectedItem(qualifiedClassName);

    fieldsList.setListData(service.getFields().toArray());
    methodsList.setListData(service.getMethods().toArray());
  }

  /**
   * Common routine-action for selecting either CLASSPATH entry,
   * package, class or member (method or field)
   *
   * @param selectedEntry  selected CLASSPATH entry
   * @param selectedPackage selected package
   * @param selectedClass selected class
   * @param selectedMethod selected method
   * @param selectedField selected field
   */
  public void select(String selectedEntry, String selectedPackage,
         String selectedClass, String selectedMethod, String selectedField) {
    if(selectedEntry == null) {
      return;
    }

    if(selectedPackage == null) {
      return;
    }

    if(selectedClass == null) {
      selectClass(selectedEntry, selectedPackage);
    }
    else {
      selectMember(selectedEntry, selectedPackage, selectedClass,
                   selectedField, selectedMethod);
    }
  }

  /**
   * Routine-action for selecting given class
   *
   * @param selectedEntry  selected CLASSPATH entry
   * @param selectedPackage selected package
   */
  private void selectClass(String selectedEntry, String selectedPackage) {
    try {
      if(selectedEntry != null) {
        parent.open(new File(selectedEntry));
      }
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Routine-action for selecting given member (method or field)
   *
   * @param selectedEntry  selected CLASSPATH entry
   * @param selectedPackage selected package
   */
  private void selectMember(String selectedEntry, String selectedPackage,
                            String selectedClass,
                            String selectedField, String selectedMethod) {

    boolean isAnonymous = (selectedPackage.equals(ClassHound.ANONYMOUS_PACKAGE));

    String selectedDir = "";

    if(!isAnonymous) {
      selectedDir = selectedPackage.replace('.', '/') + "/";
    }

    String name = selectedClass + ".class";

    if(!isAnonymous) {
      name = selectedDir + name;
    }

    try {
      DataSource dataSource = new ArchivedDataSource(new ZipFile(selectedEntry), name);

 /*     CustomFrame frame = parent.getFrameManager().getFrame(dataSource);

      if(frame != null) {
        frame.open();
      }
      */
      parent.open(dataSource);
    }
    catch(IOException e) {

    }

    /*
    MainFrame mainFrame = (MainFrame)getJFrame();

    JInternalFrame frame = mainFrame.getFrameManager().getCurrentFrame();

    if(frame instanceof ClassFrame) {
      ClassFrame classFrame = (ClassFrame)frame;

      if(selectedField != null) {
        classFrame.setFieldPosition(selectedField);
      }
      else {
        classFrame.setMethodPosition(selectedMethod);
      }
    }
    */

    if(selectedField != null) {
      parent.setFieldPosition(selectedField);
    }
    else {
      parent.setMethodPosition(selectedMethod);
    }

  }

/**
 * Class-listener for double clicking in the list
 */
class DoubleClickListener extends MouseAdapter {
  public void mouseClicked(MouseEvent event) {
    Object source = event.getSource();

    if(source instanceof JList) {
      JList list = (JList)source;

      int index = list.getSelectedIndex();

      if(index != -1) {
        if(event.getClickCount() == 2) {
          select(selectedEntry, selectedPackage, selectedClass, selectedMethod, selectedField);
        }
      }
    }
  }
}

/**
 * Listener class that contains code for actions in the lists.
 * If the user double-clicks or press Enter, these actions will
 * be performed
 */
class EntriesListener implements ActionListener {
  public void actionPerformed(ActionEvent event) {
    String cmd = event.getActionCommand();

    if(cmd.equals(UP_COMMAND)) {
      int index = entriesCombo.getSelectedIndex();

      if(index > 0) {
        entriesCombo.setSelectedIndex(--index);
      }
    }
    else if(cmd.equals(DOWN_COMMAND)) {
      int index = entriesCombo.getSelectedIndex();

      if(index >= 0 && index < entriesCombo.getItemCount()-1) {
        entriesCombo.setSelectedIndex(++index);
      }
    }
    else if(cmd.equals(ADD_COMMAND)) {
      File file = parent.getFileChooser().open("Select directory or archive");

      if(file != null) {
        String name = file.getName().toLowerCase();
        boolean isArchive = (name.endsWith(".jar") || name.endsWith(".zip"));

        if((file.isDirectory() || isArchive)) {
          String newEntryName = file.getPath();

          Map entryToFilesMap = service.getEntryToFilesMap();

          List files = (List)entryToFilesMap.get(newEntryName);

          if(files == null) {
            entriesCombo.addItem(newEntryName);
            service.addEntry(newEntryName);
            entriesCombo.setSelectedIndex(entriesCombo.getItemCount()-1);
            combosListener.itemStateChanged(
               new ItemEvent(entriesCombo, ItemEvent.ITEM_STATE_CHANGED,
                             entriesCombo.getSelectedItem(),ItemEvent.SELECTED)
            );
          }
        }
      }
    }
    else if(cmd.equals(REMOVE_COMMAND)) {
      String item = (String)entriesCombo.getSelectedItem();

      int index = entriesCombo.getSelectedIndex();

      if(index >= 0) {
        entriesCombo.removeItemAt(index);
        service.removeEntry(item);
      }
    }
    else if(cmd.equals(SET_DEFAULTS_COMMAND)) {
      startThread();
    }
  }

}

/**
 * Listener class that contains code for selecting different lists.
 * When new lis have choosen, the rest of the list should change
 * their content.
 */
class ListsListener implements ListSelectionListener {

  public void valueChanged(ListSelectionEvent event) {
    Object source = event.getSource();

    if(source == packagesList) {
      // fill classes list
      fillClassesList((String)packagesList.getSelectedValue());
    }
    else if(source == classesList) {
      // fill methods list
      fillMembersLists((String)classesList.getSelectedValue());
    }
    else if(source == fieldsList) {
      selectedMethod = null;
      methodsList.clearSelection();
      selectedField  = (String)fieldsList.getSelectedValue();
    }
    else if(source == methodsList) {
      selectedField  = null;
      fieldsList.clearSelection();
      selectedMethod = (String)methodsList.getSelectedValue();
    }
  }
}


/**
 * Listener class that contains code which executed when
 * the user moves position in the list
 */
class CombosListener implements ItemListener {
  public void itemStateChanged(ItemEvent event) {
    if(event.getID() == ItemEvent.DESELECTED) {
      return;
    }

    Object source = event.getSource();
    if(source == entriesCombo) {
      // fill packages list
      if(entriesCombo.getSelectedIndex() >= 0)
        fillPackagesList((String)entriesCombo.getSelectedItem());
      else {
        selectedEntry   = null;
        selectedPackage = null;
        selectedClass   = null;
        selectedMethod  = null;
        selectedField   = null;

        service.clearPackages();
        service.clearClasses();
        service.clearFields();
        service.clearMethods();

        packagesList.setListData(service.getPackages().toArray());
        classesList.setListData(service.getClasses().toArray());
        hierarchyCombo.setModel(new DefaultComboBoxModel(new Object[0]));
        fieldsList.setListData(service.getFields().toArray());
        methodsList.setListData(service.getMethods().toArray());
      }
    }
    else if(source == hierarchyCombo) {
      String item = hierarchyCombo.getSelectedItem().toString();

      String packagePart = null,
             classPart   = null;

      int pos = item.lastIndexOf(".");

      if(pos == -1) {
        packagePart = ClassHound.ANONYMOUS_PACKAGE;
        classPart    = item;
      }
      else {
        packagePart = item.substring(0, pos);
        classPart    = item.substring(pos+1);
      }

      List packages = service.getPackages();

      if(packages.contains(packagePart)) {
        packagesList.setSelectedValue(packagePart, true);
        classesList.setSelectedValue(classPart, true);
      }
      else {
        String searchingFileName = item.replace('.', '/') + ".class";

        List entries = service.getEntries();

        for(int i=0; i < entries.size(); i++) {
          String iEntry = (String)entries.get(i);

          if(!iEntry.equals(selectedEntry)) {
            Map entryToFilesMap = service.getEntryToFilesMap();

            List files = (List)entryToFilesMap.get(iEntry);

            for(int j=0; j < files.size(); j++) {
              String iFileName = ((String)files.get(j));
              iFileName = iFileName.replace(File.separatorChar, '/');

              if(searchingFileName.equals(iFileName)) {
                entriesCombo.setSelectedItem(iEntry);
                packagesList.setSelectedValue(packagePart, true);
                classesList.setSelectedValue(classPart, true);
                return;
              }
            }
          }
        }
      }
    }
  }
}

}