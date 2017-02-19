package com.dbrage.lib.easyrs.processor.enums;

/**
 * The requests available for testing the end points
 * 
 * @author Dorin Brage
 */
public enum ClientOperation {

  ALL(0, "ALL"),

  GET_ALL(1, "getAll"),

  GET(2, "getById"),

  PUT(3, "create"),

  POST(4, "update"),

  DELETE(5, "delete");

  private int ordinal;

  private String nameMethod;

  private ClientOperation(int ordinal, String nameMethod) {
    this.ordinal = ordinal;
    this.nameMethod = nameMethod;
  }

  public int getOrdinal() {
    return ordinal;
  }

  public void setOrdinal(int ordinal) {
    this.ordinal = ordinal;
  }

  public String getNameMethod() {
    return nameMethod;
  }

  public void setNameMethod(String nameMethod) {
    this.nameMethod = nameMethod;
  }

}
