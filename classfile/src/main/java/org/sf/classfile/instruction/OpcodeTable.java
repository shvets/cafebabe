// OpcodeTable.java

package org.sf.classfile.instruction;

/**
 * This class represents a table for descriptions of all JVM instructions.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public class OpcodeTable implements Opcode {
  /* An opcode table */
  private static OpcodeDescription[] table = new OpcodeDescription[256];

  /* Disable creation of any instance of this class - it is only a container
   * for static methods.
   */
  private OpcodeTable() {}

  /**
   * Gets a description of specified instruction
   *
   * @param  opcode the opcode of instruction
   * @return  an instruction that corresponds to a given opcode
   */
  public static OpcodeDescription getDescription(byte opcode) {
    return table[opcode & 0xff];
  }

  /**
   * Gets an opcode of specified instruction
   *
   * @param  mnemonic the mnemonic of instruction
   * @return  the opcode of instruction
   */
  public static byte getOpcode(String mnemonic) {
    for(byte i=0; i < table.length; i++) {
      OpcodeDescription description = table[i & 0xff];

      if(description.mnemonic.equals(mnemonic)) {
        return i;
      }
    }

    return (byte)0xff;
  }

  /**
   * Gets the opcode table.
   *
   * @return  Get the opcode table
   */
  public static OpcodeDescription[] getTable() {
    return table;
  }

  /* This list contains instructions that support wide mode.
   * wide instruction is placed in front of an instruction that
   * accesses a local variable.
   */
  private static byte[] wideSupport = new byte[] {
    ILOAD, LLOAD, FLOAD, DLOAD, ALOAD,
    ISTORE, LSTORE, FSTORE, DSTORE, ASTORE,
    RET,
    IINC
  };

  /* this list contains conditional instructions */
  private static byte[] conditions = new byte[] {
    IFEQ, IFNE, IFLT, IFGE, IFGT, IFLE,
    IF_ICMPEQ, IF_ICMPNE, IF_ICMPLT, IF_ICMPGE, IF_ICMPGT, IF_ICMPLE,
    IF_ACMPEQ, IF_ACMPNE,
    GOTO, JSR, RET,
    IFNULL, IFNONNULL, GOTO_W, JSR_W,
  };

  /* simple seek in the list */
  private static boolean seek(byte opcode, byte[] list) {
    for(byte i=0; i < list.length; i++) {
      if(list[i] == opcode) {
        return true;
      }
    }

    return false;
  }

  /**
   * Checks if specified instruction support wide mode.
   *
   * @param tag the tag for instruction
   * @return  true if instruction supports wide mode; false otherwise.
   */
  public static boolean isSupportWideMode(byte tag) {
    return seek(tag, wideSupport);
  }

  /**
   * Checks if specified instruction belongs to conditions.
   *
   * @param tag the tag for instruction
   * @return  true if instruction  belongs to conditions; false otherwise.
   */
  public static boolean isCondition(byte tag) {
    return seek(tag, conditions);
  }

