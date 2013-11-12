// ClassTreeModel.java

package org.sf.cafebabe.gadget.classtree;

import java.io.IOException;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.MutableTreeNode;

import org.sf.classfile.ClassFile;
import org.sf.classfile.ConstPool;
import org.sf.classfile.EntryCollection;
import org.sf.classfile.Entry;
import org.sf.classfile.PoolEntry;
import org.sf.classfile.InterfaceEntry;
import org.sf.classfile.MemberEntry;
import org.sf.classfile.FieldEntry;
import org.sf.classfile.MethodEntry;
import org.sf.classfile.AttributeEntry;
import org.sf.classfile.Converter;
import org.sf.classfile.Utf8Const;

import org.sf.cafebabe.Constants;
import org.sf.classfile.attribute.MethodBody;
import org.sf.classfile.attribute.SourceFileAttribute;
import org.sf.classfile.attribute.ConstantValueAttribute;
import org.sf.classfile.attribute.InnerClassesAttribute;
import org.sf.classfile.attribute.ExceptionsAttribute;
import org.sf.classfile.attribute.LineNumberTableAttribute;
import org.sf.classfile.attribute.LocalVariableTableAttribute;
import org.sf.classfile.attribute.CodeAttribute;
import org.sf.classfile.attribute.InnerClassEntry;
import org.sf.classfile.attribute.ExceptionEntry;
import org.sf.classfile.attribute.ExceptionHandlerEntry;
import org.sf.classfile.attribute.LineNumberEntry;
import org.sf.classfile.attribute.LocalVariableEntry;

/**
 * Data model for ClassTree object
 */
public class ClassTreeModel extends DefaultTreeModel implements Constants {
  private static String NONE_STRING            = "None";
  private static String MAX_STACK_STRING       = "Max Stack";
  private static String MAX_LOCALS_STRING      = "Max Locals";

  private RootNode root;
  private ConstPool constPool;

  private ClassFile classFile;

  /**
   * Creates new class tree model
   *
   * @param classFile  the class file
   */
  public ClassTreeModel(ClassFile classFile) {
    super(new RootNode());

    this.classFile = classFile;

    root = (RootNode)getRoot();

    constPool = classFile.getConstPool();

    int magicNumber    = classFile.getMagicNumber();
    short minorVersion = classFile.getMinorVersion();
    short majorVersion = classFile.getMajorVersion();

    MagicNumberNode magicNumberNode = new MagicNumberNode(MAGIC_NUMBER_TEXT, magicNumber);
    VersionNode minorVersionNode    = new VersionNode(MINOR_VERSION_TEXT, minorVersion);
    VersionNode majorVersionNode    = new VersionNode(MAJOR_VERSION_TEXT, majorVersion);
    EntryNode accessFlagsNode       = new EntryNode(true, classFile.getAccessFlags());
    ClassReferenceNode thisClassNode  = new ClassReferenceNode(classFile.getThisClassIndex());
    ClassReferenceNode superClassNode = new ClassReferenceNode(classFile.getSuperClassIndex());

    CollectionNode constPoolFolder = new CollectionNode(ConstPool.TYPE, constPool);
    ClassTreeNode accessFlagsFolder = new ClassTreeNode(ACCESS_FLAGS_TEXT, true);
    ClassTreeNode thisClassFolder   = new ClassTreeNode(THIS_CLASS_TEXT, true);
    ClassTreeNode superClassFolder  = new ClassTreeNode(SUPER_CLASS_TEXT, true);

    insertCollection(constPoolFolder);

    addNode(accessFlagsFolder, accessFlagsNode);
    addNode(thisClassFolder, thisClassNode);
    addNode(superClassFolder, superClassNode);

    CollectionNode interfacesFolder  = new CollectionNode(INTERFACES_TEXT, classFile.getInterfaces());
    CollectionNode fieldsFolder      = new CollectionNode(FIELDS_TEXT, classFile.getFields());
    CollectionNode methodsFolder     = new CollectionNode(METHODS_TEXT, classFile.getMethods());
    CollectionNode classAttrFolder   = new CollectionNode(CLASS_ATTRIBUTES_TEXT, classFile.getAttributes());

    insertCollection(interfacesFolder);
    insertCollection(fieldsFolder);
    insertCollection(methodsFolder);
    insertCollection(classAttrFolder);

    addNode(root, magicNumberNode);
    addNode(root, minorVersionNode);
    addNode(root, majorVersionNode);
    addNode(root, constPoolFolder);
    addNode(root, accessFlagsFolder);
    addNode(root, thisClassFolder);
    addNode(root, superClassFolder);

    addNode(root, interfacesFolder);
    addNode(root, fieldsFolder);
    addNode(root, methodsFolder);
    addNode(root, classAttrFolder);
  }

