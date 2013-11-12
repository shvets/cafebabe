// InstructionEditor.java

package org.sf.cafebabe.gadget.bodyeditor;

import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.sf.cafebabe.gadget.bodyeditor.parameter.ParameterEditor;
import org.sf.classfile.ConstPool;
import org.sf.classfile.Converter;
import org.sf.classfile.instruction.*;


public class InstructionEditor extends JDialog
                               implements Opcode, ActionListener {
  private final String GROUP_TEXT     = "Group";
  private final String MNEMONIC_TEXT  = "Mnemonic";
  private final String OPCODE_TEXT    = "Opcode: ";
  private final String LENGTH_TEXT    = "Length: ";
  private final String WIDE_TEXT      = "Wide  : ";

  private final Object[] wideValues   = { Boolean.FALSE, Boolean.TRUE };

  private RankTable rankTable         = UsageRanking.getInstance();

  private JList groupList             = new JList(rankTable.descriptions());
  private JComboBox wideCombo         = new JComboBox(wideValues);
  private JList mnemonicsList         = new JList();
  private JLabel opcodeField          = new JLabel();
  private JLabel lengthField          = new JLabel();

  private ParameterEditor paramEditor;

  private JButton okButton            = new JButton("Ok");
  private JButton cancelButton        = new JButton("Cancel");

  private boolean isOk                = true;

  private MnemonicsModel mnemonicsModel = new MnemonicsModel();

   public InstructionEditor(Component parent, ConstPool constPool) {
    super((Frame)null, true);

    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        isOk = false;
      }
    });

    Rectangle r = parent.getBounds();
    this.setBounds(r.x + r.width/2 - 200, r.y + r.height/2 - 190, 410, 450);

    this.setResizable(false);

