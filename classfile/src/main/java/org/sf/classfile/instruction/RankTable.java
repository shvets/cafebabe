// RankTable.java

package org.sf.classfile.instruction;

/**
 * This class represents a table of ranks. In such manner we can classify
 * all instructions dividing them to predefined set of ranks.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public class RankTable {

  /** An array of all ranks in the table */
  protected Rank[] ranks;

  /** An array with descriptions for all ranks */
  protected String[] descriptions;

  /**
   * A default constructor that empty rank table
   */
  public RankTable() {}

  /**
   * Gets descriptions for all ranks.
   *
   * @return  array of descriptions for all ranks
   */
  public String[] descriptions() {
    return descriptions;
  }

  /**
   * Finds the rank for a given description.
   *
   * @return  the corresponding rank
   */
  public Rank getRank(String description) {
    Rank rank = null;

    for(int i=0; i < ranks.length; i++) {
      if(ranks[i].key.equals(description)) {
        rank = ranks[i];
        break;
      }
    }

    return rank;
  }

  /**
   * Finds the rank for a given opcode.
   *
   * @return  the corresponding rank
   */
  public Rank getRank(byte opcode) {
    for(int i=0; i < ranks.length; i++) {
      byte[] opcodes = ranks[i].opcodes;

      for(int j=0; j < opcodes.length; j++) {
        if(opcodes[j] == opcode) {
          return ranks[i];
        }
      }
    }

    return null;
  }

}
