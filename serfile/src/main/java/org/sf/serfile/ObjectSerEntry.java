// ObjectSerEntry.java

package org.sf.serfile;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;
import java.util.List;
import java.util.ArrayList;

/*
 * Class that represents a serialized object entry.
 *
 * ObjectSer: TC_OBJECT ClassDesc newHandle classdata[] // data for each class
 *
 * classdata:
 *       values |              // SC_SERIALIZABLE   && !SC_WRITE_METHOD
 *       (values Annotation) | // SC_SERIALIZABLE   && SC_WRITE_METHOD
 *       externalContents    | // SC_EXTERNALIZABLE && !SC_BLOCKDATA
 *       Annotation            // SC_EXTERNALIZABLE && SC_BLOCKDATA
 *
 * values // fields in order of class descriptor
 * values: // The size and types are described by the classDesc for the current object
 *
 * externalContents:  externalContent | (externalContents externalContent)
 * // externalContent written by writeExternal in PROTOCOL_VERSION_1
 *
 * // Only parseable by readExternal
 * externalContent: (bytes) // primitive data | object
 *
 * @version 1.0 05/25/2000
 * @author Alexander Shvets
 */
public class ObjectSerEntry extends HandleSerEntry {

  private List classesList    = new ArrayList();
  private List valuesList     = new ArrayList();
  private List annotationsList = new ArrayList();

  /** Class description for this object entry */
  private ClassDescEntry classDescEntry;

  /** Externalizable information */
  private ContentEntry externalizableEntry;

  /**
   * The constructor that creates serialized object entry
   *
   * @param handlesTable  table of handles
   */
  public ObjectSerEntry(HandlesTable handlesTable) {
    super(handlesTable);
  }

  /**
   * Reads this entry from input stream.
   *
   * @param  in  input stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void read(DataInput in) throws IOException  {
    (classDescEntry = new ClassDescEntry(handlesTable)).read(in);

    handle = handlesTable.put(this);

    classesList.clear();
    valuesList.clear();
    annotationsList.clear();

    ClassDescSerEntry classDescSerEntry = classDescEntry.getClassDescSerEntry();

    HandleSerEntry desc = classDescSerEntry;
    while(desc != null) {
      classesList.add(desc);

      ClassDescEntry descEntry = ((ClassDescSerEntry)desc).getSuperClassDescEntry();

      desc = descEntry.getClassDescSerEntry();
    }

    SerFlags serFlags = classDescSerEntry.getSerFlags();

    if(serFlags.isSerializable()) {
      valuesList = new ArrayList();
      annotationsList = new ArrayList();

      for(int i=0; i < classesList.size(); i++) {
        valuesList.add(new Object());
        annotationsList.add(new Object());
      }

      for(int i=0; i < classesList.size(); i++) {
        valuesList.add(new Object());
      }


      for(int i=classesList.size()-1; i >=0; i--) {
        ClassDescSerEntry desc_i = (ClassDescSerEntry)classesList.get(i);

        Values values = new Values(handlesTable, desc_i.getFields());
        values.read(in);

        valuesList.set(i, values);

        if(desc_i.getSerFlags().hasOwnWriteMethod()) {
          Annotation objectAnnotation = new Annotation("Object Annotation", handlesTable);
          objectAnnotation.read(in);

          annotationsList.set(i, objectAnnotation);
        }
      }
    }
    else if(serFlags.isExternalizable()) {
      annotationsList = new ArrayList();
      annotationsList.add(new Object());

      if(serFlags.isBlockDataMode()) {
        Annotation objectAnnotation = new Annotation("Object Annotation", handlesTable);
        objectAnnotation.read(in);

        annotationsList.set(0, objectAnnotation);
      }
      else {
        (externalizableEntry = new ContentEntry(in.readByte(), handlesTable)).read(in);
      }
    }
  }

  /**
   * Writes this entry to output stream.
   *
   * @param  out  output stream.
   * @exception  IOException if an I/O error occurs.
   */
  public void write(DataOutput out) throws IOException {
    out.writeByte(getTag());

    classDescEntry.write(out);

    ClassDescSerEntry classDescSerEntry = classDescEntry.getClassDescSerEntry();
    SerFlags serFlags = classDescSerEntry.getSerFlags();

    if(serFlags.isSerializable()) {
      for(int i=classesList.size()-1; i >=0; i--) {
        ClassDescSerEntry desc_i = (ClassDescSerEntry)classesList.get(i);

        Values values = (Values)valuesList.get(i);
        values.write(out);

        if(desc_i.getSerFlags().hasOwnWriteMethod()) {
          Annotation objectAnnotation = (Annotation)annotationsList.get(i);
          objectAnnotation.write(out);
        }
      }
    }
    else if(serFlags.isExternalizable()) {
      if(serFlags.isBlockDataMode()) {
        Annotation objectAnnotation = (Annotation)annotationsList.get(0);
        objectAnnotation.write(out);
      }
      else {
        externalizableEntry.write(out);
      }
    }
  }