//    this.setBounds(10, 10, 200, 200);

    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    paramEditor = new ParameterEditor(constPool);

    init();
  }

  private void init() {
    JLabel opcodeLabel = new JLabel(OPCODE_TEXT);
    JLabel lengthLabel = new JLabel(LENGTH_TEXT);
    JLabel wideLabel   = new JLabel(WIDE_TEXT);

    Font font = new Font("Monospaced", Font.BOLD, 12);

    opcodeLabel.setFont(font);
    lengthLabel.setFont(font);
    wideLabel.setFont(font);

    groupList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    groupList.addListSelectionListener(new GroupListener());
    mnemonicsList.addListSelectionListener(new MnemonicListener());
    wideCombo.addItemListener(new WideListener());

    mnemonicsList.setModel(mnemonicsModel);

    okButton.addActionListener(this);
    cancelButton.addActionListener(this);

    okButton.setActionCommand("ok");
    cancelButton.setActionCommand("cancel");

    JScrollPane sp1a = new JScrollPane();
    sp1a.setViewportView(groupList);

    Dimension dim1 = new Dimension(370, 220);
    sp1a.setMaximumSize(dim1);
    sp1a.setMinimumSize(dim1);

    JPanel panel1a1 = new JPanel();
    panel1a1.setLayout(new BoxLayout(panel1a1, BoxLayout.Y_AXIS));
    panel1a1.add(Box.createRigidArea(new Dimension(0, 5)));
    panel1a1.add(sp1a);
    panel1a1.add(Box.createRigidArea(new Dimension(0, 5)));

    JPanel panel1a = new JPanel();
    Dimension dim2 = new Dimension(380, 230);
    panel1a.setMaximumSize(dim2);
    panel1a.setMinimumSize(dim2);
    panel1a.setBorder(new TitledBorder(new EtchedBorder(), GROUP_TEXT));
    panel1a.setLayout(new BoxLayout(panel1a, BoxLayout.X_AXIS));
    panel1a.add(Box.createRigidArea(new Dimension(5, 0)));
    panel1a.add(panel1a1);
    panel1a.add(Box.createRigidArea(new Dimension(5, 0)));

    JScrollPane sp1b = new JScrollPane(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

    sp1b.setViewportView(mnemonicsList);

    Dimension dim3   = new Dimension(370, 140);
    sp1b.setMaximumSize(dim3);
    sp1b.setMinimumSize(dim3);

    JPanel panel1b1 = new JPanel();
    panel1b1.setLayout(new BoxLayout(panel1b1, BoxLayout.X_AXIS));
    panel1b1.add(Box.createRigidArea(new Dimension(5, 0)));
    panel1b1.add(sp1b);
    panel1b1.add(Box.createRigidArea(new Dimension(5, 0)));

    JPanel panel1b2 = new JPanel();
    panel1b2.setLayout(new BoxLayout(panel1b2, BoxLayout.X_AXIS));
    panel1b2.add(Box.createRigidArea(new Dimension(5, 0)));
    panel1b2.add(opcodeLabel);
    panel1b2.add(Box.createRigidArea(new Dimension(10, 0)));
    panel1b2.add(opcodeField);
    panel1b2.add(Box.createGlue());

    JPanel panel1b3 = new JPanel();
    panel1b3.setLayout(new BoxLayout(panel1b3, BoxLayout.X_AXIS));
    panel1b3.add(Box.createRigidArea(new Dimension(5, 0)));
    panel1b3.add(lengthLabel);
    panel1b3.add(Box.createRigidArea(new Dimension(10, 0)));
    panel1b3.add(lengthField);
    panel1b3.add(Box.createGlue());

    JPanel panel1b4 = new JPanel();
    panel1b4.setLayout(new BoxLayout(panel1b4, BoxLayout.X_AXIS));
    panel1b4.add(Box.createRigidArea(new Dimension(5, 0)));
    panel1b4.add(wideLabel);
    panel1b4.add(Box.createRigidArea(new Dimension(10, 0)));
    panel1b4.add(wideCombo);
    panel1b4.add(Box.createRigidArea(new Dimension(5, 0)));

    JPanel panel1b = new JPanel();
    panel1b.setMaximumSize(dim2);
    panel1b.setMinimumSize(dim2);
    panel1b.setLayout(new BoxLayout(panel1b, BoxLayout.Y_AXIS));
    panel1b.setBorder(new TitledBorder(new EtchedBorder(), MNEMONIC_TEXT));
    panel1b.add(Box.createRigidArea(new Dimension(0, 5)));
    panel1b.add(panel1b1);
    panel1b.add(Box.createRigidArea(new Dimension(0, 5)));
    panel1b.add(panel1b2);
    panel1b.add(Box.createRigidArea(new Dimension(0, 5)));
    panel1b.add(panel1b3);
    panel1b.add(Box.createRigidArea(new Dimension(0, 5)));
    panel1b.add(panel1b4);
    panel1b.add(Box.createRigidArea(new Dimension(0, 5)));

    Dimension dim4 = new Dimension(panel1a.getSize().width, 160);
    paramEditor.setMaximumSize(dim4);
    paramEditor.setMinimumSize(dim4);

    JPanel panel1c = new JPanel();
    panel1c.setLayout(new BoxLayout(panel1c, BoxLayout.X_AXIS));
    panel1c.add(Box.createRigidArea(new Dimension(5, 0)));
    panel1c.add(paramEditor);
    panel1c.add(Box.createRigidArea(new Dimension(5, 0)));

    JPanel panel1 = new JPanel();
    panel1.setLayout(new BorderLayout());
    panel1.add(panel1a, BorderLayout.WEST);
    panel1.add(panel1b, BorderLayout.EAST);
    panel1.add(panel1c, BorderLayout.SOUTH);

    JPanel panel2 = new JPanel();
    panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
    panel2.add(Box.createRigidArea(new Dimension(5, 0)));
    panel2.add(okButton);
    panel2.add(Box.createRigidArea(new Dimension(10, 0)));
    panel2.add(cancelButton);
    panel2.add(Box.createRigidArea(new Dimension(5, 0)));

    JPanel panel = (JPanel)this.getContentPane();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.add(panel1);
    panel.add(Box.createRigidArea(new Dimension(0, 5)));
    panel.add(panel2);
    panel.add(Box.createRigidArea(new Dimension(0, 5)));
  }

  public void actionPerformed(ActionEvent evt) {
    String cmd = evt.getActionCommand();
    if(cmd.equals("ok")) {
      isOk = true;
      dispose();
    }
    else if(cmd.equals("cancel")) {
      isOk = false;
      dispose();
    }
  }

  public boolean isOk() {
    return isOk;
  }

  public Instruction getInstruction(boolean wide) {
    String mnemonic = (String)mnemonicsList.getSelectedValue();
    int opcode      = OpcodeTable.getOpcode(mnemonic);

    byte[] buffer   = paramEditor.getBytes();

    Instruction instruction = InstructionFactory.create((byte)opcode, wide);

    if(buffer != null) {
      try {
        instruction.read(new DataInputStream(new ByteArrayInputStream(buffer)));
      }
      catch(IOException e) {}
    }

    return instruction;
  }

  public Instruction getInstruction() {
    boolean wide = ((Boolean)wideCombo.getSelectedItem()).booleanValue();

    return getInstruction(wide);
  }

  private String currMnemonic = null;
  private short offset        = 0;

  private Instruction instruction;

  public void setInstruction(short offset, Instruction instruction) {
    this.offset = offset;

    if(instruction == null)
      instruction = new DefaultInstruction(NOP, false);

    this.instruction = instruction;

    byte tag  = instruction.getTag();
    Rank rank = rankTable.getRank(tag);

    currMnemonic = OpcodeTable.getDescription(tag).mnemonic;

    groupList.setSelectedValue(rank.key, true);
  }

  private synchronized void groupChanged(String group) {
    Rank rank = rankTable.getRank(group);

    mnemonicsModel.setOpcodes(rank.opcodes);
    mnemonicsList.setSelectedIndex(0);

    mnemonicChanged(currMnemonic);
    currMnemonic = null;
  }

  private synchronized void mnemonicChanged(String mnemonic) {
    if(!mnemonic.equals(mnemonicsList.getSelectedValue())) {
      mnemonicsList.setSelectedValue(mnemonic, true);
      return;
    }

    byte tag   = OpcodeTable.getOpcode(mnemonic);

    opcodeField.setText("0x" + Converter.toHexString(tag));

    Instruction newInstruction = null;

    if(tag == instruction.getTag()) {
      newInstruction = instruction;
    }
    else {
      newInstruction = InstructionFactory.create(tag, false);
    }

    paramEditor.setInstruction(offset, newInstruction);

    lengthField.setText("" + newInstruction.length());

    wideCombo.setEnabled(OpcodeTable.isSupportWideMode(tag) ? true : false);
    okButton.setEnabled(paramEditor.isPossible() ? true : false);

    wideCombo.setSelectedItem(new Boolean(newInstruction.isWide()));
  }

class GroupListener implements ListSelectionListener {

  public void valueChanged(ListSelectionEvent e) {
    if(e.getValueIsAdjusting())
      return;

    JList list   = (JList)e.getSource();
    String group = (String)list.getSelectedValue();

    if(currMnemonic == null) {
      Rank rank = rankTable.getRank(group);
      byte tag  = rank.opcodes[0];

      currMnemonic = OpcodeTable.getDescription(tag).mnemonic;
    }

    groupChanged(group);
  }

}

class MnemonicListener implements ListSelectionListener {

  public void valueChanged(ListSelectionEvent e) {
    if(e.getValueIsAdjusting())
      return;

    JList list      = (JList)e.getSource();
    String mnemonic = (String)list.getSelectedValue();

    mnemonicChanged(mnemonic);
  }

}

class WideListener implements ItemListener {

  public void itemStateChanged(ItemEvent e) {
    if(e.getStateChange() == ItemEvent.SELECTED) {
      boolean wide = ((Boolean)e.getItem()).booleanValue();
      paramEditor.setInstruction(offset, getInstruction(wide));

      validate();
      repaint();
    }
  }

}

}
