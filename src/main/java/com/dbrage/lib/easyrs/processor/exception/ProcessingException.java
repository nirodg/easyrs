package com.dbrage.lib.easyrs.processor.exception;

import javax.lang.model.element.Element;

import com.dbrage.lib.easyrs.processor.enums.ProcessingError;

/**
 * The Processing Exception
 * 
 * @author Dorin Brage
 */
public class ProcessingException extends Exception {

	private static final long serialVersionUID = 1L;

	private Element element;

	public ProcessingException(Element element, ProcessingError error, Object... args) {
		super(String.format(error.getValue(), args));
		this.element = element;
	}

	public Element getElement() {
		return element;
	}

}
