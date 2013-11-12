// SearchDialog.java

package org.sf.cafebabe.task.serfile;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import org.sf.cafebabe.Constants;

/**
 * dialog for search preparation process through class file entities
 */
public class SearchDialog extends JDialog {
  private final String PLAIN_ACTION     = "plain";
  private final String CP_ACTION        = "cp";
  private final String FIELD_ACTION     = "field";
  private final String METHOD_ACTION    = "method";
  private final String ATTRIBUTE_ACTION = "attribute";

  private boolean isOk = true;

  static private String searchString;
  static private int searchMode = Constants.SEARCH_PLAIN;

  private JTextField inputField;

  public SearchDialog(JFrame parentFrame) {
    // Make sure we call the parent
    super(parentFrame, true);

    enableEvents(AWTEvent.FOCUS_EVENT_MASK);

    this.getContentPane().setLayout(new BorderLayout());

    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        searchString = null;
      }
    });

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

    radio1.setSelected((searchMode == Constants.SEARCH_PLAIN) ? true : false);
    radio2.setSelected((searchMode == Constants.SEARCH_CONST_POOL) ? true : false);
    radio3.setSelected((searchMode == Constants.SEARCH_FIELD) ? true : false);
    radio4.setSelected((searchMode == Constants.SEARCH_METHOD) ? true : false);
    radio5.setSelected((searchMode == Constants.SEARCH_CLASS_ATTRIBUTE) ? true : false);

    group.add(radio1);
    group.add(radio2);
    group.add(radio3);
    group.add(radio4);
    group.add(radio5);

    inputField = new JTextField(searchString, 45);
    JButton searchButton = new JButton("Ok");
    JButton cancelButton = new JButton("Cancel");

    ModeListener modeListener    = new ModeListener();
    SearchListener searchListener = new SearchListener();

    radio1.addActionListener(modeListener);
    radio2.addActionListener(modeListener);
    radio3.addActionListener(modeListener);
    radio4.addActionListener(modeListener);
    radio5.addActionListener(modeListener);
    inputField.addActionListener(searchListener);
    searchButton.addActionListener(searchListener);
    cancelButton.addActionListener(new CancelListener());

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
    panel5.add(cancelButton);

    JPanel topPanel = new JPanel();
    topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
    this.setContentPane(topPanel);
    topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    topPanel.add(panel1);
    topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    topPanel.add(panel3);
    topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    topPanel.add(panel5);
    topPanel.add(Box.createRigidArea(new Dimension(0, 10)));

    inputField.requestFocus();
  }

  public boolean getAnswer() { return isOk; }

  public String getSearchString() { return searchString; }

  public int getSearchMode() { return searchMode; }

// inner classes-adapters

class SearchListener implements ActionListener {
  public void actionPerformed(ActionEvent e) {
    searchString = inputField.getText();
    isOk = true;
    dispose();
  }
}

class CancelListener implements ActionListener {
  public void actionPerformed(ActionEvent e) {
    isOk = false;
    dispose();
  }
}

class ModeListener implements ActionListener {
  public void actionPerformed(ActionEvent event) {
    String cmd = event.getActionCommand();
    if(cmd.equals(PLAIN_ACTION))
      searchMode = Constants.SEARCH_PLAIN;
    else if(cmd.equals(CP_ACTION))
      searchMode = Constants.SEARCH_CONST_POOL;
    else if(cmd.equals(FIELD_ACTION))
      searchMode = Constants.SEARCH_FIELD;
    else if(cmd.equals(METHOD_ACTION))
      searchMode = Constants.SEARCH_METHOD;
    else if(cmd.equals(ATTRIBUTE_ACTION))
      searchMode = Constants.SEARCH_CLASS_ATTRIBUTE;
  }
}


}
