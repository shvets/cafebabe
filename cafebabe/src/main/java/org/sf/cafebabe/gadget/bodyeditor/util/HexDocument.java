// HexDocument.java

package org.sf.cafebabe.gadget.bodyeditor.util;

import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;


/**
 * Document replacement to only accept hex digits as input. If the user
 * enters something other than hex digits, they are not entered, and
 * the system beeps.
 */
public class HexDocument extends PlainDocument {

  private int maxLength;

  public HexDocument(byte[] buffer) {
    super();

    try {
      super.insertString(0, toHexString(buffer), null);
    }
    catch(BadLocationException e) {}

    this.maxLength = buffer.length*2;
  }

  public byte[] getBytes() {
    byte[] buffer = new byte[maxLength/2];

    try {
      StringBuffer sb = new StringBuffer(getText(0, getLength()));

      int length = sb.length();

      for(int i=length; i < maxLength; i++) {
        sb.insert(0, '0');
      }

      String hexText = sb.toString();

      for(int i=0, j=0; i < maxLength; i+=2, j++) {
        buffer[j] = (byte)Integer.parseInt(hexText.substring(i, i+2), 16);
      }
    }
    catch(BadLocationException e) {}

    return buffer;
  }

  /**
   * Handles the insertion of characters into the document,
   * disallowing anything other than hex digits. This will work for
   * text entered from the keyboard, as well as pasted from the
   * clipboard.
   *
   * @param offset <CODE>int</CODE> describing the caret location
   * @param str <CODE>String</CODE> that is being inserted
   * @param attr <CODE>AttributeSet</CODE>
   *
   * @exception BadLocationException
   */
  public void insertString(int offset, String str, AttributeSet attr)
                           throws BadLocationException {
    if(getLength() + str.length() <= maxLength) {
      if(str.length() > 0) {
        char charCode = str.charAt(0);

        if(isHexDigit(charCode)) {
          super.insertString(offset, str, attr);
        }
      }
    }
  }

  private static boolean isHexDigit(char charCode) {
    if(Character.isDigit(charCode)) {
      return true;
    }
    else if(Character.isLetter(charCode)) {
      charCode = Character.toLowerCase(charCode);
      if(charCode == 'a' || charCode == 'b' || charCode == 'c' ||
         charCode == 'd' || charCode == 'e' || charCode == 'f')
        return true;
    }

    return false;
  }

  private static String toHexString(byte[] buffer) {
    StringBuffer sb = new StringBuffer();

    for(int i=0; i < buffer.length; i++) {
      int b = (int)buffer[i];
      String byteStr = Integer.toHexString(b);

      if(byteStr.length() == 1) {
        sb.append("0");
        sb.append(byteStr);
      }
      else if(byteStr.length() == 2) {
        sb.append(byteStr);
      }
      else if(byteStr.length() > 2) {
        sb.append(byteStr.substring(byteStr.length()-2));
      }
    }

    return sb.toString();
  }

}
