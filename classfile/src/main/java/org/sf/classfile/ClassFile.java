// ClassFile.java

package org.sf.classfile;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;

/**
 * Class that represent single class file
 *
 * @version 1.0 06/13/2001
 * @author Alexander Shvets
 */
public class ClassFile extends Entry {
  public static final String TYPE = "ClassFile";

  public static final int JAVA_MAGIC         = 0xCAFEBABE;
  public static final int JAVA_VERSION       = 45;
  public static final int JAVA_MINOR_VERSION = 3;

  protected int magicNumber;
  protected short minorVersion, majorVersion;

  protected ConstPool constPool = new ConstPool();

  protected AccessFlags accessFlags = new ClassAccessFlags();

  protected short thisClassIndex;
  protected short superClassIndex;

  protected EntryCollection interfaces =
                 new EntryCollection(InterfaceEntry.class, "Interfaces");

  protected EntryCollection fields =
                 new EntryCollection(FieldEntry.class, "Fields");

  protected EntryCollection methods =
                 new EntryCollection(MethodEntry.class, "Methods");

  protected EntryCollection attributes =
                 new EntryCollection(AttributeEntry.class, "Class Attributes");

  // for checking some modification of class file
  private boolean changed = false;

  /**
   * Reads class file from input stream.
   *
   * @param  in  the input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public synchronized void read(DataInput in) throws IOException {
    magicNumber = in.readInt();

    if(magicNumber != JAVA_MAGIC) {
      throw new IOException("Illegal magic number '0x" +
                             Integer.toHexString(magicNumber) + "'");
    }

    minorVersion = in.readShort();
    majorVersion = in.readShort();

    constPool.read(in);

    accessFlags.read(in);

    thisClassIndex = in.readShort();
    superClassIndex = in.readShort();

    interfaces.read(in);
    fields.read(in);
    methods.read(in);
    attributes.read(in);

    changed = false;
  }

  /**
   * Writes class file to output stream.
   *
   * @param  out  the output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    out.writeInt(magicNumber);

    out.writeShort(minorVersion);
    out.writeShort(majorVersion);

    constPool.write(out);

    accessFlags.write(out);

    out.writeShort(thisClassIndex);
    out.writeShort(superClassIndex);

    interfaces.write(out);
    fields.write(out);
    methods.write(out);
    attributes.write(out);

    changed = false;
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
   * Gets the length of class file
   *
   * @return  length of class file.
   */
  public long length() {
    return 4 + 2 + 2 + // CAFEBABE, Minor version, Major version
           2 + constPool.length() +
           2 + 2 + 2 +
           interfaces.length() +
           fields.length() +
           methods.length() +
           attributes.length();
  }

  /**
   * Reads bytecodes from a file.
   *
   * @param  fileName the name of the file with bytecodes.
   * @exception  IOException if an I/O error occurs.
   */
  public synchronized void read(String fileName) throws IOException {
    DataInputStream in = new DataInputStream(
             new BufferedInputStream(
                          new FileInputStream(fileName), 8192));
    read(in);

    in.close();
  }

  /**
   * Writes bytecodes to a file.
   *
   * @param  fileName the name of the file with bytecodes.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(String fileName) throws IOException {
    DataOutputStream out = new DataOutputStream(
              new BufferedOutputStream(
                          new FileOutputStream(fileName), 8192));
    write(out);

    out.close();
  }

  /**
   * Checks if class file has changed.
   *
   * @return true, if class file has changed; false otherwise
   */
  public boolean isChanged() {
    return changed;
  }

  /**
   * Sets up a flag that keeps changes in class file.
   *
   * @param changed  whether changes in class file took place or not
   */
  public void setChanged(boolean changed) {
    this.changed = changed;
  }

  /**
   * Gets the magic number.
   *
   * @return the magic number
   */
  public int getMagicNumber() {
    return magicNumber;
  }

  /**
   * Gets the minor version.
   *
   * @return the minor version
   */
  public short getMinorVersion() {
    return minorVersion;
  }

  /**
   * Gets the major version.
   *
   * @return the major version
   */
  public short getMajorVersion() {
    return majorVersion;
  }

  /**
   * Gets the index to this class name in const pool.
   *
   * @return the index to this class name in const pool
   */
  public short getThisClassIndex() {
    return thisClassIndex;
  }

  /**
   * Gets the index to super class name in const pool.
   *
   * @return the index to super class name in const pool
   */
  public short getSuperClassIndex() {
    return superClassIndex;
  }

  /**
   * Gets the access flags for this class.
   *
   * @return the access flags for this class
   */
  public AccessFlags getAccessFlags() {
    return accessFlags;
  }

  /**
   * Gets the const pool.
   *
   * @return the const pool
   */
  public ConstPool getConstPool() {
    return constPool;
  }

  /**
   * Gets the list of interfaces, implemented by this class.
   *
   * @return the list of interfaces, implemented by this class
   */
  public EntryCollection getInterfaces() {
    return interfaces;
  }

  /**
   * Gets the list of fields, defined in this class.
   *
   * @return the list of fields, defined in this class
   */
  public EntryCollection getFields() {
    return fields;
  }

  /**
   * Gets the list of methods, defined in this class.
   *
   * @return the list of methods, defined in this class
   */
  public EntryCollection getMethods() {
    return methods;
  }

  /**
   * Gets the list of attributes, defined in this class.
   *
   * @return the list of attributes, defined in this class
   */
  public EntryCollection getAttributes() {
    return attributes;
  }

  public static void main(String args[]) throws Exception {
    ClassFile classFile = new ClassFile();

    classFile.read(args[0]);

    System.out.println("File length      :" + new File(args[0]).length());
    System.out.println("ClassFile length :" + classFile.length());

    classFile.write(args[1]);
  }

}
