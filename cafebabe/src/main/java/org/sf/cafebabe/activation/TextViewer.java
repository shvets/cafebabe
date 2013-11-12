package org.sf.cafebabe.activation;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.awt.GridLayout;
import java.awt.Dimension;

import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.activation.DataHandler;
import javax.activation.CommandObject;

/**
 * This class represents custom viewer for the text
 *
 * @version 1.0 03/04/2002
 * @author Alexander Shvets
 */
public class TextViewer extends JPanel implements CommandObject {
  protected JTextArea textArea;

  /**
   * Creates new text viewer
   */
  public TextViewer() {
    setLayout(new GridLayout(1, 1));

    textArea = new JTextArea();
    textArea.setEditable(false);

    add(new JScrollPane(textArea));
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

    int i = 0;

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte abyte0[] = new byte[1024];

    while((i = is.read(abyte0)) > 0) {
      baos.write(abyte0, 0, i);
    }

    is.close();

    textArea.setText(baos.toString());
  }

  /**
   * Notifies this component that it now has a parent component.
   */
  public void addNotify() {
    super.addNotify();

    invalidate();
  }

  /**
   * Gets the preferred size of this component.
   *
   * @return a dimension object indicating this component's preferred size.
   */
  public Dimension getPreferredSize() {
    return textArea.getMinimumSize();
  }

}
