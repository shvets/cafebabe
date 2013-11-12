// BodyTable.java

package org.sf.cafebabe.gadget.bodyeditor;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.StringTokenizer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.sf.cafebabe.Constants;
import org.sf.cafebabe.util.IconProducer;
import org.sf.classfile.AttributeEntry;
import org.sf.classfile.ClassFile;
import org.sf.classfile.ConstPool;
import org.sf.classfile.MethodEntry;
import org.sf.classfile.attribute.CodeAttribute;
import org.sf.classfile.instruction.*;

public abstract class BodyTable extends JTable implements Opcode, ClipboardOwner {
  String archive = "lib/" + Constants.JAR_NAME;

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

  private BodyTableModel model;

  private JPopupMenu instructionMenu = new InstructionMenu();

  private Clipboard clipboard = this.getToolkit().getSystemClipboard();

  private ConstPool constPool;

  private Component parent;
  private MethodEntry methodEntry;
  private ClassFile classFile;

  public BodyTable(Component parent, MethodEntry methodEntry, ClassFile classFile) {
    super();

    this.parent = parent;
    this.methodEntry = methodEntry;
    this.classFile = classFile;

    constPool = classFile.getConstPool();

    setModel(new BodyTableModel(methodEntry, constPool));

    model = (BodyTableModel)getModel();

    TableColumn column1 = getColumnModel().getColumn(0);
    TableColumn column2 = getColumnModel().getColumn(1);
    TableColumn column3 = getColumnModel().getColumn(2);
    column1.setPreferredWidth(45);
    column2.setPreferredWidth(140);
    column3.setPreferredWidth(600);

    TableCellRenderer renderer = new BodyRenderer();
    column3.setCellRenderer(renderer);

    setShowHorizontalLines(false);

    setRowSelectionInterval(0, 0);

    addMouseListener(new CustomMouseAdapter());

    ToolTipManager.sharedInstance().registerComponent(this);
    ToolTipManager.sharedInstance().setInitialDelay(2000);
  }

  // implements ClipboardOwner
  public void lostOwnership(Clipboard clipboard, Transferable contents) {}

  public String getToolTipText(MouseEvent e) {

    String toolTip = null;

    Point p = new Point(e.getX(), e.getY());
    int row    = this.rowAtPoint(p);
    int column = this.columnAtPoint(p);

    toolTip = ((BodyTableModel)getModel()).getTooltipAt(row, column);

    return toolTip;
  }

  public String getCurrentMnemonic() {
    int row = getSelectedRow();

    if(row == -1)
      return null;

    return (String)getValueAt(row, 1);
  }

  public void editInstruction() {
    int currentRow = (model.getRowCount() == 0) ? -1 : getSelectedRow();

    if(currentRow == -1) {
      JOptionPane.showMessageDialog(this.getTopLevelAncestor(),
                  "Please select an instruction for editing.\n",
                  "Warning", JOptionPane.WARNING_MESSAGE);
       return;
    }

    InstructionEditor instructionEditor = new InstructionEditor(parent, constPool);

    short offset = model.getOffset(currentRow);
    Instruction instruction = model.getInstruction(currentRow);

    instructionEditor.setInstruction(offset, instruction);

    instructionEditor.setTitle("Edit instruction");
    instructionEditor.show();

    if(instructionEditor.isOk()) {
      model.editInstruction(instructionEditor.getInstruction(), currentRow);
      updateClassFile();
    }
  }

  public void addInstruction(Instruction instruction) {
    int currentRow = getSelectedRow();

    if(currentRow == -1 && model.getRowCount() > 0) {
      JOptionPane.showMessageDialog(this.getTopLevelAncestor(),
                  "Please select an instruction after which " +
                  "new instruction(s) will be added.\n",
                  "Warning", JOptionPane.WARNING_MESSAGE);
       return;
    }

    InstructionEditor instructionEditor = new InstructionEditor(parent, constPool);

    short offset = (model.getRowCount() == 0) ? 0 : model.getOffset(currentRow);

    instructionEditor.setInstruction(offset, instruction);

    instructionEditor.setTitle("Add instruction");
    instructionEditor.show();

    if(instructionEditor.isOk()) {
      currentRow = (model.getRowCount() == 0) ? -1 : currentRow;
      model.addInstruction(instructionEditor.getInstruction(), currentRow+1);
      setRowSelectionInterval(currentRow+1, currentRow+1);

      updateClassFile();
    }
  }

