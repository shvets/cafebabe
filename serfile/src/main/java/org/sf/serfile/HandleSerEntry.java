// HandleSerEntry.java

package org.sf.serfile;


/**
 * Abstact class for all handled serialized entries.
 *
 * @version 1.0 05/25/2000
 * @author Alexander Shvets
 */

public abstract class HandleSerEntry extends SerEntry {

  /** handle */
  protected int handle;

  /** handles table */
  protected HandlesTable handlesTable;

  /**
   * The default constructor that creates handled entry.
   * Handles table is equals null.
   *
   */
  public HandleSerEntry() {
    this(null);
  }

  /**
   * The default constructor that creates handled entry.
   *
   * @param handlesTable  table of handles
   */
  public HandleSerEntry(HandlesTable handlesTable) {
    this.handlesTable = handlesTable;
  }

  /**
   * Gets the handle for this entry.
   *
   * @return   the handle
   */
  public int getHandle() {
    return handle;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return  a string representation of the object.
   */
  public String toString() {
    return getType() + "(" + handle + ")";
  }

}
