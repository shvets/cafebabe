// ConstPoolEntry.java

package org.sf.classfile;

/**
 * Basic interface for any entry that will be inserted into constant pool.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public abstract class ConstPoolEntry extends PoolEntry {

  /** A constant for presentation of UTF8 entry */
  public final static byte CONSTANT_UTF8            = 1;

  /** A constant for presentation of Unicode entry */
  public final static byte CONSTANT_UNICODE         = 2;

  /** A constant for presentation of Integer entry */
  public final static byte CONSTANT_INTEGER         = 3;

  /** A constant for presentation of Float entry */
  public final static byte CONSTANT_FLOAT           = 4;

  /** A constant for presentation of Long entry */
  public final static byte CONSTANT_LONG            = 5;

  /** A constant for presentation of Double entry */
  public final static byte CONSTANT_DOUBLE          = 6;

  /** A constant for presentation of Class entry */
  public final static byte CONSTANT_CLASS           = 7;

  /** A constant for presentation of String entry */
  public final static byte CONSTANT_STRING          = 8;

  /** A constant for presentation of Field entry */
  public final static byte CONSTANT_FIELD           = 9;

  /** A constant for presentation of Method entry */
  public final static byte CONSTANT_METHOD          = 10;

  /** A constant for presentation of Interface Method entry */
  public final static byte CONSTANT_INTERFACEMETHOD = 11;

  /** A constant for presentation of Name and Type entry */
  public final static byte CONSTANT_NAMEANDTYPE     = 12;

}
