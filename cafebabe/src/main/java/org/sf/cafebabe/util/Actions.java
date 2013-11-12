// Actions.java

package org.sf.cafebabe.util;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.border.Border;
import javax.swing.border.BevelBorder;

public class Actions {

  // convenient method for adding submenu to parent menu and its adjacting
  public JMenu addMenu(JComponent parentMenu, String menuName, int mnemonic) {
    JMenu menu = new JMenu(menuName);

    return addMenu(parentMenu, menu, mnemonic);
  }

  public JMenu addMenu(JComponent parentMenu, JMenu menu, int mnemonic) {
    if(mnemonic > 0)
      menu.setMnemonic(mnemonic);

    menu.addMouseListener(new NiceListener(menu, parentMenu.getBackground()));
    parentMenu.add(menu);

    return menu;
  }

  // method that adds action to tollbar
  public void addActionToToolBar(Action action, JToolBar toolBar,
                      boolean buttonText, String toolTip, int mnemonic) {
    AbstractButton button = null;

    if(action instanceof CustomAction) {
      button = ((CustomAction)action).getButton();
      toolBar.add(button);
    }
    else {
      button = toolBar.add(action);
    }

    if(!buttonText) {
      button.setText("");
    }

    if(toolTip != null) {
      button.setToolTipText(toolTip);
    }

    if(mnemonic > 0) {
      button.setMnemonic(mnemonic);
    }

//    button.addMouseListener(new NiceListener(button, toolBar.getBackground()));
  }

  // method that adds action to submenu
  public JMenuItem addActionToMenu(Action action, JMenu menu,
                      boolean itemIcon, String toolTip, int mnemonic) {
    JMenuItem menuItem = menu.add(action);
    if(!itemIcon)
      menuItem.setIcon(null);
    if(toolTip != null)
      menuItem.setToolTipText(toolTip);
    if(mnemonic > 0)
      menuItem.setMnemonic(mnemonic);

    return menuItem;
  }


// inner class-adapter that realize mouse-sensitive interface
// for menu items and tollbar's elements
private class NiceListener extends MouseAdapter {

  Border borderEntered, borderExited;

  AbstractButton button;

  NiceListener(AbstractButton button, Color c) {
    this.button = button;

    borderEntered = BorderFactory.createRaisedBevelBorder();
    borderExited  = BorderFactory.createBevelBorder(BevelBorder.LOWERED, c, c, c, c);
    button.setBorder(borderExited);
  }

  public void mouseClicked(MouseEvent e) {
    if(button.isEnabled()) {
      button.setBorder(borderEntered);
    }
  }

  public void mousePressed(MouseEvent e) {
    if(button.isEnabled()) {
      button.setBorder(borderEntered);
    }
  }

  public void mouseReleased(MouseEvent e) {
    if(button.isEnabled()) {
      button.setBorder(borderExited);
    }
  }

  public void mouseEntered(MouseEvent e) {
    if(button.isEnabled()) {
      button.setBorder(borderEntered);
    }
  }

  public void mouseExited(MouseEvent e) {
    if(button.isEnabled()) {
      button.setBorder(borderExited);
    }
  }

}


/**
 * This class allows to use for Action object different types of buttons,
 * not only JButton objects
 */
public abstract class CustomAction extends AbstractAction {

  private AbstractButton button;

  public CustomAction(String text, ImageIcon icon, AbstractButton button) {
    super(text, icon);

    this.button = button;

    button.addActionListener(this);
    button.setIcon((Icon)this.getValue(Action.SMALL_ICON));
    button.setEnabled(this.isEnabled());
  }

  public void setEnabled(boolean newValue) {
    super.setEnabled(newValue);
    button.setEnabled(newValue);
  }

  public AbstractButton getButton() {
    return button;
  }

}

}
