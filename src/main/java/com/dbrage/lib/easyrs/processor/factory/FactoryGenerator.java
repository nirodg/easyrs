package com.dbrage.lib.easyrs.processor.factory;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.dbrage.lib.easyrs.arquillian.Container;
import com.dbrage.lib.easyrs.client.Client;
import com.dbrage.lib.easyrs.processor.enums.ClientRequest;
import com.squareup.javawriter.JavaWriter;

/**
 * The Factory Generator it generates the class
 * 
 * @author Dorin_Brage
 */
public class FactoryGenerator {

	private static final String VAR_CLIENT = "client";
	private static final String VAR_PERSISTED_ENTITY = "persistedEntity";
	private static final String VAR_PERSISTED_ENTITIES = "persistedEntities";


	private static final String GENERATED_CLASS_SUFFIX = "TestEndpoint";

	private static final String METHOD_VOID = "void";
	private static final String METHOD_DEPLOYMENT = "deployment";
	private static final String METHOD_SETUP = "setUp";
	private static final String METHOD_GET_ALL = "getAll";
	private static final String METHOD_CREATE = "create";
	private static final String METHOD_UPDATE = "update";
	private static final String METHOD_DELETE = "delete";

	private short sequence = 1;

	private List<String> classImports;

	private JavaWriter jw;
	private Messager messager;

	private TypeElement annotatedClazz;

	private FactoryClasses factory;
	
	public FactoryGenerator(Messager messager) {
		this.messager = messager;
	}

	public void write(FactoryClasses factoryClasses, TypeElement annotatedClazz, Filer filer, Elements elements) {

		this.annotatedClazz = annotatedClazz;
		this.factory = factoryClasses;
		
		String name = factoryClasses.getQualifiedName().toString().concat(GENERATED_CLASS_SUFFIX);

		setUpWriter(name, filer);

		createPackage(annotatedClazz, elements);

		setRunWithArquillian();

		startClass(name);
		
		defineVariables();

		setSetUp();

		setDeploymentMethod(factoryClasses.getClassName(), jw);

		if (factoryClasses.getTestOperations().length == 1
				&& factoryClasses.getTestOperations()[0].equals(ClientRequest.ALL)) {
			setGetAllMethod();
			sequence++;

			setCreateMethod();
			sequence++;

			setUpdateMethod();
			sequence++;

			setDeleteMethod();
		} else {
			for (ClientRequest operation : factoryClasses.getTestOperations()) {

				if (operation.equals(ClientRequest.GET)) {
					setGetAllMethod();
					sequence++;
				}
				if (operation.equals(ClientRequest.PUT)) {
					setCreateMethod();
					sequence++;
				}
				if (operation.equals(ClientRequest.POST)) {
					setUpdateMethod();
					sequence++;
				}
				if (operation.equals(ClientRequest.DELETE)) {
					setDeleteMethod();
				}
			}
		}

		// Reset counter
		sequence = 1;

		endClass();

	}

	private void setRunWithArquillian() {
		try {
			jw.emitAnnotation(RunWith.class, Arquillian.class.getSimpleName().concat(".class"));
		} catch (IOException e) {
			error(annotatedClazz, "Couldn't add Arquillian's annotation for %s\n%s", annotatedClazz.getSimpleName(),
					e.getMessage());
		}
	}

	private void setUpWriter(String qualifiedName, Filer filer) {
		JavaFileObject jfo;
		try {
			jfo = filer.createSourceFile(qualifiedName);
			Writer writer = jfo.openWriter();
			jw = new JavaWriter(writer);
		} catch (IOException e) {
			error(annotatedClazz, "Couldn't setup the Writer %s\n%s", annotatedClazz.getSimpleName(), e.getMessage());
		}
	}

	private void setUpImports() {
		classImports = new ArrayList<String>();
		classImports.add(Container.class.getCanonicalName());
		classImports.add(WebArchive.class.getCanonicalName());
		classImports.add(Deployment.class.getCanonicalName());
		classImports.add(Arquillian.class.getCanonicalName());
		classImports.add(RunWith.class.getCanonicalName());
		classImports.add(Test.class.getCanonicalName());
		classImports.add(InSequence.class.getCanonicalName());
		classImports.add(Before.class.getCanonicalName());
		classImports.add(Client.class.getCanonicalName());
		
		classImports.add(this.factory.getEntity().toString());
	}

	private void createPackage(TypeElement annotatedClazz, Elements elements) {

		PackageElement pkg = elements.getPackageOf(annotatedClazz);
		try {

			// Write package
			if (!pkg.isUnnamed()) {
				jw.emitPackage(pkg.getQualifiedName().toString());
			} else {
				jw.emitPackage("");
			}

			setUpImports();

			// Add all the imports
			jw.emitImports(classImports);
			jw.emitEmptyLine();

		} catch (IOException e) {
			error(annotatedClazz, "Couldn't create the pakage for %s\n%s", annotatedClazz.getSimpleName(),
					e.getMessage());
		}

	}

