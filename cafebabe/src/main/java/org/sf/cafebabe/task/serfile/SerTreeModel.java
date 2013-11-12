// SerTreeModel.java

package org.sf.cafebabe.task.serfile;

import java.util.Iterator;
import java.util.List;

import javax.swing.tree.TreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.sf.serfile.SerFile;
import org.sf.serfile.Converter;
import org.sf.serfile.Signature;
import org.sf.serfile.Contents;
import org.sf.serfile.Values;
import org.sf.serfile.HandlesTable;
import org.sf.serfile.Entry;
import org.sf.serfile.EntryCollection;
import org.sf.serfile.NullSerEntry;
import org.sf.serfile.StringSerEntry;
import org.sf.serfile.LongStringSerEntry;
import org.sf.serfile.ArraySerEntry;
import org.sf.serfile.ReferenceSerEntry;
import org.sf.serfile.ClassDescSerEntry;
import org.sf.serfile.ClassSerEntry;
import org.sf.serfile.ObjectSerEntry;
import org.sf.serfile.BlockDataShortEntry;
import org.sf.serfile.BlockDataLongEntry;
import org.sf.serfile.EndBlockDataEntry;
import org.sf.serfile.ArrayEntry;
import org.sf.serfile.ContentEntry;
import org.sf.serfile.ValueEntry;
import org.sf.serfile.ObjectEntry;
import org.sf.serfile.StringEntry;
import org.sf.serfile.HandleSerEntry;
import org.sf.serfile.ClassDescEntry;
import org.sf.serfile.ProxyClassDescSerEntry;
import org.sf.serfile.SerFlags;
import org.sf.serfile.Annotation;

import org.sf.cafebabe.Constants;

public class SerTreeModel extends DefaultTreeModel implements Constants {
  public static final String VALUES_TEXT = "Values";

  private RootNode root;

  private SerFile serFile;

  public SerTreeModel() {
    super(new RootNode());

    root = (RootNode)getRoot();
  }

  public void addNode(MutableTreeNode parent, MutableTreeNode node) {
    insertNodeInto(node, parent, parent.getChildCount());
  }

  public void setSerFile(SerFile serFile) {
    this.serFile = serFile;

    short streamMagic   = serFile.getStreamMagic();
    short streamVersion = serFile.getStreamVersion();
    Contents contents = serFile.getContents();
    HandlesTable handlesTable = serFile.getHandlesTable();

    FolderNode streamMagicFolder = new FolderNode(STREAM_MAGIC_TEXT);
    LeafNode streamMagicNode     = new LeafNode(Converter.toHexString(streamMagic, ""));

    addNode(root, streamMagicFolder);
    addNode(streamMagicFolder, streamMagicNode);

    FolderNode streamVersionFolder = new FolderNode(STREAM_VERSION_TEXT);
    LeafNode streamVersionNode     = new LeafNode(Converter.toHexString(streamVersion, ""));

    addNode(root, streamVersionFolder);
    addNode(streamVersionFolder, streamVersionNode);

    insertContents(contents, root);

    insertReferenceTable(handlesTable, root);
  }

  private void insertContents(Contents contents, MutableTreeNode parent) {
    FolderNode contentsFolder = new FolderNode(contents.getType());

    addNode(parent, contentsFolder);

    for(short i=0; i < contents.size(); i++) {
      ContentEntry contentEntry = (ContentEntry)contents.get(i);
      Entry value = contentEntry.getValue();

      if(value instanceof HandleSerEntry) {
        addNode(contentsFolder, new FolderNode(getHandle((HandleSerEntry)value)));
      }
      else {
        insertContent(contentEntry, contentsFolder);
      }
    }
  }

  private void insertReferenceTable(HandlesTable handlesTable, MutableTreeNode parent) {
    FolderNode refsTableFolder = new FolderNode(REFERENCE_TABLE_TEXT);
    addNode(parent, refsTableFolder);

    Iterator iterator = handlesTable.values().iterator();
    while(iterator.hasNext()) {
      insertObject((HandleSerEntry)iterator.next(), refsTableFolder);
    }
  }

