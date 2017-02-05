package com.dbrage.lib.easyrs.processor.annotation.common;

import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;

import com.dbrage.lib.easyrs.processor.annotation.EndpointTest;
import com.dbrage.lib.easyrs.processor.enums.ClientOperation;

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

	public AnnotatedClass(TypeElement annotatedClazz, EndpointTest annotation) {

		this.qualifiedName = annotatedClazz.getQualifiedName();
		this.className = annotatedClazz.getSimpleName().toString();

		getEntityFieldAnnotation(annotation);
		getEndpointFieldAnnotation(annotation);
		getTestOperations(annotation);

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

	private void getTestOperations(EndpointTest annotation) {
		clientOperations = annotation.operations();
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

}
