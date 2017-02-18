package com.dbrage.lib.easyrs.processor.enums;

/**
 * The Processing Error messages
 * 
 * @author Dorin Brage
 */
public enum ProcessingError {
	
	INIT("Something is missig or null"),
	INIT_NOT_DEFINES("Consider initializing the init() method"),
	INIT_WRITER("Error initializing the writer %s\n%s"), 
	INIT_PACKAGE("Error initializing the package %s\n%s"),
	INIT_IMPORTS("Error defining the imports %s\n%s"),
	SET_INIT_CLASS("Couldn't start writing the class for %s\n%s"), 
	SET_END_CLASS("Couldn't close the class for %s\n%s"), 
	SET_DEPLOYMENT_METHOD("Couldn't setup the deployment menthod for %s\n%s"), 
	SET_BEFORE_METHOD("Couldn't define the Before's method\n %s"),
	PROCESSOR_ONLY_INTERFACES("Only interfaces can be annotated with @%s"), 
	PROCESSOR_ERROR_GENERATING("Error generating the class"), 
	CLASS_CANT_BE_DUPLICATED("%s it can't be generated twice");
	
	private String value;

	private ProcessingError(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
