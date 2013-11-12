                ClassFile library
                
                1. Basics

To manipulate with the bytecode content the notion of the entry
is used. Everything in bytecode could be represented as set of entries.
Each entry has the following API:

- read()/write() methods for extracting the entry from input stream or
saving the entry to output stream;

- getBytes() - get the entry as an array of bytes;

- length() - calculates the number of bytes, occupied by the entry;

- getType() returns the type of the entry in form of string. This method
is not really important, just for convenient representation of the entry
for end user.

Entries could be collected into collections with the help of EntryCollection
class. Examples: constant pool, interfaces list, fields liast, methods list.
This class is nothing more than ordinary list, but it could hold entries
of only predefined type. EntryCollection is also a kind of Entry, so it
could be inserted as easily as another entries.

The central part of bytecode architecture is the constant pool. Everything 
in bytecode uses indexes-pointers to strings or another indexes in this 
pool. To get access to them the entry should be rersolved against 
the constant pool. Such entries should implement Resolvable interface. This
interface has the only one method - resolve(Pool pool), which returns the 
string with resolved information.

All entries in the constant pool can be represented as follows:

Entry
  PoolEntry
    ConstPoolEntry
      UnicodeConst
      Utf8Const
      StringConst
      ClassConst
      NameAndTypeConst
      MemberConst
        FieldConst
        MethodConst
        InterfaceMethodConst
      LongConst
      IntegerConst
      FloatConst
      DoubleConst 

The rest of bytecode is represented as collections of interfaces, fields, 
methods or attributes. The following classes are used to support that:

Entry  
  InterfaceEntry
  AttributeEntry
  MemberEntry
    MethodEntry
    FieldEntry

Fields, methods and class files use access flag modifier, which is also 
entry, but it doesn't require resolution process. The following classes
are used to support that:
Entry  
  AccessFlags
    FieldAccessFlags
    MethodAccessFlags
    ClassAccessFlags

The ClassFile class encapsulates common behaviour for a file with 
the bytecode content. In turn, this class is also an entry and it follows 
all the requirements for entries.

ClassFile API includes all methods from Entry API plus:

- read(String) - reads bytecodes from a file;
- write(String) - writes bytecodes from a file;

- isChanged() - checks if class file has been changed;
- setChanged(boolean) - sets up a flag that keeps changes in class file;

These methods return different parts of the bytecode:
- getMagicNumber() - returns the magic number for bytecode;
- getMinorVersion() - returns the minor version for bytecode;
- getMajorVersion() - returns the major version for bytecode;
- getThisClassIndex() - gets the index to the string in constant pool
with the name for this class;
- getSuperClassIndex() - gets the index to the string in constant pool
with the name for super class;
- getAccessFlags() - gets the access flags for this class;
- getConstPool() - gets the const pool;
- getInterfaces() - gets the list of interfaces, implemented by this class;
- getFields() - gets the list of fields, defined in this class;
- getMethods() - gets the list of methods, defined in this class;
- getAttributes() - gets the list of attributes, defined in this class.

We can start using basic of ClassFile library with the very simple task:

import org.javalobbby.classfile.*;

  ...

  String inputFileName = "classes/org/shvets/classfile/ClassFile.class";
  String outputFileName = "result/ClassFile.class";

  ClassFile classFile = new ClassFile();

  classFile.read(inputFileName);

  System.out.println("File length :" + new File(inputFileName).length());

  System.out.println("ClassFile length :" + classFile.length());

  classFile.write(outputFileName);

For low level of resolution all described classes comprise required minimum.
To work with advanced features, such as method body or line number table,
additional classes are required.

Another example could be modification of UTF string inside constant pool:

  ClassFile classFile = new ClassFile();

  classFile.read(...);

  ConstPool constPool = classFile.getConstPool();

  short index = 5;

  PoolEntry entry = constPool.getEntry(index);

  if(entry instanceof Utf8Const) {
    Utf8Const utfConst = (Utf8Const)entry;
    String oldValue = utfConst.getValue();

    if(oldValue.equals("Some String")) {
      String newValue = "New String";

      utfConst.setValue(newValue);

      classFile.setChanged(true);
    }
  }

  if(classFile.isChanged()) {
    classFile.write(...);
  }

