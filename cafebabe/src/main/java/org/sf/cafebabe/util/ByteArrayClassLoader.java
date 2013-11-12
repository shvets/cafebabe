// ByteArrayClassLoader.java

package org.sf.cafebabe.util;

import java.util.Map;
import java.util.HashMap;

public abstract class ByteArrayClassLoader extends ClassLoader {

  private Map cache = new HashMap();

  private byte[] classBytes;

  public ByteArrayClassLoader() {}

  public ByteArrayClassLoader(byte[] classBytes) {
    this.classBytes = classBytes;
  }

  public void setByteArray(byte[] classBytes) {
    this.classBytes = classBytes;
  }

  protected byte[] loadClassBytes(String name) {
    return classBytes;
  }

  public synchronized Class loadClass(String className) throws ClassNotFoundException {
    return loadClass(className, true);
  }

  public synchronized Class loadClass(String className, boolean resolveIt)
                                      throws ClassNotFoundException {
    // Check the cache of classes
    Class result = (Class)cache.get(className);

    if(result == null) {
      // Check with the primordial class loader
      try {
        // Try to get the class from the system class loader.
        result = findSystemClass(className);
      }
      catch (ClassNotFoundException e) {
        // It was not a system class so try getting it via our own method
        result = null;
      }
      catch (Error t) {
        result = null;
      }

      if(result == null) {
        // Check if the class was already loaded
        try {
          result = findLoadedClass(className);
        }
        catch (Error t) {
          result = null;
        }

        // Still no class found?  Try our class loader...
        if (result == null) {
          try {
            // Try to load it from the added class paths
            byte classData[] = loadClassBytes(className);

            if (classData == null) {
              throw new ClassNotFoundException(className);
            }

            // Define it (parse the class file)
            result = defineClass(className, classData, 0, classData.length);
          }
          catch(Exception e) {
            e.printStackTrace();
            throw new ClassFormatError(className);
          }

          // Add the class to the cache
          cache.put(className, result);
        }
      }
    }

    if (result != null && resolveIt) {
      resolveClass(result);
    }

    return result;
  }

/*  public synchronized Class loadClass(String name, boolean resolve)
                            throws ClassNotFoundException {
//    System.err.println("1 " + name);
    Class c = (Class)cache.get(name);
    if(c != null) return c;

    c = findLoadedClass(name);
    if(c != null) {
      // Return an already-loaded class
      return c;
    }


//    System.err.println("2 ");
    // Don't attempt to load a system file except
    // through the primordial class loader
//    if(name.startsWith("java.") || name.startsWith("javax."))
      return Class.forName(name);

//    if(!checkValid(name))
//      throw new ClassNotFoundException(name);

    //    System.err.println("3 ");
    byte data[] = loadClassBytes(name);
    if(data == null)
      throw new ClassNotFoundException(name);

//    System.err.println("4");
    c = defineClass(name, data, 0, data.length);
    if(c == null)
      throw new ClassFormatError();
//    System.err.println("5");
    cache.put(name, c);

    if(resolve)
      resolveClass(c);

    return c;
  }
*/

  protected abstract boolean checkValid(String name);

}
