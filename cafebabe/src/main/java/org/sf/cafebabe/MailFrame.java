// MailFrame.java

package org.sf.cafebabe;

import java.net.InetAddress;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JDesktopPane;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.SwingUtilities;

import org.sf.cafebabe.util.TimerLongOperation;
import org.sf.cafebabe.util.LongOperation;
import org.sf.cafebabe.util.SMTPClient;

/**
 * Internal frame for sending the letter to author
 */
public final class MailFrame extends CustomFrame implements ActionListener {
  final private static String MAIL_HOST_NAME = "mail.earthlink.net";

  private static String userName, hostName;

  static {
    try {
      userName = System.getProperty("user.name");
      hostName = InetAddress.getLocalHost().getHostName();
    }
    catch(Exception e) {}
  }

  final String USER_EMAIL = userName + "@" + hostName;
  final String SUBJECT    = "I have good idea about CafeBabe";

  final JComboBox mailhostCombo = new JComboBox();
  final JTextField toField      = new JTextField(Constants.EMAIL, 12);
  final JTextField fromField    = new JTextField(USER_EMAIL, 12);
  final JTextField subjectField = new JTextField(SUBJECT, 12);
  final JTextArea  messageField = new JTextArea(12, 5);

  final JButton sendButton  = new JButton("Send");
  final JButton closeButton = new JButton("Close");

  final JProgressBar progressBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);

  JDesktopPane desktopPane;

  public MailFrame(final JDesktopPane desktopPane, Integer layer) {
    setTitle("Direct mail to author");

    setResizable(false);

    this.desktopPane = desktopPane;

//    enableEvents(AWTEvent.FOCUS_EVENT_MASK);

    // Create a panel for the components
    JPanel topPanel = new JPanel();
    topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
    this.setContentPane(topPanel);

    final JLabel label0     = new JLabel("Mail-host : ");
    final JLabel label1     = new JLabel("To        : ");
    final JLabel label2     = new JLabel("From      : ");
    final JLabel label3     = new JLabel("Subject   : ");
    final JLabel label4     = new JLabel("Body      : ");

    Font font = new Font("Monospaced", Font.BOLD, 14);
    label0.setFont(font);
    label1.setFont(font);
    label2.setFont(font);
    label3.setFont(font);
    label4.setFont(font);

    toField.setEditable(false);
    subjectField.setForeground(Color.red);

    mailhostCombo.setEditable(true);
    mailhostCombo.addItem(MAIL_HOST_NAME);
//    mailhostCombo.addItem(MAIL_HOST_NAME2);
//    mailhostCombo.addItem(MAIL_HOST_NAME3);
    mailhostCombo.addItem(hostName);

    mailhostCombo.setSelectedIndex(0);

    sendButton.addActionListener(new ActionListener() {
      TimerLongOperation sender;
      public void actionPerformed(ActionEvent e) {
        String text = sendButton.getText();
        if(text.equals("Send")) {
          Runnable runnable = new Runnable() {
            public void run() {
              sender = new Sender(MailFrame.this);
              setCursor(new Cursor(Cursor.WAIT_CURSOR));
              sendButton.setText("Cancel");
              closeButton.setEnabled(false);
            }
          };
          SwingUtilities.invokeLater(runnable);
        }
        else if(text.equals("Cancel")) {
          if(sender != null) {
            sender.stop();
            sender = null;
          }
        }
      }
    });

    closeButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        close();
      }
    });

    JPanel panel0 = new JPanel();
    panel0.setLayout(new BoxLayout(panel0, BoxLayout.X_AXIS));
    panel0.add(Box.createRigidArea(new Dimension(10, 0)));
    panel0.add(label0);
    panel0.add(Box.createRigidArea(new Dimension(10, 0)));
    panel0.add(mailhostCombo);
    panel0.add(Box.createRigidArea(new Dimension(10, 0)));

    JPanel panel1 = new JPanel();
    panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
    panel1.add(Box.createRigidArea(new Dimension(10, 0)));
    panel1.add(label1);
    panel1.add(Box.createRigidArea(new Dimension(10, 0)));
    panel1.add(toField);
    panel1.add(Box.createRigidArea(new Dimension(10, 0)));

    JPanel panel2 = new JPanel();
    panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
    panel2.add(Box.createRigidArea(new Dimension(10, 0)));
    panel2.add(label2);
    panel2.add(Box.createRigidArea(new Dimension(10, 0)));
    panel2.add(fromField);
    panel2.add(Box.createRigidArea(new Dimension(10, 0)));

    JPanel panel3 = new JPanel();
    panel3.setLayout(new BoxLayout(panel3, BoxLayout.X_AXIS));
    panel3.add(Box.createRigidArea(new Dimension(10, 0)));
    panel3.add(label3);
    panel3.add(Box.createRigidArea(new Dimension(10, 0)));
    panel3.add(subjectField);
    panel3.add(Box.createRigidArea(new Dimension(10, 0)));

    JPanel panel4 = new JPanel();
    panel4.setLayout(new BoxLayout(panel4, BoxLayout.X_AXIS));
    panel4.add(Box.createRigidArea(new Dimension(10, 0)));
    panel4.add(label4);
    panel4.add(Box.createRigidArea(new Dimension(10, 0)));
    panel4.add(new JScrollPane(messageField));
    panel4.add(Box.createRigidArea(new Dimension(10, 0)));

    JPanel panelp = new JPanel();
    panelp.setLayout(new BoxLayout(panelp, BoxLayout.X_AXIS));
    panelp.add(Box.createRigidArea(new Dimension(20, 0)));
    panelp.add(progressBar);
    panelp.add(Box.createRigidArea(new Dimension(20, 0)));

    JPanel panel5 = new JPanel();
    panel5.setLayout(new BoxLayout(panel5, BoxLayout.X_AXIS));
    panel5.add(Box.createRigidArea(new Dimension(50, 0)));
    panel5.add(sendButton);
    panel5.add(Box.createRigidArea(new Dimension(20, 0)));
    panel5.add(closeButton);

    topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    topPanel.add(panel0);
    topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    topPanel.add(panel1);
    topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    topPanel.add(panel2);
    topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    topPanel.add(panel3);
    topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    topPanel.add(panel4);
    topPanel.add(Box.createRigidArea(new Dimension(0, 20)));
    topPanel.add(panelp);
    topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    topPanel.add(panel5);
    topPanel.add(Box.createRigidArea(new Dimension(0, 10)));

    desktopPane.add(this, layer);

    messageField.requestFocus();
  }

  public void open() {
   super.open();

    // Set the characteristics for this dialog instance
    Rectangle r = desktopPane.getBounds();
    this.setBounds(r.x + r.width/2 - 600/2, r.y + r.height/2 - 400/2, 600, 300);
  }

  // implements ActionListener
  public void actionPerformed(ActionEvent e) {
    if(progressBar.getValue() < progressBar.getMaximum()) {
      progressBar.setValue(progressBar.getValue() + 1);
    }
    else {
      progressBar.setValue(0);
    }
  }


