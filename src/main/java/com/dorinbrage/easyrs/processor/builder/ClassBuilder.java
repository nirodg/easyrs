package com.dorinbrage.easyrs.processor.builder;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Generated;
import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolverSystem;
import org.jboss.shrinkwrap.resolver.api.maven.MavenStrategyStage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.dorinbrage.easyrs.arquillian.Container;
import com.dorinbrage.easyrs.processor.EndpointProcessor;
import com.dorinbrage.easyrs.processor.common.AnnotatedClass;
import com.dorinbrage.easyrs.processor.enums.ClientOperation;
import com.dorinbrage.easyrs.processor.enums.ExecutionMode;
import com.dorinbrage.easyrs.processor.enums.ProcessingError;
import com.dorinbrage.easyrs.processor.enums.StatementType;
import com.dorinbrage.easyrs.processor.exception.ProcessingException;
import com.google.common.reflect.ClassPath;
import com.squareup.javawriter.JavaWriter;

/**
 * The Rest Endpoint Test Builder
 * 
 * @author Dorin Brage
 */
@SuppressWarnings("unused")
public class ClassBuilder {

  private static final String GENERATED_CLASS_SUFFIX = "TestEndpoint";
  private static final String METHOD_DEPLOYMENT = "deployment";
  private static final String METHOD_VOID = "void";
  private static final String METHOD_SETUP = "setUp";
  private static final String VAR_CLIENT = "getClient(%s)";

  private Elements elements;
  private TypeElement typeAnnotatedClazz;
  private Filer filer;

  private String finalGeneratedClass = null;

  private JavaWriter jw;

  private AnnotatedClass annotatedClass;

  private Map<ClientOperation, MethodBuilder> methods;

  private boolean isInitialized;
  private boolean isMethodInitialized;

  /**
   * The {@code init() } initializes the builder
   * 
   * @param typeAnnotatedClazz the annotated class
   * @param filer the {@link Filer}
   * @param elements a list of {@link Elements}
   * @param annotatedClass the annotated class
   * @return {@link ClassBuilder}
   * @throws Exception if it was unsuccessful
   */
  public ClassBuilder init(TypeElement typeAnnotatedClazz, AnnotatedClass annotatedClass,
      Filer filer, Elements elements) throws Exception {

    if (typeAnnotatedClazz == null || filer == null || elements == null) {
      throw new ProcessingException(typeAnnotatedClazz, ProcessingError.INIT_PACKAGE);
    }

    this.typeAnnotatedClazz = typeAnnotatedClazz;
    this.annotatedClass = annotatedClass;
    this.elements = elements;
    this.filer = filer;

    /** Class name + suffix **/
    this.finalGeneratedClass =
        typeAnnotatedClazz.getQualifiedName().toString().concat(GENERATED_CLASS_SUFFIX);

    this.isInitialized = true;

    return this;
  }

  /**
   * Sets the end of the class
   * 
   * @throws ProcessingException if it was unsuccessful
   */
  private void setEndClass() throws ProcessingException {

    try {
      jw.endType();
      jw.close();
    } catch (IOException e) {
      throw new ProcessingException(typeAnnotatedClazz, ProcessingError.SET_END_CLASS,
          typeAnnotatedClazz.getSimpleName(), e.getMessage());
    }

  }

  /**
   * Initializes the class with {@code @RunWith(Arquillian.class")}
   * 
   * @throws ProcessingException if it was unsuccessful
   */
  private void setStartClass() throws ProcessingException {
    String extendedContainer = String.format("%s<%s,%s>", Container.class.getName(),
        annotatedClass.getEntity(), annotatedClass.getEndpoint());

    try {

      if (annotatedClass.getExecutionMode().equals(ExecutionMode.ARQUILLIAN)) {
        jw.emitAnnotation(RunWith.class, Arquillian.class.getSimpleName().concat(".class"));
      }

      jw.emitAnnotation(SuppressWarnings.class, "\"unchecked\"");

      jw.beginType(this.finalGeneratedClass, "class", getCustomModifier(Modifier.PUBLIC),
          extendedContainer);
      jw.emitEmptyLine();
    } catch (IOException e) {
      throw new ProcessingException(typeAnnotatedClazz, ProcessingError.SET_INIT_CLASS,
          typeAnnotatedClazz.getSimpleName(), e.getMessage());
    }

  }

