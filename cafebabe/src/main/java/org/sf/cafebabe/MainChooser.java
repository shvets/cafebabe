// MainChooser.java

package org.sf.cafebabe;

import java.io.File;

import javax.swing.JFileChooser;

import org.sf.cafebabe.util.IconProducer;

public class MainChooser extends JFileChooser {

  public MainChooser() {
    super();

    MainFileView fileView = new MainFileView();

    IconProducer.setClass(getClass());

    fileView.putIcon("class", IconProducer.getImageIcon(Constants.ICON_CLASS_FILE));
    fileView.putIcon("ser", IconProducer.getImageIcon(Constants.ICON_SER_FILE));

    fileView.putIcon("zip", IconProducer.getImageIcon(Constants.ICON_ZIP_FILE));
    fileView.putIcon("jar", IconProducer.getImageIcon(Constants.ICON_ZIP_FILE));
    fileView.putIcon("war", IconProducer.getImageIcon(Constants.ICON_ZIP_FILE));
    fileView.putIcon("ear", IconProducer.getImageIcon(Constants.ICON_ZIP_FILE));

    setFileView(fileView);

    addChoosableFileFilter(new ClassFileFilter());
    addChoosableFileFilter(new SerFileFilter());
    addChoosableFileFilter(new ZipFileFilter());
  }

  public File open(String title) {
    this.rescanCurrentDirectory();

    int returnVal = this.showDialog(this, title);

    if(returnVal == JFileChooser.APPROVE_OPTION) {
      return this.getSelectedFile();
    }

    return null;
  }

}

