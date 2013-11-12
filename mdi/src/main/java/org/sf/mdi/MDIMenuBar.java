// MDIMenuBar.java

package org.sf.mdi;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

/**
 * @author Alexander Shvets
 * @version 1.0
 */
public class MDIMenuBar extends JMenuBar {
  protected JMenu windowMenu;

  public MDIMenuBar(MDIDesktopPane desktopPane) {
    windowMenu = new WindowMenu(desktopPane);

    windowMenu.setText("Window");
  }

  public JMenuItem addWindowMenuItem(String windowName) {
    JMenuItem menuItem = null;

    int pos = findWindowMenuItem(windowName);

    if(pos == -1) {
      menuItem = new JMenuItem(windowName);
      windowMenu.add(menuItem);
    }
    else {
      menuItem = windowMenu.getItem(pos);
    }

    return menuItem;
  }

  public void removeWindowMenuItem(String windowName) {
    int pos = findWindowMenuItem(windowName);

    if(pos >= 0) {
      windowMenu.remove(pos);
    }
  }

  private int findWindowMenuItem(String windowName) {
    int cnt = windowMenu.getMenuComponentCount();

    for(int i=0; i < cnt; i++) {
      JMenuItem item = windowMenu.getItem(i);

      if(item != null) {
        String name = item.getActionCommand();

        if(name.equals(windowName)) {
          return i;
        }
      }
    }

    return -1;
  }

  public JMenu getWindowMenu() {
    return windowMenu;
  }

}
