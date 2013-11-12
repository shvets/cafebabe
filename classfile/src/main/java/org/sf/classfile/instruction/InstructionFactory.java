// InstructionFactory.java

package org.sf.classfile.instruction;

/**
 * This helps to create different type of instructions. It hides differences
 * in creation process between DefaultInstruction, TableSwitchInstruction
 * and LookupSwitchInstruction. This clas is an example of Factory Pattern.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public class InstructionFactory {

  /* Disables creation of an instance for this class, because it is simple
   * container for static function.
   */
  private InstructionFactory() {}

  /**
   * Creates new instruction with a specified tag value and wide flag.
   *
   * @param tag  the tag value
   * @param wide  the wide flag
   * @ return  created instruction
   */
  public static Instruction create(byte tag, boolean wide) {
    Instruction instruction = null;

    if(tag == Opcode.TABLESWITCH) {
      instruction = new TableSwitchInstruction(tag);
    }
    else if(tag == Opcode.LOOKUPSWITCH) {
      instruction = new LookupSwitchInstruction(tag);
    }
    else {
      instruction = new DefaultInstruction(tag, wide);
    }

    return instruction;
  }

}
