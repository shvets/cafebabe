// Opcode.java

package org.sf.classfile.instruction;

/**
 * This interface contains all opcodes for all JVM instructions.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public interface Opcode {

  public static final byte NOP                       = (byte)0x00;
  public static final byte ACONST_NULL               = (byte)0x01;

  // int consts
  public static final byte ICONST_M1                 = (byte)0x02;
  public static final byte ICONST_0                  = (byte)0x03;
  public static final byte ICONST_1                  = (byte)0x04;
  public static final byte ICONST_2                  = (byte)0x05;
  public static final byte ICONST_3                  = (byte)0x06;
  public static final byte ICONST_4                  = (byte)0x07;
  public static final byte ICONST_5                  = (byte)0x08;

  // long consts
  public static final byte LCONST_0                  = (byte)0x09;
  public static final byte LCONST_1                  = (byte)0x0a;

  // float consts
  public static final byte FCONST_0                  = (byte)0x0b;
  public static final byte FCONST_1                  = (byte)0x0c;
  public static final byte FCONST_2                  = (byte)0x0d;

  // double consts
  public static final byte DCONST_0                  = (byte)0x0e;
  public static final byte DCONST_1                  = (byte)0x0f;

  public static final byte BIPUSH                    = (byte)0x10;
  public static final byte SIPUSH                    = (byte)0x11;

  public static final byte LDC                       = (byte)0x12;
  public static final byte LDC_W                     = (byte)0x13;
  public static final byte LDC2_W                    = (byte)0x14;

  // typed loads local
  public static final byte ILOAD                     = (byte)0x15;
  public static final byte LLOAD                     = (byte)0x16;
  public static final byte FLOAD                     = (byte)0x17;
  public static final byte DLOAD                     = (byte)0x18;
  public static final byte ALOAD                     = (byte)0x19;

  // int loads
  public static final byte ILOAD_0                   = (byte)0x1a;
  public static final byte ILOAD_1                   = (byte)0x1b;
  public static final byte ILOAD_2                   = (byte)0x1c;
  public static final byte ILOAD_3                   = (byte)0x1d;

  // long loads
  public static final byte LLOAD_0                   = (byte)0x1e;
  public static final byte LLOAD_1                   = (byte)0x1f;
  public static final byte LLOAD_2                   = (byte)0x20;
  public static final byte LLOAD_3                   = (byte)0x21;

  // float loads
  public static final byte FLOAD_0                   = (byte)0x22;
  public static final byte FLOAD_1                   = (byte)0x23;
  public static final byte FLOAD_2                   = (byte)0x24;
  public static final byte FLOAD_3                   = (byte)0x25;

  // double loads
  public static final byte DLOAD_0                   = (byte)0x26;
  public static final byte DLOAD_1                   = (byte)0x27;
  public static final byte DLOAD_2                   = (byte)0x28;
  public static final byte DLOAD_3                   = (byte)0x29;

  // ref loads
  public static final byte ALOAD_0                   = (byte)0x2a;
  public static final byte ALOAD_1                   = (byte)0x2b;
  public static final byte ALOAD_2                   = (byte)0x2c;
  public static final byte ALOAD_3                   = (byte)0x2d;

  // array loads
  public static final byte IALOAD                    = (byte)0x2e;
  public static final byte LALOAD                    = (byte)0x2f;
  public static final byte FALOAD                    = (byte)0x30;
  public static final byte DALOAD                    = (byte)0x31;
  public static final byte AALOAD                    = (byte)0x32;
  public static final byte BALOAD                    = (byte)0x33;
  public static final byte CALOAD                    = (byte)0x34;
  public static final byte SALOAD                    = (byte)0x35;

  public static final byte ISTORE                    = (byte)0x36;
  public static final byte LSTORE                    = (byte)0x37;
  public static final byte FSTORE                    = (byte)0x38;
  public static final byte DSTORE                    = (byte)0x39;
  public static final byte ASTORE                    = (byte)0x3a;

  // int stores
  public static final byte ISTORE_0                  = (byte)0x3b;
  public static final byte ISTORE_1                  = (byte)0x3c;
  public static final byte ISTORE_2                  = (byte)0x3d;
  public static final byte ISTORE_3                  = (byte)0x3e;

  // long stores
  public static final byte LSTORE_0                  = (byte)0x3f;
  public static final byte LSTORE_1                  = (byte)0x40;
  public static final byte LSTORE_2                  = (byte)0x41;
  public static final byte LSTORE_3                  = (byte)0x42;

  // float stores
  public static final byte FSTORE_0                  = (byte)0x43;
  public static final byte FSTORE_1                  = (byte)0x44;
  public static final byte FSTORE_2                  = (byte)0x45;
  public static final byte FSTORE_3                  = (byte)0x46;

  // double stores
  public static final byte DSTORE_0                  = (byte)0x47;
  public static final byte DSTORE_1                  = (byte)0x48;
  public static final byte DSTORE_2                  = (byte)0x49;
  public static final byte DSTORE_3                  = (byte)0x4a;

  // ref stores
  public static final byte ASTORE_0                  = (byte)0x4b;
  public static final byte ASTORE_1                  = (byte)0x4c;
  public static final byte ASTORE_2                  = (byte)0x4d;
  public static final byte ASTORE_3                  = (byte)0x4e;

  // array stores
  public static final byte IASTORE                   = (byte)0x4f;
  public static final byte LASTORE                   = (byte)0x50;
  public static final byte FASTORE                   = (byte)0x51;
  public static final byte DASTORE                   = (byte)0x52;
  public static final byte AASTORE                   = (byte)0x53;
  public static final byte BASTORE                   = (byte)0x54;
  public static final byte CASTORE                   = (byte)0x55;
  public static final byte SASTORE                   = (byte)0x56;

  public static final byte POP                       = (byte)0x57;
  public static final byte POP2                      = (byte)0x58;

  // dup's
  public static final byte DUP                       = (byte)0x59;
  public static final byte DUP_X1                    = (byte)0x5a;
  public static final byte DUP_X2                    = (byte)0x5b;
  public static final byte DUP2                      = (byte)0x5c;
  public static final byte DUP2_X1                   = (byte)0x5d;
  public static final byte DUP2_X2                   = (byte)0x5e;
  public static final byte SWAP                      = (byte)0x5f;

  // arith
  public static final byte IADD                      = (byte)0x60;
  public static final byte LADD                      = (byte)0x61;
  public static final byte FADD                      = (byte)0x62;
  public static final byte DADD                      = (byte)0x63;

  public static final byte ISUB                      = (byte)0x64;
  public static final byte LSUB                      = (byte)0x65;
  public static final byte FSUB                      = (byte)0x66;
  public static final byte DSUB                      = (byte)0x67;

  public static final byte IMUL                      = (byte)0x68;
  public static final byte LMUL                      = (byte)0x69;
  public static final byte FMUL                      = (byte)0x6a;
  public static final byte DMUL                      = (byte)0x6b;

  public static final byte IDIV                      = (byte)0x6c;
  public static final byte LDIV                      = (byte)0x6d;
  public static final byte FDIV                      = (byte)0x6e;
  public static final byte DDIV                      = (byte)0x6f;

  // arith misc
  public static final byte IREM                      = (byte)0x70;
  public static final byte LREM                      = (byte)0x71;
  public static final byte FREM                      = (byte)0x72;
  public static final byte DREM                      = (byte)0x73;

  public static final byte INEG                      = (byte)0x74;
  public static final byte LNEG                      = (byte)0x75;
  public static final byte FNEG                      = (byte)0x76;
  public static final byte DNEG                      = (byte)0x77;

  public static final byte ISHL                      = (byte)0x78;
  public static final byte LSHL                      = (byte)0x79;

  public static final byte ISHR                      = (byte)0x7a;
  public static final byte LSHR                      = (byte)0x7b;

  public static final byte IUSHR                     = (byte)0x7c;
  public static final byte LUSHR                     = (byte)0x7d;

  public static final byte IAND                      = (byte)0x7e;
  public static final byte LAND                      = (byte)0x7f;

  public static final byte IOR                       = (byte)0x80;
  public static final byte LOR                       = (byte)0x81;

  public static final byte IXOR                      = (byte)0x82;
  public static final byte LXOR                      = (byte)0x83;

  // local int += const
  public static final byte IINC                      = (byte)0x84;

  // int conversions
  public static final byte I2L                       = (byte)0x85;
  public static final byte I2F                       = (byte)0x86;
  public static final byte I2D                       = (byte)0x87;

  // long conversions
  public static final byte L2I                       = (byte)0x88;
  public static final byte L2F                       = (byte)0x89;
  public static final byte L2D                       = (byte)0x8a;

  // float conversions
  public static final byte F2I                       = (byte)0x8b;
  public static final byte F2L                       = (byte)0x8c;
  public static final byte F2D                       = (byte)0x8d;

  // double conversions
  public static final byte D2I                       = (byte)0x8e;
  public static final byte D2L                       = (byte)0x8f;
  public static final byte D2F                       = (byte)0x90;

  // int conversions
  public static final byte I2B                       = (byte)0x91;
  public static final byte I2C                       = (byte)0x92;
  public static final byte I2S                       = (byte)0x93;

  // comparisions
  public static final byte LCMP                      = (byte)0x94;
  public static final byte FCMPL                     = (byte)0x95;
  public static final byte FCMPG                     = (byte)0x96;
  public static final byte DCMPL                     = (byte)0x97;
  public static final byte DCMPG                     = (byte)0x98;

  // int to zero comparisions
  public static final byte IFEQ                      = (byte)0x99;
  public static final byte IFNE                      = (byte)0x9a;
  public static final byte IFLT                      = (byte)0x9b;
  public static final byte IFGE                      = (byte)0x9c;
  public static final byte IFGT                      = (byte)0x9d;
  public static final byte IFLE                      = (byte)0x9e;

  // int to int comparision's
  public static final byte IF_ICMPEQ                 = (byte)0x9f;
  public static final byte IF_ICMPNE                 = (byte)0xa0;
  public static final byte IF_ICMPLT                 = (byte)0xa1;
  public static final byte IF_ICMPGE                 = (byte)0xa2;
  public static final byte IF_ICMPGT                 = (byte)0xa3;
  public static final byte IF_ICMPLE                 = (byte)0xa4;

  // ref comparisions
  public static final byte IF_ACMPEQ                 = (byte)0xa5;
  public static final byte IF_ACMPNE                 = (byte)0xa6;

  // goto
  public static final byte GOTO                      = (byte)0xa7;
  public static final byte JSR                       = (byte)0xa8;
  public static final byte RET                       = (byte)0xa9;
  public static final byte TABLESWITCH               = (byte)0xaa;
  public static final byte LOOKUPSWITCH              = (byte)0xab;

  // return's
  public static final byte IRETURN                   = (byte)0xac;
  public static final byte LRETURN                   = (byte)0xad;
  public static final byte FRETURN                   = (byte)0xae;
  public static final byte DRETURN                   = (byte)0xaf;
  public static final byte ARETURN                   = (byte)0xb0;
  public static final byte RETURN                    = (byte)0xb1;

  public static final byte GETSTATIC                 = (byte)0xb2;
  public static final byte PUTSTATIC                 = (byte)0xb3;
  public static final byte GETFIELD                  = (byte)0xb4;
  public static final byte PUTFIELD                  = (byte)0xb5;

  public static final byte INVOKEVIRTUAL             = (byte)0xb6;
  public static final byte INVOKESPECIAL             = (byte)0xb7;
  public static final byte INVOKESTATIC              = (byte)0xb8;
  public static final byte INVOKEINTERFACE           = (byte)0xb9;

  public static final byte XXXUNUSEDXXX              = (byte)0xba;

  public static final byte NEW                       = (byte)0xbb;
  public static final byte NEWARRAY                  = (byte)0xbc;
  public static final byte ANEWARRAY                 = (byte)0xbd;
  public static final byte ARRAYLENGTH               = (byte)0xbe;

  public static final byte ATHROW                    = (byte)0xbf;
  public static final byte CHECKCAST                 = (byte)0xc0;
  public static final byte INSTANCEOF                = (byte)0xc1;

  public static final byte MONITORENTER              = (byte)0xc2;
  public static final byte MONITOREXIT               = (byte)0xc3;

  public static final byte WIDE                      = (byte)0xc4;
  public static final byte MULTIANEWARRAY            = (byte)0xc5;
  public static final byte IFNULL                    = (byte)0xc6;
  public static final byte IFNONNULL                 = (byte)0xc7;
  public static final byte GOTO_W                    = (byte)0xc8;
  public static final byte JSR_W                     = (byte)0xc9;

  //quick opcodes:
  public static final byte LDC_QUICK                 = (byte)0xcb;
  public static final byte LDC_W_QUICK               = (byte)0xcc;
  public static final byte LDC2_W_QUICK              = (byte)0xcd;
  public static final byte GETFIELD_QUICK            = (byte)0xce;
  public static final byte PUTFIELD_QUICK            = (byte)0xcf;
  public static final byte GETFIELD2_QUICK           = (byte)0xd0;
  public static final byte PUTFIELD2_QUICK           = (byte)0xd1;
  public static final byte GETSTATIC_QUICK           = (byte)0xd2;
  public static final byte PUTSTATIC_QUICK           = (byte)0xd3;
  public static final byte GETSTATIC2_QUICK          = (byte)0xd4;
  public static final byte PUTSTATIC2_QUICK          = (byte)0xd5;
  public static final byte INVOKEVIRTUAL_QUICK       = (byte)0xd6;
  public static final byte INVOKENONVIRTUAL_QUICK    = (byte)0xd7;
  public static final byte INVOKESUPER_QUICK         = (byte)0xd8;
  public static final byte INVOKESTATIC_QUICK        = (byte)0xd9;
  public static final byte INVOKEINTERFACE_QUICK     = (byte)0xda;
  public static final byte INVOKEVIRTUALOBJECT_QUICK = (byte)0xdb;

  public static final byte NEW_QUICK                 = (byte)0xdd;
  public static final byte ANEWARRAY_QUICK           = (byte)0xde;
  public static final byte MULTIANEWARRAY_QUICK      = (byte)0xdf;

  public static final byte CHECKCAST_QUICK           = (byte)0xe0;
  public static final byte INSTANCEOF_QUICK          = (byte)0xe1;
  public static final byte INVOKEVIRTUAL_QUICK_W     = (byte)0xe2;
  public static final byte GETFIELD_QUICK_W          = (byte)0xe3;
  public static final byte PUTFIELD_QUICK_W          = (byte)0xe4;

  // Reserved opcodes:
  public static final byte BREAKPOINT                = (byte)0xca;
  public static final byte IMPDEP1                   = (byte)0xfe;
  public static final byte IMPDEP2                   = (byte)0xff;

}
