package com.dorinbrage.easyrs.processor;

import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import org.apache.commons.lang.ClassUtils;

import com.dorinbrage.easyrs.processor.annotation.EndpointTest;
import com.dorinbrage.easyrs.processor.builder.ClassBuilder;
import com.dorinbrage.easyrs.processor.common.AnnotatedClass;
import com.dorinbrage.easyrs.processor.common.CommonProcessor;
import com.google.auto.service.AutoService;

/**
 * The Processor
 * 
 * @author Dorin Brage
 */
@AutoService(Processor.class)
public class EndpointProcessor extends CommonProcessor {

  /** The Generator factory */
  private ClassBuilder generator;

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    this.roundEnv = roundEnv;
    this.generator = new ClassBuilder();
    getAnnotatedInterfaces();


    for (Entry<String, AnnotatedClass> item : container.entrySet()) {
      if (!processedInterfaces.contains(item.getValue())) {

        TypeElement annotatedClazz = null;
        try {

          for (Element element : roundEnv.getElementsAnnotatedWith(EndpointTest.class)) {
            if (element.getSimpleName().toString().equals(item.getKey())) {
              annotatedClazz = (TypeElement) element;
            }
          }

          generator.init(annotatedClazz,
              container.get(ClassUtils.getShortClassName(item.getValue().getEndpoint().toString())),
              filer, elements).build();
        } catch (Exception e) {
          e.printStackTrace();
        }

        // add processed interface to the list
        processedInterfaces.add(item.getValue());
      }
    }
    return true;
  }

}
