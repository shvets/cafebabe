// ClassFrame.java

package org.sf.cafebabe.task.classfile;

import java.awt.*;
import java.io.DataInputStream;
import java.io.File;

import javax.swing.*;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import org.sf.cafebabe.Constants;
import org.sf.cafebabe.CustomFrame;
import org.sf.cafebabe.gadget.classtree.ClassTree;
import org.sf.cafebabe.util.Configurator;
import org.sf.classfile.ClassFile;

/**
 * This class represents the frame for dispaying the content of ClassFile
 *
 * @version 1.0 02/04/2002
 * @author Alexander Shvets
 */
public class ClassFrame extends CustomFrame implements Constants {
  /** The visual structure for representing ClassFile content
      as complex tree */
  private ClassTree classTree;

  /**
   * Gets the class tree object
   *
   * @return the class tree object
   */
  public ClassTree getClassTree() {
    return classTree;
  }

  /**
   * Loads properties of internal frame
   *
   * @param configurator  the helper class for working with properties
   */
  public void load(Configurator configurator) {
    //name = configurator.getProperty("lastClassFileName");

    setBounds(configurator.getBoundsProperty("classFrame", "20;20;500;400"));
  }

  /**
   * Saves properties of internal frame
   *
   * @param configurator  the helper class for working with properties
   */
  public void save(Configurator configurator) {
/*    if(name != null) {
      configurator.setProperty("lastClassFileName", name);
    }
*/
    configurator.setBoundsProperty("classFrame", getBounds());
  }

  /**
   * Checks whether this window could be closed. In case of
   * any changes the confirmation dialog will arise.
   *
   * @return thue id this window could be closed; false otherwise
   */
  public boolean canExit() {
    if(classTree == null) {
      return true;
    }

    if(classTree.isAnyChange()) {
      int value = JOptionPane.showConfirmDialog(this,
                       Constants.SAVE_FILE_QUESTION + " " +
                       dataSource.getName() + "?",
                       Constants.CONFIRMATION_STRING,
                       JOptionPane.YES_NO_CANCEL_OPTION);

      if(value == JOptionPane.CANCEL_OPTION) {
        return false;
      }

      if(value == JOptionPane.YES_OPTION) {
        try {
          saveAs(dataSource);
        }
        catch(Exception e) {
          return false;
        }
      }
    }

    return true;
  }

  /**
   * Opens class file in internal window
   */
  public void open() {
    super.open();

    try {
      ClassFile classFile = new ClassFile();
      classFile.read(new DataInputStream(dataSource.getInputStream()));

      ClassTreeActions actions = new ClassTreeActions(getClassTree());

      JPanel topPanel = new JPanel();
      topPanel.setLayout(new BorderLayout());
      this.getContentPane().add(topPanel);

      topPanel.add(actions.getToolBar(), BorderLayout.NORTH);

      classTree = new ClassTree(getDesktopPane(), new Integer(getLayer()));

      classTree.setClassFile(classFile);

      // Create a new class tree
      JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                               JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
      topPanel.add(scrollPane, BorderLayout.CENTER);

      scrollPane.getViewport().add(classTree);

      topPanel.add(classTree.getHexEditor(), BorderLayout.SOUTH);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Saves the content of the editor as external file
   *
   * @param dataSource the data ?ource
   */
  public void saveAs(DataSource dataSource) throws Exception {
    File file = null;

    if(dataSource instanceof FileDataSource) {
      FileDataSource fileDataSource = (FileDataSource)dataSource;

      file = new File(fileDataSource.getFile().getPath());
    }
    else {
      file = new File(dataSource.getName());
    }

    classTree.saveAsClassFile(file);

    setTitle(dataSource.getName());
  }

  /**
   * Looks for desired position in ClassFile tree and
   * positions on it
   *
   * @param selMethod selected method name
   */
  /*public void setMethodPosition(String selMethod) {
    if(classTree.search(selMethod, SEARCH_METHOD, false, true)) {
      classTree.expandMethodFolder();
    }
  }
 */

  /**
   * Looks for desired position in ClassFile tree and
   * positions on it
   *
   * @param selField selected field name
   */
/*  public void setFieldPosition(String selField) {
    boolean b = classTree.search(selField, SEARCH_FIELD, false, true);

    if(b) {
      classTree.expandFieldFolder();
    }
  }
*/

  /**
   * Checks specified class file for integrity
   */
 /* public void integrityTest() {
    Integrity integrity = new Integrity(classTree.getClassFile());

    final StringBuffer results = new StringBuffer();

    integrity.check();

    if(results.length() > 0) {
      JOptionPane.showMessageDialog(ClassFrame.this,
                   results.toString(), "Integrity Test",
                   JOptionPane.INFORMATION_MESSAGE);
    }
    else {
      JOptionPane.showMessageDialog(ClassFrame.this,
                   "Test completed successfully.", "Integrity Test",
                   JOptionPane.INFORMATION_MESSAGE);
    }
  }
    */
}
