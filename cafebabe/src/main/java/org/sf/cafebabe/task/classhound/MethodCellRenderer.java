// MethodCellRenderer.java

package org.sf.cafebabe.task.classhound;

import java.awt.Component;
import java.awt.Color;
import java.awt.Insets;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.StringTokenizer;

import javax.swing.UIManager;
import javax.swing.ListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * Class-renderer for reperesenting custom method cell for Classfile
 *
 * @version 1.0 02/04/2002
 * @author Alexander Shvets
 */
class MethodCellRenderer extends JLabel implements ListCellRenderer {

  protected Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

  /**
   * Return a component that has been configured to display the specified value. That component's paint method is then called to "render" the cell. If it is necessary to compute the dimensions of a list because the list cells do not have a fixed size, this method is called to generate a component on which getPreferredSize can be invoked.
   *
   * @param list  The JList we're painting.
   * @param value  The value returned by list.getModel().getElementAt(index).
   * @param index  The cells index.
   * @param isSelected  True if the specified cell was selected.
   * @param cellHasFocus True if the specified cell has the focus.
   * @return  A component whose paint() method will render
   *          the specified value.
   */
  public Component getListCellRendererComponent(JList list, Object value,
                     int index, boolean isSelected, boolean cellHasFocus) {
    this.setText(value.toString());

    if (isSelected) {
      this.setBackground(list.getSelectionBackground());
      this.setForeground(list.getSelectionForeground());
    }
    else {
      this.setBackground(list.getBackground());
      this.setForeground(list.getForeground());
    }
    this.setFont(list.getFont());

    if(cellHasFocus)
      this.setBorder(UIManager.getBorder("List.focusCellHighlightBorder"));
    else
      this.setBorder(noFocusBorder);

    return this;
  }

  /**
   * Paints this component.
   *
   * @param g The graphics context to use for painting.
   */
  public void paint(Graphics g) {
    FontMetrics m_fm = g.getFontMetrics();

    g.setColor(this.getBackground());
    g.fillRect(0, 0, getWidth(), getHeight());
    getBorder().paintBorder(this, g, 0, 0, getWidth(), getHeight());

    g.setColor(this.getForeground());
    g.setFont(getFont());
    Insets m_insets = getInsets();
    int x = m_insets.left;
    int y = m_insets.top + m_fm.getAscent();

    StringTokenizer st = new StringTokenizer(getText());

    while(st.hasMoreTokens()) {
      String token = st.nextToken();

      int pos = token.indexOf("(");

      if(pos != -1) {
        String methodName = token.substring(0, pos);
        Color oldColor = g.getColor();
        g.setColor(Color.blue);
        g.drawString(methodName, x, y);
        g.setColor(oldColor);
        g.drawString(token.substring(pos), x + m_fm.stringWidth(methodName), y);
      }
      else {
        g.drawString(token, x, y);
      }

      x += m_fm.stringWidth(token) + 5;

      if(!st.hasMoreTokens()) {
        break;
      }
    }
  }

}
