// BlockDataShortEntry.java

package org.sf.serfile;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

/**
 *
 * Abstract class for representing short block data
 *
 * blockdatashort: TC_BLOCKDATA (unsigned byte)<size> (byte)[size]
 *
 * @version 1.0 05/25/2000
 * @author Alexander Shvets
 */
public class BlockDataShortEntry extends BlockDataEntry {

  /**
   * Reads this entry from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException  {
    int size = in.readUnsignedByte();

    buffer = new byte[size];
    in.readFully(buffer);
  }

  /**
   * Writes this entry to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    out.writeByte(getTag());

    int size = buffer.length;

    out.writeByte(size);
    out.write(buffer, 0, size);
  }

  /**
   * Gets the length of this entry in bytes
   *
   * @return  the length of entry
   */
  public long length() {
    return 1 + 1 + buffer.length;
  }

  /**
   * Gets the type of this entry.
   *
   * @return  the type of entry
   */
  public String getType() {
    return "Block Data Short";
  }

  /**
   * Gets the tag of this entry.
   *
   * @return  the tag for entry
   */
  public byte getTag() {
    return TC_BLOCKDATA;
  }

}
