// ClassFrameActions.java

package org.sf.cafebabe.task.classfile;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.JToggleButton;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;

import org.sf.cafebabe.Constants;
import org.sf.cafebabe.util.Actions;
import org.sf.cafebabe.util.IconProducer;
import org.sf.cafebabe.gadget.classtree.ClassTree;

/**
 * This class represents the class frame actions
 *
 * @version 1.0 02/05/2002
 * @author Alexander Shvets
 */
public class ClassFrameActions extends Actions implements Constants {
  private final static String COLLAPSE_TREE_STRING     = "Collapse tree";
  private final static String EXPAND_1ST_LEVEL_STRING  = "Expand first level";
  private final static String EXPAND_ALL_LEVELS_STRING = "Expand all levels";
  private final static String SEARCH_STRING            = "Search string";
  private final static String INTEGRITY_STRING         = "Integrity test";
  private final static String TUTORIAL_MODE_STRING     = "Tutorial mode";

  private Action collapceAction, expandFirstLevelAction,
                 expandAllLevelsAction, searchAction, integrityAction, tutorialAction;

  protected JToolBar toolBar = new JToolBar();

  private ClassFrame classFrame;

  /**
   * Creates actions for ClassFrame
   *
   * @param classFrame  the class frame
   */
  public ClassFrameActions(ClassFrame classFrame) {
    this.classFrame = classFrame;

    init();
  }

  /**
   * Gets the toot bar
   *
   * @return the toot bar
   */
  public JToolBar getToolBar() {
    return toolBar;
  }

  /**
   * Initializes actions
   */
  protected void init() {
    ImageIcon collapseIcon = IconProducer.getImageIcon(ICON_COLLAPSE);
    ImageIcon expandIcon   = IconProducer.getImageIcon(ICON_EXPAND);
    ImageIcon expandAllIcon = IconProducer.getImageIcon(ICON_EXPAND_ALL);
    ImageIcon searchIcon = IconProducer.getImageIcon(ICON_SEARCH);
    ImageIcon integrityIcon = IconProducer.getImageIcon(ICON_INTEGRITY);
    ImageIcon tutorialOnIcon = IconProducer.getImageIcon(ICON_TUTORIAL_ON);
    ImageIcon tutorialOffIcon = IconProducer.getImageIcon(ICON_TUTORIAL_OFF);

    collapceAction = createCollapceAction(COLLAPSE_TREE_STRING, collapseIcon);
    expandFirstLevelAction = createExpandFirstLevelAction(EXPAND_1ST_LEVEL_STRING, expandIcon);
    expandAllLevelsAction  = createExpandAllLevelsAction(EXPAND_ALL_LEVELS_STRING, expandAllIcon);
    searchAction           = createSearchAction(SEARCH_STRING, searchIcon);
    integrityAction        = createIntegrityAction(INTEGRITY_STRING, integrityIcon);

    AbstractButton tutoButton = new JToggleButton();

    tutoButton.setSelectedIcon(tutorialOnIcon);

    tutorialAction = createTutorialAction(TUTORIAL_MODE_STRING, tutorialOffIcon, tutoButton);

    addActionToToolBar(collapceAction, toolBar, false, COLLAPSE_TREE_STRING, 0);
    addActionToToolBar(expandFirstLevelAction, toolBar, false,
                       EXPAND_1ST_LEVEL_STRING, 0);
    addActionToToolBar(expandAllLevelsAction, toolBar, false,
                       EXPAND_ALL_LEVELS_STRING, 0);
    addActionToToolBar(searchAction, toolBar, false, SEARCH_STRING, 0);
    addActionToToolBar(integrityAction, toolBar, false, INTEGRITY_STRING, 0);

    JPanel emptyArea = new JPanel();
    toolBar.add(emptyArea);

    addActionToToolBar(tutorialAction, toolBar, false, TUTORIAL_MODE_STRING, 0);
  }

  /**
   * Creates the action for collapcing class file tree
   */
  public Action createCollapceAction(String text, ImageIcon icon) {
    return new AbstractAction(text, icon) {
      public void actionPerformed(ActionEvent e) {
        ClassTree classTree = classFrame.getClassTree();

        classTree.collapse();
      }
    };
  }

  /**
   * Creates the action for the expanding first level only
   */
  public Action createExpandFirstLevelAction(String text, ImageIcon icon) {
    return new AbstractAction(text, icon) {
      public void actionPerformed(ActionEvent e) {
        ClassTree classTree = classFrame.getClassTree();

        classTree.expandFirstLevel();
      }
    };
  }

  /**
   * Creates the action for the expanding all levels
   */
  public Action createExpandAllLevelsAction(String text, ImageIcon icon) {
    return new AbstractAction(text, icon) {
      public void actionPerformed(ActionEvent e) {
        ClassTree classTree = classFrame.getClassTree();

        classTree.expandAllLevels();
      }
    };
  }

  /**
   * Creates the action for the integrity command
   */
  public Action createIntegrityAction(String text, ImageIcon icon) {
    return new AbstractAction(text, icon) {
      public void actionPerformed(ActionEvent e) {
        //classFrame.integrityTest();
        ClassTree classTree = classFrame.getClassTree();
        
        classTree.integrityTest();
      }
    };
  }

  /**
   * Creates the action for the search command
   */
  public Action createSearchAction(String text, ImageIcon icon) {
    return new AbstractAction(text, icon) {
      public void actionPerformed(ActionEvent e) {
        classFrame.getClassTree().search(/*classFrame.getJFrame()*/);
      }
    };
  }

  /**
   * Creates the action for the turorial command
   */
  public Action createTutorialAction(String text, ImageIcon icon,
                                     AbstractButton button) {
    return new CustomAction(text, icon, button) {
      boolean isTutorialMode = false;
      public void actionPerformed(ActionEvent e) {
        ClassTree classTree = classFrame.getClassTree();

        classTree.setTutorialMode(isTutorialMode = !isTutorialMode);
      }
    };
  }

}
