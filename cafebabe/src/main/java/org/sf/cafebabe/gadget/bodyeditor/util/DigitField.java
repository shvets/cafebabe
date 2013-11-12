// DigitField.java

package org.sf.cafebabe.gadget.bodyeditor.util;

import javax.swing.JTextField;
import javax.swing.text.Document;

public class DigitField extends JTextField {

  public DigitField() {
    this.setDocument(new DigitDocument());
  }

  public DigitField(String text) {
    super(text);
    this.setDocument(new DigitDocument());
  }

  public DigitField(int columns) {
    super(columns);
    this.setDocument(new DigitDocument(columns));
  }

  public DigitField(String text, int columns) {
    super(text, columns);
    this.setDocument(new DigitDocument(columns));
  }

  public DigitField(Document doc, String text, int columns) {
    super(doc, text, columns);
  }

  public void setRange(int low, int high) {
/*    int length = new Integer(high).toString().length();

    setColumns(length);
    this.setDocument(new DigitDocument(length));
*/

    ((DigitDocument)getDocument()).setRange(low, high);
  }

}
