// ClassHound.java

package org.sf.cafebabe.task.classhound;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.StringTokenizer;
import java.util.Enumeration;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;

import org.sf.classfile.ClassFile;
import org.sf.classfile.ConstPool;
import org.sf.classfile.EntryCollection;
import org.sf.classfile.FieldEntry;
import org.sf.classfile.MethodEntry;

import org.sf.cafebabe.util.FileUtil;

/**
 * This class reperesent ClassHound Service
 *
 * @version 1.0 02/04/2002
 * @author Alexander Shvets
 */
public class ClassHound {
  public final static String ANONYMOUS_PACKAGE = "<anonymous package>";

  private final List entries  = new ArrayList();
  private final List packages = new ArrayList();
  private final List classes  = new ArrayList();
  private final List fields   = new ArrayList();
  private final List methods  = new ArrayList();

  // map: classpath entry -> files list
  private final Map entryToFilesMap = new HashMap();

  private static ClassHound instance;

  /**
   * Creates single instance of ClassHound Service
   */
  private ClassHound() {
    collectDefaults();
  }

  /**
   * Gets the instance of ClassHound Service
   *
   * @return ClassHound Service instance
   */
  public static ClassHound getInstance() {
    if(instance == null) {
      instance = new ClassHound();
    }

    return instance;
  }

  /**
   * Gets the list of entries
   *
   * @return the list of entries
   */
  public List getEntries() {
    return entries;
  }

  /**
   * Gets the list of packages
   *
   * @return the list of packages
   */
  public List getPackages() {
    return packages;
  }

  /**
   * Gets the list of classes
   *
   * @return the list of classes
   */
  public List getClasses() {
    return classes;
  }

  /**
   * Gets the list of fields
   *
   * @return the list of fields
   */
  public List getFields() {
    return fields;
  }

  /**
   * Gets the list of methods
   *
   * @return the list of methods
   */
  public List getMethods() {
    return methods;
  }

  /**
   * Gets the list of methods
   *
   * @return the list of methods
   */
  public Map getEntryToFilesMap() {
    return entryToFilesMap;
  }

  /**
   * Clears packages list
   */
  public void clearPackages() {
    packages.clear();
  }

  /**
   * Clears classes list
   */
  public void clearClasses() {
    classes.clear();
  }

  /**
   * Clears fields list
   */
  public void clearFields() {
    fields.clear();
  }

  /**
   * Clears methods list
   */
  public void clearMethods() {
    methods.clear();
  }

  /**
   * Finds out all files for specified CLASSPATH entry and
   * creates assosiation between them and entry name. Adds this
   * assosiation the list
   *
   * @param entryName the name of the entry to be analyzed
   */
  public void addEntry(String entryName) {
    List filesForEntry = new ArrayList();

    File file = new File(entryName);

    if(file.isDirectory()) {
      collectFromDirectory(entryName, "", filesForEntry);
    }
    else {
      collectFromArchive(entryName, filesForEntry);
    }

    entryToFilesMap.put(entryName, filesForEntry);
  }

  /**
   * Removes new entry assosiation from the list
   *
   * @param entryName the name of the entry to be removed
   */
  public void removeEntry(String entryName) {
    entryToFilesMap.remove(entryName);
  }

