// TypesModel.java

package org.sf.cafebabe.gadget.bodyeditor.parameter;

import java.util.*;

import javax.swing.*;

public abstract class TypesModel extends AbstractListModel
                                 implements ComboBoxModel {
  protected Object selectedObject;

  protected List types;

  public TypesModel(List types) {
    this.types = types;
  }

  public int getSize() {
    return types.size();
  }

  public Object getTypeAt(int index) {
    return types.get(index);
  }

  public Object getElementAt(int index) {
    if(index >= 0 && index < types.size()) {
      return getItem(((Short)types.get(index)).shortValue());
    }
    else {
      return null;
    }
  }

  protected abstract String getItem(short index);

  public void navigate(short index) {
    try {
      Object item = getItem(index);
      if(item == null)
        throw new Exception();

      setSelectedItem(item);
    }
    catch(Exception e) {
      Short indexWrapper = (Short)types.get(0);
      setSelectedItem(null);
      setSelectedItem(getItem(indexWrapper.shortValue()));
    }
  }

  // implements ComboBoxModel

  public Object getSelectedItem() {
    return selectedObject;
  }

  public void setSelectedItem(Object anObject) {
    if((selectedObject != null && !selectedObject.equals(anObject)) ||
       selectedObject == null && anObject != null ) {
      selectedObject = anObject;
      fireContentsChanged(this, -1, -1);
    }
  }

}
