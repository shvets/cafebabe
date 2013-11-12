// ClassFilePrinter.java

package org.sf.classfile.attribute;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

import org.sf.classfile.PoolEntry;
import org.sf.classfile.ConstPool;
import org.sf.classfile.AccessFlags;
import org.sf.classfile.AttributeEntry;
import org.sf.classfile.MemberEntry;
import org.sf.classfile.EntryCollection;
import org.sf.classfile.Utf8Const;
import org.sf.classfile.ClassConst;
import org.sf.classfile.InterfaceEntry;
import org.sf.classfile.FieldEntry;
import org.sf.classfile.MethodEntry;
import org.sf.classfile.ClassFile;
import org.sf.classfile.Converter;

import org.sf.classfile.instruction.Instruction;

/**
 * Class that represent the printer class for a class file
 *
 * @version 1.0 06/27/2001
 * @author  Alexander Shvets
 */
public class ClassFilePrinter {
  private PrintWriter out;
  private ConstPool constPool;

  private ClassFile classFile;

  /**
   * Creates new instance of class file printer
   *
   * @param classFile the class file
   */
  public ClassFilePrinter(ClassFile classFile) {
    this.classFile = classFile;
  }

  /**
   * Prints human-like content for class file to a file
   *
   * @param fileName  the name of the file
   * @exception  IOException if an I/O error occurs.
   */
  public void toFile(String fileName) throws IOException {
    out = new PrintWriter(new FileWriter(fileName));

    toFile(out);

    out.close();
  }

  /**
   * Prints human-like content for class file to a file
   *
   * @param     out the output stream
   * @exception IOException if an I/O error occurs.
   */
  public void toFile(PrintWriter out) throws IOException {
    this.out = out;

    int magicNumber = classFile.getMagicNumber();
    short minorVersion = classFile.getMinorVersion();
    short majorVersion = classFile.getMajorVersion();

    print(0, "Magic number  : 0x" + Converter.toHexString(magicNumber));
    println();
    print(0, "Minor version : 0x" + Converter.toHexString(minorVersion));
    println();
    print(0, "Major version : 0x" + Converter.toHexString(majorVersion));
    println();

    constPool = classFile.getConstPool();

    println();
    print(0, "Constant Pool : 0x" + Converter.toHexString((short)constPool.size()));
    println();

    for(short i=1; i < constPool.size(); i++) {
      PoolEntry entry = (PoolEntry)constPool.get(i);

      if(entry == null) {
        continue;
      }

      print(2, "0x" + Converter.toHexString(i) + " " + entry);
      println();
    }

    AccessFlags accessFlags = classFile.getAccessFlags();

    println();
    print(0, "Access flags : 0x" +
             Converter.toHexString(accessFlags.getValue()) + " " +
             accessFlags);
    println();

    short thisIndex = classFile.getThisClassIndex();
    short superIndex = classFile.getSuperClassIndex();

    ClassConst thisClassConst  = (ClassConst)constPool.getEntry(thisIndex);
    ClassConst superClassConst = (ClassConst)constPool.getEntry(superIndex);

    String thisName = thisClassConst.resolve(constPool);
    String superName = superClassConst.resolve(constPool);

    println();
    print(0, "This class   : 0x" + thisIndex + " " + thisName);
    println();

    println();
    print(0, "Super class  : 0x" + superIndex + " " + superName);
    println();

    EntryCollection interfaces = classFile.getInterfaces();

    println();
    print(0, "Interfaces: 0x" + Converter.toHexString((short)interfaces.size()));
    println();

    for(short i=0; i < interfaces.size(); i++) {
      InterfaceEntry entry = (InterfaceEntry)interfaces.get(i);

      print(2, Converter.toHexString((short)(i+1)));
      print(2, entry.resolve(constPool));
      println();
    }

    EntryCollection fields = classFile.getFields();

    println();
    print(0, "Fields: 0x" + Converter.toHexString((short)fields.size()));
    println();

    for(short i=0; i < fields.size(); i++) {
      FieldEntry entry = (FieldEntry)fields.get(i);

      print(2, Converter.toHexString((short)(i+1)));
      printEntry(2, entry);
      println();
    }

    EntryCollection methods = classFile.getMethods();

    println();
    print(0, "Methods: 0x" + Converter.toHexString((short)methods.size()));
    println();

    for(short i=0; i < methods.size(); i++) {
      MethodEntry entry = (MethodEntry)methods.get(i);

      print(2, Converter.toHexString((short)(i+1)));
      printEntry(2, entry);
      println();
    }

    EntryCollection attributes = classFile.getAttributes();

    println();
    print(0, "Class attributes: 0x" + Converter.toHexString((short)attributes.size()));
    println();

    for(short i=0; i < attributes.size(); i++) {
      AttributeEntry entry = (AttributeEntry)attributes.get(i);

      print(2, "0x" + Converter.toHexString((short)(i+1)));
      printEntry(2, entry);
      println();
      println();
    }
  }

