// ZipClassLoader.java

package org.sf.cafebabe.util;

import java.io.File;
import java.io.IOException;
import java.io.EOFException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;

import java.util.Vector;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.lang.reflect.Method;

public class ZipClassLoader extends ByteArrayClassLoader {

  private String zipName;
  private Vector cpEntries;

  public ZipClassLoader(String zipName, Vector cpEntries) {
    this.zipName   = zipName;
    this.cpEntries = cpEntries;
  }

  public InputStream getResourceAsStream(String name) {
    name = name.replace('\\', '/');

    if(name.startsWith("/")) {
      name = name.substring(1);
    }

    InputStream is = super.getResourceAsStream(name);

    if(is == null) {
      is = getSystemResourceAsStream(name);
      if(is == null) {
        try {
          ZipFile zipFile = new ZipFile(zipName);
          ZipEntry zipEntry = zipFile.getEntry(name);

          if(zipEntry != null) {
            is = zipFile.getInputStream(zipEntry);
          }
        }
        catch(IOException e) {
          is = null;
        }
      }
    }

    return is;
  }

  protected byte[] loadClassBytes(String className) {
    byte[] buffer = null;

    try {
      buffer = loadFile(zipName, className);
    }
    catch(Exception e) {
      e.printStackTrace();
      buffer = null;
    }

    if(buffer == null) {
      for(int i=0; i < cpEntries.size() && (buffer == null); i++) {
        try {
          buffer = loadFile((String)cpEntries.elementAt(i), className);
        }
        catch(Exception e) {
          e.printStackTrace();
          buffer = null;
        }
      }
    }

    return buffer;
  }

  private byte[] loadFile(String zipName, String className) throws IOException {
    ZipFile zipFile = new ZipFile(zipName);

    className = className.replace('.', '/').replace('\\', '/') + ".class";

    ZipEntry zipEntry = zipFile.getEntry(className);

    if(zipEntry == null) {
      return null;
    }

    InputStream is = zipFile.getInputStream(zipEntry);

    BufferedInputStream bis   = new BufferedInputStream(is);
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try {
      int c = bis.read();
      while (c != -1) {
        out.write(c);
        c = bis.read();
      }
    }
    catch(EOFException e2) {}

    byte[] buffer = out.toByteArray();

    is.close();
    out.close();
    zipFile.close();

    return buffer;
  }

  protected boolean checkValid(String name) {
    try {
      ZipFile zipFile = new ZipFile(zipName);
      boolean check = (zipFile.getEntry(name.replace('.', File.separatorChar) + ".class") != null);
      zipFile.close();

      return check;
    }
    catch(IOException e) {
      e.printStackTrace();
    }

    return false;
  }

  public String getZipName() {
    return zipName;
  }

  public static void main(String[] args) throws Exception {
    Vector cpEntries = new Vector();

    cpEntries.addElement("lib/CafeBabe.jar");

    ZipClassLoader loader = new ZipClassLoader("plugins/HoundPlug.cba", cpEntries);

    Class clazz = loader.loadClass("org.sf.tools.plugins.houndplug.ClassHoundFrame", true);

    System.out.println("class " + clazz.getName());

    Method methods[] = clazz.getDeclaredMethods();

    for(int i=0; i < methods.length; i++) {
      String methodName = methods[i].getName();
      System.out.println(methodName);
    }

    System.exit(0);
  }
}
