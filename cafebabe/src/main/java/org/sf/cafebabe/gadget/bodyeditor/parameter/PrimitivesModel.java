// PrimitivesModel.java

package org.sf.cafebabe.gadget.bodyeditor.parameter;

import java.util.Vector;

public class PrimitivesModel extends TypesModel {

  private static Vector primitives = new Vector();
  static {
    primitives.addElement(new Short((short)4));
    primitives.addElement(new Short((short)5));
    primitives.addElement(new Short((short)6));
    primitives.addElement(new Short((short)7));
    primitives.addElement(new Short((short)8));
    primitives.addElement(new Short((short)9));
    primitives.addElement(new Short((short)10));
    primitives.addElement(new Short((short)11));
  };

  private static String[] names = {
     "boolean", "char", "float", "double", "byte", "short", "int", "long"
  };

  public PrimitivesModel() {
    super(primitives);

    selectedObject = getElementAt(0);

    fireContentsChanged(this, 0, types.size()-1);
  }

  protected String getItem(short index) {
    Short typeIndex = new Short(index);
    String type     = null;

    for(int i=0; i < primitives.size(); i++) {
      if(typeIndex.equals(primitives.elementAt(i))) {
        type = names[i];
        break;
      }
    }

    return type;
  }

}

