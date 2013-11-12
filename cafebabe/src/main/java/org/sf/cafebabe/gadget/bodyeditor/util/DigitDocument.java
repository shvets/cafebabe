// DigitDocument.java

package org.sf.cafebabe.gadget.bodyeditor.util;

import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

/**
 * Document replacement to only accept digits as input. If the user
 * enters something other than digits, they are not entered, and
 * the system beeps.
 */
public class DigitDocument extends PlainDocument {

  private int maxLength;
  private int low  = Integer.MIN_VALUE;
  private int high = Integer.MAX_VALUE;

  public DigitDocument() {
    this(0);
  }

  public DigitDocument(int maxLength) {
    super();

    if(maxLength <= 0)
      this.maxLength = 10;
    else
      this.maxLength = maxLength;
  }

  /**
   * Handles the insertion of characters into the document,
   * disallowing anything other than hex digits. This will work for
   * text entered from the keyboard, as well as pasted from the
   * clipboard.
   *
   * @param offset <CODE>int</CODE> describing the caret location
   * @param str <CODE>String</CODE> that is being inserted
   * @param a <CODE>AttributeSet</CODE>
   *
   * @exception BadLocationException
   */
  public void insertString(int offset, String str, AttributeSet a)
                           throws BadLocationException {
    if(getLength() + str.length() <= maxLength) {
      String digitText = new String(getText(0, getLength())) + str;
      if(digitText.length() > 0) {
        try {
          int digit = Integer.parseInt(digitText);
          if(digit < low || digit > high) {
            return;
          }
        }
        catch(NumberFormatException e) {
          return;
        }
      }

      if(str.length() > 0) {
        char charCode = str.charAt(0);

        if(Character.isDigit(charCode)) {
          super.insertString(offset, str, a);
        }
      }
    }
  }

  public void setRange(int low, int high) {
    this.low  = low;
    this.high = high;
  }

}

