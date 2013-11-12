// MembersModel.java

package org.sf.cafebabe.gadget.bodyeditor.parameter;

import java.util.StringTokenizer;
import java.util.Vector;

import org.sf.classfile.ConstPool;
import org.sf.classfile.PoolEntry;

public class MembersModel extends RefsModel {

  public MembersModel(Vector types, ConstPool constPool) {
    super(types, constPool);
  }

  protected String getItem(short index) {
    PoolEntry entry = (PoolEntry)constPool.get(index);

    return entry.resolve(constPool);
  }

  protected String getName(short index) {
    PoolEntry entry = (PoolEntry)constPool.get(index);

    StringTokenizer st = new StringTokenizer(entry.resolve(constPool));

    st.nextToken();

    return st.nextToken();
  }


  protected String getClass(short index) {
    PoolEntry entry = (PoolEntry)constPool.get(index);

    StringTokenizer st = new StringTokenizer(entry.resolve(constPool));

    return st.nextToken();
  }

  protected String getSignature(short index) {
    PoolEntry entry = (PoolEntry)constPool.get(index);

    StringTokenizer st = new StringTokenizer(entry.resolve(constPool));

    st.nextToken();
    st.nextToken();

    return st.nextToken();
  }

}
