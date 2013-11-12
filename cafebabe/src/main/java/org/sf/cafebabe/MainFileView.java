package org.sf.cafebabe;

import java.io.File;
import java.util.Hashtable;

import javax.swing.Icon;

import org.sf.cafebabe.util.FileUtil;
import javax.swing.filechooser.FileView;

public class MainFileView extends FileView {
  private Hashtable icons = new Hashtable();

  private Hashtable fileNames = new Hashtable();
  private Hashtable fileDescriptions = new Hashtable();
  private Hashtable typeDescriptions = new Hashtable();

  /**
  * The name of the file.
  * @see #getName
  */
  public void setName(File f, String fileName) {
    fileNames.put(fileName, f);
  }

  /**
  * The name of the file.
  * @see #setName
  * @see FileView#getName
  */
  public String getName(File f) {
    return (String)fileNames.get(f);
  }

  /**
  * Adds a human readable description of the file.
  */
  public void putDescription(File f, String fileDescription) {
    fileDescriptions.put(fileDescription, f);
  }

  /**
  * A human readable description of the file.
  *
  * @see FileView#getDescription
  */
  public String getDescription(File f) {
    return (String)fileDescriptions.get(f);
  };

  /**
  * Adds a human readable type description for files. Based on "dot"
  * extension strings, e.g: ".gif". Case is ignored.
  */
  public void putTypeDescription(String extension, String typeDescription) {
    typeDescriptions.put(typeDescription, extension);
  }

  /**
  * Adds a human readable type description for files of the type of
  * the passed in file. Based on "dot" extension strings, e.g: ".gif".
  * Case is ignored.
  */
  public void putTypeDescription(File f, String typeDescription) {
    putTypeDescription(FileUtil.getExtension(f), typeDescription);
  }

  /**
  * A human readable description of the type of the file.
  *
  * @see FileView#getTypeDescription
  */
  public String getTypeDescription(File f) {
    return (String) typeDescriptions.get(FileUtil.getExtension(f));
  }

  /**
  * Adds an icon based on the file type "dot" extension
  * string, e.g: ".gif". Case is ignored.
  */
  public void putIcon(String extension, Icon icon) {
    if(icon != null) {
      icons.put(extension, icon);
    }
  }

  /**
  * Icon that reperesents this file. Default implementation returns
  * null. You might want to override this to return something more
  * interesting.
  *
  * @see FileView#getIcon
  */
  public Icon getIcon(File f) {
    Icon icon = null;

    String extension = FileUtil.getExtension(f);

    if(extension != null) {
      icon = (Icon)icons.get(extension);
    }

    return icon;
  }

}
