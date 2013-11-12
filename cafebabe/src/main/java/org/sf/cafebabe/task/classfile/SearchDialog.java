// SearchDialog.java

package org.sf.cafebabe.task.classfile;

import org.sf.cafebabe.Constants;
import org.sf.cafebabe.gadget.classtree.EntryNode;
import org.sf.cafebabe.gadget.classtree.PlainClassTree;
import org.sf.classfile.*;
import org.sf.classfile.attribute.CodeAttribute;
import org.sf.classfile.attribute.MethodBody;
import org.sf.classfile.instruction.Instruction;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Enumeration;
import java.util.StringTokenizer;

/**
 * Dialog for search preparation process through class file entities
 */
public class SearchDialog extends JDialog {
  private final String PLAIN_ACTION     = "plain";
  private final String CP_ACTION        = "cp";
  private final String FIELD_ACTION     = "field";
  private final String METHOD_ACTION    = "method";
  private final String ATTRIBUTE_ACTION = "attribute";

  static private int searchMode         = Constants.SEARCH_PLAIN;
  static private String searchString    = null;

  static private JTextField inputField;

  private PlainClassTree classTree;
  private ConstPool constPool;

  public SearchDialog(JFrame parentFrame, PlainClassTree classTree, ConstPool constPool) {
    // Make sure we call the parent
    super(parentFrame, false);

    this.classTree = classTree;
    this.constPool = constPool;

    enableEvents(AWTEvent.FOCUS_EVENT_MASK);

    this.getContentPane().setLayout(new BorderLayout());

    // Set the characteristics for this dialog instance
    this.setTitle("Search a string...");
    this.setResizable(false);
    Rectangle r = parentFrame.getBounds();
    this.setBounds(r.x + r.width/2 - 550/2, r.y + r.height/2 - 210/2, 550, 210);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    ButtonGroup group = new ButtonGroup();

    JRadioButton radio1 = new JRadioButton("Plain");
    JRadioButton radio2 = new JRadioButton("Constant pool");
    JRadioButton radio3 = new JRadioButton("Fields");
    JRadioButton radio4 = new JRadioButton("Methods");
    JRadioButton radio5 = new JRadioButton("Class attributes");

    radio1.setActionCommand(PLAIN_ACTION);
    radio2.setActionCommand(CP_ACTION);
    radio3.setActionCommand(FIELD_ACTION);
    radio4.setActionCommand(METHOD_ACTION);
    radio5.setActionCommand(ATTRIBUTE_ACTION);

    radio1.setSelected((searchMode == Constants.SEARCH_PLAIN));
    radio2.setSelected((searchMode == Constants.SEARCH_CONST_POOL));
    radio3.setSelected((searchMode == Constants.SEARCH_FIELD));
    radio4.setSelected((searchMode == Constants.SEARCH_METHOD));
    radio5.setSelected((searchMode == Constants.SEARCH_CLASS_ATTRIBUTE));

    group.add(radio1);
    group.add(radio2);
    group.add(radio3);
    group.add(radio4);
    group.add(radio5);

    inputField = new JTextField(searchString, 45);
    JButton searchButton = new JButton("Next");
    JButton closeButton   = new JButton("Close");

    ModeListener modeListener     = new ModeListener();
    SearchListener searchListener = new SearchListener();

    radio1.addActionListener(modeListener);
    radio2.addActionListener(modeListener);
    radio3.addActionListener(modeListener);
    radio4.addActionListener(modeListener);
    radio5.addActionListener(modeListener);
    inputField.addActionListener(searchListener);
    searchButton.addActionListener(searchListener);
    closeButton.addActionListener(new CloseListener());

    JPanel panel11 = new JPanel();
    panel11.setBorder(new TitledBorder(new EtchedBorder(), "Search mode"));

    JPanel panel1 = new JPanel();
    panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
    panel1.add(Box.createRigidArea(new Dimension(10, 0)));
    panel1.add(panel11);
    panel1.add(Box.createRigidArea(new Dimension(10, 0)));

    panel1.add(Box.createRigidArea(new Dimension(10, 0)));
    panel11.add(radio1);
    panel11.add(Box.createRigidArea(new Dimension(10, 0)));
    panel11.add(radio2);
    panel11.add(Box.createRigidArea(new Dimension(10, 0)));
    panel11.add(radio3);
    panel11.add(Box.createRigidArea(new Dimension(10, 0)));
    panel11.add(radio4);
    panel11.add(Box.createRigidArea(new Dimension(10, 0)));
    panel11.add(radio5);
    panel11.add(Box.createRigidArea(new Dimension(10, 0)));

    JPanel panel31 = new JPanel();
    panel31.setBorder(new TitledBorder(new EtchedBorder(), "Input a string:"));

    JPanel panel3 = new JPanel();
    panel3.setLayout(new BoxLayout(panel3, BoxLayout.X_AXIS));
    panel3.add(Box.createRigidArea(new Dimension(10, 0)));
    panel3.add(panel31);
    panel3.add(Box.createRigidArea(new Dimension(10, 0)));
    panel31.add(inputField);
    panel3.add(Box.createRigidArea(new Dimension(10, 0)));

    JPanel panel5 = new JPanel();
    panel5.setLayout(new BoxLayout(panel5, BoxLayout.X_AXIS));
    panel5.add(Box.createRigidArea(new Dimension(30, 0)));
    panel5.add(searchButton);
    panel5.add(Box.createRigidArea(new Dimension(20, 0)));
    panel5.add(closeButton);

    JPanel topPanel = new JPanel();
    topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
    this.setContentPane(topPanel);
    topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    topPanel.add(panel3);
    topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    topPanel.add(panel1);
    topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    topPanel.add(panel5);
    topPanel.add(Box.createRigidArea(new Dimension(0, 10)));

    inputField.requestFocus();
  }

// inner classes-adapters

class CloseListener implements ActionListener {
  public void actionPerformed(ActionEvent e) {
    dispose();
  }
}

class ModeListener implements ActionListener {
  public void actionPerformed(ActionEvent event) {
    String cmd = event.getActionCommand();

    if(cmd.equals(PLAIN_ACTION)) {
      searchMode = Constants.SEARCH_PLAIN;
    }
    else if(cmd.equals(CP_ACTION)) {
      searchMode = Constants.SEARCH_CONST_POOL;
    }
    else if(cmd.equals(FIELD_ACTION)) {
      searchMode = Constants.SEARCH_FIELD;
    }
    else if(cmd.equals(METHOD_ACTION)) {
      searchMode = Constants.SEARCH_METHOD;
    }
    else if(cmd.equals(ATTRIBUTE_ACTION)) {
      searchMode = Constants.SEARCH_CLASS_ATTRIBUTE;
    }
  }
}

class SearchListener implements ActionListener {
  private SearchThread searchThread = null;
  private boolean isFirstClick     = true;

