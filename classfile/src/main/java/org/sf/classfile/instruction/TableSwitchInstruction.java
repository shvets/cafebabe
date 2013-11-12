// TableSwitchInstruction.java

package org.sf.classfile.instruction;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

import org.sf.classfile.Pool;
import org.sf.classfile.Converter;

/**
 * This class is an implementation of Instruction interface for TABLESWITCH
 * instruction.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public class TableSwitchInstruction extends DefaultInstruction {

  /* default offset value */
  private int defaultOffset;

  /* low value */
  private int low;

  /* high value */
  private int high;

  /**
   * This constructor that creates TABLESWITCH instruction with a specified
   * tag value. Flag "wide" for this instruction is meaningless.
   *
   * @param tag  the tag value
   */
  public TableSwitchInstruction(byte tag) {
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
    low           = in.readInt();
    high          = in.readInt();

    if(high > 0) {
      int restLength = (high-low+1)*4;

      buffer = new byte[restLength];

      in.readFully(buffer);
    }
  }

  /**
   * Writes this instruction to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    out.writeInt(defaultOffset);
    out.writeInt(low);
    out.writeInt(high);

    out.write(buffer, 0, buffer.length);
  }

  /**
   * Gets the length of instruction in bytes
   * (tag + default offset + low + high + size of buffer-parameter)
   *
   * @return  the length of instruction in bytes
   */
  public long length() {
    return (long)(1 + 4 + 4 + 4 + buffer.length);
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
   * Gets the low value
   *
   * @return the low value
   */
  public int getLow() {
    return low;
  }

  /**
   * Gets the high value
   *
   * @return the high value
   */
  public int getHigh() {
    return high;
  }

  /**
   * Resolves this entry.
   *
   * @param   constPool  the pool
   * @return  the resolved information in form of an array
   */
  public String resolve(Pool constPool) {
    StringBuffer sb = new StringBuffer(" ");

    for(int i=0; i < buffer.length/4; i++) {
      sb.append(Integer.toHexString(Converter.getInt(buffer, i)));
      sb.append(" ");
    }

    return "default: " + Integer.toHexString(defaultOffset) + "; " +
           "from: "    + Integer.toHexString(low) + "; " +
           "to: "      + Integer.toHexString(high) + "; " +
           "{" + sb.toString() + "}";
  }

  /**
   * Compares two Objects for equality. Two table switch instructions
   * will be equals if they both have the same tag value, wide flag,
   * parameter, default offset, lov and high values.
   *
   * @param   object  the reference object with which to compare.
   * @return  true if this object is the same as the obj
   *          argument; false otherwise.
   */
  public boolean equals(Object object) {
    if(object != null && object instanceof TableSwitchInstruction) {
      TableSwitchInstruction instruction = (TableSwitchInstruction)object;

      if(super.equals(instruction) &&
         this.defaultOffset == instruction.defaultOffset &&
         this.low == instruction.low &&
         this.high == instruction.high)
        return true;
    }

    return false;
  }

}

