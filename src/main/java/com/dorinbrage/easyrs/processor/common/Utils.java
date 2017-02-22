package com.dorinbrage.easyrs.processor.common;

import java.lang.annotation.Annotation;

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
   * @param clazz the Class
   * @return The root path, if not {@code NULL}
   */
  public static String getPathFromEndpoint(Class<?> clazz) {
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
