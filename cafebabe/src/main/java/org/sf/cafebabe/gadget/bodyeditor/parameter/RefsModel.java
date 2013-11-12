// RefsModel.java

package org.sf.cafebabe.gadget.bodyeditor.parameter;

import java.util.Vector;

import org.sf.classfile.ConstPool;
import org.sf.classfile.PoolEntry;

public class RefsModel extends TypesModel {

  protected ConstPool constPool;

  public RefsModel(Vector types, ConstPool constPool) {
    super(types);

    this.constPool = constPool;

    if(getSize() > 0)
      selectedObject = getElementAt(0);

    fireContentsChanged(this, 0, types.size()-1);
  }

  protected String getItem(short index) {
    PoolEntry entry = (PoolEntry)constPool.get(index);

/*    String res = entry.resolve(constPool);

    StringBuffer sb = new StringBuffer();
    for(int i=1; i < res.length-1; i++) {
      sb.append(res[i] + " ");
    }

    sb.append(res[res.length-1]);

    return sb.toString();
*/

    return entry.resolve(constPool);
  }

}
