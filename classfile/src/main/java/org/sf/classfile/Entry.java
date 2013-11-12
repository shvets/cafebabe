// Entry.java

package org.sf.classfile;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;

/**
 * Class, that represents any entry inside class file:
 * const entry, interface entry, attribute entry etc.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public abstract class Entry {
  /**
   * Reads this entry from the input stream.
   *
   * @param  in  the input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public abstract void read(DataInput in) throws IOException;

  /**
   * Writes this entry to the output stream.
   *
   * @param  out  the output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public abstract void write(DataOutput out) throws IOException;

  /**
   * Gets the type of this entry.
   *
   * @return  the type of the entry
   */
  public abstract String getType();

  /**
   * Gets the length of this entry
   *
   * @return  length of entry.
   */
  public abstract long length();

  /**
   * Converts an entry into an array of bytes.
   *
    * @return an entry in form of a byte array
   */
  public byte[] getBytes() {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    DataOutputStream dos = new DataOutputStream(baos);

    try {
      write(dos);

      dos.close();
    }
    catch(IOException e) {
      e.printStackTrace();
    }

    return baos.toByteArray();
  }

}
