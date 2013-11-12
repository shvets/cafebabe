package org.sf.mdi;

import java.beans.PropertyVetoException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JInternalFrame;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/**
 * Menu component that handles the functionality expected of a standard
 * "Windows" menu for MDI applications.
 *
 * @author Alexander Shvets
 * @version 1.0
 */
public class WindowMenu extends JMenu {
  private JMenuItem cascade = new JMenuItem("Cascade");
  private JMenuItem tile    = new JMenuItem("Tile");

  private MDIDesktopPane desktop;

  public WindowMenu(MDIDesktopPane desktop) {
    this.desktop = desktop;

    setText("Window");

    cascade.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        WindowMenu.this.desktop.cascadeFrames();
      }
    });

    tile.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        WindowMenu.this.desktop.tileFrames();
      }
    });

    addMenuListener(new MenuListener() {
      public void menuCanceled (MenuEvent e) {}

      public void menuDeselected (MenuEvent e) {
        removeAll();
      }

      public void menuSelected (MenuEvent e) {
        buildChildMenus();
      }
    });
  }

  /* Sets up the children menus depending on the current desktop state */
  private void buildChildMenus() {
    int i;
    ChildMenuItem menu;
    JInternalFrame[] array = desktop.getAllFrames();

    add(cascade);
    add(tile);

    if(array.length > 0) {
      addSeparator();
    }

    cascade.setEnabled(array.length > 0);
    tile.setEnabled(array.length > 0);

    for(i = 0; i < array.length; i++) {
      menu = new ChildMenuItem(array[i]);
      menu.setState(i == 0);
      menu.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent ae) {
          JInternalFrame frame = ((ChildMenuItem)ae.getSource()).getFrame();
          frame.moveToFront();

          try {
            frame.setSelected(true);
          }
          catch (PropertyVetoException e) {
            e.printStackTrace();
          }
        }
      });

      menu.setIcon(array[i].getFrameIcon());
      add(menu);
    }
  }

  /* This JCheckBoxMenuItem descendant is used to track the child frame that corresponds
     to a give menu. */
  class ChildMenuItem extends JCheckBoxMenuItem {
    private JInternalFrame frame;

    public ChildMenuItem(JInternalFrame frame) {
      super(frame.getTitle());

      this.frame = frame;
    }

    public JInternalFrame getFrame() {
      return frame;
    }
  }

}