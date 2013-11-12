// MethodBody.java

package org.sf.classfile.attribute;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;
import java.util.List;
import java.util.ArrayList;

import org.sf.classfile.Entry;
import org.sf.classfile.instruction.Instruction;
import org.sf.classfile.instruction.InstructionFactory;
import org.sf.classfile.instruction.Opcode;

/**
 * Class that the body of method, i.e. a sequence of JVM instructions.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public final class MethodBody extends Entry {

  public static final String TYPE = "MethodBody";

  /* Check if method's body has some changes */
  private boolean isBodyChanged = false;

  /* The list of instructions for this method */
  private List instructions = new ArrayList();

  /* length of method body in bytes */
  private int length;

  /*
   * Creates a method body with the specified space for instructions
   *
   * @param length the length of method's body in bytes
   */
  public MethodBody(int length) {
    this.length = length;
  }

  /**
   * Reads this entry from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException {
    int ic = 0;

    while(ic < length) {
      boolean wide = false;
      byte tag = in.readByte();
      ic++;

      if(tag == Opcode.WIDE) { // Read next tag after wide byte
        wide = true;
        tag  = in.readByte();
        ic++;
      }

      if(tag == Opcode.TABLESWITCH || tag == Opcode.LOOKUPSWITCH) {
        for(;(ic % 4) != 0; ic++) {
          in.readByte(); // skip padding
        }
      }

      Instruction instruction = InstructionFactory.create(tag, wide);

      instruction.read(in);

      instructions.add(instruction);

      ic += instruction.length()-1;
    }
  }

  /**
   * Writes this entry to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    for(int i=0, ic=0; i < instructions.size(); i++) {
      Instruction instruction = (Instruction)instructions.get(i);

      byte tag     = instruction.getTag();
      boolean wide = instruction.isWide();

      if(wide) {
        out.writeByte(Opcode.WIDE);
        ic++;
      }

      out.writeByte(tag);
      ic++;

      if(tag == Opcode.TABLESWITCH || tag == Opcode.LOOKUPSWITCH) {
        for(;(ic % 4) != 0; ic++) {
          out.writeByte((byte)0);
        }
      }

      instruction.write(out);
      ic += instruction.length()-1;
    }
  }

  /**
   * Gets the type of this entry.
   *
   * @return  the type of entry
   */
  public String getType() {
    return TYPE;
  }

  /**
   * Length of this entry in bytes
   *
   * @return  length of entry.
   */
  public long length() {
    return length;
  }

  /**
   * Gets an instruction from a body.
   *
   * @param index  the index of instruction in the list of instructions
   * @return an instruction that occupies the specified index
   */
  public Instruction getInstruction(int index) {
    return (Instruction)instructions.get(index);
  }

  /**
   * Sets an instruction to a body at the specified index.
   *
   * @param instruction an instruction that should be setted up at the specified index
   * @param index  the index
   */
  public void setInstruction(Instruction instruction, int index) {
    instructions.set(index, instruction);

    calculateLength();
    isBodyChanged = true;
  }

  /**
   * Inserts (adds) an instruction after the specified index.
   *
   * @param instruction an instruction that will be added
   * @param index  the index
   */
  public void insertInstruction(Instruction instruction, int index) {
    instructions.add(index, instruction);

    calculateLength();
    isBodyChanged = true;
  }

  /**
   * Removes an instruction from instructions list.
   *
   * @param instruction an instruction that will be removed
   */
  public void removeInstruction(Instruction instruction) {
    instructions.remove(instruction);

    calculateLength();
    isBodyChanged = true;
  }

  /**
   * Checks if body of method hase some changes.
   *
   * @return  true if body of method hase some changes; false - otherwise
   */
  public boolean isBodyChanged() {
    return isBodyChanged;
  }

  /**
   * Gets the list of instructions for this body.
   *
   * @return  list of instructions
   */
  public Instruction[] getInstructions() {
    Instruction[] array = new Instruction[instructions.size()];

    instructions.toArray(array);

    return array;
  }

  /**
   * Sets list of instructions for this body.
   *
   * @param  array list of instructions
   */
  public void setInstructions(Instruction[] array) {
    instructions.clear();

    for(int i=0; i < array.length; i++) {
      instructions.add(array[i]);
    }

    calculateLength();

    isBodyChanged = true;
  }

  /**
   * The number of instructions in instructions list
   *
   * @return  the number of instructions in instructions list
   */
  public int size() {
    return instructions.size();
  }

  /* Calculates length for each instruction in instructions list after any
   * change in this list.
   */
  private void calculateLength() {
    int sz = 0;
    for(int i=0; i < instructions.size(); i++) {
      Instruction instruction = (Instruction)instructions.get(i);

      byte tag = instruction.getTag();
      boolean wide = instruction.isWide();

      if(wide) {
        sz++;
      }

      sz++;

      if((tag & 0xff) == Opcode.TABLESWITCH ||
         (tag & 0xff) == Opcode.LOOKUPSWITCH) {
        for(;(sz % 4) != 0; sz++);
      }

      sz += instruction.length()-1;
    }

    length = sz;
  }

}
