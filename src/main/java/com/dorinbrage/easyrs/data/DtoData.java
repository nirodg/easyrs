package com.dorinbrage.easyrs.data;

import com.google.gson.internal.LinkedTreeMap;

/**
 * The Dto Data, reflects the JSON file
 * 
 * @author Dorin Brage
 */
public class DtoData {

  private LinkedTreeMap<String, Object> create;

  private LinkedTreeMap<String, Object> update;

  private int getAll;

  /**
   * @return the create
   */
  public LinkedTreeMap<String, Object> getCreate() {
    return create;
  }

  /**
   * @return the update
   */
  public LinkedTreeMap<String, Object> getUpdate() {
    return update;
  }

  /**
   * @param create the create to set
   */
  public void setCreate(LinkedTreeMap<String, Object> create) {
    this.create = create;
  }

  /**
   * @param update the update to set
   */
  public void setUpdate(LinkedTreeMap<String, Object> update) {
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