In this example we are trying to change UTF string in the fifth position
from "Some String" to "New String".

                2. Attribute interpretation

All attributes are read and written as an array of bytes without 
understanding the information inside this bytes. That's enough for the
reading/writing operations.

Package org.shvets.classfile.attribute contains AttributeInterpretator
class for hanling all interpretations. It works in manner, similar to 
wrapper classes from java.io package. It takes an existing attribute as
a parameter pf constructor, parses array of bytes and recognizes all 
specific parts of the original attribute. Interpretator behaves as 
an attribute entry, so all logic, that recognizes content of attribute, 
could be conveniently concentrated in read()/write() methods of derived
classes. This is important, because everything is expressed in one, 
entry-like way.

There are 7 interpretators:

AttributeInterpretator
  SourceFileAttribute
  InnerClassesAttribute

  ConstantValueAttribute

  LineNumberTableAttribute
  LocalVariableTableAttribute
  ExceptionsAttribute
  CodeAttribute

First 2 attributes are used for interpretation of class attributes, third -
for serving fileld entry and last 4 - for serving method entry. They form high,
more sofisticated level of understanding of attributes content.

On this level the developer should take into account additional types of 
entries:

Entry
  InnerClassEntry       (for InnerClassesAttribute)
  LineNumberEntry       (for LineNumberTableAttribute)
  LocalVariableEntry    (for LocalVariableTableAttribute)
  ExceptionEntry        (for ExceptionsAttribute)
  ExceptionHandlerEntry (for CodeAttribute)
  MethodBody            (for CodeAttribute)

As an example let's look at "SourceFile" attribute:

  ClassFile classFile = new ClassFile();

  classFile.read(...);

  ConstPool constPool = classFile.getConstPool();

  EntryCollection attributes = classFile.getAttributes(); 

  for(int i=0; i < attributes.size(); i++) {
    AttributeEntry attribute = (AttributeEntry)attributes.get(i);

    String attributeName = attribute.resolve(constPool);

    if(attributeName.equals(AttributeEntry.SOURCE_FILE)) {
      SourceFileAttribute a = new SourceFileAttribute(attribute);

      short index = a.getIndex();

      Utf8Const utfConst = (Utf8Const)constPool.get(index);

      System.out.println("This bytecode points to a source file: " + 
                         utfConst.getValue());
    }
  }