  /**
   * Initializes the package
   * 
   * @throws ProcessingException if it was unsuccessful
   */
  private void initPackage() throws ProcessingException {
    try {

      PackageElement pkg = elements.getPackageOf(typeAnnotatedClazz);

      if (!pkg.isUnnamed()) {
        jw.emitPackage(pkg.getQualifiedName().toString());
      } else {
        jw.emitPackage("");
      }

    } catch (Exception e) {
      throw new ProcessingException(typeAnnotatedClazz, ProcessingError.INIT_PACKAGE,
          typeAnnotatedClazz.getSimpleName(), e.getMessage());
    }
  }

  /**
   * Initializes the writer
   * 
   * @throws Exception if it was unsuccessful
   */
  private void initWritter() throws ProcessingException {
    JavaFileObject jfo;
    try {
      jfo = filer.createSourceFile(this.finalGeneratedClass);
      Writer writer = jfo.openWriter();
      jw = new JavaWriter(writer);
    } catch (IOException e) {
      throw new ProcessingException(typeAnnotatedClazz, ProcessingError.INIT_WRITER,
          typeAnnotatedClazz.getSimpleName(), e.getMessage());
    }
  }

  /**
   * Define the methods
   * 
   * @throws IOException if something went wrong
   */
  private void setMethods() throws IOException {

    byte sequence = 0;

    TypeMirror entity = annotatedClass.getEntity();
    String simpleEntityName = entity.toString().substring(entity.toString().lastIndexOf(".") + 1);

    methods = new TreeMap<>();

    // Get All's method
    methods.put(ClientOperation.GET_ALL, new MethodBuilder(ClientOperation.GET_ALL.getNameMethod())
        // Local Entities
        .addStatements(StatementType.defineEntities.getValue(), simpleEntityName,
            StatementType.nameEntities.getValue(), simpleEntityName, ClientOperation.GET_ALL.name())
        // Fetched Entities
        .addStatements(StatementType.defineFetchEntities.getValue(), simpleEntityName,
            StatementType.nameFetchedEntities.getValue(), simpleEntityName)
        // Add empty line
        .addEmptyLine()
        // Define Assert
        .addStatements(StatementType.defineAssertEqualsSize.getValue(),
            StatementType.nameEntities.getValue(), StatementType.nameFetchedEntities.getValue()));

    // Create's method
    methods.put(ClientOperation.PUT,
        new MethodBuilder(ClientOperation.PUT.getNameMethod())
            // Local simpleEntityName
            .addStatements(StatementType.defineEntity.getValue(), simpleEntityName,
                StatementType.nameEntity.getValue(), simpleEntityName, ClientOperation.PUT.name())
            // Assert Not Null
            .addStatements(StatementType.defineAssertNotNull.getValue(),
                StatementType.nameEntity.getValue())
            // Add empty line
            .addEmptyLine()
            // Persist simpleEntityName
            .addStatements(StatementType.defineCreateEntity.getValue(), simpleEntityName,
                StatementType.nameFetchedEntity.getValue(), simpleEntityName,
                StatementType.nameEntity.getValue())
            // Assert Not Null
            .addStatements(StatementType.defineAssertNotNull.getValue(),
                StatementType.nameFetchedEntity.getValue())
            // Add empty line
            .addEmptyLine()
            // Assert Equal
            .addStatements(StatementType.defineAssertEquals.getValue(),
                StatementType.nameEntity.getValue(), StatementType.nameFetchedEntity.getValue()));

    // Update's method
    methods.put(ClientOperation.POST,
        new MethodBuilder(ClientOperation.POST.getNameMethod())
            // Local simpleEntityName
            .addStatements(StatementType.defineEntity.getValue(), simpleEntityName,
                StatementType.nameEntity.getValue(), simpleEntityName, ClientOperation.POST.name())
            // Assert Not Null
            .addStatements(StatementType.defineAssertNotNull.getValue(),
                StatementType.nameEntity.getValue())
            // Add empty line
            .addEmptyLine()
            // Persist simpleEntityName
            .addStatements(StatementType.definePersistEntity.getValue(),
                StatementType.nameEntity.getValue(), simpleEntityName,
                StatementType.nameEntity.getValue())
            // Assert Not Null
            .addStatements(StatementType.defineAssertNotNull.getValue(),
                StatementType.nameEntity.getValue())
            // Add empty line
            .addEmptyLine()
            // Fetched Entity
            .addStatements(StatementType.defineUpdateEntity.getValue(), simpleEntityName,
                StatementType.nameFetchedEntity.getValue(), simpleEntityName,
                StatementType.nameEntity.getValue(), annotatedClass.getIdentifier().getValue(),
                StatementType.nameEntity.getValue())
            // Assert Not Null
            .addStatements(StatementType.defineAssertNotNull.getValue(),
                StatementType.nameFetchedEntity.getValue())
            // Add empty line
            .addEmptyLine()
            // Assert Equal
            .addStatements(StatementType.defineAssertEquals.getValue(),
                StatementType.nameEntity.getValue(), StatementType.nameFetchedEntity.getValue()));

    // Delete's method
    methods.put(ClientOperation.DELETE, new MethodBuilder(ClientOperation.DELETE.getNameMethod())
        // Local simpleEntityName
        .addStatements(StatementType.defineEntity.getValue(), simpleEntityName,
            StatementType.nameEntity.getValue(), simpleEntityName, ClientOperation.DELETE.name())
        // Assert Not Null
        .addStatements(StatementType.defineAssertNotNull.getValue(),
            StatementType.nameEntity.getValue())
        // Add empty line
        .addEmptyLine()
        // Persist simpleEntityName
        .addStatements(StatementType.definePersistEntity.getValue(),
            StatementType.nameEntity.getValue(), simpleEntityName,
            StatementType.nameEntity.getValue())
        // Assert Not Null
        .addStatements(StatementType.defineAssertNotNull.getValue(),
            StatementType.nameEntity.getValue())
        // Add empty line
        .addEmptyLine()
        // Delete simpleEntityName
        .addStatements(StatementType.defineDeleteEntity.getValue(),
            StatementType.nameFetchedEntity.getValue(), StatementType.nameEntity.getValue(),
            annotatedClass.getIdentifier().getValue())
        // Assert True of Not Null
        .addStatements(StatementType.defineTrueorNotNullAssert.getValue(),
            StatementType.nameFetchedEntity.getValue(), simpleEntityName,
            StatementType.nameFetchedEntity.getValue(), StatementType.nameFetchedEntity.getValue(),
            StatementType.nameFetchedEntity.getValue(),
            StatementType.nameFetchedEntity.getValue()));

    /*
     * If the operations's field is not specified or operations = {ClientOperation.GET} , by default
     * will define all the methods. Otherwise will check those which are missing the they will be
     * removed.
     */

    boolean isDefault = false;

    Map<ClientOperation, MethodBuilder> methodsToGenerate = new HashMap<>();
    if (annotatedClass.getClientOperations()[0] != ClientOperation.ALL) {

      for (ClientOperation operation : annotatedClass.getClientOperations()) {
        if (methods.containsKey(operation)) {
          methodsToGenerate.put(operation, methods.get(operation));
        }
      }

      // Clear the list of methods and assign those which were specified
      methods.clear();
      methods.putAll(methodsToGenerate);
    }

    for (MethodBuilder method : methods.values()) {

      // Increment in 1 the current value
      sequence++;

      jw.emitAnnotation(Test.class);
      if (annotatedClass.getExecutionMode().equals(ExecutionMode.ARQUILLIAN)) {
        jw.emitAnnotation(InSequence.class, sequence);
      }

      jw.beginMethod(method.getTypeMethod(), method.getName(), method.getModifiers(), null, null);

      for (String statement : method.getStatements()) {
        if (statement.equals(MethodBuilder.EMPTY_LINE)) {
          jw.emitEmptyLine();
        } else {
          jw.emitStatement(statement, (Object[]) null);
        }
      }

      jw.endMethod();
      jw.emitEmptyLine();
    }

  }

