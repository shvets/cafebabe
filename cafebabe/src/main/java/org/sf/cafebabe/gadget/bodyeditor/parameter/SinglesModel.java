// SinglesModel.java

package org.sf.cafebabe.gadget.bodyeditor.parameter;

import java.util.Vector;

import org.sf.classfile.ConstPool;
import org.sf.classfile.PoolEntry;

public class SinglesModel extends RefsModel {

  public SinglesModel(Vector types, ConstPool constPool) {
    super(types, constPool);
  }

  protected String getItem(short index) {
    PoolEntry entry = (PoolEntry)constPool.get(index);

    return entry.resolve(constPool).replace('/', '.');
  }

}
