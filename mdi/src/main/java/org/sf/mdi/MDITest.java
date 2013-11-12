package org.sf.mdi;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.BoxLayout;
import javax.swing.Box;

/**
 * @author Alexander Shvets
 * @version 1.0
 */
public class MDITest extends JFrame {
  private MDIDesktopPane desktopPane = new MDIDesktopPane();

  private MDIMenuBar menuBar = new MDIMenuBar(desktopPane);

  private JMenu fileMenu = new JMenu("File");

  private JMenuItem newMenu = new JMenuItem("New");

  private JScrollPane scrollPane = new JScrollPane();

  private int windowNumber = 0;

  public MDITest() {
    setTitle("MDI Test");

    menuBar.add(fileMenu);
    menuBar.add(menuBar.getWindowMenu());
    fileMenu.add(newMenu);
    setJMenuBar(menuBar);

    scrollPane.getViewport().add(desktopPane);

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(scrollPane, BorderLayout.CENTER);

    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });

    newMenu.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        TestMDIFrame frame = new TestMDIFrame("Window " + (windowNumber++));
        desktopPane.add(frame/*, PROGRAM_LAYER*/);

        frame.open();
      }
    });
  }

  InternalFrameListener internalFrameListener = new InternalFrameAdapter() {

    public void internalFrameActivated(InternalFrameEvent e) {
      System.out.println("Activated");
    }

    public void internalFrameDeactivated(InternalFrameEvent e) {
      System.out.println("Deactivated");
    }

    public void internalFrameClosing(InternalFrameEvent e) {
      System.out.println("Closing");
    }

    public void internalFrameOpened(InternalFrameEvent e) {
      System.out.println("Opened");
    }

    public void internalFrameClosed(InternalFrameEvent e) {
      System.out.println("Closed");
    }

    public void internalFrameIconified(InternalFrameEvent e) {
      System.out.println("Iconified");
    }

    public void internalFrameDeiconified(InternalFrameEvent e) {
      System.out.println("Deiconified");
    }
  };

class TestMDIFrame extends MDIFrame {
  TestMDIFrame(String title) {
    super(title);

    setSize(200,300);

    JPanel topPanel = new JPanel();
    topPanel.setLayout(new BorderLayout());
    this.getContentPane().add(topPanel);

    JButton okButton  = new JButton("Ok");

    okButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        close();
      }
    });

    JPanel panel1 = new JPanel();
    panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));

    panel1.add(Box.createRigidArea(new Dimension(0, 70)));
    panel1.add(okButton);
    panel1.add(Box.createRigidArea(new Dimension(0, 70)));

    topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
    topPanel.add(Box.createRigidArea(new Dimension(250, 0)));
    topPanel.add(panel1);
    topPanel.add(Box.createRigidArea(new Dimension(250, 0)));

    addInternalFrameListener(internalFrameListener);
  }

}

  public static void main(String[] args) {
    MDITest test = new MDITest();

    test.setSize(600,400);

    test.setVisible(true);
  }

}