  /**
   * Define the global variables
   */
  private void setVariables() throws ProcessingException {
    try {
      jw.emitSingleLineComment("Here you can define your global variables", (Object[]) null);
      jw.emitEmptyLine();
    } catch (IOException e) {
      throw new ProcessingException(typeAnnotatedClazz, ProcessingError.GENERIC_ERROR,
          e.getMessage());
    }
  }

  /**
   * Define deployment method
   * 
   * @throws ProcessingException
   */
  private void setDeploymentMethod() throws ProcessingException {
    Set<Modifier> modifier = getCustomModifier(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);

    try {
      jw.emitAnnotation(Deployment.class.getSimpleName());

      jw.beginMethod(WebArchive.class.getName(), METHOD_DEPLOYMENT, modifier, null, null);
      jw.emitStatement("%s %s = getDeployment(%s.class)", WebArchive.class.getSimpleName(),
          METHOD_DEPLOYMENT, annotatedClass.getClassName());
      jw.emitStatement("return %s", METHOD_DEPLOYMENT);
      jw.endMethod();
      jw.emitEmptyLine();
    } catch (IOException e) {
      throw new ProcessingException(typeAnnotatedClazz, ProcessingError.SET_DEPLOYMENT_METHOD,
          e.getMessage());
    }
  }

