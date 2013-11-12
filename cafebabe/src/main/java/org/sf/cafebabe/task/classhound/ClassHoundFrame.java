// ClassHoundFrame.java

package org.sf.cafebabe.task.classhound;

import java.awt.event.*;
import java.awt.*;

import javax.swing.*;
import org.sf.cafebabe.Constants;
import org.sf.cafebabe.CustomFrame;
import org.sf.cafebabe.MainFrame;
import org.sf.cafebabe.util.Configurator;
import org.sf.cafebabe.util.IconProducer;

/**
 * Internal frame for displaying class-hound service
 *
 * @version 1.0 01/30/2002
 * @author Alexander Shvets
 */
public class ClassHoundFrame extends CustomFrame {
  private ClassHoundPanel panel;

  private static String TITLE = "Class-Hound Service";

  private static String CLOSE_STRING = "Close";
  private final JButton closeButton  = new JButton(CLOSE_STRING);

  /**
   * Creates new window for Class-Hound service
   * @param parent  the parent
   * @param desktopPane desktop pane
   * @param layer the layer
   */
  public ClassHoundFrame(MainFrame parent, JDesktopPane desktopPane, Integer layer) {
     panel = new ClassHoundPanel(parent);

    setTitle(TITLE);
    setFrameIcon(IconProducer.getImageIcon(Constants.ICON_HOUND));

    closeButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        close();
      }
    });

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
    mainPanel.add(Box.createRigidArea(new Dimension(20, 0)));
    mainPanel.add(panel);
    mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    mainPanel.add(closeButton);
    mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

    JScrollPane sp = new JScrollPane(mainPanel);
    this.setContentPane(sp);

    desktopPane.add(this, layer);
  }

  public void load(Configurator configurator) {
    setBounds(configurator.getBoundsProperty("ClassHoundFrame", "20;20;500;400"));
  }

  public void save(Configurator configurator) {
    configurator.setBoundsProperty("classHoundFrame", getBounds());
  }

  public void open() {
    super.open();

    panel.startThread();
  }

}

