// HelpPanel.java

package org.sf.cafebabe.help;

import java.awt.Dimension;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JEditorPane;
import javax.swing.JComboBox;
import javax.swing.BoxLayout;
import javax.swing.SwingUtilities;
import javax.swing.Box;
import javax.swing.text.Document;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class HelpPanel extends JPanel {
  private final String PREVIOUS_BUTTON_TEXT = "Previous";
  private final String NEXT_BUTTON_TEXT     = "Next";
  private final String HISTORY_TEXT         = "History";

  private JButton previousButton = new JButton(PREVIOUS_BUTTON_TEXT);
  private JButton nextButton     = new JButton(NEXT_BUTTON_TEXT);
  private JComboBox historyCombo = new JComboBox();

  private JEditorPane editorPane = new JEditorPane();
  private JLabel status          = new JLabel("  ");

  private Vector visitedRefs = new Vector();
  private String currRef;

  public HelpPanel() {
    HtmlListener htmlListener = new HtmlListener();
    HistoryListener historyListener = new HistoryListener();
    NavigationListener navigationListener = new NavigationListener();

    editorPane.addHyperlinkListener(htmlListener);

    historyCombo.addItemListener(historyListener);
    nextButton.addActionListener(navigationListener);
    previousButton.addActionListener(navigationListener);

    JScrollPane scrollPane1 = new JScrollPane();
    scrollPane1.setAutoscrolls(true);

    editorPane.setContentType("text/html");
    editorPane.setEditable(false);

    editorPane.addPropertyChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        String property = propertyChangeEvent.getPropertyName();
        if(property.equals("page")) {
          status.setText("  ");
          updateHistory(currRef);
        }
      }
    });

    previousButton.setActionCommand("previous");
    nextButton.setActionCommand("next");

    status.setFont(new Font("SanSerif", Font.PLAIN, 10));

    previousButton.setEnabled(false);
    nextButton.setEnabled(false);
    historyCombo.setEnabled(false);

    status.setText(" ");
    status.setFont(new Font("Dialog", Font.PLAIN, 12));

    scrollPane1.getViewport().add(editorPane, null);

    JPanel panel1 = new JPanel();
    panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
    panel1.add(Box.createRigidArea(new Dimension(5, 0)));
    panel1.add(previousButton);
    panel1.add(Box.createRigidArea(new Dimension(5, 0)));
    panel1.add(nextButton);
    panel1.add(Box.createRigidArea(new Dimension(25, 0)));
    panel1.add(new JLabel(HISTORY_TEXT));
    panel1.add(Box.createRigidArea(new Dimension(25, 0)));
    panel1.add(historyCombo);
    panel1.add(Box.createRigidArea(new Dimension(5, 0)));

    JPanel panel2 = new JPanel();
    panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
    panel2.add(Box.createRigidArea(new Dimension(5, 0)));
  //  panel2.add(splitPane);
    panel2.add(scrollPane1);
    panel2.add(Box.createRigidArea(new Dimension(5, 0)));

    JPanel panel3 = new JPanel();
    panel3.setPreferredSize(new Dimension(700, 20));
    panel3.setLayout(new BoxLayout(panel3, BoxLayout.X_AXIS));
    panel3.add(Box.createRigidArea(new Dimension(5, 0)));
    panel3.add(status);
    panel3.add(Box.createRigidArea(new Dimension(700, 0)));

    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.add(Box.createRigidArea(new Dimension(0, 5)));
    this.add(panel1);
    this.add(Box.createRigidArea(new Dimension(0, 5)));
    this.add(panel2);
    this.add(Box.createRigidArea(new Dimension(0, 5)));
    this.add(panel3);
    this.add(Box.createRigidArea(new Dimension(0, 5)));
  }

  public Dimension getPreferredSize() {
    return new Dimension(750, 500);
  }

  public void updateHistory(String ref) {
    String oldRef = (String)historyCombo.getSelectedItem();
    if(oldRef != null && oldRef.equals(ref)) return;

    if(!visitedRefs.contains(ref)) {
      visitedRefs.addElement(ref);
      historyCombo.addItem(ref);
    }

    setControls(visitedRefs.indexOf(ref));
  }

  private void setControls(int index) {
    historyCombo.setSelectedIndex(index);

    if(index == visitedRefs.size()-1)
      nextButton.setEnabled(false);
    else
      nextButton.setEnabled(true);

    if(index == 0)
      previousButton.setEnabled(false);
    else
      previousButton.setEnabled(true);

    if(visitedRefs.size() > 0)
      historyCombo.setEnabled(true);
    else
      historyCombo.setEnabled(false);
  }

  private synchronized void showRef(String ref) {
    ref = ref.replace('\\', '/');

    if(ref.equals(currRef)) {
      return;
    }

    try {
      status.setText("Please wait...");

      currRef = ref;

      editorPane.setPage(new URL(ref));
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


class HistoryListener implements ItemListener {
  public void itemStateChanged(ItemEvent event) {
    if(event.getStateChange() == ItemEvent.SELECTED) {
      setControls(historyCombo.getSelectedIndex());
      showRef((String)historyCombo.getSelectedItem());
    }
  }
}

class HtmlListener implements HyperlinkListener {
  public void hyperlinkUpdate(HyperlinkEvent event) {
    if(event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
      linkActivated(event.getURL());
    }
    else if(event.getEventType() == HyperlinkEvent.EventType.ENTERED) {
      status.setText(event.getURL().toString());
      editorPane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
    else if(event.getEventType() == HyperlinkEvent.EventType.EXITED) {
      status.setText("  ");
      editorPane.setCursor(Cursor.getDefaultCursor());
    }
  }

  protected void linkActivated(URL url) {
    Cursor oldCursor = editorPane.getCursor();

    editorPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    SwingUtilities.invokeLater(new PageLoader(url, oldCursor));

    updateHistory(url.getFile().toString());
  }

}

class PageLoader implements Runnable {
  URL url;
  Cursor cursor;

  PageLoader(URL url, Cursor cursor) {
    this.url    = url;
    this.cursor = cursor;
  }

  public void run() {
    if(url == null) {
      editorPane.setCursor(cursor);

      Container parent = editorPane.getParent();
      parent.repaint();
    }
    else {
      Document doc = editorPane.getDocument();
      try {
        editorPane.setPage(url);
      }
      catch (IOException ioe) {
        editorPane.setDocument(doc);
        getToolkit().beep();
      }
      finally {
        url = null;
        SwingUtilities.invokeLater(this);
      }
    }
  }

}

class NavigationListener implements ActionListener {
  public void actionPerformed(ActionEvent event) {
    String cmd = event.getActionCommand();
    int index = historyCombo.getSelectedIndex();
    if(cmd.equals("next")) {
      setControls(++index);
    }
    else if(cmd.equals("previous")) {
      setControls(--index);
    }
  }

}

  /**
   * main method
   */
  public static void main(String[] argv) throws Exception {
    JFrame frame = new JFrame("Help System");

    frame.setSize(400, 500);

    HelpPanel helpViewer = new HelpPanel();

    frame.getContentPane().add(helpViewer);

    frame.addWindowListener(new WindowAdapter() {
      public void windowClosed(WindowEvent event) {
        System.exit(0);
      }
    });

    frame.setVisible(true);
  }

}
