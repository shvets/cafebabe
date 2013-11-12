// OpcodeDescription.java

package org.sf.classfile.instruction;

/**
 * This class describes single record of OpcodeTable.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public class OpcodeDescription {

  /** The mnemonic value */
  public final String mnemonic;

  /** The length of corresponding opcode */
  public final int length;

  /**
   * Creates a single description with a specified mnemonic and length for
   * instruction.
   *
   * @param mnemonic  the tag value
   * @param length  the wide flag
   */
  public OpcodeDescription(String mnemonic, int length) {
    this.mnemonic = mnemonic;
    this.length   = length;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return  a string representation of the object.
   */
  public String toString() {
    return mnemonic;
  }
}
