// Resolvable.java

package org.sf.classfile;

/**
 * This interface define an entity that could be resolved on base of
 * information from a pool.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public interface Resolvable {

  /**
   * Resolves this entity against the information from a pool.
   *
   * @param   pool  the pool with detailed information for this entity
   * @return  the resolved information about this entity
   */
  public String resolve(Pool pool);

}
