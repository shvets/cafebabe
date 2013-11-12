// ClassTreeRenderer.java

package org.sf.cafebabe.gadget.classtree;

import java.util.StringTokenizer;
import java.awt.Component;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.Font;

import javax.swing.JTree;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.tree.TreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.sf.classfile.Entry;
import org.sf.classfile.Resolvable;
import org.sf.classfile.ConstPool;
import org.sf.classfile.PoolEntry;
import org.sf.classfile.InterfaceEntry;
import org.sf.classfile.Converter;
import org.sf.classfile.ClassConst;
import org.sf.classfile.NameAndTypeConst;
import org.sf.classfile.MethodConst;
import org.sf.classfile.InterfaceMethodConst;
import org.sf.classfile.FieldConst;
import org.sf.classfile.IntegerConst;
import org.sf.classfile.LongConst;
import org.sf.classfile.DoubleConst;
import org.sf.classfile.FloatConst;
import org.sf.classfile.StringConst;
import org.sf.classfile.Utf8Const;
import org.sf.classfile.UnicodeConst;

import org.sf.cafebabe.Constants;
import org.sf.cafebabe.util.IconProducer;

public class ClassTreeRenderer extends DefaultTreeCellRenderer {
  public ImageIcon CLASS_ICON     = IconProducer.getImageIcon(Constants.ICON_CLASS);
  public ImageIcon FIELD_ICON     = IconProducer.getImageIcon(Constants.ICON_FIELD);
  public ImageIcon METHOD_ICON    = IconProducer.getImageIcon(Constants.ICON_METHOD);
  public ImageIcon IMETHOD_ICON   = IconProducer.getImageIcon(Constants.ICON_IMETHOD);
  public ImageIcon NAT_ICON       = IconProducer.getImageIcon(Constants.ICON_NAME_AND_TYPE);
  public ImageIcon STRING_ICON    = IconProducer.getImageIcon(Constants.ICON_STRING);
  public ImageIcon UTF8_ICON      = IconProducer.getImageIcon(Constants.ICON_UTF8);
  public ImageIcon UNICODE_ICON   = IconProducer.getImageIcon(Constants.ICON_UNICODE);
  public ImageIcon JUMP_ICON      = IconProducer.getImageIcon(Constants.ICON_JUMP);
  public ImageIcon INT_ICON       = IconProducer.getImageIcon(Constants.ICON_INT);
  public ImageIcon LONG_ICON      = IconProducer.getImageIcon(Constants.ICON_LONG);
  public ImageIcon FLOAT_ICON     = IconProducer.getImageIcon(Constants.ICON_FLOAT);
  public ImageIcon DOUBLE_ICON    = IconProducer.getImageIcon(Constants.ICON_DOUBLE);

  private Object value;
  private boolean isSelected;

  private ConstPool constPool;

  public ClassTreeRenderer(ConstPool constPool) {
    this.constPool = constPool;
  }

  public Component getTreeCellRendererComponent(
         JTree tree, Object value, boolean isSelected, boolean expanded,
         boolean leaf, int row, boolean hasFocus) {
    this.value      = value;
    this.isSelected = isSelected;
    this.hasFocus   = hasFocus;

    StringBuffer sb = new StringBuffer(value.toString());

    if(!((ClassTreeNode)value).isCorrect()) {
      setForeground(Color.red);
    }

/*    if(value instanceof MagicNumberNode || value instanceof VersionNode) {
      if(expanded) {
        setIcon(getOpenIcon());
      }
      else {
        setIcon(getClosedIcon());
      }
    }
    else */if(value instanceof ClassReferenceNode) {
      short classReference = ((ClassReferenceNode)value).getClassReference();
      renderPoolEntry(constPool.getEntry(classReference), sb);
    }
    else if(value instanceof EntryNode) {
      Entry entry = ((EntryNode)value).getEntry();

      if(entry instanceof PoolEntry) {
        renderPoolEntry((PoolEntry)entry, sb);
      }
      else if(entry instanceof Resolvable) {
        Resolvable resolvable = (Resolvable)entry;

        sb = new StringBuffer();
        if(entry instanceof InterfaceEntry) {
          setIcon(CLASS_ICON);

          short index = ((InterfaceEntry)entry).getNameIndex();
          sb.append(Converter.toHexString(index, "") + " ");
        }

        sb.append(resolvable.resolve(constPool));
      }
      else {
        sb = new StringBuffer(entry.toString());
      }
    }
    else {
      if(expanded) {
        setIcon(getOpenIcon());
      }
      else {
        setIcon(getClosedIcon());
      }
    }

    setText(sb.toString());

    return this;
  }

  private void renderPoolEntry(PoolEntry poolEntry, StringBuffer sb) {
    String str = poolEntry.resolve(constPool);

    short index = constPool.indexOf(poolEntry);

    sb.append(Converter.toHexString(index, "") + " ");

    if(poolEntry instanceof ClassConst) {
      sb.append(str.replace('/', '.'));
      setIcon(CLASS_ICON);
    }
    else if(poolEntry instanceof NameAndTypeConst) {
      sb.append(str.replace('/', '.'));
      setIcon(NAT_ICON);
    }
    else if(poolEntry instanceof MethodConst) {
      sb.append(str.replace('/', '.'));
      setIcon(METHOD_ICON);
    }
    else if(poolEntry instanceof InterfaceMethodConst) {
      sb.append(str.replace('/', '.'));
      setIcon(IMETHOD_ICON);
    }
    else if(poolEntry instanceof FieldConst) {
      sb.append(str.replace('/', '.'));
      setIcon(FIELD_ICON);
    }
    else if(poolEntry instanceof IntegerConst) {
      sb.append(str);
      setIcon(INT_ICON);
    }
    else if(poolEntry instanceof LongConst) {
      sb.append(str);
      setIcon(LONG_ICON);
    }
    else if(poolEntry instanceof DoubleConst) {
      sb.append(str);
      setIcon(DOUBLE_ICON);
    }
    else if(poolEntry instanceof FloatConst) {
      sb.append(str);
      setIcon(FLOAT_ICON);
    }
    else if(poolEntry instanceof StringConst) {
      sb.append(str);
      setIcon(STRING_ICON);
    }
    else if(poolEntry instanceof Utf8Const) {
      sb.append(str);
      setIcon(UTF8_ICON);
    }
    else if(poolEntry instanceof UnicodeConst) {
      sb.append(str);
      setIcon(UNICODE_ICON);
    }
  }

