// SerFileFilter.java

package org.sf.cafebabe;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import org.sf.cafebabe.util.FileUtil;

/**
 * Class that represents the filter of serialized files for
 * custom file chooser
 *
 * @version 1.0 09/05/2003
 * @author Alexander Shvets
 */
public class SerFileFilter extends FileFilter {

  /**
   * Gets the description of the filter in drop-down list
   *
   * @return the description of the filter
   */
  public String getDescription() {
    return new String("Serialized files (*.ser)");
  }

  /**
   * Returns wehether the given file is accepted by this filter.
   *
   * @return true if the given file is accepted by this filter;
   *              false otherwise
   */
  public boolean accept(File f) {
    if(f != null) {
      if(f.isDirectory()) {
        return true;
      }

      String extension = FileUtil.getExtension(f);

      if(extension != null && extension.equals("ser")) {
        return true;
      }
    }

    return false;
  }

}


