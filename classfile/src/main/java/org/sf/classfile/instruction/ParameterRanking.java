// ParameterRanking.java

package org.sf.classfile.instruction;

/**
 * This class represents a table of instructions divided to ranks on
 * parameter approach. This is an example of Singleton Pattern.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public class ParameterRanking implements Opcode {

  /** Constants for representing all ranks by parameter value */
  public static final String FIELD_RANK     = "field";
  public static final String METHOD_RANK    = "method";
  public static final String IMETHOD_RANK   = "imethod";
  public static final String LDC_RANK       = "ldc";
  public static final String LDC2_RANK      = "ldc2";
  public static final String CLASS_RANK     = "class";
  public static final String PRIMITIVE_RANK = "primitive";
  public static final String MULTI_RANK     = "multi";
  public static final String PLAIN_RANK     = "plain";
  public static final String SWITCH_RANK    = "switch";

  /* An instance of a rank table */
  private static RankTable instance;

  /* Initialization of a rank table */
  private static void init() {
    instance.ranks = new Rank[10];
    instance.descriptions = new String[instance.ranks.length];

    instance.ranks[0] = new Rank(FIELD_RANK, new byte[] {
                GETFIELD, PUTFIELD,   // 3
                GETSTATIC, PUTSTATIC, // 3
                GETFIELD_QUICK, PUTFIELD_QUICK,   // 3
                GETSTATIC_QUICK, PUTSTATIC_QUICK, // 3
                GETFIELD2_QUICK, PUTFIELD2_QUICK,   // 3
                GETSTATIC2_QUICK, PUTSTATIC2_QUICK, // 3
                GETFIELD_QUICK_W, PUTFIELD_QUICK_W   // 3
             });

    instance.ranks[1] = new Rank(METHOD_RANK, new byte[] {
                INVOKEVIRTUAL, INVOKESPECIAL,
                INVOKESTATIC, INVOKEVIRTUAL_QUICK,
                INVOKENONVIRTUAL_QUICK, INVOKESUPER_QUICK,
                INVOKESTATIC_QUICK, INVOKEINTERFACE_QUICK,
                INVOKEVIRTUALOBJECT_QUICK, INVOKEVIRTUAL_QUICK_W
             });

    instance.ranks[2] = new Rank(IMETHOD_RANK, new byte[] {
               INVOKEINTERFACE // 5
             });

    instance.ranks[3] = new Rank(LDC_RANK, new byte[] {
                LDC, LDC_QUICK
             });

    instance.ranks[4] = new Rank(LDC2_RANK, new byte[] {
                LDC_W, LDC2_W,
                LDC_W_QUICK, LDC2_W_QUICK
             });

    instance.ranks[5] = new Rank(CLASS_RANK, new byte[] {
                NEW, ANEWARRAY, CHECKCAST, INSTANCEOF,
                NEW_QUICK, ANEWARRAY_QUICK,
                CHECKCAST_QUICK, INSTANCEOF_QUICK
             });

    instance.ranks[6] = new Rank(PRIMITIVE_RANK, new byte[] {
                NEWARRAY
             });

    instance.ranks[7] = new Rank(MULTI_RANK, new byte[] {
                MULTIANEWARRAY, MULTIANEWARRAY_QUICK
             });

    instance.ranks[8] = new Rank(PLAIN_RANK, new byte[] {
                BIPUSH, SIPUSH,
                ILOAD, LLOAD, FLOAD, DLOAD, ALOAD,
                ISTORE, LSTORE, FSTORE, DSTORE, ASTORE,
                IINC,
                IFEQ, IFNE, IFLT, IFGE, IFGT, IFLE,
                IF_ICMPEQ, IF_ICMPNE, IF_ICMPLT, IF_ICMPGE,
                IF_ICMPGT, IF_ICMPLE, IF_ACMPEQ, IF_ACMPNE,
                GOTO, JSR, RET,
                IFNULL, IFNONNULL,
                GOTO_W, JSR_W
             });

    instance.ranks[9] = new Rank(SWITCH_RANK, new byte[] {
                TABLESWITCH, LOOKUPSWITCH
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