	private void setDeploymentMethod(String name, JavaWriter jw2) {
		Set<Modifier> modifier = getCustomModifier(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);

		try {
			jw.emitAnnotation(Deployment.class.getSimpleName());

			jw.beginMethod(WebArchive.class.getName(), METHOD_DEPLOYMENT, modifier, null, null);
			jw.emitStatement("%s %s = getDeployment(%s.class)", WebArchive.class.getSimpleName(), METHOD_DEPLOYMENT,
					name);
			jw.emitStatement("return %s", METHOD_DEPLOYMENT);
			jw.endMethod();
			jw.emitEmptyLine();
		} catch (IOException e) {
			error(annotatedClazz, "Couldn't setup the deployment menthod for %s\n%s", annotatedClazz.getSimpleName(),
					e.getMessage());
		}
	}

	private void startClass(String className) {

		try {
			// Add the class
			jw.beginType(className, "class", EnumSet.of(Modifier.PUBLIC), Container.class.getName());
			jw.emitEmptyLine();
		} catch (IOException e) {
			error(annotatedClazz, "Couldn't start writing the class for %s\n%s", annotatedClazz.getSimpleName(),
					e.getMessage());
		}

	}

	private void endClass() {
		try {
			jw.endType();
			jw.close();
		} catch (IOException e) {
			error(annotatedClazz, "Couldn't end the class for %s\n%s", annotatedClazz.getSimpleName(), e.getMessage());
		}
	}

	private void setSetUp() {

		Set<Modifier> modifiers = getCustomModifier(Modifier.PUBLIC);

		try {
			jw.emitAnnotation(Before.class);

			jw.beginMethod(METHOD_VOID, METHOD_SETUP, modifiers, null, null);
			
			initializeInstances();
			
			jw.endMethod();
			jw.emitEmptyLine();

		} catch (Exception e) {
			error(annotatedClazz, "Couldn't define the setup\n %s", e.getMessage());
		}
	}

	private void defineVariables() {
		try {
			// HTTP Client
			String clientFormatedVariable = String.format("%s<%s>", Client.class.getSimpleName(), this.factory.getEntity());
			jw.emitField(clientFormatedVariable, VAR_CLIENT, getCustomModifier(Modifier.PRIVATE));
			
			
			jw.emitEmptyLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initializeInstances() {
		try {

			jw.emitStatement(VAR_CLIENT + " = new %s<>()", Client.class.getSimpleName());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void setGetAllMethod() {

		Set<Modifier> modifier = getCustomModifier(Modifier.PUBLIC);

		try {
			// Annotation
			jw.emitAnnotation(Test.class);
			jw.emitAnnotation(InSequence.class, sequence);

			// Deployment method
			jw.beginMethod(METHOD_VOID, METHOD_GET_ALL, modifier, null, null);
			jw.endMethod();
			jw.emitEmptyLine();

		} catch (IOException e) {
			error(annotatedClazz, "Couldn't write the getAll() method for %s\n%s", annotatedClazz.getSimpleName(),
					e.getMessage());
		}

	}

	private void setCreateMethod() {

		Set<Modifier> modifier = getCustomModifier(Modifier.PUBLIC);

		try {
			// Annotation
			jw.emitAnnotation(Test.class);
			jw.emitAnnotation(InSequence.class, sequence);

			// Deployment method
			jw.beginMethod(METHOD_VOID, METHOD_CREATE, modifier, null, null);
			jw.endMethod();
			jw.emitEmptyLine();

		} catch (IOException e) {
			error(annotatedClazz, "Couldn't write the create() method for %s\n%s", annotatedClazz.getSimpleName(),
					e.getMessage());
		}

	}

	private void setUpdateMethod() {

		Set<Modifier> modifier = getCustomModifier(Modifier.PUBLIC);

		try {
			// Annotation
			jw.emitAnnotation(Test.class);
			jw.emitAnnotation(InSequence.class, sequence);

			// Deployment method
			jw.beginMethod(METHOD_VOID, METHOD_UPDATE, modifier, null, null);
			jw.endMethod();
			jw.emitEmptyLine();

		} catch (IOException e) {
			error(annotatedClazz, "Couldn't write the update() method for %s\n%s", annotatedClazz.getSimpleName(),
					e.getMessage());
		}
	}

	private void setDeleteMethod() {

		Set<Modifier> modifier = getCustomModifier(Modifier.PUBLIC);

		try {
			// Annotation
			jw.emitAnnotation(Test.class);
			jw.emitAnnotation(InSequence.class, sequence);

			// Deployment method
			jw.beginMethod(METHOD_VOID, METHOD_DELETE, modifier, null, null);
			jw.endMethod();
			jw.emitEmptyLine();

		} catch (IOException e) {
			error(annotatedClazz, "Couldn't write the delete() method for %s\n%s", annotatedClazz.getSimpleName(),
					e.getMessage());
		}
	}

	private Set<Modifier> getCustomModifier(Object... objects) {
		Set<Modifier> type = new HashSet<>();

		for (Object modifier : objects) {
			type.add((Modifier) modifier);
		}
		return type;
	}

	private void error(Element element, String string, Object... args) {
		messager.printMessage(Kind.ERROR, String.format(string, args), element);
		System.err.println(String.format(string, args));
	}
}