  private void insertContent(ContentEntry contentEntry, FolderNode parent) {
    Entry entry = contentEntry.getValue();

    if(entry instanceof NullSerEntry) {
      insert((NullSerEntry)entry, parent);
    }
    else if(entry instanceof StringSerEntry) {
      insert((StringSerEntry)entry, parent);
    }
    else if(entry instanceof LongStringSerEntry) {
      insert((LongStringSerEntry)entry, parent);
    }
    else if(entry instanceof ArraySerEntry) {
      insert((ArraySerEntry)entry, parent);
    }
    else if(entry instanceof ReferenceSerEntry) {
      insert((ReferenceSerEntry)entry, parent);
    }
    else if(entry instanceof ClassDescSerEntry) {
      insert((ClassDescSerEntry)entry, parent);
    }
    else if(entry instanceof ClassSerEntry) {
      insert((ClassSerEntry)entry, parent);
    }
    else if(entry instanceof ObjectSerEntry) {
      insert((ObjectSerEntry)entry, parent);
    }
    else if(entry instanceof BlockDataShortEntry) {
      addNode(parent, new FolderNode(entry.toString()));
    }
    else if(entry instanceof BlockDataLongEntry) {
      addNode(parent, new FolderNode(entry.toString()));
    }
    else if(entry instanceof EndBlockDataEntry) {
      addNode(parent, new FolderNode(entry.toString()));
    }
    else {
      addNode(parent, new FolderNode(entry.toString()));
    }
  }

  private void insertObject(HandleSerEntry entry, FolderNode parent) {
    if(entry instanceof NullSerEntry) {
      insert((NullSerEntry)entry, parent);
    }
    else if(entry instanceof StringSerEntry) {
      insert((StringSerEntry)entry, parent);
    }
    else if(entry instanceof LongStringSerEntry) {
      insert((LongStringSerEntry)entry, parent);
    }
    else if(entry instanceof ArraySerEntry) {
      insert((ArraySerEntry)entry, parent);
    }
    else if(entry instanceof ReferenceSerEntry) {
      insert((ReferenceSerEntry)entry, parent);
    }
    else if(entry instanceof ClassDescSerEntry) {
      insert((ClassDescSerEntry)entry, parent);
    }
    else if(entry instanceof ClassSerEntry) {
      insert((ClassSerEntry)entry, parent);
    }
    else if(entry instanceof ObjectSerEntry) {
      insert((ObjectSerEntry)entry, parent);
    }
  }

  private void insertClassDesc(HandleSerEntry entry, FolderNode parent) {
    if(entry instanceof NullSerEntry) {
      insert((NullSerEntry)entry, parent);
    }
    else if(entry instanceof ReferenceSerEntry) {
      insert((ReferenceSerEntry)entry, parent);
    }
    else if(entry instanceof ProxyClassDescSerEntry) {
      addNode(parent, new FolderNode(getHandle(entry)));
    }
    else if(entry instanceof ClassDescSerEntry) {
      insert((ClassDescSerEntry)entry, parent);
    }
  }

  private void insertArray(HandleSerEntry entry, FolderNode parent) {
    if(entry instanceof NullSerEntry) {
      insert((NullSerEntry)entry, parent);
    }
    else if(entry instanceof ReferenceSerEntry) {
      insert((ReferenceSerEntry)entry, parent);
    }
    else if(entry instanceof ArraySerEntry) {
      insert((ArraySerEntry)entry, parent);
    }
  }

/*  private void insertString(HandleSerEntry entry, FolderNode parent) {
    if(entry instanceof NullSerEntry) {
      insert((NullSerEntry)entry, parent);
    }
    else if(entry instanceof ReferenceSerEntry) {
      insert((ReferenceSerEntry)entry, parent);
    }
    else if(entry instanceof StringSerEntry) {
      insert((StringSerEntry)entry, parent);
    }
    else if(entry instanceof LongStringSerEntry) {
      insert((LongStringSerEntry)entry, parent);
    }
  }
*/

