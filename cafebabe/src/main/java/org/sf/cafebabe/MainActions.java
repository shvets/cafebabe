// MainActions.java

package org.sf.cafebabe;

import java.awt.event.ActionEvent;
import java.awt.Component;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.ButtonGroup;
import javax.swing.JToolBar;
import javax.swing.ImageIcon;
import javax.swing.AbstractAction;
import javax.swing.UIManager;
import javax.swing.LookAndFeel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JPanel;
import javax.activation.FileDataSource;

import org.sf.cafebabe.util.Actions;
import org.sf.cafebabe.util.Configurator;
import org.sf.cafebabe.util.IconProducer;
import org.sf.cafebabe.theme.AquaMetalTheme;
import org.sf.cafebabe.theme.DemoMetalTheme;
import org.sf.cafebabe.theme.CharcoalMetalTheme;
import org.sf.cafebabe.theme.ContrastMetalTheme;
import org.sf.cafebabe.theme.BigContrastMetalTheme;
import org.sf.cafebabe.theme.EmeraldMetalTheme;
import org.sf.cafebabe.theme.GreenMetalTheme;
import org.sf.cafebabe.theme.KhakiMetalTheme;
import org.sf.cafebabe.theme.RubyMetalTheme;
import org.sf.cafebabe.theme.PropertiesMetalTheme;
import org.sf.mdi.MDIMenuBar;

/**
 * This class encapsulates all actions for CafeBabe application.
 * All actions can be shared among appication menu and application toolbar.
 */
public class MainActions extends Actions implements Constants {
  private Action newAction, openAction, saveAsAction, closeAction,
                 changeThemeAction, currPlafAction, exitAction;
  private Action houndAction;
  private Action mailAction, aboutAction;

  private JMenu plafMenu, themeMenu, taskMenu;

  private ButtonGroup themesMenuGroup = new ButtonGroup();

  private MDIMenuBar menuBar;
  private JToolBar toolBar;

  private MainFrame parent;

  public MainActions(MainFrame parent) {
    this.parent = parent;

    menuBar = parent.getMDIMenuBar();
    toolBar = parent.getJToolBar();

    init();
  }

  protected void init() {
    JMenu windowMenu = menuBar.getWindowMenu();
    windowMenu.setText(WINDOW_MENU_TEXT);

    JMenu fileMenu = addMenu(menuBar, FILE_MENU_TEXT, FILE_MENU_SHORTCUT);
    taskMenu = addMenu(menuBar, TASK_MENU_TEXT, TASK_MENU_SHORTCUT);

    addMenu(menuBar, windowMenu, WINDOW_MENU_SHORTCUT);
    JMenu helpMenu = addMenu(menuBar, HELP_MENU_TEXT, HELP_MENU_SHORTCUT);

    newAction    = createNewAction(NEW_ITEM_TEXT, null);
    openAction   = createOpenAction(OPEN_ITEM_TEXT,
                         IconProducer.getImageIcon(ICON_OPEN_FILE));
    saveAsAction = createSaveAsAction(SAVE_AS_ITEM_TEXT,
                         IconProducer.getImageIcon(ICON_SAVE_FILE));
    closeAction  = createCloseAction(CLOSE_ITEM_TEXT, null);
    changeThemeAction = createChangeThemeAction();
    exitAction   = createExitAction(EXIT_ITEM_TEXT, null);

    houndAction = createHoundAction(CLASS_HOUND_ITEM_TEXT,
                    IconProducer.getImageIcon(ICON_CLASS_HOUND));

    mailAction = createMailAction(EMAIL_ITEM_TEXT,
                      IconProducer.getImageIcon(ICON_MAIL));
    aboutAction = createAboutAction(ABOUT_ITEM_TEXT,
                        IconProducer.getImageIcon(ICON_ABOUT));

    // fill File menu
    addActionToMenu(newAction, fileMenu, true, NEW_ITEM_DESCR_TEXT, NEW_ITEM_SHORTCUT);

    addActionToMenu(openAction, fileMenu, true, OPEN_ITEM_DESCR_TEXT, OPEN_ITEM_SHORTCUT);
    addActionToToolBar(openAction, toolBar, false, OPEN_ITEM_DESCR_TEXT, 0);

    addActionToMenu(saveAsAction, fileMenu, true, SAVE_AS_ITEM_DESCR_TEXT, SAVE_AS_ITEM_SHORTCUT);
    addActionToToolBar(saveAsAction, toolBar, false, SAVE_AS_ITEM_DESCR_TEXT, 0);

    addActionToMenu(closeAction, fileMenu, false, CLOSE_ITEM_DESCR_TEXT, CLOSE_ITEM_SHORTCUT);

    fileMenu.addSeparator();
    plafMenu = addMenu(fileMenu, CHANGE_PLAF_ITEM_TEXT, CHANGE_PLAF_ITEM_SHORTCUT);
    themeMenu = addMenu(fileMenu, CHANGE_THEME_ITEM_TEXT, CHANGE_THEME_ITEM_SHORTCUT);

    fillPlafMenu(plafMenu);
    fillThemeMenu(themeMenu);
    fileMenu.addSeparator();

    addActionToMenu(exitAction, fileMenu, false, EXIT_ITEM_DESCR_TEXT, EXIT_ITEM_SHORTCUT);

    houndAction = createHoundAction(CLASS_HOUND_ITEM_TEXT,
                        IconProducer.getImageIcon(ICON_CLASS_HOUND));

    // fill Task menu
    addActionToMenu(houndAction, taskMenu, true, CLASS_HOUND_ITEM_DESCR_TEXT,
                                                 CLASS_HOUND_ITEM_SHORTCUT);
    addActionToToolBar(houndAction, toolBar, false, CLASS_HOUND_ITEM_DESCR_TEXT, 0);

    // fill Help menu
    JPanel emptyArea = new JPanel();
    toolBar.add(emptyArea);

    addActionToMenu(mailAction, helpMenu, true, EMAIL_ITEM_DESCR_TEXT, EMAIL_ITEM_SHORTCUT);

    addActionToMenu(aboutAction, helpMenu, true, ABOUT_ITEM_DESCR_TEXT, ABOUT_ITEM_SHORTCUT);
    addActionToToolBar(aboutAction, toolBar, false, ABOUT_ITEM_DESCR_TEXT, 0);
  }

