// SerFlags.java

package org.sf.serfile;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

/**
 * Class that represents access modifiers for class description serialized entry.
 *
 * @version 1.0 05/25/2000
 * @author Alexander Shvets
 */
public final class SerFlags extends Entry implements SerConstants {

  /* a byte value for holding all attributes */
  private byte serFlags;

  /**
   * The default constructor that creates "serialized flags" entry.
   */
  public SerFlags() {}

  /**
   * Reads this entry from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException {
    serFlags = in.readByte();
  }

  /**
   * Writes this entry to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    out.writeByte(serFlags);
  }

  /**
   * Gets the type of this entry.
   *
   * @return  the type of entry
   */
  public String getType() {
    return "Serialization Flags";
  }

  /**
   * Gets the length of this entry in bytes.
   *
   * @return  the length of entry
   */
  public long length() {
    return 1;
  }

  /**
   * Gets serialized flags.
   *
   * @return serialized flags
   */
  public byte getSerFlags() {
    return serFlags;
  }

  /**
   * Sets serialized flags.
   *
   * @param serFlags serialized flags
   */
  public void setSerFlags(byte serFlags) {
    this.serFlags = serFlags;
  }

  /**
   * Compares two serialized flags. They
   * will be equals if they both have the same flags.
   *
   * @param   f  the flag tor comparison.
   * @return  true if this flag has the same flags; false otherwise.
   */
  public boolean equals(SerFlags f) {
    return (serFlags == f.serFlags);
  }

  /**
   * Checks if serialized flags contain "serializable" flag
   *
   * @return true if serialized flags contain "serializable" flag
   */
  public boolean isSerializable() {
    return (serFlags & SC_SERIALIZABLE) != 0;
  }

  /**
   * Checks if serialized flags contain "extenrnalizable" flag
   *
   * @return true if serialized flags contain "extenrnalizable" flag
   */
  public boolean isExternalizable() {
    return (serFlags & SC_EXTERNALIZABLE) != 0;
  }

  /**
   * Checks if serialized flags contain "write method" flag
   *
   * @return true if serialized flags contain "write method" flag
   */
  public boolean hasOwnWriteMethod() {
    return (serFlags & SC_WRITE_METHOD) != 0;
  }

  /**
   * Checks if serialized flags contain "block data" flag
   *
   * @return true if serialized flags contain "block data" flag
   */
  public boolean isBlockDataMode() {
    return (serFlags & SC_BLOCK_DATA) != 0;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return  a string representation of the object.
   */
  public String toString() {
    StringBuffer sb = new StringBuffer();

    if((serFlags & SC_SERIALIZABLE) > 0) {
      sb.append("serializable");

      if((serFlags & SC_WRITE_METHOD) > 0) {
        sb.append(", has own writeObject()");
      }
    }

    else if((serFlags & SC_EXTERNALIZABLE) > 0) {
      sb.append("externalizable");

      if((serFlags & SC_BLOCK_DATA) > 0) {
        sb.append(", \"Block Data\" mode");
      }
    }

    return sb.toString();
  }

}