  /**
   * Adds new node to the tree
   *
   * @param parent the parent node
    * @param node the node to add
   */
  public void addNode(MutableTreeNode parent, MutableTreeNode node) {
    insertNodeInto(node, parent, parent.getChildCount());
  }

  private void insertCollection(CollectionNode folder) {
    EntryCollection entries = folder.getEntries();

    if(entries.size() == 0) {
      addNode(folder, new LeafNode(NONE_STRING));
    }
    else {
      for(short i=0; i < entries.size(); i++) {
        insert(folder, entries.get(i));
      }
    }
  }

  private void insert(FolderNode folder, Entry entry) {
    if(entry instanceof PoolEntry) {
      insert(folder, (PoolEntry)entry);
    }
    else if(entry instanceof InterfaceEntry) {
      insert(folder, (InterfaceEntry)entry);
    }
    else if(entry instanceof FieldEntry) {
      insert(folder, new EntryNode(true, entry), (MemberEntry)entry);
    }
    else if(entry instanceof MethodEntry) {
      insert(folder, new EntryNode(true, entry), (MemberEntry)entry);
    }
    else if(entry instanceof AttributeEntry) {
      insert(folder, (AttributeEntry)entry);
    }
  }

  private void insert(FolderNode folder, PoolEntry constant) {
    if(constant != null) {
      addNode(folder, new EntryNode(false, constant));
    }
  }

  private void insert(FolderNode folder, InterfaceEntry entry) {
    addNode(folder, new EntryNode(false, entry));
  }

  private void insert(FolderNode folder, ClassTreeNode node, MemberEntry entry) {
    addNode(folder, node);

    node.setUserObject(entry.resolve(constPool));

    EntryCollection attributes = entry.getAttributes();

    CollectionNode attrFolder = new CollectionNode(attributes.getType(), attributes);

    addNode(node, attrFolder);

    insertCollection(attrFolder);
  }

  private void insert(FolderNode folder, AttributeEntry attribute) {
    String attrName = attribute.resolve(constPool);

    if(attrName.equals(AttributeEntry.SOURCE_FILE)) {
      insertSourceFileAttribute(folder, attribute);
    }
    else if(attrName.equals(AttributeEntry.CONSTANT_VALUE)) {
      insertConstantValueAttribute(folder, attribute);
    }
    else if(attrName.equals(AttributeEntry.INNER_CLASSES)) {
      insertInnerClassesAttribute(folder, attribute);
    }
    else if(attrName.equals(AttributeEntry.EXCEPTIONS)) {
      insertExceptionsAttribute(folder, attribute);
    }
    else if(attrName.equals(AttributeEntry.CODE)) {
      insertCode(folder, attribute);
    }
    else if(attrName.equals(AttributeEntry.LINE_NUMBER_TABLE)) {
      insertLineNumberTableAttribute(folder, attribute);
    }
    else if(attrName.equals(AttributeEntry.LOCAL_VARIABLE_TABLE)) {
      insertLocalVariableTableAttribute(folder, attribute);
    }
    else {
      insertUnknown(folder, attribute);
    }
  }

  private void insertUnknown(FolderNode folder, AttributeEntry attribute) {
    byte[] buffer = attribute.getBuffer();
    String name   = attribute.resolve(constPool);

    AttributeNode attrNode = new AttributeNode(name, attribute);
    addNode(folder, attrNode);

    String text = null;

    if(buffer.length == 2) {
      short index = (short)((buffer[0] & 0xff) << 8 | (buffer[1] & 0xff));
      if(index < constPool.size()) {
        PoolEntry pe = constPool.getEntry(index);

        if(pe instanceof Utf8Const) {
          Utf8Const utf8Const = (Utf8Const)pe;

          text = utf8Const.getValue();
        }
        else {
          text = Converter.toHexString(buffer);
        }
      }
      else {
        text = Converter.toHexString(buffer);
      }
    }
    else if(buffer.length > 0) {
      text = Converter.toHexString(buffer);
    }

    if(text != null) {
      addNode(attrNode, new LeafNode(text));
    }
  }

  private void insertSourceFileAttribute(FolderNode folder, AttributeEntry attribute) {
    AttributeNode attrNode = new AttributeNode(AttributeEntry.SOURCE_FILE, attribute);

    addNode(folder, attrNode);

    try {
      SourceFileAttribute a = new SourceFileAttribute(attribute);

      PoolEntry entry = constPool.getEntry(a.getIndex());

      String sourceFileName = entry.resolve(constPool);

      addNode(attrNode, new LeafNode(sourceFileName));
    }
    catch(IOException e) {
      e.printStackTrace();
    }
  }

