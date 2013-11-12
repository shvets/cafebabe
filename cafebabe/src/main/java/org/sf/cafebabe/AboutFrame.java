// AboutFrame.java

package org.sf.cafebabe;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import org.sf.cafebabe.util.IconProducer;

/**
 * Internal frame which informs user about a program
 * and author of this program
 */
public class AboutFrame extends CustomFrame {
  private JDesktopPane desktopPane;
  private Integer layer;

  public AboutFrame(final JDesktopPane desktopPane, Integer layer) {
    super();

    setTitle("About a program");

    setResizable(false);

    this.desktopPane = desktopPane;

    this.layer = layer;

    // Create a panel for the components
    JPanel topPanel = new JPanel();
    topPanel.setLayout(new BorderLayout());
    this.getContentPane().add(topPanel);

    final JLabel label0     = new JLabel();
    final JLabel label1     = new JLabel(Constants.MAIN_FRAME_TITLE);
    final JLabel label2     = new JLabel("Author: " + Constants.AUTHOR);
    final JLabel label3     = new JLabel("e-mail: <" + Constants.EMAIL + ">");
    final JButton okButton  = new JButton("Ok");

    label1.setAlignmentX(CENTER_ALIGNMENT);
    label2.setAlignmentX(CENTER_ALIGNMENT);
    label3.setAlignmentX(CENTER_ALIGNMENT);
    okButton.setAlignmentX(CENTER_ALIGNMENT);

    final ImageIcon icon1 = IconProducer.getImageIcon(Constants.ICON_FACE1);
    final ImageIcon icon2 = IconProducer.getImageIcon(Constants.ICON_FACE2);

    label0.addMouseListener(new MouseAdapter() {
      { label0.setIcon(icon1); }
      public void mouseEntered(MouseEvent e) {
        label0.setIcon(icon2);
      }
      public void mouseExited(MouseEvent e) {
        label0.setIcon(icon1);
      }
    });

    // create mouse-sensitive label
    label3.setToolTipText("Press label to send comments to author");
    label3.addMouseListener(new MailAdapter(label3));

    okButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        close();
      }
    });

    JPanel panel1 = new JPanel();
    panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));

    panel1.add(Box.createRigidArea(new Dimension(0, 70)));
    panel1.add(label1);
    panel1.add(Box.createRigidArea(new Dimension(0, 10)));
    panel1.add(label2);
    panel1.add(Box.createRigidArea(new Dimension(0, 10)));
    panel1.add(label3);
    panel1.add(Box.createRigidArea(new Dimension(0, 50)));
    panel1.add(okButton);

    topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
    topPanel.add(Box.createRigidArea(new Dimension(20, 0)));
    topPanel.add(label0);
    topPanel.add(Box.createRigidArea(new Dimension(20, 0)));
    topPanel.add(panel1);
    topPanel.add(Box.createRigidArea(new Dimension(20, 0)));

    desktopPane.add(this, layer);

    okButton.requestFocus();
  }

  public void open() {
   super.open();

   Rectangle r = desktopPane.getBounds();

   this.setBounds(r.x + r.width/2 - 500/2, r.y + r.height/2 - 400/2, 500, 300);
  }

// inner class-adapter for mouse-sensitive label
class MailAdapter extends MouseAdapter {

  private final Border borderEntered = BorderFactory.createRaisedBevelBorder();
  private final Border borderPressed = BorderFactory.createLoweredBevelBorder();

  private JLabel label;

  MailAdapter(JLabel label) {
    this.label = label;
    label.setBorder(getExitedBorder());
  }

  private Border getExitedBorder() {
    Color c = getBackground();
    return BorderFactory.createBevelBorder(BevelBorder.LOWERED, c, c, c, c);
  }

  public void mouseEntered(MouseEvent e) {
    label.setBorder(borderEntered);
  }

  public void mouseExited(MouseEvent e) {
    label.setBorder(getExitedBorder());
  }

  public void mousePressed(MouseEvent e) {
    label.setBorder(borderPressed);
  }

  public void mouseReleased(MouseEvent e) {
    label.setBorder(borderEntered);

    new MailFrame(desktopPane, layer);
  }

}

}
