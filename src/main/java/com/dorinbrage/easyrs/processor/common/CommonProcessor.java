package com.dorinbrage.easyrs.processor.common;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import com.dorinbrage.easyrs.processor.annotation.EndpointTest;
import com.dorinbrage.easyrs.processor.enums.ProcessingError;
import com.dorinbrage.easyrs.processor.exception.ProcessingException;


@SupportedSourceVersion(SourceVersion.RELEASE_8)
public abstract class CommonProcessor<T> extends AbstractProcessor {

  protected Elements elements;
  protected Filer filer;
  protected RoundEnvironment roundEnv;

  /** Contains all the annotated interfaces */
  protected Map<String, AnnotatedClass> container;

  protected Map<String, T> collectionTypes;

  public final Elements getElements() {
    return elements;
  }

  public final void setElements(Elements elements) {
    this.elements = elements;
  }

  public final Filer getFiler() {
    return filer;
  }

  public final void setFiler(Filer filer) {
    this.filer = filer;
  }

  public Map<String, AnnotatedClass> getContainer() {
    return container;
  }

  public void setContainer(Map<String, AnnotatedClass> container) {
    this.container = container;
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    Set<String> annotations = new LinkedHashSet<String>();
    annotations.add(EndpointTest.class.getCanonicalName());
    return annotations;
  }

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    elements = processingEnv.getElementUtils();
    filer = processingEnv.getFiler();
    container = new HashMap<String, AnnotatedClass>();
  }

  public void getAnnotatedInterfaces() {
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

      }
    } catch (ProcessingException e) {
      e.printStackTrace();
    }
  }

  /**
   * Checks if the annotation was used only by interfaces
   * 
   * @param element Represents a program element such as a package, class, or method
   * @return {@code true} if the element is an {@code Interface}, {@code false} is it's not
   * @throws ProcessingException
   */
  protected boolean isOnlyInterfaceAnnotated(Element element) throws ProcessingException {
    if (!element.getKind().equals(ElementKind.INTERFACE)) {
      throw new ProcessingException(element, ProcessingError.PROCESSOR_ONLY_INTERFACES,
          EndpointTest.class.getName());
    }
    return true;
  }
}
