// SectionsDialog.java

package org.sf.cafebabe.task.classfile;

import java.awt.AWTEvent;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.Box;
import javax.swing.BoxLayout;

import org.sf.classfile.ConstPool;
import org.sf.cafebabe.Constants;

/**
 * dialog that allows show/hide sections of class file
 */
public class SectionsDialog extends JDialog implements Constants {

  private boolean isOk = true, anyChanges = false;

  private JCheckBox magicNumberCheck,  minorVersionCheck, majorVersionCheck,
                    constantPoolCheck, accessFlagsCheck,  thisClassCheck,
                    superClassCheck,   interfacesCheck,   fieldsCheck,
                    methodsCheck,      attributesCheck;

  private boolean   magicNumberState,  minorVersionState, majorVersionState,
                    constantPoolState, accessFlagsState,  thisClassState,
                    superClassState,   interfacesState,   fieldsState,
                    methodsState,      attributesState;

  boolean[] foldersVisible;

  public SectionsDialog(JFrame parentFrame, boolean[] foldersVisible) {
    // Make sure we call the parent
    super(parentFrame, true);

    this.foldersVisible = foldersVisible;

    enableEvents(AWTEvent.FOCUS_EVENT_MASK);

    // Set the characteristics for this dialog instance
    this.setTitle("Show/hide classpath sections");
    this.setResizable(false);
    Rectangle r = parentFrame.getBounds();
    this.setBounds(r.x + r.width/2 - 300/2, r.y + r.height/2 - 200/2, 300, 200);
    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    magicNumberCheck  = new JCheckBox(MAGIC_NUMBER_TEXT);
    minorVersionCheck = new JCheckBox(MINOR_VERSION_TEXT);
    majorVersionCheck = new JCheckBox(MAJOR_VERSION_TEXT);
    constantPoolCheck = new JCheckBox(ConstPool.TYPE);
    accessFlagsCheck  = new JCheckBox(ACCESS_FLAGS_TEXT);
    thisClassCheck    = new JCheckBox(THIS_CLASS_TEXT);
    superClassCheck   = new JCheckBox(SUPER_CLASS_TEXT);
    interfacesCheck   = new JCheckBox(INTERFACES_TEXT);
    fieldsCheck       = new JCheckBox(FIELDS_TEXT);
    methodsCheck      = new JCheckBox(METHODS_TEXT);
    attributesCheck   = new JCheckBox(CLASS_ATTRIBUTES_TEXT);

    init();

    // Control panel

    JPanel panel1 = new JPanel();
    panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
    JPanel panel2 = new JPanel();
    panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));

    panel1.add(magicNumberCheck);
    panel1.add(minorVersionCheck);
    panel1.add(majorVersionCheck);
    panel1.add(constantPoolCheck);
    panel1.add(accessFlagsCheck);
    panel1.add(thisClassCheck);

    panel2.add(superClassCheck);
    panel2.add(interfacesCheck);
    panel2.add(fieldsCheck);
    panel2.add(methodsCheck);
    panel2.add(attributesCheck);

    JPanel optionsPanel = new JPanel();
    optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.X_AXIS));
    optionsPanel.add(Box.createRigidArea(new Dimension(20, 0)));
    optionsPanel.add(panel1);
    optionsPanel.add(Box.createRigidArea(new Dimension(20, 0)));
    optionsPanel.add(panel2);
    optionsPanel.add(Box.createRigidArea(new Dimension(20, 0)));

    // Control panel

    JPanel controlPanel = new JPanel();
    controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));

    JButton okButton     = new JButton("Ok");
    JButton cancelButton = new JButton("Cancel");

    okButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        isOk = true;
        anyChanges = save();
        SectionsDialog.this.dispose();
      }
    });

    cancelButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        isOk = false;
        anyChanges = false;
        SectionsDialog.this.dispose();
      }
    });

    controlPanel.add(Box.createHorizontalGlue());
    controlPanel.add(okButton);
    controlPanel.add(Box.createRigidArea(new Dimension(10, 0)));
    controlPanel.add(cancelButton);
    controlPanel.add(Box.createHorizontalGlue());

    Container contentPane = this.getContentPane();

    contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
    contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
    contentPane.add(optionsPanel);
    contentPane.add(Box.createRigidArea(new Dimension(0, 20)));
    contentPane.add(controlPanel);
    contentPane.add(Box.createRigidArea(new Dimension(0, 10)));

    pack();

    okButton.requestFocus();
  }


  private void init() {
    magicNumberState  = foldersVisible[0];
    minorVersionState = foldersVisible[1];
    majorVersionState = foldersVisible[2];
    constantPoolState = foldersVisible[3];
    accessFlagsState  = foldersVisible[4];
    thisClassState    = foldersVisible[5];
    superClassState   = foldersVisible[6];
    interfacesState   = foldersVisible[7];
    fieldsState       = foldersVisible[8];
    methodsState      = foldersVisible[9];
    attributesState   = foldersVisible[10];

    magicNumberCheck.setSelected(magicNumberState);
    minorVersionCheck.setSelected(minorVersionState);
    majorVersionCheck.setSelected(majorVersionState);
    constantPoolCheck.setSelected(constantPoolState);
    accessFlagsCheck.setSelected(accessFlagsState);
    thisClassCheck.setSelected(thisClassState);
    superClassCheck.setSelected(superClassState);
    interfacesCheck.setSelected(interfacesState);
    fieldsCheck.setSelected(fieldsState);
    methodsCheck.setSelected(methodsState);
    attributesCheck.setSelected(attributesState);

    CheckBoxListener myListener = new CheckBoxListener();

    magicNumberCheck.addItemListener(myListener);
    minorVersionCheck.addItemListener(myListener);
    majorVersionCheck.addItemListener(myListener);
    constantPoolCheck.addItemListener(myListener);
    accessFlagsCheck.addItemListener(myListener);
    thisClassCheck.addItemListener(myListener);
    superClassCheck.addItemListener(myListener);
    interfacesCheck.addItemListener(myListener);
    fieldsCheck.addItemListener(myListener);
    methodsCheck.addItemListener(myListener);
    attributesCheck.addItemListener(myListener);
  }

  private boolean save() {
    boolean any = false;

    if(foldersVisible[0] != magicNumberState)  any = true;
    if(foldersVisible[1] != minorVersionState) any = true;
    if(foldersVisible[2] != majorVersionState) any = true;

    if(foldersVisible[3] != constantPoolState) any = true;

    if(foldersVisible[4] != accessFlagsState)  any = true;
    if(foldersVisible[5] != thisClassState)    any = true;
    if(foldersVisible[6] != superClassState)   any = true;

    if(foldersVisible[7] != interfacesState)   any = true;
    if(foldersVisible[8] != fieldsState)       any = true;
    if(foldersVisible[9] != methodsState)      any = true;
    if(foldersVisible[10] != attributesState)  any = true;

    foldersVisible[0]  = magicNumberState;
    foldersVisible[1]  = minorVersionState;
    foldersVisible[2]  = majorVersionState;
    foldersVisible[3]  = constantPoolState;
    foldersVisible[4]  = accessFlagsState;
    foldersVisible[5]  = thisClassState;
    foldersVisible[6]  = superClassState;
    foldersVisible[7]  = interfacesState;
    foldersVisible[8]  = fieldsState;
    foldersVisible[9]  = methodsState;
    foldersVisible[10] = attributesState;

    return any;
  }

  public boolean getAnswer() { return isOk; }

  public boolean isAnyChange() { return anyChanges; }

