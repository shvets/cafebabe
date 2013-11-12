// SerFrame.java

package org.sf.cafebabe.task.serfile;

import java.awt.*;
import java.io.File;

import javax.swing.*;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import org.sf.cafebabe.CustomFrame;
import org.sf.cafebabe.util.Configurator;

public class SerFrame extends CustomFrame {
  private SerTree serTree;
  private String serFileName;

  public SerFrame() {
    serTree = new SerTree();
    PlainActions actions = new PlainActions(this);

    JPanel topPanel = new JPanel();
    topPanel.setLayout(new BorderLayout());
    this.getContentPane().add(topPanel);

    topPanel.add(actions.getToolBar(), BorderLayout.NORTH);

    JScrollPane scrollPane = new JScrollPane(serTree);
    topPanel.add(scrollPane, BorderLayout.CENTER);
  }

  public void load(Configurator configurator) {
    setBounds(configurator.getBoundsProperty("SerFrame", "20;20;500;400"));
  }

  public void save(Configurator configurator) {
    configurator.setBoundsProperty("SerFrame", getBounds());
  }

  public boolean canExit() {
    if(serTree.isAnyChange()) {
      int value = JOptionPane.showConfirmDialog(this,
                      "Do you want to save file " + serFileName + "?",
                       "Confirmation",
                       JOptionPane.YES_NO_CANCEL_OPTION);

      if(value == JOptionPane.CANCEL_OPTION) return false;

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

  public void setTutorialMode(boolean mode) {
    serTree.setTutorialMode(mode);
  }

  public void invoke(final Runnable runnable) {
    Thread thread = new Thread() {
      public void run() {
//        Component glassPane = ((JFrame)SerFrame.this.getTopLevelAncestor()).getGlassPane();

//        glassPane.setVisible(true);
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        runnable.run();
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
//        glassPane.setVisible(false);
      }
    };

    thread.setPriority(Thread.MIN_PRIORITY+2);
    thread.start();
  }

  public void collapse() {
    invoke(new Runnable() {
      public void run() {
        serTree.collapse();
      }
    });
  }

  public void expandFirstLevel() {
    invoke(new Runnable() {
      public void run() {
        serTree.expandFirstLevel();
      }
    });
  }

  public void expandAllLevels() {
    invoke(new Runnable() {
      public void run() {
        serTree.expandAllLevels();
      }
    });
  }

  public void search() {
    Component current = this;
    Component parent  = null;
    while(true) {
      parent = current.getParent();
      if(parent == null) break;
      if(parent instanceof JFrame) break;
      current = parent;
    }

    if(parent != null)
      serTree.search((JFrame)parent);
  }

  /**
   * Opens class file in internal window
   */
  public void open() {
    super.open();

    setTitle(dataSource.getName());

    File file = null;

    if(dataSource instanceof FileDataSource) {
      FileDataSource fileDataSource = (FileDataSource)dataSource;

      file = new File(fileDataSource.getFile().getPath());
    }
    else {
      file = new File(dataSource.getName());
    }

    serTree.setFile(file);
  }

  public void close() {
    serTree.clear();
    serFileName = null;
  }

  /**
   * Saves the content of the editor as external file
   *
   * @param dataSource the data source
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

    setTitle(dataSource.getName());

    serTree.saveAs(file);

    serFileName = dataSource.getName();
  }

}