  /**
   * Gets the type of this entry.
   *
   * @return  the type of entry
   */
  public String getType() {
    return "Object";
  }

  /**
   * Gets the length of this entry in bytes.
   *
   * @return  the length of entry
   */
  public long length() {
    long len = 1 + classDescEntry.length();

    ClassDescSerEntry classDescSerEntry = classDescEntry.getClassDescSerEntry();
    SerFlags serFlags = classDescSerEntry.getSerFlags();

    if(serFlags.isSerializable()) {
      for(int i=classesList.size()-1; i >=0; i--) {
        ClassDescSerEntry desc_i = (ClassDescSerEntry)classesList.get(i);

        Values values = (Values)valuesList.get(i);
        len += values.length();

        if(desc_i.getSerFlags().hasOwnWriteMethod()) {
          Annotation objectAnnotation = (Annotation)annotationsList.get(i);
          len += objectAnnotation.length();
        }
      }
    }
    else if(serFlags.isExternalizable()) {
      if(serFlags.isBlockDataMode()) {
        Annotation objectAnnotation = (Annotation)annotationsList.get(0);
        len += objectAnnotation.length();
      }
      else {
        len += externalizableEntry.length();
      }
    }

    return len;
  }

  /**
   * Gets the tag of this entry.
   *
   * @return  the tag for entry
   */
  public byte getTag() {
    return TC_OBJECT;
  }

  /**
   * Gets class description
   *
   * @return   class description
   */
  public ClassDescEntry getClassDescEntry() {
    return classDescEntry;
  }

  /**
   * Gets the list of all classes: this class and its parents
   *
   * @return  the list of all classes
   */
  public List getClassesList() {
    return classesList;
  }

  /**
   * Gets values for this class and its parents
   *
   * @return   values for this class and its parents
   */
  public List getValuesList() {
    return valuesList;
  }

  /**
   * Gets object annotations for this class and its parents
   *
   * @return   object annotation for this class and its parents
   */
  public List getAnnotationsList() {
    return annotationsList;
  }

  /**
   * Gets externalizable information
   *
   * @return   externalizable information
   */
  public ContentEntry getExternalizableEntry() {
    return externalizableEntry;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return  a string representation of the object.
   */
  public String toString() {
    StringBuffer sb = new StringBuffer();

    sb.append(super.toString() + " -> ");

    ClassDescSerEntry classDescSerEntry = classDescEntry.getClassDescSerEntry();

    SerFlags serFlags = classDescSerEntry.getSerFlags();

    if(serFlags.isSerializable()) {
       sb.append("Values :\n");
      for(int i=classesList.size()-1; i >=0; i--) {
        ClassDescSerEntry desc_i = (ClassDescSerEntry)classesList.get(i);

        sb.append("  " + desc_i.getClassName() + " -> ");
        sb.append(desc_i.getType() + "(" + desc_i.getHandle() + ")\n");

        Values values = (Values)valuesList.get(i);
        if(values.size() > 0) {
          sb.append(values.toString() + "\n");
        }

        if(desc_i.getSerFlags().hasOwnWriteMethod()) {
          sb.append("Annotation :\n");
          Annotation objectAnnotation = (Annotation)annotationsList.get(i);
          sb.append(objectAnnotation.toString() + "\n");
        }
      }
    }
    else if(serFlags.isExternalizable()) {
      if(serFlags.isBlockDataMode()) {
        sb.append("Annotation :\n");
        Annotation objectAnnotation = (Annotation)annotationsList.get(0);
        sb.append(objectAnnotation.toString() + "\n");
      }
      else {
        sb.append("(externalizable)\n");
      }
    }

    return sb.toString();
  }

}
