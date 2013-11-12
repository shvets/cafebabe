// SerFile.java

package org.sf.serfile;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.StreamCorruptedException;
import java.util.Iterator;

/*
 * Class that represents serialized file
 *
 * stream: STREAM_MAGIC STREAM_VERSION contents
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public class SerFile extends Entry implements SerConstants {
  public static final String TYPE = "Class File";

  /** Table that contains all handles to allocated elements of serialized file */
  private HandlesTable handlesTable = new HandlesTable();

  /** Tracks changes in serialized file */
  private boolean changed = false;

  /** Stream magic value */
  protected short streamMagic;

  /** Stream version value */
  protected short streamVersion;

  /** Contents of serialized file */
  protected Contents contents;

  /**
   * Reads serialized file from input stream.
   *
   * @param  in  the input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException {
    streamMagic = in.readShort();

    if(streamMagic != STREAM_MAGIC) {
      throw new StreamCorruptedException("Illegal stream magic number '0x" +
                             Converter.toHexString(streamMagic) + "'");
    }

    streamVersion = in.readShort();

    if(streamVersion != STREAM_VERSION) {
      throw new StreamCorruptedException("Illegal stream version '0x" +
                      Converter.toHexString(streamVersion) + "'" + " expected '0x" +
                      Converter.toHexString(STREAM_VERSION) + "'" );
    }

    contents = new Contents(handlesTable);

    contents.read(in);

    changed = false;
  }

  /**
   * Writes serialized file to output stream.
   *
   * @param  out  the output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    out.writeShort(streamMagic);
    out.writeShort(streamVersion);

    contents.write(out);

    changed = false;
  }

  /**
   * Reads serialized bytes from a file.
   *
   * @param  fileName the name of the file with serialized bytes.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(String fileName) throws IOException {
    FileInputStream fis = new FileInputStream(fileName);
    DataInputStream in = new DataInputStream(new BufferedInputStream(fis, 8192));

    read(in);

    in.close();
  }

  /**
   * Writes serialized bytes to a file.
   *
   * @param  fileName the name of the file with serialized bytes.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(String fileName) throws IOException {
    FileOutputStream fos = new FileOutputStream(fileName);
    DataOutputStream out = new DataOutputStream(new BufferedOutputStream(fos, 8192));

    write(out);

    out.close();
  }

  /**
   * Gets the type of this entry.
   *
   * @return  the type of entry
   */
  public String getType() {
    return TYPE;
  }

  /**
   * Gets theength of serialized file in bytes.
   *
   * @return  length of serialized file in bytes
   */
  public long length() {
    return 2 + 2 + contents.length(); // stream magic, stream version, contents length
  }

  /**
   * Checks if serialized file has changed.
   *
   * @return true, if serialized file has changed; false otherwise
   */
  public boolean isChanged() {
    return changed;
  }

  /**
   * Sets up a flag that keeps changes in serialized file.
   *
   * @param changed  whether changes in serialized file took place or not
   */
  public void setChanged(boolean changed) {
    this.changed = changed;
  }

  /**
   * Gets the magic number.
   *
   * @return the magic number
   */
  public short getStreamMagic() {
    return streamMagic;
  }

  /**
   * Gets the stream version.
   *
   * @return the stream version
   */
  public short getStreamVersion() {
    return streamVersion;
  }

  /**
   * Gets the contents of serialized file
   *
   * @return the contents of serialized file
   */
  public Contents getContents() {
    return contents;
  }

  /**
   * Gets the handles table
   *
   * @return the handles table
   */
  public HandlesTable getHandlesTable() {
    return handlesTable;
  }

  /**
   * Prints the content of serialized file.
   *
   * @param  fileName the name of the file with serialized bytes.
   * @exception  IOException if an I/O error occurs.
   */
  public void print(String fileName) throws IOException {
    PrintWriter out = new PrintWriter(new FileWriter(fileName));

    out.println();
    out.println("Stream magic  : 0x" + Converter.toHexString(streamMagic, ""));
    out.println("Stream version: 0x" + Converter.toHexString(streamVersion, ""));

    out.println("\nContents:");

    for(short i=0; i < contents.size(); i++) {
      ContentEntry contentEntry = (ContentEntry)contents.get(i);
      Entry value = contentEntry.getValue();

      if(value instanceof HandleSerEntry) {
        HandleSerEntry entry = (HandleSerEntry)value;
        out.println(entry.getType() + " (" + entry.getHandle() + ")");
      }
      else {
        out.println(contentEntry);
      }
    }

    out.print("\nReference table:");

    Iterator iterator = handlesTable.values().iterator();

    if(!iterator.hasNext()) {
      out.print("empty");
    }
    else {
      out.println();

      while(iterator.hasNext()) {
        out.println(iterator.next());
      }
    }

    out.close();
  }

  public static void main(String args[]) throws Exception {
    SerFile serFile = new SerFile();

    serFile.read(args[0]);

    System.out.println("serfile's length : " + serFile.length());

    serFile.print(args[2]);

    if(args.length > 1) {
      serFile.write(args[1]);
    }
  }

}

