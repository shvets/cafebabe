// DefaultInstruction.java

package org.sf.classfile.instruction;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

import org.sf.classfile.Pool;
import org.sf.classfile.PoolEntry;
import org.sf.classfile.Converter;

/**
 * This class is a default implementation of Instruction interface. It is
 * suitable for the most instruction except 2 switch instructions:
 * TABLESWITCH and LOOKUPSWITCH. They bot should be interpreted in a special
 * fasion (see TableSwitchInstruction and LookupSwitchInstruction).
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public class DefaultInstruction extends Instruction implements Opcode {
  public static final String TYPE = "Instruction";

  /* a parameter for this instruction */
  protected byte[] buffer = new byte[0];

  /* Opcode number */
  protected byte tag;

  /*  wide flag */
  protected boolean wide;

  /**
   * This constructor that creates default instruction with a specified
   * tag value and wide flag.
   *
   * @param tag  the tag value
   * @param wide  the wide flag
   */
  public DefaultInstruction(byte tag, boolean wide) {
    this.tag  = tag;
    this.wide = wide;

    int length = OpcodeTable.getDescription(tag).length;

    if(length > 1) {
      int restLength = length-1;

      if(wide) {
        restLength *= 2;
      }

      buffer = new byte[restLength];
    }
  }

  /**
   * Reads this instruction from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException {
    in.readFully(buffer);
  }

  /**
   * Writes this instruction to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    out.write(buffer, 0, buffer.length);
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
   * Get the length of instruction in bytes
   *
   * @return  the length of instruction in bytes
   */
  public long length() {
    return (long)OpcodeTable.getDescription(tag).length;
  }

  /**
   * Get the tag
   *
   * @return the tag for this instruction
   */
  public byte getTag() {
    return tag;
  }

  /**
   * Get the mnemonic
   *
   * @return the mnemonic for this instruction
   */
  public String getMnemonic() {
    OpcodeDescription description = OpcodeTable.getDescription(tag);

    String mnemonic = description.mnemonic;

    if(wide) {
      mnemonic += "_w";
    }

    return mnemonic;
  }

  /**
   * Gets the parameter for this instruction.
   *
   * @return the parameter in form of a byte array
   */
  public byte[] getParameter() {
    return buffer;
  }

  /**
   * Checks is this instruction is wide
   *
   * @return true if is this instruction is wide; false otherwise
   */
  public boolean isWide() {
    return wide;
  }

  /**
   * Resolves this entry.
   *
   * @param   constPool  the pool
   * @return  the resolved information about this entry
   */
  public String resolve(Pool constPool) {
    if(buffer == null) {
      return getMnemonic();
    }

    StringBuffer params = new StringBuffer();

    if(wide) {
      switch(tag) {
        case ILOAD : case LLOAD : case FLOAD: case DLOAD : case ALOAD :
        case ISTORE: case LSTORE: case FSTORE: case DSTORE: case ASTORE:
        case RET:
          params.append(Converter.toHexString((short)Converter.getShort(buffer, 0), ""));
          break;

        case IINC:
          params.append(Converter.toHexString(Converter.getInt(buffer, 0), ""));
          break;
        default:
          params.append("Invalid opcode");
          break;
      }
    }
    else {
      switch(tag) {
        case ILOAD : case LLOAD : case FLOAD: case DLOAD : case ALOAD :
        case ISTORE: case LSTORE: case FSTORE: case DSTORE: case ASTORE:
        case RET:
          params.append(Converter.toHexString((byte)Converter.getByte(buffer, 0)));
          break;

        case IINC:
          params.append(Converter.toHexString((short)Converter.getShort(buffer, 0), ""));
          break;

        case SIPUSH:
          params.append(Converter.toHexString((short)Converter.getShort(buffer, 0), ""));
          break;

        case BIPUSH:
          params.append(Converter.toHexString((byte)Converter.getByte(buffer, 0)));
          break;

        case IFEQ: case IFNE: case IFLT: case IFGE: case IFGT: case IFLE:
        case IF_ICMPEQ: case IF_ICMPNE: case IF_ICMPLT:
        case IF_ICMPGE: case IF_ICMPGT: case IF_ICMPLE:
        case IF_ACMPEQ: case IF_ACMPNE: case GOTO:
        case JSR: case IFNULL: case IFNONNULL:
          params.append(Converter.toHexString((short)Converter.getShort(buffer, 0)));
          break;

        case GOTO_W: case JSR_W:
          params.append(Converter.toHexString(Converter.getInt(buffer, 0)));
          break;

        case NEWARRAY:
          switch(Converter.getByte(buffer, 0)) {
            case 4 : params.append("boolean");    break;
            case 5 : params.append("char");       break;
            case 6 : params.append("float");      break;
            case 7 : params.append("double");     break;
            case 8 : params.append("byte");       break;
            case 9 : params.append("short");      break;
            case 10: params.append("int");        break;
            case 11: params.append("long");       break;
            default: params.append("BOGUS-TYPE"); break;
          }
          break;

        case LDC_W: case LDC2_W:
        case GETSTATIC: case PUTSTATIC: case GETFIELD: case PUTFIELD:
        case INVOKEVIRTUAL: case INVOKESPECIAL: case INVOKESTATIC:
        case NEW:
        case CHECKCAST: case INSTANCEOF:
          {
            PoolEntry entry = constPool.getEntry((short)Converter.getShort(buffer, 0));
            params.append(entry.resolve(constPool));
          }
          break;

        case ANEWARRAY:
          {
            PoolEntry entry = constPool.getEntry((short)Converter.getShort(buffer, 0));
            params.append(entry.resolve(constPool));
          }
          break;

        case LDC:
          {
            PoolEntry entry = constPool.getEntry((short)Converter.getByte(buffer, 0));
            params.append(entry.resolve(constPool));
          }
          break;

        case INVOKEINTERFACE:
          {
            PoolEntry entry = constPool.getEntry((short)Converter.getShort(buffer, 0));
            params.append(entry.resolve(constPool) +
                          " args " + (byte)Converter.getByte(buffer, 2));
          }
          break;

        case MULTIANEWARRAY:
          {
            short i1 = (short)Converter.getShort(buffer, 0);
            int i2   = Converter.getByte(buffer, 2);

            PoolEntry entry1 = constPool.getEntry(i1);

            params.append(entry1.resolve(constPool) + " dim " + i2);
          }
          break;

        default:
          break;
      }
    }

    if(params.length() == 0) {
      return getMnemonic();
    }

    return getMnemonic() + " " + params.toString();
  }

  /**
   * Compares two Objects for equality. Two default instructionss
   * will be equals if they both have the same tag value, wide flag
   * and parameter.
   *
   * @param   object  the reference object with which to compare.
   * @return  true if this object is the same as the obj
   *          argument; false otherwise.
   */
  public boolean equals(Object object) {
    if(object != null && object instanceof DefaultInstruction) {
      DefaultInstruction instruction = (DefaultInstruction)object;

      if(this.getTag() == instruction.getTag() &&
         this.isWide() == instruction.isWide() &&
         eq(this.getParameter(), instruction.getParameter()))
        return true;
    }

    return false;
  }

  private static boolean eq(byte[] buffer1, byte[] buffer2) {
    int lenght1 = buffer1.length;
    int lenght2 = buffer2.length;

    if(lenght1 != lenght2)
      return false;

    for(int i=0; i < lenght1; i++) {
      if(buffer1[i] != buffer2[i])
        return false;
    }

    return true;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return  a string representation of the object.
   */
  public String toString() {
    return getMnemonic() + "(" + "length:" + length() + ")";
  }

}