  private void insertConstantValueAttribute(FolderNode folder, AttributeEntry attribute) {
    AttributeNode attrNode = new AttributeNode(AttributeEntry.CONSTANT_VALUE, attribute);

    addNode(folder, attrNode);

    try {
      ConstantValueAttribute a = new ConstantValueAttribute(attribute);

      PoolEntry entry = constPool.getEntry(a.getIndex());

      String constantValue = entry.resolve(constPool);

      addNode(attrNode, new LeafNode(constantValue));
    }
    catch(IOException e) {
      e.printStackTrace();
    }
  }

  private void insertInnerClassesAttribute(FolderNode folder, AttributeEntry attribute) {
    AttributeNode attrNode = new AttributeNode(AttributeEntry.INNER_CLASSES, attribute);

    addNode(folder, attrNode);

    try {
      InnerClassesAttribute a = new InnerClassesAttribute(attribute);

      InnerClassEntry[] innerClasses = a.getInnerClasses();

      for(int i=0; i < innerClasses.length; i++) {
        InnerClassEntry innerClass = innerClasses[i];

        addNode(attrNode, new LeafNode(innerClass.resolve(constPool)));
      }
    }
    catch(IOException e) {
      e.printStackTrace();
    }
  }

  private void insertExceptionsAttribute(FolderNode folder, AttributeEntry attribute) {
    AttributeNode attrNode = new AttributeNode(AttributeEntry.EXCEPTIONS, attribute);
    addNode(folder, attrNode);

    try {
      ExceptionsAttribute a = new ExceptionsAttribute(attribute);
      ExceptionEntry[] exceptions = a.getExceptions();

      for(int i=0; i < exceptions.length; i++) {
        ExceptionEntry exception = exceptions[i];

        addNode(attrNode, new LeafNode(exception.resolve(constPool)));
      }
    }
    catch(IOException e) {
      e.printStackTrace();
    }
  }

  private void insertCode(FolderNode folder, AttributeEntry attribute) {
    AttributeNode codeAttrNode = new AttributeNode(AttributeEntry.CODE, attribute);
    addNode(folder, codeAttrNode);

    try {
      CodeAttribute codeAttribute = new CodeAttribute(attribute);

      addNode(codeAttrNode, new ShortNode(MAX_STACK_STRING, codeAttribute.getMaxStack()));
      addNode(codeAttrNode, new ShortNode(MAX_LOCALS_STRING, codeAttribute.getMaxLocals()));
      addNode(codeAttrNode, new MethodBodyNode(MethodBody.TYPE, codeAttribute.getMethodBody()));

      EntryCollection exceptionHandlers = codeAttribute.getExceptionHandlers();

      CollectionNode exceptionTableNode = new CollectionNode(exceptionHandlers.getType(), exceptionHandlers);
      addNode(codeAttrNode, exceptionTableNode);

      for(short i=0; i < exceptionHandlers.size(); i++) {
        ExceptionHandlerEntry exHandler = (ExceptionHandlerEntry)exceptionHandlers.get(i);

        addNode(exceptionTableNode, new EntryNode(false, exHandler));
      }

      EntryCollection attributes = codeAttribute.getAttributes();

      CollectionNode attributesNode = new CollectionNode(attributes.getType(), attributes);
      addNode(codeAttrNode, attributesNode);

      for(short i=0; i < attributes.size(); i++) {
        insert(attributesNode, (AttributeEntry)attributes.get(i));
      }
    }
    catch(IOException e) {
      e.printStackTrace();
    }
  }

  private void insertLineNumberTableAttribute(FolderNode folder, AttributeEntry attribute) {
    AttributeNode attrNode = new AttributeNode(AttributeEntry.LINE_NUMBER_TABLE, attribute);
    addNode(folder, attrNode);

    try {
      LineNumberTableAttribute a = new LineNumberTableAttribute(attribute);
      LineNumberEntry[] lineNumbers = a.getLineNumbers();

      for(int i=0; i < lineNumbers.length; i++) {
        addNode(attrNode, new EntryNode(false, lineNumbers[i]));
      }
    }
    catch(IOException e) {
      e.printStackTrace();
    }
  }

  private void insertLocalVariableTableAttribute (FolderNode folder, AttributeEntry attribute) {
    AttributeNode attrNode = new AttributeNode(AttributeEntry.LOCAL_VARIABLE_TABLE, attribute);
    addNode(folder, attrNode);

    try {
      LocalVariableTableAttribute a = new LocalVariableTableAttribute(attribute);

      LocalVariableEntry[] localVariables = a.getLocalVariables();

      for(int i=0; i < localVariables.length; i++) {
        addNode(attrNode, new EntryNode(false, localVariables[i]));
      }
    }
    catch(IOException e) {
      e.printStackTrace();
    }
  }

  public String getToolTipText(TreeNode node) {
    if(classFile != null) {
      return ClassTreeToolTips.getToolTipText(node);
    }

    return null;
  }

}
