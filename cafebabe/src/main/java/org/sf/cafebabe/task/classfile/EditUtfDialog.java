// EditUtfDialog.java

package org.sf.cafebabe.task.classfile;

import java.awt.Rectangle;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.BoxLayout;

/**
 * This class represents the dialog window for editing
 * UTF8 string
 *
 * @version 1.0 02/05/2002
 * @author Alexander Shvets
 */
public class EditUtfDialog extends JDialog {
  private String utfString;

  /**
   * Creates new dialog window for editing UTF8 string
   *
   * @param parent the parent frame
   * @param utf the default value for UTF8 string
   */
  public EditUtfDialog(JFrame parent, String utf) {
    super(parent, true);

    enableEvents(AWTEvent.FOCUS_EVENT_MASK);

    this.getContentPane().setLayout(new BorderLayout());

    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        utfString = null;
      }
    });

    // Set the characteristics for this dialog instance
    this.setTitle("Edit UTF string...");
    this.setResizable(false);
    Rectangle r = parent.getBounds();
    this.setBounds(r.x + r.width/2 - 300, r.y + r.height/2 - 90/2, 200, 90);

    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    // Create a panel for the components
    JPanel topPanel = new JPanel();

    this.getContentPane().add(topPanel, BorderLayout.CENTER);

    final JTextField input = new JTextField(48);
    input.setText(utf);

    JButton okButton = new JButton("Ok");

    okButton.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        utfString = input.getText();
        EditUtfDialog.this.dispose();
      }
    });

    JButton cancelButton = new JButton("Cancel");

    cancelButton.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        utfString = null;
        EditUtfDialog.this.dispose();
      }
    });

    topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
    topPanel.add(new JLabel("UTF string to edit: "));
    topPanel.add(input);
    topPanel.add(okButton);
    topPanel.add(cancelButton);
    pack();

    input.requestFocus();
  }

  /**
   * Gets the UTf8 string
   *
   * @return the UTf8 string
   */
  public String getUtfString() {
    return utfString;
  }

}