// inner class-adapter that listens state of checkbox
class CheckBoxListener implements ItemListener {
  public void itemStateChanged(ItemEvent e) {
    Object source = e.getItemSelectable();

    if(source == magicNumberCheck) {
      magicNumberState = magicNumberCheck.isSelected();
    }
    else if(source == minorVersionCheck) {
      minorVersionState = minorVersionCheck.isSelected();
    }
    else if(source == majorVersionCheck) {
      majorVersionState = majorVersionCheck.isSelected();
    }
    else if(source == constantPoolCheck) {
      constantPoolState = constantPoolCheck.isSelected();
    }
    else if(source == accessFlagsCheck) {
      accessFlagsState = accessFlagsCheck.isSelected();
    }
    else if(source == thisClassCheck) {
      thisClassState = thisClassCheck.isSelected();
    }
    else if(source == superClassCheck) {
      superClassState = superClassCheck.isSelected();
    }
    else if(source == interfacesCheck) {
      interfacesState = interfacesCheck.isSelected();
    }
    else if(source == fieldsCheck) {
      fieldsState = fieldsCheck.isSelected();
    }
    else if(source == methodsCheck) {
      methodsState = methodsCheck.isSelected();
    }
    else if(source == attributesCheck) {
      attributesState = attributesCheck.isSelected();
    }
  }

}

}
