// AccessFlags.java

package org.sf.classfile;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

/**
 * Class that represents access modifiers for class members
 * (fields, methods, class attributes)
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public abstract class AccessFlags extends Entry {
  public static final String TYPE = "AccessFlags";

  /** public flag */
  public static final short ACC_PUBLIC = 0x0001;

  /** private flag */
  public static final short ACC_PRIVATE = 0x0002;

  /** protected flag */
  public static final short ACC_PROTECTED = 0x0004;

  /** static flag */
  public static final short ACC_STATIC = 0x0008;

  /** final flag */
  public static final short ACC_FINAL = 0x0010;

  /** synchronized flag */
  public static final short ACC_SYNCHRONIZED = 0x0020;

  /**  this flag has historical significance only; was used for class file only */
  public static final short ACC_SUPER = 0x0020;

  /** volatile flag */
  public static final short ACC_VOLATILE = 0x0040;

  /** transient flag */
  public static final short ACC_TRANSIENT = 0x0080;

  /** native flag */
  public static final short ACC_NATIVE = 0x0100;

  /** interface flag */
  public static final short ACC_INTERFACE = 0x0200;

  /** abstract flag */
  public static final short ACC_ABSTRACT = 0x0400;

  /** strict flag */
  public static final short ACC_STRICT = 0x0800;

  public static final String ACC_PUBLIC_NAME = "public";
  public static final String ACC_PRIVATE_NAME = "private";
  public static final String ACC_PROTECTED_NAME = "protected";
  public static final String ACC_STATIC_NAME = "static";
  public static final String ACC_FINAL_NAME = "final";
  public static final String ACC_SYNCHRONIZED_NAME = "synchronized";
  public static final String ACC_VOLATILE_NAME = "volatile";
  public static final String ACC_TRANSIENT_NAME = "transient";
  public static final String ACC_NATIVE_NAME = "native";
  public static final String ACC_INTERFACE_NAME = "interface";
  public static final String ACC_ABSTRACT_NAME = "abstract";
  public static final String ACC_STRICT_NAME = "strict";

  /* a short value for holding all attributes */
  private short value;

  /**
   * The default constructor that creates "access flags" entry.
   */
  public AccessFlags() {
  }

  /**
   * Reads this entry from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException {
    value = in.readShort();

    if(!checkAccessFlags()) {
      throw new IOException("Wrong flag in " + getType() + ": " + value + ".");
    }
  }

  /**
   * Writes this entry to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    if(!checkAccessFlags()) {
      throw new IOException("Wrong flag in " + getType() + ": " + value + ".");
    }

    out.writeShort(value);
  }

  /**
   * Checks the access flags for valid flags
   *
   * @return  true if all flags are correct; false otherwise
   */
  protected abstract boolean checkAccessFlags();

  /**
   * Gets the type of this entry.
   *
   * @return  the type of entry
   */
  public String getType() {
    return TYPE;
  }

  /**
   * Calculates length of this entry in bytes.
   *
   * @return  length of entry
   */
  public long length() {
    return 2;
  }

  /**
   * Gets access flags.
   *
   * @return access flags
   */
  public short getValue() {
    return value;
  }

  /**
   * Set the value.
   *
   * @param value  the value
   */
  public void setValue(short value) {
    this.value = value;
  }

  /**
   * Compares two access flags. They will be equals if they both
   * have the same value.
   *
   * @param   flags  the access flags tor comparison.
   * @return  true if this access flags has the same flags; false otherwise.
   */
  public boolean equals(AccessFlags flags) {
    return (value == flags.value);
  }

  /**
   * Checks if "public" flag is on
   *
   * @return true if "public" flag is on; false otherwise
   */
  public boolean isPublic() {
    return (value & ACC_PUBLIC) != 0;
  }

  /**
   * Checks if "private" flag is on
   *
   * @return true if "private" flag is on; false otherwise
   */
  public boolean isPrivate() {
    return (value & ACC_PRIVATE) != 0;
  }

  /**
   * Checks if "protected" flag is on
   *
   * @return true if "protected" flag is on; false otherwise
   */
  public boolean isProtected() {
    return (value & ACC_PROTECTED) != 0;
  }

  /**
   * Checks if "package" flags are on
   *
   * @return true if "package" flag are on; false otherwise
   */
  public boolean isPackage() {
    return (value & 0x07) == 0;
  }

  /**
   * Checks if "static" flag is on
   *
   * @return true if "static" flag is on; false otherwise
   */
  public boolean isStatic() {
    return (value & ACC_STATIC) != 0;
  }

  /**
   * Checks if "final" flag is on
   *
   * @return true if "final" flag is on; false otherwise
   */
  public boolean isFinal() {
    return (value & ACC_FINAL) != 0;
  }

  /**
   * Checks if "synchronized" flag is on
   *
   * @return true if "synchronized" flag is on; false otherwise
   */
  public boolean isSynchronized() {
    return (value & ACC_SYNCHRONIZED) != 0;
  }

  /**
   * Checks if "volatile" flag is on
   *
   * @return true if "volatile" flag is on; false otherwise
   */
  public boolean isVolatile() {
    return (value & ACC_VOLATILE) != 0;
  }

  /**
   * Checks if "transient" flag is on
   *
   * @return true if "transient" flag is on; false otherwise
   */
  public boolean isTransient() {
    return (value & ACC_TRANSIENT) != 0;
  }

  /**
   * Checks if "native" flag is on
   *
   * @return true if "native" flag is on; false otherwise
   */
  public boolean isNative() {
    return (value & ACC_NATIVE) != 0;
  }

  /**
   * Checks if "interface" flag is on
   *
   * @return true if "interface" flag is on; false otherwise
   */
  public boolean isInterface() {
    return (value & ACC_INTERFACE) != 0;
  }

  /**
   * Checks if "abstract" flag is on
   *
   * @return true if "abstract" flag is on; false otherwise
   */
  public boolean isAbstract() {
    return (value & ACC_ABSTRACT) != 0;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return  a string representation of the object.
   */
  public String toString() {
    StringBuffer sb = new StringBuffer();

    if((value & 0x07) == 0) {
      sb.append("");
    }
    else if((value & ACC_PUBLIC) > 0) {
      sb.append(ACC_PUBLIC_NAME);
    }
    else if((value & ACC_PRIVATE) > 0) {
      sb.append(ACC_PRIVATE_NAME);
    }
    else if((value & ACC_PROTECTED) > 0) {
      sb.append(ACC_PROTECTED_NAME);
    }

    if((value & ACC_STATIC) > 0) {
      sb.append(" " + ACC_STATIC_NAME);
    }

    if((value & ACC_FINAL) > 0) {
      sb.append(" " + ACC_FINAL_NAME);
    }

    if((value & ACC_SYNCHRONIZED) > 0) {
      sb.append(" " + ACC_SYNCHRONIZED_NAME);
    }

    if((value & ACC_VOLATILE) > 0) {
      sb.append(" " + ACC_VOLATILE_NAME);
    }

    if((value & ACC_TRANSIENT) > 0) {
      sb.append(" " + ACC_TRANSIENT_NAME);
    }

    if((value & ACC_NATIVE) > 0) {
      sb.append(" " + ACC_NATIVE_NAME);
    }

    if((value & ACC_ABSTRACT) > 0) {
      sb.append(" " + ACC_ABSTRACT_NAME);
    }

    if((value & ACC_INTERFACE) > 0) {
      sb.append(" " + ACC_INTERFACE_NAME);
    }

    return sb.toString();
  }

}
