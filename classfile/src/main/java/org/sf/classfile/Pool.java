// Pool.java

package org.sf.classfile;

/**
 * Interface for presentation the collection of detailed information
 * about entities,
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public interface Pool {

  /**
   * Gets specified entry from a pool.
   *
   * @param   index  the index of entry in a pool.
   * @return  specified by index entry
   */
  public PoolEntry getEntry(short index);

}
