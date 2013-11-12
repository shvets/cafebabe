// Instruction.java

package org.sf.classfile.instruction;

import java.io.Serializable;

import org.sf.classfile.Entry;
import org.sf.classfile.Tagged;
import org.sf.classfile.Resolvable;

/**
 * This interface represents instruction behavoir. An instruction is
 * a single entry inside method body.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public abstract class Instruction extends Entry implements Tagged, Resolvable, Serializable {

  /**
   * Get a mnemonic
   *
   * @return a mnemonic for this instruction
   */
  public abstract String getMnemonic();

  /**
   * Gets a parameter in form of a byte array. This information occupies
   * a place after first byte of instruction (tag). If command has
   * a length = 1, then this parameter is null.
   *
   * @return a parameter in form of a byte array
   */
  public abstract byte[] getParameter();

  /**
   * Checks is this instruction is wide
   *
   * @return true if is this instruction is wide; false otherwise
   */
  public abstract boolean isWide();

}
