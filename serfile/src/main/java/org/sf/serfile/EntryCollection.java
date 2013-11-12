// EntryCollection.java

package org.sf.serfile;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class that represents a container, a collection of entries e.g.
 * constant pool, interfaces list, methods list etc.
 * Collection of entries like constant pool, lists of fields, methods,
 * class attributes etc.
 *
 * @version 1.0 06/10/2001
 * @author Alexander Shvets
 */
public class EntryCollection extends Entry {

  /** The list that holds all entries in a collection */
  protected ArrayList entries = new ArrayList();

  /** The type of class that is allowable for elements of this collection.
   *  Collection must contains only enities of specified type.
   */
  private Class clazz;

  /** The type of the entry */
  private String type;

  /**
   * Creates a collection that can hold only elements of specified type.
   *
   * @param clazz  the type of allowed in this collection class
   */
  public EntryCollection(Class clazz, String type) {
    this.clazz = clazz;
    this.type  = type;
  }

  /**
   * Reads this collection from input stream.
   *
   * @param  in  the input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException {
    short cnt = in.readShort();

    for(int i=0; i < cnt; i++) {
      entries.add(createEntry(in));
    }
  }

  /**
   * Creates an entry for this collection with the predefined type.
   *
   * @param  in  input stream.
   * @return an entry with the specified type
   */
  protected Entry createEntry(DataInput in) throws IOException {
    try {
      Entry entry = (Entry)clazz.newInstance();

      entry.read(in);

      return entry;
    }
    catch(Exception e) {
      throw new IOException(e.toString());
    }
  }

  /**
   * Writes this collection to output stream.
   *
   * @param  out  the output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    short cnt = (short)entries.size();

    out.writeShort(cnt);

    for(int i=0; i < cnt; i++) {
      ((Entry)entries.get(i)).write(out);
    }
  }

  /**
   * Gets the type of this entry.
   *
   * @return  the type of the entry
   */
  public String getType() {
    return type;
  }

  /**
   * Calculates the length of this collection in bytes
   *
   * @return  the length of еру collection in bytes
   */
  public long length() {
    int len = 0;

    for(int i=0; i < entries.size(); i++) {
      len += ((Entry)entries.get(i)).length();
    }

    return 2 + len;
  }

  /**
   * Gets the number of entries in the collection
   *
   * @return  the number of entries in the collection
   */
  public int size() {
    return entries.size();
  }

  /**
   * Adds new entry to a collection.
   *
   * @param  entry  the entry to be added.
   */
  public void add(Entry entry) {
    if(!entries.contains(entry)) {
      entries.add(entry);
    }
  }

  /**
   * Removes the entry from the collection.
   *
   * @param  entry  the entry to be removed.
   */
  public void remove(Entry entry) {
    entries.remove(entry);
  }

  /**
   * Gets the entry that resides at the specified index.
   *
   * @param  index  a position of entry in this collection
   * @return an entry in this collection that resides at the specified index
   */
  public Entry get(short index) {
    return (Entry)entries.get(index);
  }

  /**
   * Sets the entry at the specified index in this collection.
   *
   * @param  index  the position in collection where new entry will be set up
   * @param entry  the entry that should be set up
   */
  public void set(short index, Entry entry) {
    entries.set(index, entry);
  }

  /**
   * Removes all entries from the collection.
   */
  public void clear() {
    entries.clear();
  }

  /**
   * Clones this collection.
   *
   * @return  a clone of this object
   */
  public Object clone() {
    EntryCollection e = new EntryCollection(clazz, type);

    e.entries = (ArrayList)entries.clone();

    return e;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return  a string representation of the object.
   */
  public String toString() {
    StringBuffer sb = new StringBuffer();

    int sz = entries.size();

    for(int i=0; i < sz; i++) {
      sb.append(" " + entries.get(i).toString());
    }

    return "{(" + sz + ")" + sb + "}";
  }

}
