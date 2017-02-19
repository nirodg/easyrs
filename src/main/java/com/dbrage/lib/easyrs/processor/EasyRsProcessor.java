package com.dbrage.lib.easyrs.processor;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import com.dbrage.lib.easyrs.processor.annotation.EndpointTest;
import com.dbrage.lib.easyrs.processor.annotation.common.AnnotatedClass;
import com.dbrage.lib.easyrs.processor.builder.ClassBuilder;
import com.dbrage.lib.easyrs.processor.enums.ProcessingError;
import com.dbrage.lib.easyrs.processor.exception.ProcessingException;
import com.google.auto.service.AutoService;

/**
 * The Processor
 * 
 * @author Dorin Brage
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class EasyRsProcessor extends AbstractProcessor {

  private Elements elements;
  private Filer filer;

  /** Will contain all the classes annotated with EndpointTest */
  private Map<String, AnnotatedClass> container;

  /** The Generator factory */
  private ClassBuilder generator;

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

    try {
      // Iterate over each class
      for (Element element : roundEnv.getElementsAnnotatedWith(EndpointTest.class)) {

        // Looks for interfaces with IndependentTest.class
        isOnlyInterfaceAnnotated(element);

        // The class with the annotation to be tested
        TypeElement annotatedClazz = (TypeElement) element;

        // It checks to do not generate the class twice
        if (!container.containsKey(annotatedClazz.getSimpleName().toString())) {
          container.put(annotatedClazz.getSimpleName().toString(),
              new AnnotatedClass(annotatedClazz, element.getAnnotation(EndpointTest.class)));
        } else {
          throw new ProcessingException(element, ProcessingError.CLASS_CANT_BE_DUPLICATED,
              annotatedClazz.getSimpleName().toString());
        }

        try {
          generator.init(annotatedClazz, container.get(annotatedClazz.getSimpleName().toString()),
              filer, elements).build();
        } catch (Exception e) {
          throw new ProcessingException(element, ProcessingError.PROCESSOR_ERROR_GENERATING);
        }

      }
    } catch (ProcessingException e) {
      e.printStackTrace();
    }
    return true;
  }

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    elements = processingEnv.getElementUtils();
    filer = processingEnv.getFiler();
    container = new HashMap<String, AnnotatedClass>();
    generator = new ClassBuilder();
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    Set<String> annotations = new LinkedHashSet<String>();
    annotations.add(EndpointTest.class.getCanonicalName());
    return annotations;
  }

  /**
   * Checks if the annotation was used only by interfaces
   * 
   * @param element Represents a program element such as a package, class, or method
   * @return {@code true} if the element is an {@code Interface}, {@code false} is it's not
   * @throws ProcessingException
   */
  private boolean isOnlyInterfaceAnnotated(Element element) throws ProcessingException {
    if (!element.getKind().equals(ElementKind.INTERFACE)) {
      throw new ProcessingException(element, ProcessingError.PROCESSOR_ONLY_INTERFACES,
          EndpointTest.class.getName());
    }
    return true;
  }

}
