package com.dbrage.lib.easyrs.processor.enums;

/**
 * The statements 
 *  
 * @author Dorin Brage
 */
public enum StatementType {
	
	/* Field name for a single entity */
	nameEntity("entity"),
	nameFetchedEntity("fetchedEntity"),
	
	/* Field name for a list of entities */
	nameEntities("entities"),
	nameFetchedEntities("fetchedEntities"),

	/* Entity */
	defineEntity("%s %s = (%s) getData(%s)"),
	defineGetById("%s %s = (%s) getClient().getById(%s)"),
	defineCreateEntity("%s %s = (%s) getClient().put(%s)"),
	defineUpdateEntity("%s %s = (%s) getClient().post(%s.%s, %s)"),
	defineDeleteEntity("%s %s = (%s) getClient().delete(%s.%s)"),
	definePersistEntity("%s = (%s) getClient().put(%s)"),
	
	/* Entities*/ 
	defineEntities("List<%s> %s = (ArrayList<%s>) getData(%s)"),
	defineFetchEntities("List<%s> %s = (ArrayList<%s>) getClient().getAll()"),
	
	/* Asserts */
	defineAssertNull("Assert.assertNull(%s)"),
	defineAssertNotNull("Assert.assertNotNull(%s)"),
	defineAssertEquals("Assert.assertEquals(%s, %s)"),
	defineAssertEqualsSize("Assert.assertEquals(%s.size(), %s.size())");
	
	private String value;
	
	private StatementType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