  /**
   * Defines the {@code Before} method
   * 
   * @throws ProcessingException
   */
  private void setBefore() throws ProcessingException {

    Set<Modifier> modifiers = getCustomModifier(Modifier.PUBLIC);

    try {
      jw.emitAnnotation(Before.class);

      jw.beginMethod(METHOD_VOID, METHOD_SETUP, modifiers, null, null);

      jw.emitSingleLineComment("Here you can initialize your variables", (Object[]) null);
      jw.emitEmptyLine();

      jw.endMethod();
      jw.emitEmptyLine();

    } catch (Exception e) {
      throw new ProcessingException(typeAnnotatedClazz, ProcessingError.SET_BEFORE_METHOD,
          e.getMessage());
    }
  }

  /**
   * Generates the class
   * 
   * @return this the class
   * @throws ProcessingException if it was unsuccessful
   * @throws IOException it something goes wrong
   */
  public ClassBuilder build() throws ProcessingException, IOException {

    if (!this.isInitialized && !this.isMethodInitialized) {
      throw new ProcessingException(typeAnnotatedClazz, ProcessingError.INIT_NOT_DEFINES);
    }

    initWritter();

    initPackage();

    setImports();

    setGenerateAnnotation();

    setStartClass();

    setVariables();

    setBefore();

    if (annotatedClass.getExecutionMode().equals(ExecutionMode.ARQUILLIAN)) {
      setDeploymentMethod();
    }

    setMethods();

    setEndClass();

    return this;
  }