  public void paintComponent(Graphics g) {
    TreeNode node   = (TreeNode)value;
    TreeNode parent = node.getParent();

    if(parent instanceof FolderNode) {
      String parentText = parent.toString();

      if(parentText.equals(Constants.FIELDS_TEXT)) {
        paintField(g);
        return;
      }
      else if(parentText.equals(Constants.METHODS_TEXT)) {
        paintMethod(g);
        return;
      }
    }

    if(node instanceof ClassReferenceNode) {
      paintPoolEntry(g);
      return;
    }
    else if(node instanceof EntryNode) {
      Entry entry = ((EntryNode)node).getEntry();

      if(entry instanceof PoolEntry || entry instanceof InterfaceEntry) {
        paintPoolEntry(g);
        return;
      }
    }

    super.paintComponent(g);
  }

  private void paintPoolEntry(Graphics g) {
    Icon icon = getIcon();
    FontMetrics fmetrics = g.getFontMetrics();

    if(isSelected) {
      Color bColor;

      if(selected) {
        bColor = getBackgroundSelectionColor();
      }
      else {
        bColor = getBackgroundNonSelectionColor();
        if(bColor == null) {
          bColor = getBackground();
        }
      }

      if(bColor != null) {
        g.setColor(bColor);
        g.fillRect(0, 0, getWidth(), getHeight());
      }
    }

    if(hasFocus) {
      Color bsColor = getBorderSelectionColor();

      if (bsColor != null) {
        g.setColor(bsColor);
        g.drawRect(0, 0, getWidth(), getHeight()-1);
      }
    }

    Insets m_insets = getInsets();
    int x = m_insets.left;
    int y = m_insets.top + fmetrics.getAscent();

    g.setColor(this.getForeground());
    g.setFont(getFont());

    StringTokenizer st = new StringTokenizer(getText());
    int cnt = st.countTokens();

    if(cnt == 0) return;

    String token = st.nextToken();

    Font oldFont = getFont();
    Font newFont = new Font(oldFont.getName(), Font.BOLD, oldFont.getSize());
    g.setFont(newFont);
    g.drawString(token, x, y);
    g.setFont(oldFont);

    x += 30;

    icon.paintIcon(this, g, x, 0);

    x += icon.getIconWidth() + getIconTextGap();

    int i=0;
    while(st.hasMoreTokens()) {
      token = st.nextToken();
      if(st.hasMoreTokens()) {
        token += ' ';
      }

      i++;
      if(i == cnt) {
        Color oldColor = g.getColor();
        g.setColor(Color.blue);
        g.drawString(token, x, y);
        g.setColor(oldColor);
      }
      else {
        g.drawString(token, x, y);
      }

      x += fmetrics.stringWidth(token);
    }
  }

  private void paintField(Graphics g) {
    Icon icon = getIcon();
    FontMetrics fmetrics = g.getFontMetrics();

    icon.paintIcon(this, g, 0, 0);

    Insets m_insets = getInsets();
    int x = m_insets.left + icon.getIconWidth() + getIconTextGap();
    int y = m_insets.top + fmetrics.getAscent();

    g.setColor(this.getForeground());
    g.setFont(getFont());

    StringTokenizer st = new StringTokenizer(getText());
    int cnt = st.countTokens();
    int i=0;
    while(st.hasMoreTokens()) {
      String token = st.nextToken();
      if(st.hasMoreTokens()) {
        token += ' ';
      }

      i++;
      if(i == cnt) {
        Color oldColor = g.getColor();
        g.setColor(Color.blue);
        g.drawString(token, x, y);
        g.setColor(oldColor);
      }
      else {
        g.drawString(token, x, y);
      }

      x += fmetrics.stringWidth(token);
    }
  }

  private void paintMethod(Graphics g) {
    Icon icon = getIcon();
    FontMetrics fmetrics = g.getFontMetrics();

    icon.paintIcon(this, g, 0, 0);

    Insets m_insets = getInsets();
    int x = m_insets.left + icon.getIconWidth() + getIconTextGap();
    int y = m_insets.top + fmetrics.getAscent();

    g.setColor(this.getForeground());
    g.setFont(getFont());

    StringTokenizer st = new StringTokenizer(getText());
    //int cnt = st.countTokens();

    while(st.hasMoreTokens()) {
      String token = st.nextToken();
      if(st.hasMoreTokens()) {
        token += ' ';
      }

      int pos = token.indexOf("(");
      if(pos != -1) {
        String methodName = token.substring(0, pos);
        Color oldColor = g.getColor();
        g.setColor(Color.blue);
        g.drawString(methodName, x, y);
        g.setColor(oldColor);
        g.drawString(token.substring(pos), x + fmetrics.stringWidth(methodName), y);
      }
      else {
        g.drawString(token, x, y);
      }

      x += fmetrics.stringWidth(token);
    }
  }

}
