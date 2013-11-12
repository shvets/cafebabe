package org.sf.cafebabe;

import java.io.IOException;
import java.io.File;
import java.awt.Component;
import java.awt.Container;
import java.awt.BorderLayout;
import java.beans.PropertyVetoException;
import java.util.zip.ZipFile;

import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.activation.DataSource;
import javax.activation.DataHandler;
import javax.activation.CommandInfo;
import javax.activation.FileDataSource;
import org.sf.mdi.MDIFrame;
import org.sf.cafebabe.task.classhound.ClassHoundFrame;
import org.sf.cafebabe.util.FileUtil;
import org.sf.cafebabe.activation.ArchivedDataSource;
import org.gjt.jclasslib.browser.config.classpath.ClasspathArchiveEntry;
import org.gjt.jclasslib.browser.config.classpath.ClasspathBrowser;

/**
 * Class that represents the manager for internal frames.
 *
 * @version 1.0 09/13/2003
 * @author Alexander Shvets
 */
public class InternalFrameManager implements InternalFrameListener {
  public final static Integer PROGRAM_LAYER = JLayeredPane.PALETTE_LAYER;

  private MDIFrame currentFrame;

  private MDIFrame aboutFrame, mailFrame;
  private ClassHoundFrame classHoundFrame;

  private MainFrame mainFrame;

  public InternalFrameManager(MainFrame mainFrame) {
    this.mainFrame = mainFrame;
  }
  public void internalFrameActivated(InternalFrameEvent e) {
    mainFrame.getActions().enable();
  }

  public void internalFrameDeactivated(InternalFrameEvent e) {
    mainFrame.getActions().disable();
  }

  public void internalFrameClosing(InternalFrameEvent e) {}

  public void internalFrameOpened(InternalFrameEvent e) {}

  public void internalFrameClosed(InternalFrameEvent e) {
    JInternalFrame frame = e.getInternalFrame();

    if(frame instanceof AboutFrame) {
      aboutFrame = null;
    }
    else if(frame instanceof MailFrame) {
      mailFrame = null;
    }
    else if(frame instanceof ClassHoundFrame) {
      classHoundFrame = null;
    }

    currentFrame = null;
  }

  public void internalFrameIconified(InternalFrameEvent e) {}

  public void internalFrameDeiconified(InternalFrameEvent e) {}

  public MDIFrame getCurrentFrame() {
    return currentFrame;
  }

  public void setCurrentFrame(MDIFrame currentFrame) {
    this.currentFrame = currentFrame;
  }

  public void openFrame(String name) {
    MDIFrame frame = null;

    if(name.equals("about")) {
      frame = getAboutFrame();
    }
    else if(name.equals("mail")) {
      frame = getMailFrame();
    }
    else if(name.equals("classhound")) {
      frame = getClassHoundFrame();
    }

    if(frame != null) {
      mainFrame.getGlassPane().setVisible(true);
  
      frame.open();

      mainFrame.getGlassPane().setVisible(false);
    }
  }

  private MDIFrame getAboutFrame() {
    if(aboutFrame == null) {
      aboutFrame = new AboutFrame(mainFrame.getMDIDesktopPane(), PROGRAM_LAYER);

      aboutFrame.addInternalFrameListener(this);
    }

    return aboutFrame;
  }

  private MDIFrame getMailFrame() {
    if(mailFrame == null) {
      mailFrame = new MailFrame(mainFrame.getMDIDesktopPane(), PROGRAM_LAYER);

      mailFrame.addInternalFrameListener(this);
    }

    return mailFrame;
  }

  private CustomFrame getClassHoundFrame() {
    if(classHoundFrame == null) {
      classHoundFrame = new ClassHoundFrame(mainFrame, mainFrame.getMDIDesktopPane(), PROGRAM_LAYER);

      classHoundFrame.addInternalFrameListener(this);
    }

    return classHoundFrame;
  }

  public void openFrame(File file) throws IOException {
    open(getDataSource(file));
  }

  private void open(DataSource dataSource) {
    CustomFrame frame = getFrame(dataSource);

    if(frame != null) {
      mainFrame.getGlassPane().setVisible(true);

      frame.open();

      mainFrame.getGlassPane().setVisible(false);
    }
  }

  private DataSource getDataSource(File file) throws IOException {
    DataSource dataSource = null;

    if(file != null && !file.isDirectory()) {
      String extension = FileUtil.getExtension(file).toLowerCase();

      if(extension != null) {
        if(extension.equals("zip") || extension.equals("jar")) {
          ClasspathArchiveEntry entry = new ClasspathArchiveEntry();

          entry.setFileName(file.getPath());
          ClasspathBrowser jarBrowser =
                  new ClasspathBrowser(mainFrame, null, "Classes in selected JAR file:", false);

          jarBrowser.clear();
          jarBrowser.setClasspathComponent(entry);
          jarBrowser.setVisible(true);

          String selectedClassName = jarBrowser.getSelectedClassName();

          if(selectedClassName != null) {
            dataSource = new ArchivedDataSource(new ZipFile(file.getPath()), selectedClassName + ".class");
          }
        }
        else {
          dataSource = new FileDataSource(file);
        }
      }
    }

    return dataSource;
  }

  public CustomFrame getFrame(DataSource dataSource) {
    CustomFrame frame = null;

    DataHandler dataHandler = new DataHandler(dataSource);

    CommandInfo ci = dataHandler.getCommand("view");

    if(ci == null) {
      JOptionPane.showMessageDialog(mainFrame,
            "Mime type \"" + dataHandler.getContentType() +
            "\" doesn't have any assosiation.",
            "Warning", JOptionPane.WARNING_MESSAGE);
    }
    else {
      try {
        String className = ci.getCommandClass();

        Object o = dataHandler.getBean(ci);

        if(o instanceof Component) {
          if(o instanceof CustomFrame) {
            frame = (CustomFrame)o;
          }
          else {
            frame = new CustomFrame();

            Container container = frame.getContentPane();

            container.setLayout(new BorderLayout());

            container.add((Component)o, BorderLayout.CENTER);
          }

          frame.addInternalFrameListener(this);
          frame.setDataSource(dataSource);
          frame.setTitle(dataSource.getName());

          mainFrame.getMDIDesktopPane().add(frame, PROGRAM_LAYER);
        }
        else if(o != null) {
          JOptionPane.showMessageDialog(mainFrame,
                "Component \"" + className + "\" should be derived from " +
                "\"Component\" class.",
                "Warning", JOptionPane.WARNING_MESSAGE);
        }
      }
      catch(Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(mainFrame,
                    e.toString() + ".", "Warning", JOptionPane.WARNING_MESSAGE);
      }
    }

    return frame;
  }

  public void newFrame() {}

  public void saveAs(DataSource dataSource) {
    MainChooser fileChooser = mainFrame.getFileChooser();

    if(currentFrame instanceof CustomFrame) {
      int returnVal = fileChooser.showDialog(mainFrame, "Save file as");

      if(returnVal != JFileChooser.APPROVE_OPTION) {
        return;
      }

      File file = fileChooser.getSelectedFile();

      if(file != null) {
        try {
          ((CustomFrame)currentFrame).saveAs(dataSource);
        }
        catch(Exception e) {
          ;
        }
      }
    }
  }

  public void close() {
    try {
      currentFrame.setClosed(true);
      currentFrame = null;

      mainFrame.getActions().disable();
    }
    catch(PropertyVetoException e) {}
  }

}
