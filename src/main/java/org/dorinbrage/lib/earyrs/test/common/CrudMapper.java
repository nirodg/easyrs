package org.dorinbrage.lib.earyrs.test.common;

public class CrudMapper<T> {

  protected T create;
  protected T update;

  protected String[] type;

  public CrudMapper() {
  }

  @Override
  public String toString() {
    return super.toString();
  }

  public T getCreate() {
    return create;
  }

  public void setCreate(T create) {
    this.create = create;
  }

  public T getUpdate() {
    return update;
  }

  public void setUpdate(T update) {
    this.update = update;
  }

  public String[] getType() {
    return type;
  }

  public void setType(String[] type) {
    this.type = type;
  }

  
}
