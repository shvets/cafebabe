package org.sf.cafebabe.task.classhound;

import org.sf.cafebabe.MainChooser;

import javax.activation.DataSource;
import java.io.File;

public interface CafeBabeParent {

  void open(File file);

  MainChooser getFileChooser();

  void open(DataSource dataSource);

  void setFieldPosition(String selectedField);

  void setMethodPosition(String selectedMethod);
}
