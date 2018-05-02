package com.dorinbrage.easyrs.processor.exception;

import javax.lang.model.element.Element;

import com.dorinbrage.easyrs.processor.common.AnnotatedClass;
import com.dorinbrage.easyrs.processor.enums.ProcessingError;

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

  public ProcessingException(AnnotatedClass annotatedClass, ProcessingError initPackage) {
    super(String.format(annotatedClass.getClassName(), initPackage));
  }

  public ProcessingException(ProcessingError processorErrorGenerating) {
    super();
  }

  public ProcessingException(ProcessingError processorErrorGenerating, String message) {
     super();
  }

  public Element getElement() {
    return element;
  }

}
