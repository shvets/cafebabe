// AttributeInterpretator.java

package org.sf.classfile.attribute;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import org.sf.classfile.AttributeEntry;

/**
 * Class that represents basic interpretator for attributes inside class file.
 * It take as a parameter plain attribute and help to interprete its contents
 * corresponding to name of attribute (e.g. type of attribute).
 *
 * This class works like a wrapper class for I/O streams.
 *
 * @version 1.0 02/22/2000
 * @author Alexander Shvets
 */
public class AttributeInterpretator extends AttributeEntry {

  /** An attribute for which interpretation should be performed */
  protected AttributeEntry attribute;

  /**
   * Creates corresponding interpretator for specified attribute
   *
   * @param attribute  an attribute for which interpretation should
   * be performed.
   */
  public AttributeInterpretator(AttributeEntry attribute) throws IOException {
    this.attribute = attribute;

    nameIndex = attribute.getNameIndex();

    buffer = attribute.getBuffer();

    ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
    DataInputStream dis = new DataInputStream(bais);

    read(dis);

    dis.close();
  }

  /**
   * Sets the byte array for this attribute. In this method there is a
   * synchronization of buffers' content both for original attribute and its
   * interpretator.
   *
   * @param buffer  the byte array
   */
  public void setBuffer(byte[] buffer) {
    this.buffer = buffer;

    attribute.setBuffer(buffer);
  }

}
