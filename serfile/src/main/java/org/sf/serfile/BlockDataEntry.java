// BlockDataEntry.java

package org.sf.serfile;

/**
 * Abstract class for representing block data
 *
 * blockdata: blockdatashort | blockdatalong
 *
 * @version 1.0 05/25/2000
 * @author Alexander Shvets
 */
public abstract class BlockDataEntry extends SerEntry {

  /** Buffer for holding data */
  protected byte buffer[];

  /**
   * The default constructor that creates block data entry.
   */
  public BlockDataEntry() {}

  /**
   * Gets the buffer
   *
   * @return  buffer
   */
  public byte[] getBuffer() {
    return buffer;
  }

  /**
   * Sest the buffer
   *
   * @param buffer  buffer
   */
  public void setBuffer(byte[] buffer) {
    this.buffer = buffer;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return  a string representation of the object.
   */
  public String toString() {
    StringBuffer sb = new StringBuffer();
    for(int i=0; i < buffer.length; i++) {
      sb.append(Converter.toHexString(buffer[i]) + " ");
    }

    return getType() + "(" + "size = " + buffer.length + " " + "buffer : " + sb.toString() + ")";
  }

}