  public void actionPerformed(ActionEvent e) {
    if(isFirstClick) {
      isFirstClick = false;

      searchString = inputField.getText();

      if(searchString != null && searchString.length() > 0) {
        searchThread = new SearchThread(searchString, searchMode);
        searchThread.start();
      }
    }
    else {
      if(searchThread != null) {
        synchronized(searchThread) {
          searchThread.notify();
        }
      }
      else {
        isFirstClick = true;
      }
    }
  }

class SearchThread extends Thread {
  TreeModel dataModel = classTree.getModel();
  TreeNode root = (TreeNode)dataModel.getRoot();

  boolean isFound = false;

  private String line;
  private int searchMode;

  SearchThread(String line, int searchMode) {
    super("SearchThread");

    setPriority(Thread.MIN_PRIORITY+2);

    this.line       = line ;
    this.searchMode = searchMode;
  }

  public void run() {
    TreeNode treeNode = null;

    if(searchMode == Constants.SEARCH_PLAIN) {
      treeNode = root;
    }
    else if(searchMode == Constants.SEARCH_CONST_POOL) {
      treeNode = getChild(ConstPool.TYPE);
    }
    else if(searchMode == Constants.SEARCH_FIELD) {
      treeNode = getChild(Constants.FIELDS_TEXT);
    }
    else if(searchMode == Constants.SEARCH_METHOD) {
      treeNode = getChild(Constants.METHODS_TEXT);
    }
    else if(searchMode == Constants.SEARCH_CLASS_ATTRIBUTE) {
      treeNode = getChild(Constants.CLASS_ATTRIBUTES_TEXT);
    }

    if(treeNode != null) {
      search(line, treeNode);
    }

    if(isFound) {
      JOptionPane.showMessageDialog(SearchDialog.this,
                  "No more string \"" + line + "\" in this class file.", "Search dialog",
                   JOptionPane.INFORMATION_MESSAGE);
    }
    else {
      JOptionPane.showMessageDialog(SearchDialog.this,
                  "String \"" + line + "\" does not found!", "Search dialog",
                   JOptionPane.INFORMATION_MESSAGE);
    }

    isFirstClick = true;
  }

  private void search(String line, TreeNode node) {
    Enumeration e = node.children();
    while(e.hasMoreElements()) {
      TreeNode iNode = (TreeNode)e.nextElement();

      if(iNode instanceof EntryNode) {
        Entry entry = ((EntryNode)iNode).getEntry();

        if(entry instanceof MethodEntry) {
          if(searchMode == Constants.SEARCH_PLAIN ||
             searchMode == Constants.SEARCH_METHOD) {
            MethodEntry methodEntry = (MethodEntry)entry;
            AttributeEntry attribute =
                           methodEntry.getAttribute(AttributeEntry.CODE, constPool);

            if(attribute != null) {
              CodeAttribute codeAttribute;
              try {
                codeAttribute = new CodeAttribute(attribute);
              }
              catch(IOException ex) {
                ex.printStackTrace();
                return;
              }

              MethodBody methodBody = codeAttribute.getMethodBody();

              Instruction[] instructions = methodBody.getInstructions();

              for(int i=0; i < instructions.length; i++) {
                Instruction instruction = instructions[i];
                String str = instruction.resolve(constPool);

                StringTokenizer st = new StringTokenizer(str);

                while(st.hasMoreTokens()) {
                  String token = st.nextToken();
                  if(token.toLowerCase().startsWith(line.toLowerCase())) {
                    classTree.openMethodBodyEditor(methodEntry, i);
                    isFound = true;
                    try {
                      synchronized(this) {
                        this.wait();
                      }
                    }
                    catch(InterruptedException ex) {}
                  }
                }
              }
            }
          }
        }
      }

      String text = iNode.toString();

      if(iNode instanceof EntryNode) {
        Entry entry = ((EntryNode)iNode).getEntry();

        if(entry instanceof Resolvable) {
          Resolvable resolvable = (Resolvable)entry;

          //System.out.println("??? " + resolvable.resolve(constPool));

          text = resolvable.resolve(constPool);
        }
      }

      StringTokenizer st = new StringTokenizer(text);
      while(st.hasMoreTokens()) {
        String token = st.nextToken();
        if(token.toLowerCase().startsWith(line.toLowerCase())) {
          classTree.changePosition(iNode);
          isFound = true;
          try {
            synchronized(this) {
              this.wait();
            }
          }
          catch(InterruptedException ex) {}
        }
      }

      search(line, iNode);
    }
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

}

}

}
