package org.sf.cafebabe.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.util.Properties;
import java.util.StringTokenizer;
import java.awt.Rectangle;


/**
 * Class that works with properties file for application
 *
 * @version 1.0 03/20/2000
 * @author Alexander Shvets
 */
public class Configurator  {
  private Properties propsList = new Properties();

  private String fileName;

  public Configurator(String fileName) {
    this.fileName = fileName;
  }

  public void load() throws IOException {
    File file = new File(fileName);

    if(!file.exists()) {
      throw new IOException("File " + fileName + " doesn't exist.");
    }

    InputStream is = new BufferedInputStream(new FileInputStream(file));
    propsList.load(is);

    is.close();
  }

  public void save() throws IOException {
    File file = new File(fileName);

    if(!file.canWrite()) {
      throw new IOException("File " + fileName + " cannot be used for write operation.");
    }

    OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
    propsList.store(os, null);

    os.close();
  }

  public void setProperty(String key, String property) {
    propsList.put(key, property);
  }

  public String getProperty(String key) {
    return propsList.getProperty(key);
  }

  public String getProperty(String key, String defaultValue) {
    return propsList.getProperty(key, defaultValue);
  }

  public boolean getBooleanProperty(String key, boolean defaultValue) {
    String s = getProperty(key, String.valueOf(defaultValue));

    if(s.equalsIgnoreCase("true"))
      return true;

    return false;
  }

  public void setBooleanProperty(String name, boolean value) {
    propsList.put(name, new Boolean(value).toString());
  }

  public int getIntProperty(String key, int defaultValue) {
    try {
      return Integer.parseInt(getProperty(key, String.valueOf(defaultValue)));
    }
    catch(Exception e) {
      return defaultValue;
    }
  }

  public void setIntProperty(String name, int value) {
    propsList.put(name, String.valueOf(value));
  }

  public Rectangle getBoundsProperty(String name, String defValue) {
    String bs = getProperty(name, String.valueOf(defValue));
    StringTokenizer st = new StringTokenizer(bs, "; ");

    Rectangle r = new Rectangle(0, 0, 0, 0);

    if(st.hasMoreTokens()) {
      r.x = Integer.parseInt(st.nextToken());
    }

    if(st.hasMoreTokens()) {
      r.y = Integer.parseInt(st.nextToken());
    }

    if(st.hasMoreTokens()) {
      r.width  = Integer.parseInt(st.nextToken());
    }

    if(st.hasMoreTokens()) {
      r.height = Integer.parseInt(st.nextToken());
    }

    return r;
  }

  public void setBoundsProperty(String name, Rectangle r) {
    setProperty(name, "" + r.x + ";" + r.y + ";" + r.width + ";" + r.height);
  }

  public void clear() {
    propsList.clear();
  }

}