// internal class-adapter that send a letter
class Sender extends TimerLongOperation {

  SMTPClient client;
  final String mailhost;

  Sender(ActionListener listener) {
    super(100, listener);

    mailhost        = (String)mailhostCombo.getSelectedItem();
    String heloHost = hostName;

    String from     = fromField.getText();
    String to       = toField.getText();
    String subject  = subjectField.getText();
    String body     = messageField.getText();

    client = new SMTPClient(mailhost, from, "", to, "", subject, body);

/*    client.setHeloHost(heloHost);
    client.setSender(from);
    client.setReceiver(to);
    client.setSubject(subject);
    client.setBody(body);

    super.startLoading();
    */
  }

  protected void execute() throws Exception {
    client.send();
  }

  public synchronized void stopLoading() {
    super.stopLoading();

    int type = getResultType();
    if(type == LongOperation.OK_OPERATION) {
      JOptionPane.showMessageDialog(desktopPane,
                 "A letter was sent succesfully.",
                 "Success", JOptionPane.WARNING_MESSAGE);
    }
    else if(type == LongOperation.ERROR_OPERATION) {
      JOptionPane.showMessageDialog(desktopPane,
               "Error during sending mail: \n" + getResultMessage(),
               "Failure", JOptionPane.ERROR_MESSAGE);
    }

    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    sendButton.setText("Send");
    closeButton.setEnabled(true);
    progressBar.setValue(0);

    client = null;
  }

}

}
