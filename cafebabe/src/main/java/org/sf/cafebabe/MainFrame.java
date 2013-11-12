// MainFrame.java

package org.sf.cafebabe;

import org.sf.cafebabe.task.classfile.ClassFrame;
import org.sf.cafebabe.task.classhound.CafeBabeParent;
import org.sf.cafebabe.util.Configurator;
import org.sf.cafebabe.util.IconProducer;
import org.sf.mdi.MDIDesktopPane;
import org.sf.mdi.MDIFrame;
import org.sf.mdi.MDIMenuBar;

import javax.activation.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.*;

/**
 * Main frame for CafeBabe application
 */
public class MainFrame extends JFrame implements CafeBabeParent {
  private InternalFrameManager frameManager;
  private MainActions actions;

  private Configurator configurator;

  private MainChooser fileChooser = new MainChooser();

  protected MDIDesktopPane desktopPane = new MDIDesktopPane();
  protected MDIMenuBar menuBar = new MDIMenuBar(desktopPane);
  protected JToolBar toolBar = new JToolBar();

  public MainFrame() {
    this.setDefaultCloseOperation(/*JFrame.DISPOSE_ON_CLOSE*/JFrame.EXIT_ON_CLOSE);

    actions = new MainActions(this);

    frameManager = new InternalFrameManager(this);

    this.setJMenuBar(menuBar);

    JPanel topPanel = new JPanel();
    topPanel.setLayout(new BorderLayout());

    topPanel.add(toolBar, BorderLayout.NORTH);
    topPanel.add(desktopPane, BorderLayout.CENTER);

    this.setContentPane(topPanel);

    Component glassPane = getGlassPane();

    glassPane.setCursor(new Cursor(Cursor.WAIT_CURSOR));

    glassPane.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
      }
    });

    setTitle(Constants.MAIN_FRAME_TITLE);

    ImageIcon icon = IconProducer.getImageIcon(Constants.ICON_MAIN_FRAME);

    if (icon != null) {
      this.setIconImage(icon.getImage());
    }

    try {
      InputStream is1 = getClass().getClassLoader().getResourceAsStream(Constants.MIMETYPES_FILE_NAME);

      MimetypesFileTypeMap fileTypesMap = new MimetypesFileTypeMap(is1);

      FileTypeMap.setDefaultFileTypeMap(fileTypesMap);

      MailcapCommandMap cmdMap = (MailcapCommandMap) CommandMap.getDefaultCommandMap();

      InputStream is2 = getClass().getClassLoader().getResourceAsStream(Constants.MAILCAP_FILE_NAME);

      BufferedReader reader = new BufferedReader(new InputStreamReader(is2));

      while (true) {
        String line = reader.readLine();
        if (line == null)
          break;

        cmdMap.addMailcap(line + "\n");
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(this, e.toString(), "Warning", JOptionPane.WARNING_MESSAGE);
    }

    load();
  }

  public void load() {
    String propFileName = Main.getInstallDir() + File.separator + Constants.PROPERTIES_NAME;

    File f = new File(propFileName);

    if (!f.exists()) {
      try {
        FileOutputStream out = new FileOutputStream(f);
        out.close();
      }
      catch (IOException e) {
      }
    }

    configurator = new Configurator(propFileName);

    try {
      configurator.load();
    }
    catch (IOException e) {
      System.out.println(e);
    }

    Rectangle bounds = configurator.getBoundsProperty("MainFrame", "50;50;600;500");
    this.setBounds(bounds);

    actions.load(configurator);

    String name2 = configurator.getProperty("MainChooser");

    if (name2 != null && new File(name2).exists()) {
      fileChooser.setSelectedFile(new File(name2));
    }

    int index = configurator.getIntProperty("chooser.filter.index", 0);

    javax.swing.filechooser.FileFilter[] filters = fileChooser.getChoosableFileFilters();

    if (index < filters.length) {
      fileChooser.setFileFilter(filters[index]);
    }
  }

  public void save() {
    try {
      configurator.load();
    }
    catch (IOException e) {
      System.out.println(e);
    }

    configurator.setBoundsProperty("MainFrame", this.getBounds());

    actions.save(configurator);

    if (fileChooser.getSelectedFile() != null) {
      configurator.setProperty("MainChooser", fileChooser.getSelectedFile().toString());
    }

    javax.swing.filechooser.FileFilter currentFilter = fileChooser.getFileFilter();
    javax.swing.filechooser.FileFilter[] filters = fileChooser.getChoosableFileFilters();

    int index = 0;

    for (int i = 0; i < filters.length; i++) {
      if (filters[i] == currentFilter) {
        index = i;
        break;
      }
    }

    configurator.setProperty("chooser.filter.index", String.valueOf(index));

    try {
      configurator.save();
    }
    catch (IOException e) {
      System.out.println(e);
    }

    System.out.println(Constants.GOODBYE_MESSAGE);
  }


  public void changePlaf() {
    SwingUtilities.updateComponentTreeUI(this);

    SwingUtilities.updateComponentTreeUI(desktopPane);

    SwingUtilities.updateComponentTreeUI(fileChooser);
    SwingUtilities.updateComponentTreeUI(menuBar);
    SwingUtilities.updateComponentTreeUI(toolBar);

    JInternalFrame[] frames = desktopPane.getAllFrames();

    for (int i = 0; i < frames.length; i++) {
      JInternalFrame frame = frames[i];

      SwingUtilities.updateComponentTreeUI(frame);
    }
  }

  public void exit() {
    WindowEvent evt = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);

    processWindowEvent(evt);
  }

  public boolean canExit() {
    JInternalFrame[] frames = desktopPane.getAllFrames();

    boolean canExit = true;

    for (int i = 0; i < frames.length; i++) {
      MDIFrame frame = (MDIFrame) frames[i];
      if (!frame.canExit()) {
        canExit = false;
        break;
      }
    }

    return canExit;
  }

  protected void processWindowEvent(WindowEvent e) {
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      if (canExit()) {
        save();
        super.processWindowEvent(e);
      }
    } else {
      super.processWindowEvent(e);
    }
  }

  public void mail() {
    frameManager.openFrame("mail");
  }

  public void about() {
    frameManager.openFrame("about");
  }

  public void hound() {
    frameManager.openFrame("classhound");
  }

  public void open() {
    open(fileChooser.open("Open file"));
  }

  public void open(File file) {
    try {
      frameManager.openFrame(file);
    }
    catch (IOException e) {
      ;
    }
  }

  public void open(DataSource dataSource) {
    CustomFrame frame = getFrameManager().getFrame(dataSource);

    if (frame != null) {
      frame.open();
    }
  }

  public void setFieldPosition(String selectedField) {
    JInternalFrame frame = getFrameManager().getCurrentFrame();

    if(frame instanceof ClassFrame) {
      ClassFrame classFrame = (ClassFrame)frame;

      if(selectedField != null) {
        classFrame.getClassTree().setFieldPosition(selectedField);
      }
    }
  }

  public void setMethodPosition(String selectedMethod) {
    JInternalFrame frame = getFrameManager().getCurrentFrame();

    if(frame instanceof ClassFrame) {
      ClassFrame classFrame = (ClassFrame)frame;

      if(selectedMethod != null) {
        classFrame.getClassTree().setMethodPosition(selectedMethod);
      }
    }
  }

  public InternalFrameManager getFrameManager() {
    return frameManager;
  }

  public MainChooser getFileChooser() {
    return fileChooser;
  }

  public MDIDesktopPane getMDIDesktopPane() {
    return desktopPane;
  }

  public JToolBar getJToolBar() {
    return toolBar;
  }

  public MDIMenuBar getMDIMenuBar() {
    return menuBar;
  }

  public MainActions getActions() {
    return actions;
  }

}
