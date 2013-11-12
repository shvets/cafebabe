// Converter.java

package org.sf.classfile;

/**
 * This is a container for static methods
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public final class Converter {

  /* Doesn't allow create any instance of this class */
  private Converter() {}

  /**
   * Gets an byte value that resides on specified position from
   * the array of bytes.
   *
   * @param buffer  byte array that contains desired byte value
   * @param pos  the position of desired byte in byte array
   * @return  selected byte value
   */
  public static int getByte(byte[] buffer, int pos) {
    return (buffer[pos] & 0xff);
  }

  /**
   * Gets a short value from  byte array that resides on specified position.
   *
   * @param buffer  byte array that contains desired short
   * @param pos  the position of desired short in byte array
   * @return  selected short value
   */
  public static int getShort(byte[] buffer, int pos) {
    pos = pos*2;

    return ((buffer[pos] & 0xff) << 8 | (buffer[pos+1] & 0xff));
  }

  /**
   * Gets an int value from  byte array that resides on specified position.
   *
   * @param buffer  byte array that contains desired int
   * @param pos  the position of desired inthort in byte array
   * @return  selected int
   */
  public static int getInt(byte[] buffer, int pos) {
    pos = pos*4;

    return ((buffer[pos+0] & 0xff) << 24 |
            (buffer[pos+1] & 0xff) << 16 |
            (buffer[pos+2] & 0xff) << 8  |
            (buffer[pos+3] & 0xff));
  }

  /**
   * Sets an int value in byte array on specified position.
   *
   * @param buffer  byte array
   * @param  i for set up
   * @param pos  the position in which int value will be setted up
   */
  public static void setInt(byte[] buffer, int i, int pos) {
    pos = pos*4;

    buffer[pos+0] = (byte)((i)       >> 24);
    buffer[pos+1] = (byte)((i << 8)  >> 24);
    buffer[pos+2] = (byte)((i << 16) >> 24);
    buffer[pos+3] = (byte)((i << 24) >> 24);
  }

  public static byte[] getBytes(short value) {
    byte b1 = (byte)((value >> 8) & 0x00FF);
    byte b2 = (byte)(value & 0x00FF);

    return new byte[] { b1, b2 };
  }

  public static byte[] getBytes(int value) {
    short s1 = (short)((value >> 16) & 0x0000FFFF);
    short s2 = (short)(value & 0x0000FFFF);

    byte b1 = (byte)((s1 >> 8) & 0x00FF);
    byte b2 = (byte)(s1 & 0x00FF);

    byte b3 = (byte)((s2 >> 8) & 0x00FF);
    byte b4 = (byte)(s2 & 0x00FF);

    return new byte[] { b1, b2, b3, b4 };
  }

  public static byte[] getBytes(long value) {
    int i1 = (int)((value >> 32) & 0x00000000FFFFFFFF);
    int i2 = (int)(value & 0x00000000FFFFFFFF);

    short s1 = (short)((i1 >> 16) & 0x0000FFFF);
    short s2 = (short)(i1 & 0x0000FFFF);

    short s3 = (short)((i2 >> 16) & 0x0000FFFF);
    short s4 = (short)(i2 & 0x0000FFFF);

    byte b1 = (byte)((s1 >> 8) & 0x00FF);
    byte b2 = (byte)(s1 & 0x00FF);

    byte b3 = (byte)((s2 >> 8) & 0x00FF);
    byte b4 = (byte)(s2 & 0x00FF);

    byte b5 = (byte)((s3 >> 8) & 0x00FF);
    byte b6 = (byte)(s3 & 0x00FF);

    byte b7 = (byte)((s4 >> 8) & 0x00FF);
    byte b8 = (byte)(s4 & 0x00FF);

    return new byte[] { b1, b2, b3, b4, b5, b6, b7, b8 };
  }

  /**
   * Converts long value to hex string with a delimiter delim
   *
   * @param value  a value to convert
   * @param delim delimiter
   * @return  string representation of long value
   */
  public static String toHexString(long value, String delim) {
    int i1 = (int)((value >> 32) & 0x00000000FFFFFFFF);
    int i2 = (int)(value & 0x00000000FFFFFFFF);
    return toHexString(i1, delim) + delim + toHexString(i2, delim);
  }

  /**
   * Converts long value to hex string with a delimiter " "
   *
   * @param value  a value to convert
   * @return  string representation of long value
   */
  public static String toHexString(long value) {
    return toHexString(value, "");
  }

  /**
   * Converts int value to hex string with a delimiter delim
   *
   * @param value  a value to convert
   * @param delim delimiter
   * @return  string representation of int value
   */
  public static String toHexString(int value, String delim) {
    short s1 = (short)((value >> 16) & 0x0000FFFF);
    short s2 = (short)(value & 0x0000FFFF);
    return toHexString(s1, delim) + delim + toHexString(s2, delim);
  }

  /**
   * Converts int value to hex string with a delimiter " "
   *
   * @param value  a value to convert
   * @return  string representation of int value
   */
  public static String toHexString(int value) {
    return toHexString(value, "");
  }

  /**
   * Converts short value to hex string with a delimiter delim
   *
   * @param value  a value to convert
   * @param delim delimiter
   * @return  string representation of short value
   */
  public static String toHexString(short value, String delim) {
    byte b1 = (byte)((value >> 8) & 0x00FF);
    byte b2 = (byte)(value & 0x00FF);
    return toHexString(b1) + delim + toHexString(b2);
  }

  /**
   * Converts short value to hex string with a delimiter " "
   *
   * @param value  a value to convert
   * @return  string representation of short value
   */
  public static String toHexString(short value) {
    return toHexString(value, "");
  }

  /**
   * Converts array of bytes to hex string with a delimiter delim
   *
   * @param buff  an array of bytes
   * @param delim delimiter
   * @return  string representation of array of bytes
   */
  public static String toHexString(byte[] buff, String delim) {
    StringBuffer sb = new StringBuffer();
    for(int i=0; i < buff.length; i++) {
      if(i > 0) sb.append(delim);

      sb.append(toHexString(buff[i]));
    }
    return sb.toString();
  }

  /**
   * Converts array of bytes to hex string with a delimiter " "
   *
   * @param buff  an array of bytes
   * @return  string representation of array of bytes
   */
  public static String toHexString(byte[] buff) {
    return toHexString(buff, "");
  }

  /**
   * Converts byte value to hex string
   *
   * @param b  a value to convert
   * @return  string representation of byte value
   */
  public static String toHexString(byte b) {
    String s = Integer.toHexString((int)b).toUpperCase();
    if(s.length() == 1)
     s = "0" + s;
    return s.substring(s.length()-2);
  }

  /**
   * Converts vector of string objects (tokens) to single string
   * with delimiter delim.
   *
   * @param v  a vector of string values
   * @param delim delimiter
   * @return  string representation of vector
   */
/*  public static String merge(Vector v, String delim) {
    String[] s = new String[v.size()];
    v.copyInto(s);

    return merge(s, delim);
  }
*/

  /**
   * Converts an array of string objects (tokens) to single string
   * with delimiter delim.
   *
   * @param str  a vector of string values
   * @param delim delimiter
   * @return  string representation of vector
   */
  public static String merge(String[] str, String delim) {
    StringBuffer sb = new StringBuffer();
    sb.append(str[0]);

    for(int i=1; i < str.length; i++)
      sb.append(delim + str[i]);

    return sb.toString();
  }

  public static void main(String args[]) throws Exception {
    byte[] buffer = new byte[1];

    buffer[0] = -128;

    System.out.println("Test0 : " + buffer[0]);
    System.out.println("Test1 : " + Converter.getByte(buffer, 0));
    System.out.println("Test2 : " + Converter.toHexString((byte)Converter.getByte(buffer, 0)));
  }

}