/* initialization of opcode table */
static {
  table[NOP & 0xff]         = new OpcodeDescription("nop", 1);
  table[ACONST_NULL & 0xff] = new OpcodeDescription("aconst_null", 1);

  table[ICONST_M1 & 0xff]   = new OpcodeDescription("iconst_m1", 1);

  table[ICONST_0 & 0xff]    = new OpcodeDescription("iconst_0", 1);
  table[ICONST_1 & 0xff]    = new OpcodeDescription("iconst_1", 1);
  table[ICONST_2 & 0xff]    = new OpcodeDescription("iconst_2", 1);
  table[ICONST_3 & 0xff]    = new OpcodeDescription("iconst_3", 1);
  table[ICONST_4 & 0xff]    = new OpcodeDescription("iconst_4", 1);
  table[ICONST_5 & 0xff]    = new OpcodeDescription("iconst_5", 1);

  table[LCONST_0 & 0xff]    = new OpcodeDescription("lconst_0", 1);
  table[LCONST_1 & 0xff]    = new OpcodeDescription("lconst_1", 1);

  table[FCONST_0 & 0xff]    = new OpcodeDescription("fconst_0", 1);
  table[FCONST_1 & 0xff]    = new OpcodeDescription("fconst_1", 1);
  table[FCONST_2 & 0xff]    = new OpcodeDescription("fconst_2", 1);

  table[DCONST_0 & 0xff]    = new OpcodeDescription("dconst_0", 1);
  table[DCONST_1 & 0xff]    = new OpcodeDescription("dconst_1", 1);

  table[BIPUSH & 0xff]      = new OpcodeDescription("bipush", 2);
  table[SIPUSH & 0xff]      = new OpcodeDescription("sipush", 3);

  table[LDC & 0xff]         = new RefOpcodeDescription("ldc", 2);
  table[LDC_W & 0xff]       = new RefOpcodeDescription("ldc_w", 3);
  table[LDC2_W & 0xff]      = new RefOpcodeDescription("ldc2_w", 3);

  table[ILOAD & 0xff]       = new OpcodeDescription("iload", 2);
  table[LLOAD & 0xff]       = new OpcodeDescription("lload", 2);
  table[FLOAD & 0xff]       = new OpcodeDescription("fload", 2);
  table[DLOAD & 0xff]       = new OpcodeDescription("dload", 2);
  table[ALOAD & 0xff]       = new OpcodeDescription("aload", 2);

  table[ILOAD_0 & 0xff]     = new OpcodeDescription("iload_0", 1);
  table[ILOAD_1 & 0xff]     = new OpcodeDescription("iload_1", 1);
  table[ILOAD_2 & 0xff]     = new OpcodeDescription("iload_2", 1);
  table[ILOAD_3 & 0xff]     = new OpcodeDescription("iload_3", 1);

  table[LLOAD_0 & 0xff]     = new OpcodeDescription("lload_0", 1);
  table[LLOAD_1 & 0xff]     = new OpcodeDescription("lload_1", 1);
  table[LLOAD_2 & 0xff]     = new OpcodeDescription("lload_2", 1);
  table[LLOAD_3 & 0xff]     = new OpcodeDescription("lload_3", 1);

  table[FLOAD_0 & 0xff]     = new OpcodeDescription("fload_0", 1);
  table[FLOAD_1 & 0xff]     = new OpcodeDescription("fload_1", 1);
  table[FLOAD_2 & 0xff]     = new OpcodeDescription("fload_2", 1);
  table[FLOAD_3 & 0xff]     = new OpcodeDescription("fload_3", 1);

  table[DLOAD_0 & 0xff]     = new OpcodeDescription("dload_0", 1);
  table[DLOAD_1 & 0xff]     = new OpcodeDescription("dload_1", 1);
  table[DLOAD_2 & 0xff]     = new OpcodeDescription("dload_2", 1);
  table[DLOAD_3 & 0xff]     = new OpcodeDescription("dload_3", 1);

  table[ALOAD_0 & 0xff]     = new OpcodeDescription("aload_0", 1);
  table[ALOAD_1 & 0xff]     = new OpcodeDescription("aload_1", 1);
  table[ALOAD_2 & 0xff]     = new OpcodeDescription("aload_2", 1);
  table[ALOAD_3 & 0xff]     = new OpcodeDescription("aload_3", 1);

  table[IALOAD & 0xff]      = new OpcodeDescription("iaload", 1);
  table[LALOAD & 0xff]      = new OpcodeDescription("laload", 1);
  table[FALOAD & 0xff]      = new OpcodeDescription("faload", 1);
  table[DALOAD & 0xff]      = new OpcodeDescription("daload", 1);
  table[AALOAD & 0xff]      = new OpcodeDescription("aaload", 1);
  table[BALOAD & 0xff]      = new OpcodeDescription("baload", 1);
  table[CALOAD & 0xff]      = new OpcodeDescription("caload", 1);
  table[SALOAD & 0xff]      = new OpcodeDescription("saload", 1);

  table[ISTORE & 0xff]      = new OpcodeDescription("istore", 2);
  table[LSTORE & 0xff]      = new OpcodeDescription("lstore", 2);
  table[FSTORE & 0xff]      = new OpcodeDescription("fstore", 2);
  table[DSTORE & 0xff]      = new OpcodeDescription("dstore", 2);
  table[ASTORE & 0xff]      = new OpcodeDescription("astore", 2);

  table[ISTORE_0 & 0xff]    = new OpcodeDescription("istore_0", 1);
  table[ISTORE_1 & 0xff]    = new OpcodeDescription("istore_1", 1);
  table[ISTORE_2 & 0xff]    = new OpcodeDescription("istore_2", 1);
  table[ISTORE_3 & 0xff]    = new OpcodeDescription("istore_3", 1);

  table[LSTORE_0 & 0xff]    = new OpcodeDescription("lstore_0", 1);
  table[LSTORE_1 & 0xff]    = new OpcodeDescription("lstore_1", 1);
  table[LSTORE_2 & 0xff]    = new OpcodeDescription("lstore_2", 1);
  table[LSTORE_3 & 0xff]    = new OpcodeDescription("lstore_3", 1);

  table[FSTORE_0 & 0xff]    = new OpcodeDescription("fstore_0", 1);
  table[FSTORE_1 & 0xff]    = new OpcodeDescription("fstore_1", 1);
  table[FSTORE_2 & 0xff]    = new OpcodeDescription("fstore_2", 1);
  table[FSTORE_3 & 0xff]    = new OpcodeDescription("fstore_3", 1);

  table[DSTORE_0 & 0xff]    = new OpcodeDescription("dstore_0", 1);
  table[DSTORE_1 & 0xff]    = new OpcodeDescription("dstore_1", 1);
  table[DSTORE_2 & 0xff]    = new OpcodeDescription("dstore_2", 1);
  table[DSTORE_3 & 0xff]    = new OpcodeDescription("dstore_3", 1);

  table[ASTORE_0 & 0xff]    = new OpcodeDescription("astore_0", 1);
  table[ASTORE_1 & 0xff]    = new OpcodeDescription("astore_1", 1);
  table[ASTORE_2 & 0xff]    = new OpcodeDescription("astore_2", 1);
  table[ASTORE_3 & 0xff]    = new OpcodeDescription("astore_3", 1);

  table[IASTORE & 0xff]     = new OpcodeDescription("iastore", 1);
  table[LASTORE & 0xff]     = new OpcodeDescription("lastore", 1);
  table[FASTORE & 0xff]     = new OpcodeDescription("fastore", 1);
  table[DASTORE & 0xff]     = new OpcodeDescription("dastore", 1);
  table[AASTORE & 0xff]     = new OpcodeDescription("aastore", 1);
  table[BASTORE & 0xff]     = new OpcodeDescription("bastore", 1);
  table[CASTORE & 0xff]     = new OpcodeDescription("castore", 1);
  table[SASTORE & 0xff]     = new OpcodeDescription("sastore", 1);

  table[POP & 0xff]         = new OpcodeDescription("pop", 1);
  table[POP2 & 0xff]        = new OpcodeDescription("pop2", 1);

  table[DUP & 0xff]         = new OpcodeDescription("dup", 1);
  table[DUP_X1 & 0xff]      = new OpcodeDescription("dup_x1", 1);
  table[DUP_X2 & 0xff]      = new OpcodeDescription("dup_x2", 1);
  table[DUP2 & 0xff]        = new OpcodeDescription("dup2", 1);
  table[DUP2_X1 & 0xff]     = new OpcodeDescription("dup2_x1", 1);
  table[DUP2_X2 & 0xff]     = new OpcodeDescription("dup2_x2", 1);
  table[SWAP & 0xff]        = new OpcodeDescription("swap", 1);

  table[IADD & 0xff]        = new OpcodeDescription("iadd", 1);
  table[LADD & 0xff]        = new OpcodeDescription("ladd", 1);
  table[FADD & 0xff]        = new OpcodeDescription("fadd", 1);
  table[DADD & 0xff]        = new OpcodeDescription("dadd", 1);

  table[ISUB & 0xff]        = new OpcodeDescription("isub", 1);
  table[LSUB & 0xff]        = new OpcodeDescription("lsub", 1);
  table[FSUB & 0xff]        = new OpcodeDescription("fsub", 1);
  table[DSUB & 0xff]        = new OpcodeDescription("dsub", 1);

  table[IMUL & 0xff]        = new OpcodeDescription("imul", 1);
  table[LMUL & 0xff]        = new OpcodeDescription("lmul", 1);
  table[FMUL & 0xff]        = new OpcodeDescription("fmul", 1);
  table[DMUL & 0xff]        = new OpcodeDescription("dmul", 1);

  table[IDIV & 0xff]        = new OpcodeDescription("idiv", 1);
  table[LDIV & 0xff]        = new OpcodeDescription("ldiv", 1);
  table[FDIV & 0xff]        = new OpcodeDescription("fdiv", 1);
  table[DDIV & 0xff]        = new OpcodeDescription("ddiv", 1);

  table[IREM & 0xff]        = new OpcodeDescription("irem", 1);
  table[LREM & 0xff]        = new OpcodeDescription("lrem", 1);
  table[FREM & 0xff]        = new OpcodeDescription("frem", 1);
  table[DREM & 0xff]        = new OpcodeDescription("drem", 1);

  table[INEG & 0xff]        = new OpcodeDescription("ineg", 1);
  table[LNEG & 0xff]        = new OpcodeDescription("lneg", 1);
  table[FNEG & 0xff]        = new OpcodeDescription("fneg", 1);
  table[DNEG & 0xff]        = new OpcodeDescription("dneg", 1);

  table[ISHL & 0xff]        = new OpcodeDescription("ishl", 1);
  table[LSHL & 0xff]        = new OpcodeDescription("lshl", 1);

  table[ISHR & 0xff]        = new OpcodeDescription("ishr", 1);
  table[LSHR & 0xff]        = new OpcodeDescription("lshr", 1);

  table[IUSHR & 0xff]       = new OpcodeDescription("iushr", 1);
  table[LUSHR & 0xff]       = new OpcodeDescription("lushr", 1);

  table[IAND & 0xff]        = new OpcodeDescription("iand", 1);
  table[LAND & 0xff]        = new OpcodeDescription("land", 1);

  table[IOR & 0xff]         = new OpcodeDescription("ior", 1);
  table[LOR & 0xff]         = new OpcodeDescription("lor", 1);

  table[IXOR & 0xff]        = new OpcodeDescription("ixor", 1);
  table[LXOR & 0xff]        = new OpcodeDescription("lxor", 1);

  table[IINC & 0xff]        = new OpcodeDescription("iinc", 3);

  table[I2L & 0xff]         = new OpcodeDescription("i2l", 1);
  table[I2F & 0xff]         = new OpcodeDescription("i2f", 1);
  table[I2D & 0xff]         = new OpcodeDescription("i2d", 1);

  table[L2I & 0xff]         = new OpcodeDescription("l2i", 1);
  table[L2F & 0xff]         = new OpcodeDescription("l2f", 1);
  table[L2D & 0xff]         = new OpcodeDescription("l2d", 1);

  table[F2I & 0xff]         = new OpcodeDescription("f2i", 1);
  table[F2L & 0xff]         = new OpcodeDescription("f2l", 1);
  table[F2D & 0xff]         = new OpcodeDescription("f2d", 1);

  table[D2I & 0xff]         = new OpcodeDescription("d2i", 1);
  table[D2L & 0xff]         = new OpcodeDescription("d2l", 1);
  table[D2F & 0xff]         = new OpcodeDescription("d2f", 1);

  table[I2B & 0xff]         = new OpcodeDescription("i2b", 1);
  table[I2C & 0xff]         = new OpcodeDescription("i2c", 1);
  table[I2S & 0xff]         = new OpcodeDescription("i2s", 1);

  table[LCMP & 0xff]        = new OpcodeDescription("lcmp", 1);
  table[FCMPL & 0xff]       = new OpcodeDescription("fcmpl", 1);
  table[FCMPG & 0xff]       = new OpcodeDescription("fcmpg", 1);
  table[DCMPL & 0xff]       = new OpcodeDescription("dcmpl", 1);
  table[DCMPG & 0xff]       = new OpcodeDescription("dcmpg", 1);

  table[IFEQ & 0xff]        = new OpcodeDescription("ifeq", 3);
  table[IFNE & 0xff]        = new OpcodeDescription("ifne", 3);
  table[IFLT & 0xff]        = new OpcodeDescription("iflt", 3);
  table[IFGE & 0xff]        = new OpcodeDescription("ifge", 3);
  table[IFGT & 0xff]        = new OpcodeDescription("ifgt", 3);
  table[IFLE & 0xff]        = new OpcodeDescription("ifle", 3);

  table[IF_ICMPEQ & 0xff]   = new OpcodeDescription("if_icmpeq", 3);
  table[IF_ICMPNE & 0xff]   = new OpcodeDescription("if_icmpne", 3);
  table[IF_ICMPLT & 0xff]   = new OpcodeDescription("if_icmplt", 3);
  table[IF_ICMPGE & 0xff]   = new OpcodeDescription("if_icmpge", 3);
  table[IF_ICMPGT & 0xff]   = new OpcodeDescription("if_icmpgt", 3);
  table[IF_ICMPLE & 0xff]   = new OpcodeDescription("if_icmple", 3);

  table[IF_ACMPEQ & 0xff]   = new OpcodeDescription("if_acmpeq", 3);
  table[IF_ACMPNE & 0xff]   = new OpcodeDescription("if_acmpne", 3);

  table[GOTO & 0xff]        = new OpcodeDescription("goto", 3);
  table[JSR & 0xff]         = new OpcodeDescription("jsr", 3);
  table[RET & 0xff]         = new OpcodeDescription("ret", 2);
  table[TABLESWITCH & 0xff] = new OpcodeDescription("tableswitch", -1); // depends on alignment
  table[LOOKUPSWITCH & 0xff]= new OpcodeDescription("lookupswitch", -1); // depends on alignment

  table[IRETURN & 0xff]     = new OpcodeDescription("ireturn", 1);
  table[LRETURN & 0xff]     = new OpcodeDescription("lreturn", 1);
  table[FRETURN & 0xff]     = new OpcodeDescription("freturn", 1);
  table[DRETURN & 0xff]     = new OpcodeDescription("dreturn", 1);
  table[ARETURN & 0xff]     = new OpcodeDescription("areturn", 1);
  table[RETURN & 0xff]      = new OpcodeDescription("return", 1);

  table[GETSTATIC & 0xff]   = new RefOpcodeDescription("getstatic", 3);
  table[PUTSTATIC & 0xff]   = new RefOpcodeDescription("putstatic", 3);
  table[GETFIELD & 0xff]    = new RefOpcodeDescription("getfield", 3);
  table[PUTFIELD & 0xff]    = new RefOpcodeDescription("putfield", 3);

  table[INVOKEVIRTUAL & 0xff]    = new RefOpcodeDescription("invokevirtual", 3);
  table[INVOKESPECIAL & 0xff]    = new RefOpcodeDescription("invokespecial", 3);
  table[INVOKESTATIC & 0xff]     = new RefOpcodeDescription("invokestatic", 3);
  table[INVOKEINTERFACE & 0xff]  = new RefOpcodeDescription("invokeinterface", 5);
  table[XXXUNUSEDXXX & 0xff]     = new OpcodeDescription("***unused***", 1);

  table[NEW & 0xff]              = new RefOpcodeDescription("new", 3);
  table[NEWARRAY & 0xff]         = new RefOpcodeDescription("newarray", 2);
  table[ANEWARRAY & 0xff]        = new RefOpcodeDescription("anewarray", 3);
  table[ARRAYLENGTH & 0xff]      = new OpcodeDescription("arraylength", 1);

  table[ATHROW & 0xff]           = new OpcodeDescription("athrow", 1);
  table[CHECKCAST & 0xff]        = new RefOpcodeDescription("checkcast", 3);
  table[INSTANCEOF & 0xff]       = new RefOpcodeDescription("instanceof", 3);
  table[MONITORENTER & 0xff]     = new OpcodeDescription("monitorenter", 1);
  table[MONITOREXIT & 0xff]      = new OpcodeDescription("monitorexit", 1);

  table[WIDE & 0xff]             = new OpcodeDescription("wide", 1);
  table[MULTIANEWARRAY & 0xff]   = new RefOpcodeDescription("multianewarray", 4);
  table[IFNULL & 0xff]           = new OpcodeDescription("ifnull", 3);
  table[IFNONNULL & 0xff]        = new OpcodeDescription("ifnonnull", 3);
  table[GOTO_W & 0xff]           = new OpcodeDescription("goto_w", 5);
  table[JSR_W & 0xff]            = new OpcodeDescription("jsr_w", 5);

  //quick opcodes:
  table[LDC_QUICK & 0xff]                 = new RefOpcodeDescription("ldc_quick", 2);
  table[LDC_W_QUICK & 0xff]               = new RefOpcodeDescription("ldc_w_quick", 3);
  table[LDC2_W_QUICK & 0xff]              = new RefOpcodeDescription("ldc2_w_quick", 3);
  table[GETFIELD_QUICK & 0xff]            = new RefOpcodeDescription("getfield_quick", 3);
  table[PUTFIELD_QUICK & 0xff]            = new RefOpcodeDescription("putfield_quick", 3);
  table[GETFIELD2_QUICK & 0xff]           = new RefOpcodeDescription("getfield2_quick", 3);
  table[PUTFIELD2_QUICK & 0xff]           = new RefOpcodeDescription("putfield2_quick", 3);
  table[GETSTATIC_QUICK & 0xff]           = new RefOpcodeDescription("getstatic_quick", 3);
  table[PUTSTATIC_QUICK & 0xff]           = new RefOpcodeDescription("putstatic_quick", 3);
  table[GETSTATIC2_QUICK & 0xff]          = new RefOpcodeDescription("getstatic2_quick", 3);
  table[PUTSTATIC2_QUICK & 0xff]          = new RefOpcodeDescription("putstatic2_quick", 3);

  table[INVOKEVIRTUAL_QUICK & 0xff]       = new RefOpcodeDescription("invokevirtual_quick", 3);
  table[INVOKENONVIRTUAL_QUICK & 0xff]    = new RefOpcodeDescription("invokenonvirtual_quick", 3);
  table[INVOKESUPER_QUICK & 0xff]         = new RefOpcodeDescription("invokesuper_quick", 3);
  table[INVOKESTATIC_QUICK & 0xff]        = new RefOpcodeDescription("invokestatic_quick", 3);
  table[INVOKEINTERFACE_QUICK & 0xff]     = new RefOpcodeDescription("invokeinterface_quick", 5);
  table[INVOKEVIRTUALOBJECT_QUICK & 0xff] = new RefOpcodeDescription("invokevirtualobject_quick", 3);

  table[0xdc & 0xff]                      = new OpcodeDescription("***unused***", 1);

  table[NEW_QUICK & 0xff]                 = new RefOpcodeDescription("new_quick", 3);
  table[ANEWARRAY_QUICK & 0xff]           = new RefOpcodeDescription("anewarray_quick", 3);
  table[MULTIANEWARRAY_QUICK & 0xff]      = new RefOpcodeDescription("multianewarray_quick", 4);

  table[CHECKCAST_QUICK & 0xff]           = new RefOpcodeDescription("checkcast_quick", 3);
  table[INSTANCEOF_QUICK & 0xff]          = new RefOpcodeDescription("instanceof_quick", 3);
  table[INVOKEVIRTUAL_QUICK_W & 0xff]     = new RefOpcodeDescription("invokevirtual_quick_w", 3);
  table[GETFIELD_QUICK_W & 0xff]          = new RefOpcodeDescription("getfield_quick_w", 3);
  table[PUTFIELD_QUICK_W & 0xff]          = new RefOpcodeDescription("putfield_quick_w", 3);

  // Unused opcodes:
  table[0xe5]                      = new OpcodeDescription("***unused***", 1);
  table[0xe6]                      = new OpcodeDescription("***unused***", 1);
  table[0xe7]                      = new OpcodeDescription("***unused***", 1);
  table[0xe8]                      = new OpcodeDescription("***unused***", 1);
  table[0xe9]                      = new OpcodeDescription("***unused***", 1);
  table[0xea]                      = new OpcodeDescription("***unused***", 1);
  table[0xeb]                      = new OpcodeDescription("***unused***", 1);
  table[0xec]                      = new OpcodeDescription("***unused***", 1);
  table[0xed]                      = new OpcodeDescription("***unused***", 1);
  table[0xee]                      = new OpcodeDescription("***unused***", 1);
  table[0xef]                      = new OpcodeDescription("***unused***", 1);
  table[0xf0]                      = new OpcodeDescription("***unused***", 1);
  table[0xf1]                      = new OpcodeDescription("***unused***", 1);
  table[0xf2]                      = new OpcodeDescription("***unused***", 1);
  table[0xf3]                      = new OpcodeDescription("***unused***", 1);
  table[0xf4]                      = new OpcodeDescription("***unused***", 1);
  table[0xf5]                      = new OpcodeDescription("***unused***", 1);
  table[0xf6]                      = new OpcodeDescription("***unused***", 1);
  table[0xf7]                      = new OpcodeDescription("***unused***", 1);
  table[0xf8]                      = new OpcodeDescription("***unused***", 1);
  table[0xf9]                      = new OpcodeDescription("***unused***", 1);
  table[0xfa]                      = new OpcodeDescription("***unused***", 1);
  table[0xfb]                      = new OpcodeDescription("***unused***", 1);
  table[0xfc]                      = new OpcodeDescription("***unused***", 1);
  table[0xfd]                      = new OpcodeDescription("***unused***", 1);

  // Reserved opcodes:
  table[BREAKPOINT & 0xff]                = new OpcodeDescription("breakpoint", 1);
  table[IMPDEP1 & 0xff]                   = new OpcodeDescription("impdep1", 1);
  table[IMPDEP2 & 0xff]                   = new OpcodeDescription("impdep2", 1);

}

}

