package com.dorinbrage.easyrs.data;

/**
 * The Dto Data, reflects the JSON file
 * 
 * @author Dorin Brage
 */
public class DtoData {

  private Object create;

  private Object update;

  private int getAll;

  /**
   * @return the create
   */
  public Object getCreate() {
    return create;
  }

  /**
   * @return the update
   */
  public Object getUpdate() {
    return update;
  }

  /**
   * @param create the create to set
   */
  public void setCreate(Object create) {
    this.create = create;
  }

  /**
   * @param update the update to set
   */
  public void setUpdate(Object update) {
    this.update = update;
  }

  /**
   * @return the getAll
   */
  public int getGetAll() {
    return getAll;
  }

  /**
   * @param getAll the getAll to set
   */
  public void setGetAll(int getAll) {
    this.getAll = getAll;
  }

}
