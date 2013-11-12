// PlainActions.java

package org.sf.cafebabe.task.serfile;

import java.awt.event.ActionEvent;

import javax.swing.*;
import org.sf.cafebabe.Constants;
import org.sf.cafebabe.util.Actions;
import org.sf.cafebabe.util.IconProducer;

public class PlainActions extends Actions implements Constants {
  private final static String COLLAPSE_TREE_STRING     = "Collapse tree";
  private final static String EXPAND_1ST_LEVEL_STRING  = "Expand first level";
  private final static String EXPAND_ALL_LEVELS_STRING = "Expand all levels";
  private final static String SEARCH_STRING            = "Search string";
  private final static String TUTORIAL_MODE_STRING     = "Tutorial mode";

  private Action collapceAction, expandFirstLevelAction,
                 expandAllLevelsAction, searchAction, tutorialAction;

  protected JToolBar toolBar = new JToolBar();

  private SerFrame parent;

  public PlainActions(SerFrame parent) {
    this.parent = parent;

    init();
  }

  public JToolBar getToolBar() {
    return toolBar;
  }

  protected void init() {
    collapceAction         = createCollapceAction(COLLAPSE_TREE_STRING,
                               IconProducer.getImageIcon(ICON_COLLAPSE));
    expandFirstLevelAction = createExpandFirstLevelAction(EXPAND_1ST_LEVEL_STRING,
                               IconProducer.getImageIcon(ICON_EXPAND));
    expandAllLevelsAction  = createExpandAllLevelsAction(EXPAND_ALL_LEVELS_STRING,
                               IconProducer.getImageIcon(ICON_EXPAND_ALL));
    searchAction           = createSearchAction(SEARCH_STRING,
                               IconProducer.getImageIcon(ICON_SEARCH));
    AbstractButton tutoButton = new JToggleButton();
    tutoButton.setSelectedIcon(IconProducer.getImageIcon(ICON_TUTORIAL_ON));
    tutorialAction         = createTutorialAction(TUTORIAL_MODE_STRING,
                               IconProducer.getImageIcon(ICON_TUTORIAL_OFF),
                               tutoButton);

    addActionToToolBar(collapceAction, toolBar, false, COLLAPSE_TREE_STRING, 0);
    addActionToToolBar(expandFirstLevelAction, toolBar, false,
                       EXPAND_1ST_LEVEL_STRING, 0);
    addActionToToolBar(expandAllLevelsAction, toolBar, false,
                       EXPAND_ALL_LEVELS_STRING, 0);
    addActionToToolBar(searchAction, toolBar, false, SEARCH_STRING, 0);

    JPanel emptyArea = new JPanel();
    toolBar.add(emptyArea);

    addActionToToolBar(tutorialAction, toolBar, false, TUTORIAL_MODE_STRING, 0);
  }


  public Action createCollapceAction(String text, ImageIcon icon) {
    return new AbstractAction(text, icon) {
      public void actionPerformed(ActionEvent e) {
        parent.collapse();
      }
    };
  }

  public Action createExpandFirstLevelAction(String text, ImageIcon icon) {
    return new AbstractAction(text, icon) {
      public void actionPerformed(ActionEvent e) {
        parent.expandFirstLevel();
      }
    };
  }

  public Action createExpandAllLevelsAction(String text, ImageIcon icon) {
    return new AbstractAction(text, icon) {
      public void actionPerformed(ActionEvent e) {
        parent.expandAllLevels();
      }
    };
  }

  public Action createSearchAction(String text, ImageIcon icon) {
    return new AbstractAction(text, icon) {
      public void actionPerformed(ActionEvent e) {
        parent.search();
      }
    };
  }

  public Action createTutorialAction(String text, ImageIcon icon,
                                     AbstractButton button) {
    return new CustomAction(text, icon, button) {
      boolean isTutorialMode = false;
      public void actionPerformed(ActionEvent e) {
        parent.setTutorialMode(isTutorialMode = !isTutorialMode);
      }
    };
  }

}
