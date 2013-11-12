// TimerLongOperation.java

package org.sf.cafebabe.util;

import java.awt.event.*;
import javax.swing.*;

/**
 * This class represents some long operation as Thread.
 * It allows correctly kill such task. Additionally it produces
 * event ActionEvent each delay mc for registered listener ActionListener.
 */
public abstract class TimerLongOperation extends LongOperation {

  private Timer timer;
  private ActionListener listener;

  public TimerLongOperation(int delay, ActionListener listener) {
    super(delay);

    this.listener = listener;
  }

  public void startLoading() {
    if(timer == null) {
      timer = new Timer(delay, listener);
      timer.start();
    }

    super.startLoading();
  }

  public void stopLoading() {
    if(timer != null) {
      timer.stop();
      timer = null;
    }

    super.stopLoading();
  }

}
