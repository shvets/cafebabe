// HandlesTable.java

package org.sf.serfile;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * The instance of this class holds all allocated objects and their handles.
 *
 * @version 1.0 05/25/2000
 * @author Alexander Shvets
 */

public class HandlesTable {

  /** current handle */
  private int currHandle;

  /** a hashMap that keeps all handled objects */
  private Map hashMap = new HashMap();

  /**
   * Default constructor that creates handles table.
   */
  public HandlesTable() {}

  /**
   * Cleates handles table
   */
  public void clear() {
    currHandle = SerConstants.baseWireHandle;

    hashMap.clear();
  }

  /**
   * Puts new handled entry into hashMap.
   * Current handler will be incremented after that.
   *
   * @param entry handled entry to be added
   */
  public int put(HandleSerEntry entry) {
    int handle = currHandle++;

    hashMap.put(new Integer(handle), entry);

    return handle;
  }

  /**
   * Gets handled entry from hashMap.
   *
   * @param handle  a handle to an entry that is looked for
   * @return handle  handled entry
   */
  public HandleSerEntry get(int handle) {
    return (HandleSerEntry)hashMap.get(new Integer(handle));
  }

  /**
   * Gets the collection of all elements in handled table.
   *
   * @return  the collection of all elements in handled table
   */
  public Collection values() {
    return hashMap.values();
  }

}