  private void insert(ObjectSerEntry entry, FolderNode parent) {
    FolderNode objectFolder = new FolderNode(getHandle(entry));

    addNode(parent, objectFolder);

    ClassDescEntry classDescEntry = entry.getClassDescEntry();

    insertClassDesc(classDescEntry.getValue(), objectFolder);

    ClassDescSerEntry classDescSerEntry = classDescEntry.getClassDescSerEntry();

    SerFlags serFlags = classDescSerEntry.getSerFlags();

    if(serFlags.isSerializable()) {
      List classesList     = entry.getClassesList();
      List valuesList      = entry.getValuesList();
      List annotationsList = entry.getAnnotationsList();

      FolderNode valuesFolder = new FolderNode(VALUES_TEXT);
      addNode(objectFolder, valuesFolder);

      for(int i=classesList.size()-1; i >=0; i--) {
        ClassDescSerEntry desc_i = (ClassDescSerEntry)classesList.get(i);

        FolderNode classFolder = new FolderNode(desc_i.getClassName() + ".class");
        addNode(valuesFolder, classFolder);

        Values values = (Values)valuesList.get(i);

        for(short j=0; j < values.size(); j++) {
          insertValue((ValueEntry)values.get(j), classFolder);
        }

        if(desc_i.getSerFlags().hasOwnWriteMethod()) {
          Annotation objectAnnotation = (Annotation)annotationsList.get(i);
          insert(objectAnnotation, classFolder);
        }
      }
    }
    else if(serFlags.isExternalizable()) {
      List annotationsList = entry.getAnnotationsList();
      if(serFlags.isBlockDataMode()) {
        Annotation objectAnnotation = (Annotation)annotationsList.get(0);

        insert(objectAnnotation, objectFolder);
      }
      else {
        insertContent(entry.getExternalizableEntry(), objectFolder);
      }
    }
  }

  private void insert(ClassDescSerEntry entry, FolderNode parent) {
    FolderNode classDescFolder = new FolderNode(getHandle(entry));
    addNode(parent, classDescFolder);

    FolderNode classNameFolder = new FolderNode(CLASS_NAME_TEXT);
    LeafNode classNameNode     = new LeafNode(entry.getClassName());

    addNode(classDescFolder, classNameFolder);
    addNode(classNameFolder, classNameNode);

    FolderNode serialVersionUIDFolder = new FolderNode(SERIAL_VERSION_UID_TEXT);
    LeafNode serialVersionUIDNode     = new LeafNode("" + entry.getSerialVersionUID() + "L");

    addNode(classDescFolder, serialVersionUIDFolder);
    addNode(serialVersionUIDFolder, serialVersionUIDNode);

    SerFlags serFlags = entry.getSerFlags();

    FolderNode flagsFolder = new FolderNode(serFlags.getType());
    LeafNode flagsNode     = new LeafNode(serFlags.toString());

    addNode(classDescFolder, flagsFolder);
    addNode(flagsFolder, flagsNode);

    insertFields(entry.getFields(), classDescFolder);

    insert(entry.getClassAnnotation(), classDescFolder);

    HandleSerEntry superEntry   = entry.getSuperClassDescEntry().getValue();
    FolderNode superClassFolder = new FolderNode(SUPER_CLASS_TEXT);
    FolderNode superClassNode   = new FolderNode(getHandle(superEntry));

    addNode(classDescFolder, superClassFolder);
    addNode(superClassFolder, superClassNode);
  }

  private void insertFields(EntryCollection fields, FolderNode parent) {
    FolderNode fieldsFolder = new FolderNode(fields.getType());
    addNode(parent, fieldsFolder);

    if(fields.size() == 0) {
      LeafNode noneNode = new LeafNode(NONE_TEXT);
      addNode(fieldsFolder, noneNode);
    }
    else {
      for(short i=0; i < fields.size(); i++) {
        insertField((org.sf.serfile.FieldEntry)fields.get(i), fieldsFolder);
      }
    }
  }

