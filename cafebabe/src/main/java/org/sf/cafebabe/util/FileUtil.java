// FileUtil.java

package org.sf.cafebabe.util;

import java.io.File;

public final class FileUtil {
  private FileUtil() {}

  public static String getExtension(String fileName) {
    String extension = null;

    int pos = fileName.lastIndexOf('.');

    if(pos > 0 && pos < fileName.length()-1) {
      extension = fileName.substring(pos+1).toLowerCase();
    }

    return extension;
  }

  /**
   * Conveinience method that returns the "dot" extension for the
   * given file.
   */
  public static String getExtension(File file) {
    if(file != null) {
      return getExtension(file.getName());
    }

    return null;
  }

}
