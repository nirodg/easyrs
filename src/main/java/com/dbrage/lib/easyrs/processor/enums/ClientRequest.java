package com.dbrage.lib.easyrs.processor.enums;

/**
 * The requests available for testing the end points
 * 
 * @see FactoryGenerator
 * @author Dorin_Brage
 */
public enum ClientRequest {

	ALL(1, "getAll"),

	GET(2, "getById"),

	PUT(3, "create"),

	POST(4, "update"),

	DELETE(5, "delete");

	private int ordinal;

	private String nameMethod;

	private ClientRequest(int ordinal, String nameMethod) {
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