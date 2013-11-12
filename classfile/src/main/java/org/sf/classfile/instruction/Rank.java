// Rank.java

package org.sf.classfile.instruction;

/**
 * This class corresponds to a single record in a table of ranks. This table is
 * like a hashtable with an array of ints as a second parameter.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public class Rank {

  /** the key value */
  public final String key;

  /** opcodes that correspond to key value */
  public final byte[] opcodes;

  /**
   * This constructor creates a rank object with a specified
   * key and corresponding array of opcodes.
   *
   * @param key  the key of the pair
   * @param opcodes opcodes that correspond trhe key in the pair
   */
  public Rank(String key, byte[] opcodes) {
    this.key     = key;
    this.opcodes = opcodes;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return  a string representation of the object.
   */
  public String toString() {
    return key;
  }
}