  /**
   * Collects accessible information form Java environment
   */
  private void collectDefaults() {
    StringBuffer defaults = new StringBuffer();

    String javaHome = System.getProperty("java.home");
    String libHome  = javaHome + File.separator + "lib";

    File libFile = new File(libHome);

    if(libFile.isDirectory()) {
      String[] list = libFile.list();

      for(int i=0; i < list.length; i++) {
        String extension = FileUtil.getExtension(list[i]);

        if(extension != null) {
          if(extension.equals("jar") || extension.equals("zip")) {
            defaults.append(libHome + File.separator +
                          list[i] + File.pathSeparator);
          }
        }
      }
    }

    String extHome = libHome + File.separator + "ext";

    File extFile = new File(extHome);

    if(extFile.isDirectory()) {
      String[] list = extFile.list();

      for(int i=0; i < list.length; i++) {
        String extension = FileUtil.getExtension(list[i]);

        if(extension != null) {
          if(extension.equals("jar") || extension.equals("zip")) {
            defaults.append(extHome + File.separator +
                            list[i] + File.pathSeparator);
          }
        }
      }
    }

    String toolHome = javaHome + File.separator + ".." +
                      File.separator + "lib";

    File toolFile = new File(toolHome);

    if(toolFile.isDirectory()) {
      String[] list = toolFile.list();

      for(int i=0; i < list.length; i++) {
        String extension = FileUtil.getExtension(list[i]);

        if(extension != null) {
          if(extension.equals("jar") || extension.equals("zip")) {
            defaults.append(toolHome + File.separator +
                            list[i] + File.pathSeparator);
          }
        }
      }
    }

    defaults.append(System.getProperty("java.class.path"));

    StringTokenizer st = new StringTokenizer(defaults.toString(), File.pathSeparator);

    while(st.hasMoreTokens()) {
      String entryName = st.nextToken();
      File fileEntry = new File(entryName);

      if(!fileEntry.exists()) {
        continue;
      }

      String name = null;

      try {
        name = fileEntry.getCanonicalPath();

        if(!entries.contains(name)) {
          entries.add(name);
        }
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }

    for(int i=0; i < entries.size(); i++) {
      addEntry((String)entries.get(i));
    }
  }

  /**
   * Collects files from the directory
   *
   * @param parent the parent object
   * @param child the child object
   * @param list the resulting list
   */
  private void collectFromDirectory(String parent, String child, List list) {
    String dirName = (child.length() == 0) ? parent : parent + File.separator + child;

    String[] files = new File(dirName).list();

    for(int i=0; i < files.length; i++) {
      String shortName = (child.length() == 0) ? files[i] : child + File.separator + files[i];
      String fullName  = dirName + File.separator + files[i];

      if(new File(fullName).isDirectory()) {
        collectFromDirectory(parent, shortName, list);
      }
      else if(shortName.toLowerCase().endsWith(".class")) {
        list.add(shortName);
      }
    }
  }

  /**
   * Collects files from the archive
   *
   * @param archiveName the archive name
   * @param list the resulting list
   */
  private void collectFromArchive(String archiveName, List list) {
    try {
      ZipFile zipFile = new ZipFile(archiveName);

      for(Enumeration e = zipFile.entries(); e.hasMoreElements(); ) {
        ZipEntry entry = (ZipEntry)e.nextElement();

        String name = entry.getName();

        if(name.toLowerCase().endsWith(".class")) {
          list.add(entry.getName());
        }
      }
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Fills out the content of the packages list
   *
   * @param selEntry  selected entry
   */
  public void fillPackagesList(String selEntry) {
    clearPackages();
    clearClasses();
    clearFields();
    clearMethods();

    boolean isArchive = !(new File(selEntry)).isDirectory();

    List files = (List)entryToFilesMap.get(selEntry);

    if(files == null) {
      return;
    }

    for(int i=0; i < files.size(); i++) {
      String fileName = (String)files.get(i);
      String packageName = null;

      int pos = 0;

      if(isArchive) {
        pos = fileName.lastIndexOf("/");
      }
      else {
        pos = fileName.lastIndexOf(File.separator);
      }

      if(pos == -1) {
        packageName = ANONYMOUS_PACKAGE;
      }
      else {
        packageName = fileName.substring(0, pos).replace(File.separatorChar, '.');

        if(isArchive) {
          packageName = packageName.replace('/', '.');
        }
      }

      if(!packages.contains(packageName)) {
        packages.add(packageName);
      }
    }

    Collections.sort(packages);
  }

  /**
   * Fills out the content of the classes list
   *
   * @param selEntry  selected entry
   * @param selPackage selected package
   */
  public void fillClassesList(String selEntry, String selPackage) {
    if(selPackage == null) {
      return;
    }

    clearClasses();
    clearFields();
    clearMethods();

    boolean isArchive = !(new File(selEntry)).isDirectory();

    List files = (List)entryToFilesMap.get(selEntry);

    if(files == null) {
      return;
    }

    for(int i=0; i < files.size(); i++) {
      String fileName = (String)files.get(i);

      String className = null;
      String packagePart = null;

      int pos1 = 0;

      if(isArchive) {
        pos1 = fileName.lastIndexOf("/");
      }
      else {
        pos1 = fileName.lastIndexOf(File.separator);
      }

      int pos2 = fileName.lastIndexOf(".");

      if(pos1 == -1) {
        className = fileName.substring(0, pos2);
      }
      else {
        packagePart = fileName.substring(0, pos1).replace(File.separatorChar, '.');

        if(isArchive) {
          packagePart = packagePart.replace('/', '.');
        }

        className   = fileName.substring(pos1+1, pos2);
      }

      if(packagePart == null) {
        if(selPackage.equals(ANONYMOUS_PACKAGE)) {
          if(!classes.contains(className)) {
            classes.add(className);
          }
        }
      }
      else {
        if(packagePart.equals(selPackage)) {
          if(!classes.contains(className)) {
            classes.add(className);
          }
        }
      }
    }

    Collections.sort(classes);
  }

  /**
   * Fills out the content of the field and method lists
   *
   * @param selEntry  selected entry
   * @param selPackage selected package
   * @param selClass selected class
   */
  public void fillMembersLists(String selEntry, String selPackage,
                               String selClass) {
    if(selClass == null) {
      return;
    }

    clearFields();
    clearMethods();

    try {
      String fromFile = selClass + ".class";

      if(selPackage != ANONYMOUS_PACKAGE) {
        fromFile = selPackage.replace('.', '/') + "/" + fromFile;
      }

      InputStream is = null;

      if((new File(selEntry)).isDirectory()) {
        is = new FileInputStream(selEntry + File.separator + fromFile);
      }
      else {
        ZipFile zipFile = new ZipFile(selEntry);
        ZipEntry zipEntry = zipFile.getEntry(fromFile);

        if(zipEntry == null) {
          return;
        }

        is = zipFile.getInputStream(zipEntry);
      }

      BufferedInputStream bis = new BufferedInputStream(is);
      ClassFile classFile = new ClassFile();
      classFile.read(new DataInputStream(bis));

      ConstPool constPool = classFile.getConstPool();
      EntryCollection fieldsCollection  = classFile.getFields();
      EntryCollection methodsCollection = classFile.getMethods();

      for(short i=0; i < fieldsCollection.size(); i++) {
        FieldEntry entry = (FieldEntry)fieldsCollection.get(i);

        fields.add(entry.resolve(constPool));
      }

      Collections.sort(fields);

      for(short i=0; i < methodsCollection.size(); i++) {
        MethodEntry entry = (MethodEntry)methodsCollection.get(i);

        methods.add(entry.resolve(constPool));
      }

      Collections.sort(methods);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

}