  /**
   * Defines the import classes
   * 
   * @throws ProcessingException if it was unsuccessful
   */
  private void setImports() throws ProcessingException {

    // Non-static classes
    List<String> imports = new ArrayList<String>();
    // Static classes
    List<String> staticImports = new ArrayList<String>();

    imports.add(Container.class.getCanonicalName());
    imports.add(Test.class.getCanonicalName());
    imports.add(Before.class.getCanonicalName());
    imports.add(Assert.class.getCanonicalName());
    imports.add(annotatedClass.getEntity().toString());
    imports.add(Generated.class.getCanonicalName());

    if (annotatedClass.getExecutionMode().equals(ExecutionMode.ARQUILLIAN)) {
      imports.add(Deployment.class.getCanonicalName());
      imports.add(Arquillian.class.getCanonicalName());
      imports.add(InSequence.class.getCanonicalName());
      imports.add(WebArchive.class.getCanonicalName());
      imports.add(RunWith.class.getCanonicalName());
    }

    if (annotatedClass.getClientOperations().length == 1
        && annotatedClass.getClientOperations()[0].equals(ClientOperation.ALL)) {
      // Non-static
      imports.add(List.class.getCanonicalName());
      imports.add(ArrayList.class.getCanonicalName());

      // Static
      staticImports
          .add(ClientOperation.class.getCanonicalName() + "." + ClientOperation.GET_ALL.name());
      staticImports
          .add(ClientOperation.class.getCanonicalName() + "." + ClientOperation.PUT.name());
      staticImports
          .add(ClientOperation.class.getCanonicalName() + "." + ClientOperation.POST.name());
      staticImports
          .add(ClientOperation.class.getCanonicalName() + "." + ClientOperation.DELETE.name());

    } else {

      for (ClientOperation operation : annotatedClass.getClientOperations()) {
        if (operation.equals(ClientOperation.GET_ALL)) {
          // Non-static
          imports.add(List.class.getCanonicalName());
          imports.add(ArrayList.class.getCanonicalName());

          // Static
          staticImports
              .add(ClientOperation.class.getCanonicalName() + "." + ClientOperation.GET_ALL.name());
        }
        if (operation.equals(ClientOperation.PUT)) {
          staticImports
              .add(ClientOperation.class.getCanonicalName() + "." + ClientOperation.PUT.name());
        }
        if (operation.equals(ClientOperation.POST)) {
          staticImports
              .add(ClientOperation.class.getCanonicalName() + "." + ClientOperation.POST.name());
        }
        if (operation.equals(ClientOperation.DELETE)) {
          staticImports
              .add(ClientOperation.class.getCanonicalName() + "." + ClientOperation.DELETE.name());
        }
      }

    }

    try {
      jw.emitImports(imports);
      jw.emitStaticImports(staticImports);
      jw.emitEmptyLine();
    } catch (IOException e) {
      throw new ProcessingException(typeAnnotatedClazz, ProcessingError.INIT_IMPORTS,
          e.getMessage());
    }
  }

  /**
   * Sets the Generate's annotation with some additional information regarding the generated class
   * 
   * @throws ProcessingException if it was unsuccessful
   */
  private void setGenerateAnnotation() throws ProcessingException {

    String comments = "\"vendor: %s %s\"";
    String vendorName = System.getProperty("java.vendor");
    String vendorVersion = System.getProperty("java.version");

    Map<String, Object> attributes = new TreeMap<>();
    attributes.put("value", String.format("\"%s\"", EndpointProcessor.class.getCanonicalName()));
    attributes.put("date", String.format("\"%s\"", new Date().toString()));
    attributes.put("comments", String.format(comments, vendorName, vendorVersion));

    try {
      jw.emitAnnotation(Generated.class, attributes);
    } catch (IOException e) {
      throw new ProcessingException(typeAnnotatedClazz, ProcessingError.SET_GENERATE_ANNOTATION);
    }

  }

  /**
   * Create a set of Modifiers
   * 
   * @param objects the modifiers
   * @return a set of modifiers
   */

  private Set<Modifier> getCustomModifier(Object... objects) {
    Set<Modifier> type = new HashSet<>();

    for (Object modifier : objects) {
      type.add((Modifier) modifier);
    }
    return type;
  }

}
