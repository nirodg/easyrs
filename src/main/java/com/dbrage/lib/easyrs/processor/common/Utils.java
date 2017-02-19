package com.dbrage.lib.easyrs.processor.common;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.ws.rs.Path;

/**
 * The Util class
 * 
 * @author Dorin Brage
 */
public class Utils {

  /**
   * Get the specified root path for a given endpoint
   * 
   * @return The root path, if not {@code NULL}
   */
  public static String getPathFromEndpoint(Class<?> clazz) {
    // Get the interface
    // Type endpoint = ((ParameterizedType)
    // clazz.getGenericSuperclass()).getActualTypeArguments()[0];
    try {
      Class<?> loadedClazz = Class.forName(clazz.getTypeName());
      // Get all the annotations from the interface
      Annotation[] annotations = loadedClazz.getDeclaredAnnotations();
      for (Annotation annotation : annotations) {
        if (annotation instanceof Path) {
          Path path = (Path) annotation;
          return path.value();
        }
      }
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

}