  public boolean removeInstructions() {
    int[] rows = getSelectedRows();

    if(rows.length == 0 || model.getRowCount() == 0) {
      JOptionPane.showMessageDialog(this.getTopLevelAncestor(),
                  "Please select instruction(s) to remove.\n",
                  "Warning", JOptionPane.WARNING_MESSAGE);
      return false;
    }

    int value = JOptionPane.showConfirmDialog(this.getTopLevelAncestor(),
                    "Do you want to delete selected instruction(s)?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION);

    if(value == JOptionPane.YES_OPTION) {
      Instruction[] instructions = new Instruction[rows.length];

      for(int i=0; i < rows.length; i++) {
        instructions[i] = model.getInstruction(rows[i]);
      }

      model.removeInstructions(instructions);
      setRowSelectionInterval(0, 0);

      updateClassFile();
    }

    return true;
  }

  private void updateClassFile() {
    if(model.isBodyChanged()) {
      CodeAttribute codeAttribute = model.getCodeAttribute();

      byte[] buffer = codeAttribute.getBytes();

      AttributeEntry attribute =
                     methodEntry.getAttribute(AttributeEntry.CODE, constPool);

      attribute.setBuffer(buffer);

      classFile.setChanged(true);
    }
  }

  private void cutCommand() {
    int[] rows = getSelectedRows();

    if(rows.length == 0) {
      JOptionPane.showMessageDialog(this.getTopLevelAncestor(),
                  "Please select instruction(s).\n",
                  "Warning", JOptionPane.WARNING_MESSAGE);
       return;
    }

    Instruction[] instructions = new Instruction[rows.length];

    for(int i=0; i < rows.length; i++) {
      instructions[i] = model.getInstruction(rows[i]);
    }

    model.removeInstructions(instructions);

    clipboard.setContents(new InstructionsSelection(instructions), this);

    updateClassFile();
  }

  private void copyCommand() {
    int[] rows = getSelectedRows();

    if(rows.length == 0) {
      JOptionPane.showMessageDialog(this.getTopLevelAncestor(),
                  "Please select instruction(s).\n",
                  "Warning", JOptionPane.WARNING_MESSAGE);
       return;
    }

    Instruction[] instructions = new Instruction[rows.length];

    for(int i=0; i < rows.length; i++) {
      instructions[i] = model.getInstruction(rows[i]);
    }

    InstructionsSelection contents = new InstructionsSelection(instructions);

    clipboard.setContents(contents, this);
  }

  private void pasteCommand() {
    int currentRow = getSelectedRow();

    if(currentRow == -1) {
      JOptionPane.showMessageDialog(this.getTopLevelAncestor(),
                  "Please select an instruction after which " +
                  "new instruction(s) will be added.\n",
                  "Warning", JOptionPane.WARNING_MESSAGE);
       return;
    }

    Transferable content = clipboard.getContents(this);

    if(content != null) {
      try {
        Instruction[] instructions = (Instruction[])
           content.getTransferData(InstructionsSelection.instructionsFlavor);

        currentRow = (model.getRowCount() == 0) ? -1 : currentRow;

        for(int i=0; i < instructions.length; i++, currentRow++) {
          model.addInstruction(instructions[i], currentRow+1);
        }

        setRowSelectionInterval(currentRow+1, currentRow+1+instructions.length);

        if(instructions.length > 0) {
          updateClassFile();
        }
      }
      catch (Exception e) {
        getToolkit().beep();
      }
    }
  }

class BodyRenderer extends DefaultTableCellRenderer {

  public Component getTableCellRendererComponent(
         JTable table, Object value, boolean isSelected,
         boolean hasFocus, int row, int column) {
    setIcon(null);

    String text = (String)table.getValueAt(row, 2);

//    System.out.println("text " + text);

    StringTokenizer st = new StringTokenizer(text);

    String mnemonic = st.nextToken();
    byte tag = OpcodeTable.getOpcode(mnemonic);

    StringBuffer param = new StringBuffer();

    if(st.hasMoreTokens()) {
      param.append(st.nextToken());
    }

    while(st.hasMoreTokens()) {
      param.append(" ");
      param.append(st.nextToken());
    }

    text = param.toString();

    RankTable usageRankTable = UsageRanking.getInstance();

    String usageGroup = usageRankTable.getRank(tag).key;

    if(usageGroup.equals(UsageRanking.FIELD_RANK)) {
      setIcon(FIELD_ICON);
    }
    else if(usageGroup.equals(UsageRanking.JUMP_RANK)) {
      setIcon(JUMP_ICON);
    }
    else if(usageGroup.equals(UsageRanking.SWITCH_RANK)) {
    }

/*    RankTable parameterRankTable = ParameterRanking.getInstance();
    String parameterGroup = parameterRankTable.getRank(tag).key;

    if(parameterGroup.equals(ParameterRanking.METHOD_RANK)) {
      setIcon(METHOD_ICON);
    }
    else if(parameterGroup.equals(ParameterRanking.IMETHOD_RANK)) {
      setIcon(IMETHOD_ICON);
    }
*/
/*      if(type.startsWith(ClassConst.TYPE)) {
        text = text.replace('/', '.');
        setIcon(CLASS_ICON);
      }
      else if(type.startsWith(IntegerConst.TYPE)) {
        setIcon(INT_ICON);
      }
      else if(type.startsWith(LongConst.TYPE)) {
        setIcon(LONG_ICON);
      }
      else if(type.startsWith(DoubleConst.TYPE)) {
        setIcon(DOUBLE_ICON);
      }
      else if(type.startsWith(FloatConst.TYPE)) {
        setIcon(FLOAT_ICON);
      }
      else if(type.startsWith(StringConst.TYPE)) {
        setIcon(STRING_ICON);
      }
      else if(type.startsWith(UnicodeConst.TYPE)) {
        setIcon(UNICODE_ICON);
      }
    }
*/
    return super.getTableCellRendererComponent(
                 table, text, isSelected, hasFocus, row, column);
  }

}

  public abstract void showTopic();

public class InstructionMenu extends JPopupMenu {
  final String CUT_STRING      = "Cut";
  final String COPY_STRING     = "Copy";
  final String PASTE_STRING    = "Paste";

  final String EDIT_STRING     = "Edit";
  final String ADD_STRING      = "Add";
  final String REMOVE_STRING   = "Remove";
  final String HELP_STRING     = "Help";

  public InstructionMenu() {
    super();

    JMenuItem cutItem    = new JMenuItem(CUT_STRING);
    JMenuItem copyItem   = new JMenuItem(COPY_STRING);
    JMenuItem pasteItem  = new JMenuItem(PASTE_STRING);

    JMenuItem editItem   = new JMenuItem(EDIT_STRING);
    JMenuItem addItem    = new JMenuItem(ADD_STRING);
    JMenuItem removeItem = new JMenuItem(REMOVE_STRING);
    JMenuItem helpItem   = new JMenuItem(HELP_STRING);

    ActionListener actionListener = new GenericActionListener();

    cutItem.addActionListener(actionListener);
    copyItem.addActionListener(actionListener);
    pasteItem.addActionListener(actionListener);

    editItem.addActionListener(actionListener);
    addItem.addActionListener(actionListener);
    removeItem.addActionListener(actionListener);
    helpItem.addActionListener(actionListener);

    this.add(cutItem);
    this.add(copyItem);
    this.add(pasteItem);
    this.addSeparator();
    this.add(editItem);
    this.add(addItem);
    this.add(removeItem);
    this.addSeparator();
    this.add(helpItem);
  }

class GenericActionListener implements ActionListener {

  public void actionPerformed(ActionEvent event) {
    String cmd = event.getActionCommand();
    if(cmd.equals(CUT_STRING)) {
      cutCommand();
    }
    else if(cmd.equals(COPY_STRING)) {
      copyCommand();
    }
    else if(cmd.equals(PASTE_STRING)) {
      pasteCommand();
    }
    else if(cmd.equals(EDIT_STRING)) {
      editInstruction();
    }
    else if(cmd.equals(ADD_STRING)) {
      addInstruction(null);
    }
    else if(cmd.equals(REMOVE_STRING)) {
      removeInstructions();
    }
    else if(cmd.equals(HELP_STRING)) {
      showTopic();
    }
  }

}

}

class CustomMouseAdapter extends MouseAdapter {

  public void mouseReleased(MouseEvent event) {
    if(event.isPopupTrigger()) {
      instructionMenu.show(event.getComponent(), event.getX(), event.getY());
    }
  }

}

}
