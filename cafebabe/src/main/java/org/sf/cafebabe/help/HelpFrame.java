// HelpFrame.java

package org.sf.cafebabe.help;

import org.sf.mdi.MDIFrame;

public class HelpFrame extends MDIFrame {
  private HelpPanel helpPanel = new HelpPanel();

  public HelpFrame() {
    super("Help System");

    this.getContentPane().add(helpPanel);
  }

  public void showTopic(String archive, String topic) {
    helpPanel.updateHistory("jar:file:" + archive + "!/" + topic);
  }

  public void showTopic(String topic) {
    helpPanel.updateHistory("file:" + topic);
  }

}
