// RefOpcodeDescription.java

package org.sf.classfile.instruction;

/**
 * This class describes single record of OpcodeTable that uses reference as
 * an additional parameter.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public class RefOpcodeDescription extends OpcodeDescription {

  /**
   * Creates a reference description with a specified mnemonic and length for
   * instruction.
   *
   * @param mnemonic  the tag value
   * @param length  the wide flag
   */
  public RefOpcodeDescription(String mnemonic, int length) {
    super(mnemonic, length);
  }

}
