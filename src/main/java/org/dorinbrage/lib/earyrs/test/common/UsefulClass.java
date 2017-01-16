package org.dorinbrage.lib.earyrs.test.common;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.ws.rs.Path;

/**
 * The Useful Class
 * 
 * @author Dorin_Brage
 */
public class UsefulClass {

  /**
   * Get the parameterized type of a given class
   * 
   * @param clazz
   * @return the parameterized type
   */
  public static Class<?> getParameterizedType(Class<?> clazz) {

    ParameterizedType parameterizedType = (ParameterizedType) clazz.getGenericInterfaces()[0];
    Type[] types = parameterizedType.getActualTypeArguments();

    return (Class<?>) types[0];
  }

  /**
   * Get the specified root path for a given endpoint
   * 
   * @return The root path, if not {@code NULL}
   */
  public static String getPathFromEndpoint(Class<?> clazz) {

    // Get the interface
    Type endpoint = ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];

    try {
      Class<?> loadedClazz = Class.forName(endpoint.getTypeName());

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
