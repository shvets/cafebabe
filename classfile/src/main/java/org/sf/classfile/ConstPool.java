// ConstPool.java

package org.sf.classfile;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

/**
 * Class that represents a collection of entities.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public final class ConstPool extends EntryCollection implements Pool {
  public static final String TYPE = "ConstantPool";

  /* A table with predefined names for all constants that can exist
   * inside constant pool
   */
  private static Class[] table = new Class[13];

  static {
    table[0]                                       = null;
    table[ConstPoolEntry.CONSTANT_UTF8]            = Utf8Const.class;
    table[ConstPoolEntry.CONSTANT_INTEGER]         = IntegerConst.class;
    table[ConstPoolEntry.CONSTANT_FLOAT]           = FloatConst.class;
    table[ConstPoolEntry.CONSTANT_LONG]            = LongConst.class;
    table[ConstPoolEntry.CONSTANT_DOUBLE]          = DoubleConst.class;
    table[ConstPoolEntry.CONSTANT_CLASS]           = ClassConst.class;
    table[ConstPoolEntry.CONSTANT_STRING]          = StringConst.class;
    table[ConstPoolEntry.CONSTANT_FIELD]           = FieldConst.class;
    table[ConstPoolEntry.CONSTANT_METHOD]          = MethodConst.class;
    table[ConstPoolEntry.CONSTANT_INTERFACEMETHOD] = InterfaceMethodConst.class;
    table[ConstPoolEntry.CONSTANT_NAMEANDTYPE]     = NameAndTypeConst.class;
    table[ConstPoolEntry.CONSTANT_UNICODE]         = UnicodeConst.class;
  }

  private short size;

  /**
   * Creates a collection of entries that all have basic parent - PoolEntry
   */
  public ConstPool() {
    super(ConstPoolEntry.class, TYPE);
  }

  /**
   * Gets specified entry from a pool. Implementation of Pool interface.
   *
   * @param   index  the index of entry in a pool.
   * @return  specified entry
   */
  public PoolEntry getEntry(short index) {
    return (PoolEntry)entries.get(index);
  }

  /**
   * Reads const pool from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException {
    size = in.readShort();

    // First slot (0) is always unused
    entries.add(null);

    int i = 1;
    while(i < size) {
      PoolEntry entry = (PoolEntry)createEntry(in);
      entries.add(entry);

      // long and double constants occupy 2 entries of const pool
      if((entry instanceof LongConst) || (entry instanceof DoubleConst)) {
        entries.add(null);
        i++;
      }

      i++;
    }
  }

  /**
   * Creates a const entry for this constant pool.
   *
   * @param  in  input stream.
   * @return an entry with the specified type
   */
  protected Entry createEntry(DataInput in) throws IOException {
    Entry entry = null;

    int tag = in.readByte();

    try {
      entry = (PoolEntry)(table[tag]).newInstance();
    }
    catch(Exception e) {
      System.out.println("Unknown tag: " + tag);
    }

    if(entry != null) {
      entry.read(in);
    }

    return entry;
  }

  /**
   * Writes constant pool to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    out.writeShort(size);

    int i = 1;
    while(i < size) {
      PoolEntry entry = (PoolEntry)entries.get(i);
      entry.write(out);

      // long and double constants occupy 2 entries of const pool
      if((entry instanceof LongConst) || (entry instanceof DoubleConst)) {
        i++;
      }

      i++;
    }
  }

  /**
   * Gets the number of entries in collection
   *
   * @return  the number of entries in collection
   */
  public int size() {
    return size;
  }

  /**
   * Calculates length of collection in bytes
   *
   * @return  length of collection in bytes
   */
  public long length() {
    long len = 0;

    for(int i=1; i < entries.size(); i++) {
      Entry e = (Entry)entries.get(i);

      if(e != null) {
        len += e.length();
      }
    }

    return len;
  }

  /**
   * Adds new entry to a constant pool.
   *
   * @param  entry  the entry to be added.
   */
  public short add(PoolEntry entry) {
    short index = indexOf(entry);

    if(index == -1) {
      entries.add(entry);

      index = (short)(entries.size()-1);
    }

    size = index;

    return size;
  }

  public short indexOf(PoolEntry entry) {
    return (short)entries.indexOf(entry);
  }

}
