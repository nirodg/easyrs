package com.dorinbrage.easyrs.processor.common;

import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;

import com.dorinbrage.easyrs.processor.annotation.EndpointTest;
import com.dorinbrage.easyrs.processor.enums.ClientOperation;
import com.dorinbrage.easyrs.processor.enums.ExecutionMode;
import com.dorinbrage.easyrs.processor.enums.UUIDIdentifier;

/**
 * It reflects the annotated interface
 * 
 * @author Dorin Brage
 */
public class AnnotatedClass {

  private Name qualifiedName;
  private String className;

  private TypeMirror entity;
  private TypeMirror endpoint;
  private ClientOperation[] clientOperations;
  private UUIDIdentifier identifier;
  private ExecutionMode executionMode;

  public AnnotatedClass(TypeElement annotatedClazz, EndpointTest annotation) {

    this.qualifiedName = annotatedClazz.getQualifiedName();
    this.className = annotatedClazz.getSimpleName().toString();
    this.clientOperations = annotation.operations();
    this.identifier = annotation.identifier();
    this.executionMode = annotation.execution();

    getEntityFieldAnnotation(annotation);
    getEndpointFieldAnnotation(annotation);

  }

  private void getEntityFieldAnnotation(EndpointTest annotation) {
    try {
      annotation.entity();
    } catch (MirroredTypeException e) {
      entity = e.getTypeMirror();
    }
  }

  private void getEndpointFieldAnnotation(EndpointTest annotation) {
    try {
      annotation.endpoint();
    } catch (MirroredTypeException e) {
      endpoint = e.getTypeMirror();
    }
  }

  public Name getQualifiedName() {
    return qualifiedName;
  }

  public void setQualifiedName(Name qualifiedName) {
    this.qualifiedName = qualifiedName;
  }

  public TypeMirror getEntity() {
    return entity;
  }

  public void setEntity(TypeMirror entity) {
    this.entity = entity;
  }

  public TypeMirror getEndpoint() {
    return endpoint;
  }

  public void setEndpoint(TypeMirror endpoint) {
    this.endpoint = endpoint;
  }

  public ClientOperation[] getClientOperations() {
    return clientOperations;
  }

  public void setClientOperations(ClientOperation[] testOperations) {
    this.clientOperations = testOperations;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public UUIDIdentifier getIdentifier() {
    return identifier;
  }

  public void setIdentifier(UUIDIdentifier identifier) {
    this.identifier = identifier;
  }

  public ExecutionMode getExecutionMode() {
    return executionMode;
  }

  public void setExecutionMode(ExecutionMode executionMode) {
    this.executionMode = executionMode;
  }

}