  public void disable() {
    saveAsAction.setEnabled(false);
    closeAction.setEnabled(false);
  }

  public void enable() {
    saveAsAction.setEnabled(true);
    closeAction.setEnabled(true);
  }

  private Action createNewAction(String text, ImageIcon icon) {
    return new AbstractAction(text, icon) {
      { this.setEnabled(false); }
      public void actionPerformed(ActionEvent e) {
        parent.getFrameManager().newFrame();
      }
    };
  }

  private Action createOpenAction(String text, ImageIcon icon) {
    return new AbstractAction(text, icon) {
      public void actionPerformed(ActionEvent e) {
        parent.open();
      }
    };
  }

  private Action createSaveAsAction(String text, ImageIcon icon) {
    return new AbstractAction(text, icon) {
      { this.setEnabled(false); }
      public void actionPerformed(ActionEvent e) {
        parent.getFrameManager().saveAs(new FileDataSource("new file.txt"));
      }
    };
  }

  private Action createCloseAction(String text, ImageIcon icon) {
    return new AbstractAction(text, icon) {
      { this.setEnabled(false); }
      public void actionPerformed(ActionEvent e) {
        parent.getFrameManager().close();
      }
    };
  }

  private Action createExitAction(String text, ImageIcon icon) {
    return new AbstractAction(text, icon) {
      public void actionPerformed(ActionEvent e) {
        parent.exit();
      }
    };
  }

  private void fillPlafMenu(JMenu plafMenu) {
    UIManager.LookAndFeelInfo plaf[] = UIManager.getInstalledLookAndFeels();
    LookAndFeel currPlaf = UIManager.getLookAndFeel();
    String currPlafName  = currPlaf.getName();

    for(int i=0; i < plaf.length; i++) {
      String plafName  = plaf[i].getName();
      String className = plaf[i].getClassName();
      Action plafAction = createPlafAction(plafName, className, null);

      JMenuItem item = addActionToMenu(plafAction, plafMenu, true, className, 0);
      item.setActionCommand(className);

      if(plafName.equals(currPlafName)) {
        currPlafAction = plafAction;
        currPlafAction.setEnabled(false);
      }
    }

    if(!currPlafName.equals("Metal")) {
      themeMenu.setEnabled(false);
    }
  }

  public Action createPlafAction(String text, final String plafName,
                                 ImageIcon icon) {
    return new AbstractAction(text, icon) {
      public void actionPerformed(ActionEvent evt) {
        try {
          UIManager.setLookAndFeel(plafName);

          parent.changePlaf();

          currPlafAction.setEnabled(true);
          currPlafAction = this;
          currPlafAction.setEnabled(false);

          if(!plafName.endsWith("MetalLookAndFeel")) {
            themeMenu.setEnabled(false);
          }
          else {
            themeMenu.setEnabled(true);
          }
        }
        catch(Exception e) {
          e.printStackTrace();
          JOptionPane.showMessageDialog(parent,
                 "Look And Feel " + plafName + " doesn't supported.",
                 "Warning", JOptionPane.WARNING_MESSAGE);
        }
      }
    };
  }

