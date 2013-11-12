// LookupSwitchInstruction.java

package org.sf.classfile.instruction;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

import org.sf.classfile.Pool;
import org.sf.classfile.Converter;

/**
 * This class is an implementation of Instruction interface for LOOKUPSWITCH
 * instruction.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public class LookupSwitchInstruction extends DefaultInstruction {
  /* default offset value */
  private int defaultOffset;

  /* default npairs value */
  private int npairs;

  /**
   * This constructor that creates LOOKUPSWITCH instruction with a specified
   * tag value. Flag "wide" for this instruction is meaningless.
   *
   * @param tag  the tag value
   */
  public LookupSwitchInstruction(byte tag) {
    super(tag, false);
  }

  /**
   * Reads this instruction from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException {
    defaultOffset = in.readInt();
    npairs = in.readInt();

    int restLength = npairs*(4 + 4);

    buffer = new byte[restLength];

    in.readFully(buffer);
  }

  /**
   * Writes this instruction to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    out.writeInt(defaultOffset);
    out.writeInt(npairs);

    out.write(buffer, 0, buffer.length);
  }

  /**
   * Gets the length of instruction in bytes
   * (tag + default offset + npairs + size of buffer-parameter)
   *
   * @return  the length of instruction in bytes
   */
  public long length() {
    return (long)(1 + 4 + 4 + buffer.length);
  }

  /**
   * Gets the default offset
   *
   * @return the default offset
   */
  public int getDefaultOffset() {
    return defaultOffset;
  }

  /**
   * Gets the npairs value
   *
   * @return the npairs value
   */
  public int getNpairs() {
    return npairs;
  }

  /**
   * Resolves this entry.
   *
   * @param   constPool  the pool
   * @return  the resolved information about this entry
   */
  public String resolve(Pool constPool) {
    StringBuffer sb = new StringBuffer(" ");

    for(int i=0; i < buffer.length/4; i+=2) {
      sb.append("<");
      sb.append(Integer.toHexString(Converter.getInt(buffer, i)));
      sb.append(" ");
      sb.append(Integer.toHexString(Converter.getInt(buffer, i+1)));
      sb.append(">");
      sb.append(" ");
    }

    return "default: " + Integer.toHexString(defaultOffset) + "; " +
           "num: "     + Integer.toHexString(npairs) + "; " +
           "{" + sb.toString() + "}";

  }

  /**
   * Compares two Objects for equality. Two lookup switch instructions
   * will be equals if they both have the same tag value, wide flag,
   * parameter, default offset and npairs values.
   *
   * @param   object  the reference object with which to compare.
   * @return  true if this object is the same as the obj
   *          argument; false otherwise.
   */
  public boolean equals(Object object) {
    if(object != null && object instanceof LookupSwitchInstruction) {
      LookupSwitchInstruction instruction = (LookupSwitchInstruction)object;

      if(super.equals(instruction) &&
         defaultOffset == instruction.defaultOffset &&
         npairs == instruction.npairs)
        return true;
    }

    return false;
  }

}

