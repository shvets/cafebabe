// UsageRanking.java

package org.sf.classfile.instruction;

/**
 * This class represents a table of instructions divided to ranks on
 * usage approach. This is an example of Singleton Pattern.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public class UsageRanking implements Opcode {

  /** Constants for representing all ranks by usage */
  public static final String ARITHMETIC_RANK  = "Arithmetic";
  public static final String CONVERSION_RANK  = "Conversion";
  public static final String COMPARE_RANK     = "Compare";
  public static final String ARRAY_RANK       = "Array";
  public static final String LOCAL_VAR_RANK   = "Local variable";
  public static final String INVOKE_RANK      = "Invoke";
  public static final String FIELD_RANK       = "Field";
  public static final String JUMP_RANK        = "Jump";
  public static final String RETURN_RANK      = "Return";
  public static final String MEM_ALLOC_RANK   = "Memory allocation";
  public static final String PUSH_RANK        = "Push";
  public static final String STACK_MANIP_RANK = "Stack manipulation";
  public static final String SWITCH_RANK      = "Switch";
  public static final String OTHER_RANK       = "Other commands";
  public static final String QUICK_RANK       = "Quick";
  public static final String RESERVED_RANK    = "Reserved commands";


  /* An instance of a rank table */
  private static RankTable instance;

  /* Initialization of a rank table */
  private static void init() {
    instance.ranks = new Rank[16];
    instance.descriptions = new String[instance.ranks.length];

    instance.ranks[0] = new Rank(ARITHMETIC_RANK, new byte[] {
                IADD, LADD, FADD, DADD, // 1
                ISUB, LSUB, FSUB, DSUB, // 1
                IMUL, LMUL, FMUL, DMUL, // 1
                IDIV, LDIV, FDIV, DDIV, // 1
                IREM, LREM, FREM, DREM, // 1
                INEG, LNEG, FNEG, DNEG, // 1
                ISHL, LSHL, ISHR, LSHR, // 1
                IUSHR, LUSHR,           // 1
                IAND, LAND,             // 1
                IOR, LOR,               // 1
                IXOR, LXOR,             // 1
                IINC                    // 3
             });
    instance.ranks[1] = new Rank(CONVERSION_RANK, new byte[] {
                I2L, I2F, I2D, // 1
                L2I, L2F, L2D, // 1
                F2I, F2L, F2D, // 1
                D2I, D2L, D2F, // 1
                I2B, I2C, I2S  // 1
             });
    instance.ranks[2] = new Rank(COMPARE_RANK, new byte[] {
               LCMP, FCMPL, FCMPG, DCMPL, DCMPG   // 1
             });
    instance.ranks[3] = new Rank(ARRAY_RANK, new byte[] {
                IALOAD, LALOAD, FALOAD, DALOAD,     // 1
                AALOAD, BALOAD, CALOAD, SALOAD,     // 1
                IASTORE, LASTORE, FASTORE, DASTORE, // 1
                AASTORE, BASTORE, CASTORE, SASTORE, // 1
                ARRAYLENGTH                         // 1
             });
    instance.ranks[4] = new Rank(LOCAL_VAR_RANK, new byte[] {
                ILOAD, LLOAD, FLOAD, DLOAD, ALOAD,      // 2
                ISTORE, LSTORE, FSTORE, DSTORE, ASTORE, // 2
                ILOAD_0, ILOAD_1, ILOAD_2, ILOAD_3,     // 1
                LLOAD_0, LLOAD_1, LLOAD_2, LLOAD_3,     // 1
                FLOAD_0, FLOAD_1, FLOAD_2, FLOAD_3,     // 1
                DLOAD_0, DLOAD_1, DLOAD_2, DLOAD_3,     // 1
                ALOAD_0, ALOAD_1, ALOAD_2, ALOAD_3,     // 1
                ISTORE_0, ISTORE_1, ISTORE_2, ISTORE_3, // 1
                LSTORE_0, LSTORE_1, LSTORE_2, LSTORE_3, // 1
                FSTORE_0, FSTORE_1, FSTORE_2, FSTORE_3, // 1
                DSTORE_0, DSTORE_1, DSTORE_2, DSTORE_3, // 1
                ASTORE_0, ASTORE_1, ASTORE_2, ASTORE_3  // 1
             });

    instance.ranks[5] = new Rank(INVOKE_RANK, new byte[] {
                INVOKEVIRTUAL, INVOKESPECIAL, INVOKESTATIC, // 3
                INVOKEINTERFACE                             // 5
             });
    instance.ranks[6] = new Rank(FIELD_RANK, new byte[] {
                GETSTATIC, PUTSTATIC, // 3
                GETFIELD, PUTFIELD    // 3
             });
    instance.ranks[7] = new Rank(JUMP_RANK, new byte[] {
                IFEQ, IFNE, IFLT, IFGE, IFGT, IFLE, // 3
                IF_ICMPEQ, IF_ICMPNE, IF_ICMPLT,    // 3
                IF_ICMPGE, IF_ICMPGT, IF_ICMPLE,    // 3
                IF_ACMPEQ, IF_ACMPNE,               // 3
                IFNULL, IFNONNULL,                  // 3
                GOTO, JSR,     // 3
                GOTO_W, JSR_W  // 5
             });
    instance.ranks[8] = new Rank(RETURN_RANK, new byte[] {
                IRETURN, LRETURN, FRETURN, // 1
                DRETURN, ARETURN, RETURN,  // 1
                RET                        // 2
             });
    instance.ranks[9] = new Rank(MEM_ALLOC_RANK, new byte[] {
                NEW, NEWARRAY, ANEWARRAY, MULTIANEWARRAY // 3, 2, 3, 4
             });
    instance.ranks[10] = new Rank(PUSH_RANK, new byte[] {
                BIPUSH,                       // 2
                SIPUSH,                       // 3
                LDC,                          // 2
                LDC_W,                        // 3
                LDC2_W,                       // 3
                ACONST_NULL,                  // 1
                ICONST_M1,                    // 1
                ICONST_0, ICONST_1, ICONST_2, // 1
                ICONST_3, ICONST_4, ICONST_5, // 1
                LCONST_0, LCONST_1,           // 1
                FCONST_0, FCONST_1, FCONST_2, // 1
                DCONST_0, DCONST_1,           // 1
             });
    instance.ranks[11] = new Rank(STACK_MANIP_RANK, new byte[] {
               POP, POP2,              // 1
               DUP, DUP_X1, DUP_X2,    // 1
               DUP2, DUP2_X1, DUP2_X2, // 1
               SWAP                    // 1
             });
    instance.ranks[12] = new Rank(SWITCH_RANK, new byte[] {
                TABLESWITCH,   // -1
                LOOKUPSWITCH // -1
             });
    instance.ranks[13] = new Rank(OTHER_RANK, new byte[] {
                NOP,          // 1
                ATHROW,       // 1
                CHECKCAST,    // 3
                INSTANCEOF,   // 3
                MONITORENTER, // 1
                MONITOREXIT  // 1
             });
    instance.ranks[14] = new Rank(QUICK_RANK, new byte[] {
                LDC_QUICK,                                        // 2
                LDC_W_QUICK, LDC2_W_QUICK,                        // 3
                GETFIELD_QUICK, PUTFIELD_QUICK,                   // 3
                GETFIELD2_QUICK, PUTFIELD2_QUICK,                 // 3
                GETSTATIC_QUICK, PUTSTATIC_QUICK,                 // 3
                GETSTATIC2_QUICK, PUTSTATIC2_QUICK,               // 3
                INVOKEVIRTUAL_QUICK, INVOKENONVIRTUAL_QUICK,      // 3
                INVOKESUPER_QUICK, INVOKESTATIC_QUICK,            // 3
                INVOKEINTERFACE_QUICK,                            // 5
                INVOKEVIRTUALOBJECT_QUICK,                        // 3
                NEW_QUICK, ANEWARRAY_QUICK, MULTIANEWARRAY_QUICK, // 3
                CHECKCAST_QUICK, INSTANCEOF_QUICK,                // 3
                INVOKEVIRTUAL_QUICK_W, GETFIELD_QUICK_W,          // 3
                PUTFIELD_QUICK_W                                  // 3
    });
    instance.ranks[15] = new Rank(RESERVED_RANK, new byte[] {
                BREAKPOINT, // 1
                IMPDEP1,    // 1
                IMPDEP2     // 1
             });

    for(int i=0; i < instance.descriptions.length; i++) {
      instance.descriptions[i] = instance.ranks[i].key;
    }
  }

  /**
   * Gets an instance for this ranking.
   *
   * @return an instance for this ranking.
   */
  public static RankTable getInstance() {
    if(instance == null) {
      instance = new RankTable();
      init();
    }

    return instance;
  }

}
