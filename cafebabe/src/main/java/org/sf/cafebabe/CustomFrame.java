// CustomFrame.java

package org.sf.cafebabe;

import java.awt.Dimension;
import java.awt.Point;

import javax.activation.DataSource;
import org.sf.cafebabe.util.Configurator;
import org.sf.mdi.MDIFrame;
import org.sf.mdi.MDIDesktopPane;

public class CustomFrame extends MDIFrame {

  protected DataSource dataSource;

  public DataSource getDataSource() {
    return dataSource;
  }

  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void open() {
    super.open();

    MainFrame mainFrame = (MainFrame)getJFrame();

    mainFrame.getGlassPane().setVisible(true);

    MDIFrame currentFrame = mainFrame.getFrameManager().getCurrentFrame();

    Point point = null;

    if(currentFrame != null) {
      point = currentFrame.getLocation();

      point.x +=15;
      point.y +=15;
    }
    else {
      point = new Point(15, 15);
    }

    setLocation(point);

    MDIDesktopPane desktopPane = mainFrame.getMDIDesktopPane();

    Dimension d = desktopPane.getSize();

    d.width -= point.x;
    d.height -= point.y;

    setSize(d);

    mainFrame.getActions().enable();

    mainFrame.getFrameManager().setCurrentFrame(this);

    mainFrame.getGlassPane().setVisible(false);
  }

  /**
   * Saves the content of the editor as external file
   *
   * @param dataSource the data source
   */
  public void saveAs(DataSource dataSource) throws Exception {}

  /**
   * Loads properties for this window
   *
   * @param  configurator the configurator class
   */
  public void load(Configurator configurator) {}

  /**
   * Saves properties for this window
   *
   * @param  configurator the configurator class
   */
  public void save(Configurator configurator) {}

}
