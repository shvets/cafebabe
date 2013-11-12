// FileClassLoader.java

package org.sf.cafebabe.util;

import java.lang.reflect.Method;
import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;

public class FileClassLoader extends ByteArrayClassLoader {

  private String dirName;

  public FileClassLoader(String dirName) {
    this.dirName = dirName;
  }

  protected byte[] loadClassBytes(String name) {
    try {
      name = name.replace('.', File.separatorChar);
      name = name.replace('/', File.separatorChar);
      String fileName = dirName + File.separator + name + ".class";

      File file = new File(fileName);
      FileInputStream fis = new FileInputStream(file);

      BufferedInputStream bis   = new BufferedInputStream(fis);
      ByteArrayOutputStream out = new ByteArrayOutputStream();

      int c = bis.read();
      while (c != -1) {
        out.write(c);
        c = bis.read();
      }

//      classBytes = new byte[(int)file.length()];
//      is.read(classBytes);

      bis.close();

      return out.toByteArray();
    }
    catch(Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  protected boolean checkValid(String name) {
    File f = new File(dirName + File.separatorChar +
                      name.replace('.', File.separatorChar) + ".class");
    if(f.exists())
      return true;

    return false;
  }

  public String getDirName() {
    return dirName;
  }

  public static void main(String[] args) throws Exception {
    FileClassLoader loader = new FileClassLoader("ppp");

    Class clazz = loader.loadClass("DirectionButton", true);
//    Class clazz = loader.loadClass("HtmlPanel", true);
    System.out.println("class " + clazz.getName());

    Method methods[] = clazz.getDeclaredMethods();

    for(int i=0; i < methods.length; i++) {
      String methodName = methods[i].getName();
      System.out.println(methodName);
    }
  }
}
