// ClassDescSerEntry.java

package org.sf.serfile;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;

/**
 * Class that represents a serialized class description entry.
 *
 * ClassDescSer: TC_CLASSDESC (utf)className (long)serialVersionUID newHandle classDescInfo
 *
 * newHandle: "The next number in sequence is assigned to the object
 *            being serialized or deserialized"
 *
 * classDescInfo: classDescFlags(byte) (short)<count> fieldDesc[count] classAnnotation classDesc
 * classDesc - "describes super class"
 *
 * fieldDesc: (prim_typecode (utf)fieldName) | (obj_typecode (utf)fieldName typeName)
 *
 * prim_typecode: 'B' | `C' | `D' | `F' | 'I' | `J' | `S' | `Z'
 *
 * obj_typecode: `[` | `L'
 *
 * typeName: (String)classDesc // String containing the field's type
 *
 * @version 1.0 05/25/2000
 * @author Alexander Shvets
 */
public class ClassDescSerEntry extends HandleSerEntry {

  /** class name */
  private String className;

  /** serial version UID */
  private long serialVersionUID;

  /** Serialization flags */
  private SerFlags serFlags;

  /** Fields */
  private Fields fields;

  /** Class annotation */
  private Annotation classAnnotation;

  /** Class desctiption for super-class */
  private ClassDescEntry superClassDescEntry;

  /**
   * The constructor that creates serialized class description entry
   *
   * @param handlesTable  table of handles
   */
  public ClassDescSerEntry(HandlesTable handlesTable) {
    super(handlesTable);
  }

  /**
   * Reads this entry from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException  {
    className        = in.readUTF();
    serialVersionUID = in.readLong();

    handle = handlesTable.put(this);

    (serFlags = new SerFlags()).read(in);

    (fields = new Fields(handlesTable)).read(in);

    (classAnnotation = new Annotation("Class Annotation", handlesTable)).read(in);

    (superClassDescEntry = new ClassDescEntry(handlesTable)).read(in);
  }

  /**
   * Writes this entry to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    out.writeByte(getTag());

    out.writeUTF(className);
    out.writeLong(serialVersionUID);

    serFlags.write(out);

    fields.write(out);
    classAnnotation.write(out);
    superClassDescEntry.write(out);
  }

  /**
   * Gets the type of this entry.
   *
   * @return  the type of entry
   */
  public String getType() {
    return "Class Description";
  }

  /**
   * Gets the length of this entry in bytes.
   *
   * @return  the length of entry
   */
  public long length() {
    return 1 + (2 + className.length()) + 8 + 1 + fields.length() +
           classAnnotation.length() + superClassDescEntry.length();
  }

  /**
   * Gets the tag of this entry.
   *
   * @return  the tag for entry
   */
  public byte getTag() {
    return TC_CLASSDESC;
  }

  /**
   * Gets class name
   *
   * @return class name
   */
  public String getClassName() {
    return className;
  }

  /**
   * Gets serial version UID
   *
   * @return serial version UID
   */
  public long getSerialVersionUID() {
    return serialVersionUID;
  }

  /**
   * Gets serialization flags
   *
   * @return serialization flags
   */
  public SerFlags getSerFlags() {
    return serFlags;
  }

  /**
   * Gets fields
   *
   * @return fields
   */
  public EntryCollection getFields() {
    return fields;
  }

  /**
   * Gets class annotation
   *
   * @return class annotation
   */
  public Annotation getClassAnnotation() {
    return classAnnotation;
  }

  /**
   * Gets class desctiption for super-class
   *
   * @return class desctiption for super-class
   */
  public ClassDescEntry getSuperClassDescEntry() {
    return superClassDescEntry;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return  a string representation of the object.
   */
  public String toString() {
    StringBuffer sb = new StringBuffer();

    sb.append(getType() + "(" + handle + ")");
    sb.append(" ->\n");
    sb.append("{\n");
    sb.append("  className         : " + className + "\n");
    sb.append("  serialVersionUID  : " + serialVersionUID + "\n");
    sb.append("  serFlags          : " + serFlags + "\n");

    sb.append("  Fields            : ");
    if(fields.size() == 0) {
      sb.append("none\n");
    }
    else {
      sb.append("\n");
      for(short i=0; i < fields.size(); i++) {
        FieldEntry fieldEntry = (FieldEntry)fields.get(i);

        sb.append("    " + fieldEntry + "\n");
      }
    }

    HandleSerEntry superEntry = superClassDescEntry.getValue();

    sb.append("  Class Annotation  : " + classAnnotation + "\n");

    sb.append("  superHandle       : " + superEntry.getType() +
                                         "(" + superEntry.getHandle() + ")" + "\n");
    sb.append("}");

    return sb.toString();
  }

}