If the developer wishes to work with local variable table, (s)he can
use the following code:

  ConstPool constPool = classFile.getConstPool();

  EntryCollection methods = classFile.getMethods(); 

  for(int i=0; i < methods.size(); i++) {
    MethodEntry method = (MethodEntry)attributes.get(i);

    System.out.println("Method: " + method.resolve(constPool));

    AttributeEntry attribute = 
         method.getAttribute(AttributeEntry.LOCAL_VARIABLE_TABLE, constPool);

    if(attribute != null) {
      LocalVariableTableAttribute a = new LocalVariableTableAttribute(attribute);

      LineNumberEntry[] lineNumbers = a.getLineNumbers();

      for(int j=0; j < lineNumbers.size(); j++) {
        LineNumberEntry lineNumber = (LineNumberEntry)lineNumbers.get(j);

        System.out.println(lineNumber.resolve(constPool));
      }
    }

                3. Work with the method body

The method entry is the most sofisticated attribute, which involves 4
interpretators. Three of them are simple enough (LineNumberTableAttribute,
LocalVariableTableAttribute and ExceptionsAttribute). Let's explain how to
work with the last, CodeAttribute interpretator.

To work with this interpretator, the org.shvets.classfile.instruction
package is required. It defines instructions as another sort of entry:

Entry
  Instruction
    DefaultInstruction
      TableSwitchInstruction
      LookupSwitchInstruction

Instruction also could be resolved against constant pool and has 
an additional API:

- getMnemonic() returns mnemonic string for this instruction;
- getParameter() returns additional parameter(s) for instruction in form
of bytes array;
- isWide() - checks is this instruction is wide.

All instructions are represented with one class - DefaultInstruction.
"LOOKUPSWITCH" and "TABLESWITCH" commands have little differences,
reflected in corresponding classes.

Opcode interface is the convenient container for all constants that represent
opcodes for instructions.

The following code could be used for disassembling the body of the method:

  ClassFile classFile = new ClassFile();

  classFile.read(...);

  ConstPool constPool = classFile.getConstPool();

  EntryCollection methods = classFile.getMethods(); 

  for(int i=0; i < methods.size(); i++) {
    MethodEntry method = (MethodEntry)methods.get(i);

    System.out.println("Method: " + method.resolve(constPool));

    AttributeEntry attribute = 
                   method.getAttribute(AttributeEntry.CODE, constPool);

    if(attribute != null) {
      CodeAttribute codeAttribute = new CodeAttribute(attribute);

      MethodBody methodBody = codeAttribute.getMethodBody();

      Instruction[] instructions = methodBody.getInstructions();

      for(int j=0; j < instructions.length; j++) {
        Instruction instruction = instructions[j];

        System.out.println(instruction.resolve(constPool));
      }
    }
  }

The MethodBody class is an attribute entry and has the following 
additional API:

- size() returns number of instructions in instructions list;
- getInstructions() gets the array of instructions;
- setInstructions() sets list of instructions;

- getInstruction() gets an instruction at the specified index;
- setInstruction() sets an instruction at the specified index;
- insertInstruction() inserts (adds) an instruction after the specified index;
- removeInstruction() removes an instruction from instructions list;

- isBodyChanged() checks if body of method have some changes.

If the developer wants to make some changes in method body, s(he) should
use the following methods: setInstructions(), setInstruction(),
insertInstruction(), removeInstruction() to fix changes.

To perform some work for a group of instructions the ranking is using. There
are two types of ranking:

a) by usage (UsageRanking):
- ARITHMETIC_RANK - arithmetic instructions (IADD, IINC, ...);
- CONVERSION_RANK- instructions that makes conversions from one type 
to another (I2L, I2F, ...);
- CONDITION_RANK - conditional instructions (IFEQ, IFNULL, ...);
- ARRAY_RANK - instructions that work with arrays (IALOAD, FALOAD, ...);
- LOCAL_VAR_RANK - instructions that work with local variables 
(ILOAD, FLOAD, ...);
- INVOKE_RANK - instructions that work with method invokation 
(INVOKEVIRTUAL, INVOKESPECIAL, ...);
- FIELD_RANK - instructions that work with the fields (GETSTATIC, PUTFIELD, ...);
- BRANCH_RANK - jump instructions (GOTO, JSR, ...);
- RETURN_RANK return instructions (IRETURN, LRETURN, ...);
- MEM_ALLOC_RANK - memory allocation instructions (NEW, NEWARRAY, ...);
- PUSH_RANK - instructions that push something into stack 
(BIPUSH, LDC, ...);
- STACK_MANIP_RANK - stack manipulation instructions (POP, DUP);
- SWITCH_RANK  - switch instructions (LOOKUPSWITCH, TABLESWITCH);
- OTHER_RANK - other instruction that it is difficult to range;
- QUICK_RANK - instructions with the quick modifier;
- RESERVED_RANK - reserved instructions.

b) by parameter (ParameterRanking):

- FIELD_RANK - instructions that work with field;
- METHOD_RANK - instructions that work with method;
- IMETHOD_RANK - instructions that work with interface method;
- LDC_RANK - LDC instructions;
- LDC2_RANK - LDC_W instructions;
- CLASS_RANK - instructions that work with class;;
- PRIMITIVE_RANK - instructions that work with 1-dimension array;
- MULTI_RANK - instructions that work with multi-dimension array;
- PLAIN_RANK -  instructions with 1-byte length;
- SWITCH_RANK - switch instructions.

Next example shows how to get access to instructions that take care about
methods:

  private RankTable rankTable = ParameterRanking.getInstance();

  Instruction instruction = ...;

  String group = rankTable.getRank(instruction.getTag()).key;

  if(group.equals(ParameterRanking.METHOD_RANK)) {
    System.out.println("Instruction " + instruction.getMnemonic() + 
                       " belongs to " + ParameterRanking.METHOD_RANK + " rank.");
  }
