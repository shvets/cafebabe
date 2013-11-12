// Integrity.java

package org.sf.cafebabe.task.classfile;

import java.io.IOException;

import org.sf.classfile.*;
import org.sf.classfile.attribute.*;
import org.sf.classfile.instruction.*;
//import org.sf.fileprocessor.event.NotificationListener;
//import org.sf.fileprocessor.event.NotificationSupport;

public class Integrity {
//  private NotificationSupport support = new NotificationSupport(this);

  private ConstPool constPool;

  private ClassFile classFile;

  public Integrity(ClassFile classFile) {
    this.classFile = classFile;

    constPool = classFile.getConstPool();
  }

  public void check() {
    checkEntry("This class section entry", classFile.getThisClassIndex());

    checkEntry("Super class section entry", classFile.getSuperClassIndex());

    EntryCollection interfaces = classFile.getInterfaces();

    for(short i=0; i < interfaces.size(); i++) {
      InterfaceEntry entry = (InterfaceEntry)interfaces.get(i);

      checkEntry("Interfaces section entry", entry.getNameIndex());
    }

    checkMembers("Field entry", classFile.getFields());

    checkMembers("Method entry", classFile.getMethods());

    EntryCollection attributes = classFile.getAttributes();

    for(short i=0; i < attributes.size(); i++) {
      checkAttribute("Class attributes section entry", (AttributeEntry)attributes.get(i));
    }
  }

  private void checkEntry(String sectionName, short index) {
/*
    if(index >= constPool.size()) {
      fireNotification(sectionName, Converter.toHexString(index, "") + " - " + "incorrect entry");
    }
*/
  }
  private void checkMembers(String sectionName, EntryCollection members) {
    for(short i=0; i < members.size(); i++) {
      MemberEntry entry = (MemberEntry)members.get(i);

      checkEntry(sectionName + " -> name", entry.getNameIndex());
      checkEntry(sectionName + " -> value", entry.getValueIndex());

      EntryCollection attributes = entry.getAttributes();

      for(short j=0; j < attributes.size(); j++) {
        AttributeEntry attribute = (AttributeEntry)attributes.get(j);

        checkAttribute(sectionName + " -> attribute", attribute);
      }
    }
  }

  private static RankTable rankTable = ParameterRanking.getInstance();

  private void checkAttribute(String sectionName, AttributeEntry attribute) {
    checkEntry(sectionName + " -> attribute name", attribute.getNameIndex());

    String attrName = attribute.resolve(constPool);

    sectionName +=" -> " + attrName + " attribute";

    try {
      if(attrName.equals(AttributeEntry.SOURCE_FILE)) {
        SourceFileAttribute a = new SourceFileAttribute(attribute);
        checkEntry(sectionName, a.getIndex());
      }
      else if(attrName.equals(AttributeEntry.CONSTANT_VALUE)) {
        ConstantValueAttribute a = new ConstantValueAttribute(attribute);

        checkEntry(sectionName, a.getIndex());
      }
      else if(attrName.equals(AttributeEntry.INNER_CLASSES)) {
        InnerClassesAttribute a = new InnerClassesAttribute(attribute);

        InnerClassEntry[] innerClasses = a.getInnerClasses();

        for(int j=0; j < innerClasses.length; j++) {
          InnerClassEntry innerClass = innerClasses[j];

          short index1 = innerClass.getInnerClassIndex();
          short index2 = innerClass.getOuterClassIndex();
          short index3 = innerClass.getInnerNameIndex();

          checkEntry(sectionName + " -> inner class", index1);

          if(index2 != 0) {
            checkEntry(sectionName + " -> outer class", index2);

            if(index3 != 0) {
              checkEntry(sectionName + " -> inner name", index3);
            }
          }
          else { // index2 == 0
            if(index3 != 0) {
              checkEntry(sectionName + " -> inner name", index3);
            }
          }
        }
      }
      else if(attrName.equals(AttributeEntry.EXCEPTIONS)) {
        ExceptionsAttribute a = new ExceptionsAttribute(attribute);

        ExceptionEntry[] exceptions = a.getExceptions();

        for(int j=0; j < exceptions.length; j++) {
          checkEntry(sectionName, exceptions[j].getNameIndex());
        }
      }
      else if(attrName.equals(AttributeEntry.CODE)) {
        CodeAttribute a = new CodeAttribute(attribute);

        MethodBody methodBody = a.getMethodBody();
        Instruction[] instructions = methodBody.getInstructions();

        for(int i=0; i < instructions.length; i++) {
          Instruction instruction = instructions[i];

          byte tag = instruction.getTag();
          OpcodeDescription description = OpcodeTable.getDescription(tag);

          if(description.length > 1) {
            byte[] parameter = instruction.getParameter();

            String group = rankTable.getRank(tag).key;

            if(group.equals(ParameterRanking.FIELD_RANK)  ||
               group.equals(ParameterRanking.METHOD_RANK) ||
               group.equals(ParameterRanking.IMETHOD_RANK)) {
              checkEntry(sectionName, (short)Converter.getShort(parameter, 0));
            }
            else if(group.equals(ParameterRanking.LDC_RANK)) {
               checkEntry(sectionName, (short)Converter.getByte(parameter, 0));
            }
            else if(group.equals(ParameterRanking.LDC2_RANK)) {
              checkEntry(sectionName, (short)Converter.getShort(parameter, 0));
            }
            else if(group.equals(ParameterRanking.PRIMITIVE_RANK)) {}
            else if(group.equals(ParameterRanking.MULTI_RANK)) {}
          }
        }

        EntryCollection exceptionHandlers = a.getExceptionHandlers();

        for(short j=0; j < exceptionHandlers.size(); j++) {
          ExceptionHandlerEntry exceptionHandler = (ExceptionHandlerEntry)exceptionHandlers.get(j);

          checkEntry(sectionName, exceptionHandler.getCatchType());
        }

        EntryCollection attributes = a.getAttributes();

        for(short i=0; i < attributes.size(); i++) {
          checkAttribute(sectionName, (AttributeEntry)attributes.get(i));
        }
      }
      else if(attrName.equals(AttributeEntry.LINE_NUMBER_TABLE)) {
        // do nothing
      }
      else if(attrName.equals(AttributeEntry.LOCAL_VARIABLE_TABLE)) {
        LocalVariableTableAttribute a = new LocalVariableTableAttribute(attribute);

        LocalVariableEntry[] localVariables = a.getLocalVariables();

        for(int j=0; j < localVariables.length; j++) {
          LocalVariableEntry localVariable = localVariables[j];

          checkEntry(sectionName, localVariable.getNameIndex());
          checkEntry(sectionName, localVariable.getDescriptionIndex());
        }
      }
      else {
        // unknown attribute, trying to use simplest scheme
        byte[] buffer = attribute.getBuffer();
        if(buffer.length == 2) {
          short index = (short)((buffer[0] & 0xff) << 8 | (buffer[1] & 0xff));

          checkEntry(sectionName, index);
        }
      }
    }
    catch(IOException e) {
      e.printStackTrace();
    }
  }

/*  public synchronized void addNotificationListener(NotificationListener l) {
    support.addNotificationListener(l);
  }
  public synchronized void removeNotificationListener(NotificationListener l) {
    support.removeNotificationListener(l);
  }

  // fire NotificationEvent to registered listeners
  protected void fireNotification(String notification, String description) {
    support.fireNotification(notification, description);
  }
*/

}