  private void insertField(org.sf.serfile.FieldEntry entry, FolderNode parent) {
    StringEntry typeEntry = entry.getTypeEntry();

    if(typeEntry == null) { // primitive
      Signature signature = new Signature(String.valueOf((char)entry.getTag()));
      FolderNode primitiveFolder = new FolderNode(signature./*getTypeName()*/toString());
      LeafNode primitiveNode     = new LeafNode(entry.getName());

      addNode(parent, primitiveFolder);
      addNode(primitiveFolder, primitiveNode);
    }
    else {
      FolderNode compoundFolder = new FolderNode(getHandle(typeEntry.getValue()));
      LeafNode compoundNode     = new LeafNode(entry.getName());

      addNode(parent, compoundFolder);
      addNode(compoundFolder, compoundNode);
    }
  }

  private void insertValue(ValueEntry entry, FolderNode parent) {
    String type = entry.getType().replace('/', '.');
    String name = entry.getName();

    FolderNode valueFolder = new FolderNode(type + " " + name);
    addNode(parent, valueFolder);

    Entry value = entry.getValue();

    if(value instanceof ObjectEntry) {
      insertObject(((ObjectEntry)value).getValue(), valueFolder);
    }
    else if(value instanceof ArrayEntry) {
      insertArray(((ArrayEntry)value).getValue(), valueFolder);
    }
    else {
      addNode(valueFolder, new LeafNode(value.toString()));
    }
  }

  private void insert(ArraySerEntry entry, FolderNode parent) {
    FolderNode arrayFolder = new FolderNode(getHandle(entry));

    addNode(parent, arrayFolder);
    insertClassDesc(entry.getClassDescEntry().getValue(), arrayFolder);

    ValueEntry[] arrayEntries = entry.getArrayEntries();
    for(int i=0; i < arrayEntries.length; i++) {
      insertValue(((ValueEntry)arrayEntries[i]), arrayFolder);
    }
  }

  private void insert(Annotation annotation, FolderNode parent) {
    FolderNode annotationFolder = new FolderNode(annotation.getType());

    addNode(parent, annotationFolder);

    for(short i=0; i < annotation.size(); i++) {
      insertContent((ContentEntry)annotation.get(i), annotationFolder);
    }
  }

  private void insert(StringSerEntry entry, FolderNode parent) {
    FolderNode stringFolder = new FolderNode(getHandle(entry));
    LeafNode stringNode     = new LeafNode(new String(entry.getValue()));

    addNode(parent, stringFolder);
    addNode(stringFolder, stringNode);
  }

  private void insert(LongStringSerEntry entry, FolderNode parent) {
    FolderNode longStringFolder = new FolderNode(getHandle(entry));
    LeafNode longStringNode     = new LeafNode(new String(entry.getValue()));

    addNode(parent, longStringFolder);
    addNode(longStringFolder, longStringNode);
  }

  private void insert(ClassSerEntry entry, FolderNode parent) {
    FolderNode classFolder = new FolderNode(getHandle(entry));

    addNode(parent, classFolder);
    insert(entry.getClassDescSerEntry(), classFolder);
  }

  private void insert(ReferenceSerEntry entry, FolderNode parent) {
    addNode(parent, new FolderNode(getHandle(entry)));
  }

  private void insert(NullSerEntry entry, FolderNode parent) {
    addNode(parent, new FolderNode(getHandle(entry)));
  }

   private String getHandle(HandleSerEntry entry) {
     return entry.getType() + " (" + Converter.toHexString(entry.getHandle(), "") + ")";
   }

  public String getToolTipText(TreeNode node) {
    String nodeText = null;

    if(serFile != null) {
      nodeText = node.toString();
    }

    return nodeText;
  }

}
