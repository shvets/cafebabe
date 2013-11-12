// ImageViewer.java

package org.sf.cafebabe.activation;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.awt.MediaTracker;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.activation.DataHandler;
import javax.activation.CommandObject;

/**
 * This class represents custom viewer for the image
 *
 * @version 1.0 03/04/2002
 * @author Alexander Shvets
 */
public class ImageViewer extends JPanel implements CommandObject {
  private ImageViewerCanvas canvas;

  /**
   * Creates new image viewer
   */
  public ImageViewer() {
    canvas = new ImageViewerCanvas();

    add(new JScrollPane(canvas));
  }

  /**
   * Initialize the Command with the verb it is requested to handle
   * and the DataHandler that describes the data it will operate on.
   * NOTE: it is acceptable for the caller to pass null as the value
   * for DataHandler.
   *
   * @param verb - The Command Verb this object refers to.
   * @param dataHandler - The DataHandler.
   * @exception IOException the I/O exception
   */
  public void setCommandContext(String verb, DataHandler dataHandler)
              throws IOException {
    InputStream is = dataHandler.getInputStream();

    MediaTracker mtracker = new MediaTracker(this);

    int i = 0;
    byte buffer[] = new byte[1024];
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    while((i = is.read(buffer)) > 0) {
      baos.write(buffer, 0, i);
    }

    is.close();

    Image image = getToolkit().createImage(baos.toByteArray());

    mtracker.addImage(image, 0);

    try {
      mtracker.waitForID(0);
      mtracker.waitForAll();
      if(mtracker.statusID(0, true) != 8) {
        throw new IOException("Error occured in image loading = " +
                              mtracker.getErrorsID(0));
      }

      canvas.setImage(image);
    }
    catch(InterruptedException e) {
      throw new IOException("Error reading image data");
    }
  }

  /**
   * Notifies this component that it now has a parent component.
   */
  public void addNotify() {
    super.addNotify();

    invalidate();
    validate();
    doLayout();
  }

  /**
   * Gets the preferred size of this component.
   *
   * @return a dimension object indicating this component's preferred size.
   */
  public Dimension getPreferredSize() {
    return canvas.getPreferredSize();
  }

}

/**
 * This class represents the panel for displaying the image
 */
class ImageViewerCanvas extends JPanel {
  private Image image;

  /**
   * Sets the image for this panel
   *
   * @param image the image
   */
  public void setImage(Image image) {
    this.image = image;

    invalidate();
    repaint();
  }

  /**
   * Gets the preferred size of this component.
   *
   * @return a dimension object indicating this component's preferred size.
   */
  public Dimension getPreferredSize() {
    Dimension dimension = null;
    if(image == null) {
      dimension = new Dimension(200, 200);
    }
    else {
      dimension = new Dimension(image.getWidth(this), image.getHeight(this));
    }

    return dimension;
  }

  /**
   * Draws the panel
   *
   * @param g the graphics object
   */
  public void paint(Graphics g) {
    if(image != null) {
      g.drawImage(image, 0, 0, this);
    }
  }

}
