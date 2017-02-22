package com.dorinbrage.easyrs.processor.enums;

/**
 * 
 * @author Dorin Brage
 */
public enum UUIDIdentifier {

  ID("getId()"),

  GUID("getGuid()"),

  UUID("getUuid()");

  private String value;

  private UUIDIdentifier(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