  private void fillThemeMenu(JMenu themeMenu) {
    JMenuItem mi = createThemesMenuItem(themeMenu,
                             "The Default blue/purple Metal Theme", new DefaultMetalTheme());
    mi.setSelected(true); // This is the default theme

    createThemesMenuItem(themeMenu,
                         "A Metal Theme that uses bluish-green colors", new AquaMetalTheme());

    createThemesMenuItem(themeMenu,
                         "", new DemoMetalTheme());

    createThemesMenuItem(themeMenu,
                         "A Metal Theme that uses dark grey colors", new CharcoalMetalTheme());

    createThemesMenuItem(themeMenu,
                         "A High Contrast Theme", new ContrastMetalTheme());

    createThemesMenuItem(themeMenu,
                         "A Big Contrast Theme", new BigContrastMetalTheme());

    createThemesMenuItem(themeMenu,
                         "A Metal Theme that uses green colors", new EmeraldMetalTheme());

    createThemesMenuItem(themeMenu,
                         "A Metal Theme that uses green colors", new GreenMetalTheme());

    createThemesMenuItem(themeMenu,
                         "A Metal Theme that uses khaki colors", new KhakiMetalTheme());

    createThemesMenuItem(themeMenu,
                         "A Metal Theme that uses red colors", new RubyMetalTheme());

    try {
      InputStream is = new FileInputStream("MyTheme.theme");
      createThemesMenuItem(themeMenu,
                           "A Metal Theme that uses colors from properties file",
                           new PropertiesMetalTheme(is));
    }
    catch(IOException e) {}

  }

  /**
  * Creates a JRadioButtonMenuItem for the Themes menu
  */
  public JMenuItem createThemesMenuItem(JMenu menu, String description, MetalTheme theme) {
    JRadioButtonMenuItem mi = (JRadioButtonMenuItem) menu.add(new JRadioButtonMenuItem(theme.getName()));
    mi.setActionCommand(theme.getName());
    mi.putClientProperty("theme", theme);
    themesMenuGroup.add(mi);

    mi.getAccessibleContext().setAccessibleDescription(description);
    mi.addActionListener(changeThemeAction);

    return mi;
  }

  protected Action createChangeThemeAction() {
    return new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        JMenuItem menuItem = (JMenuItem)e.getSource();
        MetalTheme theme = (MetalTheme)menuItem.getClientProperty("theme");

        MetalLookAndFeel.setCurrentTheme(theme);

        try {
          UIManager.setLookAndFeel(UIManager.getLookAndFeel());
        }
        catch (Exception ex) {}

        parent.changePlaf();
      }
    };
  }

  protected Action createHoundAction(String text, ImageIcon icon) {
    return new AbstractAction(text, icon) {
      public void actionPerformed(ActionEvent e) {
        parent.hound();
      }
    };
  }

  public Action createMailAction(String text, ImageIcon icon) {
    return new AbstractAction(text, icon) {
      public void actionPerformed(ActionEvent e) {
        parent.mail();
      }
    };
  }

  public Action createAboutAction(String text, ImageIcon icon) {
    return new AbstractAction(text, icon) {
      public void actionPerformed(ActionEvent e) {
        parent.about();
      }
    };
  }

  public void load(Configurator configurator) {
    String themeName = configurator.getProperty("current.theme");

    if(themeName == null) {
      return;
    }

    Component[] themes = themeMenu.getMenuComponents();
    for(int i=0; i < themes.length; i++) {
      JMenuItem menuItem = (JMenuItem)themes[i];
      String name = menuItem.getActionCommand();

      if(themeName.equals(name)) {
        menuItem.setSelected(true);
        MetalTheme theme = (MetalTheme)menuItem.getClientProperty("theme");
        if(theme != null) {
          MetalLookAndFeel.setCurrentTheme(theme);
        }

        changeThemeAction.actionPerformed(new ActionEvent(menuItem, ActionEvent.ACTION_PERFORMED, ""));
        break;
      }
    }

    try {
      String plafName = configurator.getProperty("current.plaf",
                                                 "javax.swing.plaf.metal.MetalLookAndFeel");
      Component[] plafs = plafMenu.getMenuComponents();
      for(int i=0; i < plafs.length; i++) {
        JMenuItem item = (JMenuItem)plafs[i];
        String name = item.getActionCommand();
        if(plafName.equals(name)) {
          item.setSelected(true);

          currPlafAction.actionPerformed(new ActionEvent(item, ActionEvent.ACTION_PERFORMED, ""));
          break;
        }
      }
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public void save(Configurator configurator) {
    LookAndFeel currPlaf = UIManager.getLookAndFeel();

    configurator.setProperty("current.plaf", currPlaf.getClass().getName());

    configurator.setProperty("current.theme", themesMenuGroup.getSelection().getActionCommand());
  }

}
