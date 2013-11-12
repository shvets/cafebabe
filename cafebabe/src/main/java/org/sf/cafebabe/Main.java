// Main.java

package org.sf.cafebabe;

import java.io.File;
import java.io.IOException;

import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import org.sf.cafebabe.util.IconProducer;

/**
 * Main class for CafeBabe application
 *
 * @version 1.0 01/30/2002
 * @author Alexander Shvets
 */
public final class Main {
  private static String installDir;

  static {
    IconProducer.setClass(Main.class);

    try {
      installDir = System.getProperty("install.dir");

      if(installDir == null) {
        installDir = System.getProperty("user.home");
      }

      if(installDir == null) {
        installDir = new File(".").getCanonicalPath();
      }

      if(installDir.endsWith("/")) {
        installDir = installDir.substring(0, installDir.length()-1);
      }
    }
    catch(IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Creates new CafeBabe application
   */
  public Main(final String args[]) {
    final MainFrame mainFrame = new MainFrame();

    mainFrame.setVisible(true);

    if(args.length > 0) {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          mainFrame.open(new File(args[0]));
        }
      });
    }
  }

  public static String getInstallDir() {
    return installDir;
  }

  public static String getHelpArchiveName() {
    return getInstallDir() + "/Help/" + Constants.HELP_ARCHIVE_NAME;
  }

  public static void main(String[] args) throws Exception {
    UIManager.installLookAndFeel("Macintosh", "com.sun.java.swing.plaf.mac.MacLookAndFeel");

    new Main(args);
  }

}
