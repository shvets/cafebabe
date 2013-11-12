// Tagged.java

package org.sf.serfile;

/**
 * Basic interface for any entity that can have a tag.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public interface Tagged {

  /**
   * Gets the tag of the entity.
   *
   * @return  the tag for the entity
   */
  public byte getTag();

}