  /**
   * Prints member entry (field or method)
   *
   * @param offset the offset
   * @param entry  the member entry
   */
  private void printEntry(int offset, MemberEntry entry) {
    try {
      print(offset, entry.resolve(constPool));
      println();

      EntryCollection attributes1 = entry.getAttributes();

      print(offset+2, "Attributes: 0x" + Converter.toHexString((short)attributes1.size()));
      println();

      for(short i=0;  i < attributes1.size(); i++) {
        AttributeEntry entry1 = (AttributeEntry)attributes1.get(i);

        print(offset+4, "0x" + Converter.toHexString(i));

        printEntry(offset+4, entry1);
        println();
      }
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Prints attribute entry (field or method)
   *
   * @param offset the offset
   * @param attribute  the attribute entry
   */
  private void printEntry(int offset, AttributeEntry attribute) {
    String attrName = attribute.resolve(constPool);

    try {
      if(attrName.equals(AttributeEntry.SOURCE_FILE)) {
        printAttribute(new SourceFileAttribute(attribute));
      }
      else if(attrName.equals(AttributeEntry.CONSTANT_VALUE)) {
        printAttribute(new ConstantValueAttribute(attribute));
      }
      else if(attrName.equals(AttributeEntry.INNER_CLASSES)) {
        printAttribute(offset, new InnerClassesAttribute(attribute));
      }
      else if(attrName.equals(AttributeEntry.EXCEPTIONS)) {
        printAttribute(offset, new ExceptionsAttribute(attribute));
      }
      else if(attrName.equals(AttributeEntry.CODE)) {
        printAttribute(offset, new CodeAttribute(attribute));
      }
      else if(attrName.equals(AttributeEntry.LINE_NUMBER_TABLE)) {
        printAttribute(offset, new LineNumberTableAttribute(attribute));
      }
      else if(attrName.equals(AttributeEntry.LOCAL_VARIABLE_TABLE)) {
        printAttribute(offset, new LocalVariableTableAttribute(attribute));
      }
      else {
        printAttribute(attribute);
      }
    }
    catch(IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Prints "source file" attribute entry
   *
   * @param attribute the offset
   * @param attribute the "source file" attribute entry
   */
  private void printAttribute(SourceFileAttribute attribute) {
    Utf8Const entry = (Utf8Const)constPool.getEntry(attribute.getIndex());

    String sourceFileName = entry.getValue();

    print(" " + attribute + " " + sourceFileName);
  }

  /**
   * Prints "inner classes" attribute entry
   *
   * @param offset the offset
   * @param attribute the "inner classes" attribute entry
   */
  private void printAttribute(int offset, InnerClassesAttribute attribute) {
    print(" " + attribute.getType() + " {");
    println();

    InnerClassEntry[] innerClasses = attribute.getInnerClasses();

    for(int i=0; i < innerClasses.length; i++) {
      InnerClassEntry innerClass = innerClasses[i];

      print(offset+2, innerClass.toString() + " " +
                      innerClass.resolve(constPool));
      println();
    }

    print(offset, "}");
  }

  /**
   * Prints "line number table" attribute entry
   *
   * @param offset the offset
   * @param attribute the "line number table" attribute entry
   */
  private void printAttribute(int offset, LineNumberTableAttribute attribute) {
    print(" " + attribute.getType() + " {");
    println();

    LineNumberEntry[] lineNumbers = attribute.getLineNumbers();

    for(int i=0; i < lineNumbers.length; i++) {
      LineNumberEntry lineNumber = lineNumbers[i];

      print(offset+2, lineNumber.toString() + " " +
                      lineNumber.resolve(constPool));
      println();
    }

    print(offset, "}");
  }

  /**
   * Prints "local variable table" attribute entry
   *
   * @param offset the offset
   * @param attribute the "local variable table" attribute entry
   */
  private void printAttribute(int offset, LocalVariableTableAttribute attribute) {
    print(" " + attribute.getType() + " {");
    println();

    LocalVariableEntry[] localVariables = attribute.getLocalVariables();

    for(int i=0; i < localVariables.length; i++) {
      LocalVariableEntry localVariable = localVariables[i];

      print(offset+2, localVariable.toString() + " " +
                      localVariable.resolve(constPool));
      println();
    }

    print(offset, "}");
  }

  /**
   * Prints "exceptions" attribute entry
   *
   * @param offset the offset
   * @param attribute the "exceptions" attribute entry
   */
  private void printAttribute(int offset, ExceptionsAttribute attribute) {
    print(" " + attribute.getType() + " {");
    println();

    ExceptionEntry[] exceptions = attribute.getExceptions();

    for(int i=0; i < exceptions.length; i++) {
      ExceptionEntry exception = exceptions[i];

      print(offset+2, exception.toString() + " " +
                      exception.resolve(constPool));
      println();
    }

    print(offset, "}");
  }

  /**
   * Prints "code" attribute entry
   *
   * @param offset the offset
   * @param attribute the "code" attribute entry
   */
  private void printAttribute(int offset, CodeAttribute attribute) {
    byte[] buffer = attribute.getBuffer();

    print(" Code" + " 0x" + Converter.toHexString(attribute.getNameIndex()));
    println();

    print(offset+2, "maxStack  = 0x" + Converter.toHexString(attribute.getMaxStack()));
    println();
    print(offset+2, "maxLocals = 0x" + Converter.toHexString(attribute.getMaxLocals()));
    println();

    MethodBody body = attribute.getMethodBody();

    println();
    print(offset+2, "Body: 0x" + Converter.toHexString((short)body.length()));
    println();

    Instruction[] instructions = body.getInstructions();

    short cnt = 0;
    short pos = 8;

    for(int i=0; i < instructions.length; i++) {
      Instruction instruction = instructions[i];

      String mnemonic = instruction.getMnemonic();
      byte[] parameter = instruction.getParameter();
      long length = instruction.length();

/*      if(mnemonic.equals("goto")) {
        System.out.println("parameter.length " + parameter.length + " " + " 0x" + Converter.toHexString(parameter));

        for(int i1=0; i1 < parameter.length; i1++)
          System.out.println(parameter[i1]);
      }
*/
      print(offset+4, "0x" + Converter.toHexString(cnt));

      byte[] buf = new byte[(int)length];

      for(int j = 0; j < length; j++) {
        buf[j] = buffer[pos+j];
      }

      print(" 0x" + Converter.toHexString(buf));

      if(parameter.length == 0) {
        print("        ");
      }
      else if(parameter.length == 1) {
        print("      ");
      }
      else if(parameter.length == 2) {
        print("    ");
      }
      else if(parameter.length == 3) {
        print("  ");
      }

      print(" " + mnemonic);

      if(parameter.length > 0) {
        print(" 0x" + Converter.toHexString(parameter));
      }

      println();

      cnt += length;
      pos += length;
    }

    EntryCollection exceptionHandlers = attribute.getExceptionHandlers();

    print(offset+2, "ExceptionTable: 0x" + Converter.toHexString((short)exceptionHandlers.size()));
    println();

    for(short i=0; i < exceptionHandlers.size(); i++) {
      ExceptionHandlerEntry exceptionHandler = (ExceptionHandlerEntry)exceptionHandlers.get(i);

      print(offset+4, Converter.toHexString(i));
      print(" " + exceptionHandler.resolve(constPool));
      println();
    }

    EntryCollection attributes = attribute.getAttributes();

    print(offset+2, "Attributes " + Converter.toHexString((short)attributes.size()));
    println();

    for(short i=0; i < attributes.size(); i++) {
      print(offset+4, Converter.toHexString(i));
      printEntry(offset+4, (AttributeEntry)attributes.get(i));
      println();
    }
  }

  /**
   * Prints unknown attribute entry
   *
   * @param attribute the offset
   */
  private void printAttribute(AttributeEntry attribute) {
    print(" " + attribute + " " + attribute.resolve(constPool));
  }

  /**
   * Starts printing on new line
   *
   */
  private void println() {
    out.println();
  }

  /**
   * Prints separate string on the same line
   *
   * @param s  the string
   */
  private void print(String s) {
    out.print(s);
  }

  /**
   * Prints separate string on the same line with the indentation
   *
   * @param s  the string
   */
  private void print(int offset, String s) {
    for(int i=0; i < offset; i++) {
      out.print(" ");
    }

    out.print(s);
  }


  public static void main(String args[]) throws Exception {
    ClassFile classFile = new ClassFile();

    classFile.read(args[0]);

    System.out.println("File length      :" + new File(args[0]).length());
    System.out.println("ClassFile length :" + classFile.length());

    ClassFilePrinter printer = new ClassFilePrinter(classFile);

    printer.toFile(args[1]);
  }

}