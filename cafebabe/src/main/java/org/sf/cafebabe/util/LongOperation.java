// LongOperation.java

package org.sf.cafebabe.util;

/**
 * This class represents some long operation as Thread.
 * It allows correctly kill such task. Additionally it produces
 * event ActionEvent each delay mc for registered listener ActionListener.
 */
public abstract class LongOperation extends Thread {
  public final static int OK_OPERATION     = 0;
  public final static int CANCEL_OPERATION = 1;
  public final static int ERROR_OPERATION  = 2;

  private int resultType;
  private String resultMessage;

  protected int delay;

  public LongOperation(int delay) {
    this.delay = delay;
    setPriority(Thread.MIN_PRIORITY+2);
  }

  public void startLoading() {
    start();
  }

  public void stopLoading() {}

  public void run() {
    try {
      execute();
      resultType = OK_OPERATION;
      resultMessage = null;
      stopLoading();
    }
    catch(Exception e) {
      e.printStackTrace();
      resultType = ERROR_OPERATION;
      resultMessage = e.toString();
      stopLoading();
    }
    catch(ThreadDeath td) { // !
      resultType = CANCEL_OPERATION;
      resultMessage = null;
      stopLoading();
      throw td;
    }
  }

  protected abstract void execute() throws Exception;

  public int getResultType() {
    return resultType;
  }

  public String getResultMessage() {
    return resultMessage;
  }

}
