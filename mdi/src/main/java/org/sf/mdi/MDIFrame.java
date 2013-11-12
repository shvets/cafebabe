package org.sf.mdi;

import java.beans.VetoableChangeListener;
import java.beans.PropertyVetoException;
import java.beans.PropertyChangeEvent;

import javax.swing.JInternalFrame;
import javax.swing.JFrame;
import java.awt.Component;

/*
 *
 * @author Alexander Shvets
 * @version 1.0
 */
public class MDIFrame extends JInternalFrame {
  // support of listeners for event MDIEvent
//  private ArrayList listeners = new ArrayList();

  public MDIFrame() {
    this("");
  }

  public MDIFrame(String title) {
    super(title);

    setResizable(true);
    setClosable(true);
    setMaximizable(true);
    setIconifiable(true);

    this.setOpaque(true);
    this.setVisible(true);

    setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    addVetoableChangeListener(new VetoableChangeListener() {
      public void vetoableChange(PropertyChangeEvent pce)
                                 throws PropertyVetoException {
        if(pce.getPropertyName().equals(IS_CLOSED_PROPERTY)) {
          boolean changed = ((Boolean) pce.getNewValue()).booleanValue();
          if(changed) {
            if(canExit()) {

              /*JDesktopPane desktopPane = getDesktopPane();
              if(desktopPane != null) {
                desktopPane.remove(desktopPane.getIndexOf(MDIFrame.this));
                desktopPane.repaint();
                fireUpdatePerformed(this, getTitle(), false);
              }
              close();

              */

  //            fireUpdatePerformed(this, getTitle(), false);
            }
            else
              throw new PropertyVetoException("Cancelled", pce);
          }
        }
      }
    });
  }

  public void open() {
/*    JDesktopPane desktopPane = getDesktopPane();

    if(desktopPane != null) {
      addMDIListener((MDIListener)desktopPane);
      fireUpdatePerformed(this, getTitle(), true);
      desktopPane.moveToFront(this);
*/
      try {
        setSelected(true);
      }
      catch (java.beans.PropertyVetoException pve) {
        System.out.println("Could not select " + getTitle());
      }
//    }
  }

  public void close() {
    try {
      fireVetoableChange(JInternalFrame.IS_CLOSED_PROPERTY,
                         new Boolean(false), new Boolean(true));

      setClosed(true);
    }
    catch(PropertyVetoException e) {
      e.printStackTrace();
    }
  }

  /**
   * Checks whether this window could be closed.
   *
   * @return thue id this window could be closed; false otherwise
   */
  public boolean canExit() {
    return true;
  }

/*  public synchronized void addMDIListener(MDIListener l) {
    listeners.add(l);
  }
  public synchronized void removeMDIListener(MDIListener l) {
    listeners.remove(l);
  }

  // fire MDIEvent to registered listeners
  protected void fireUpdatePerformed(Object source, String itemName,
                                     boolean visible) {
    // 1. Creating event
    MDIEvent event = new MDIEvent(source, itemName, visible);

    // 2. Cloning of listeners list to eliminate race condition
    ArrayList ls = null;
    synchronized (this) {
      ls = (ArrayList)listeners.clone();
    }

    // 3. Notification of all registered listeners
    for(int i=0; i < ls.size(); i++) {
      MDIListener l = (MDIListener)ls.get(i);
      l.updatePerformed(event);
    }
  }
*/

  public JFrame getJFrame() {
    Component current = this.getParent();

    while(current != null && !(current instanceof JFrame)) {
      current = current.getParent();
    }

    return (JFrame)current;
  }

}