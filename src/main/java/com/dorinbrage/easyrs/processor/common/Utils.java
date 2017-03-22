package com.dorinbrage.easyrs.processor.common;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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

  public static void initializeFields(Class<?> clazz, Object object) throws IllegalAccessException {
    for (Field field : clazz.getDeclaredFields()) {

      if (!field.isAccessible()) {
        field.setAccessible(true);
      }

      Type type = field.getGenericType();

      if (String.class.getName().equals(type.getTypeName())) {
        field.set(object, String.valueOf(""));

      } else if (Boolean.class.getName().equals(type.getTypeName())
          || boolean.class.getName().equals(type.getTypeName())) {
        field.set(object, Boolean.FALSE);

      } else if (Integer.class.getName().equals(type.getTypeName())
          || int.class.getName().equals(type.getTypeName())
          || byte.class.getName().equals(type.getTypeName())
          || long.class.getName().equals(type.getTypeName())
          || float.class.getName().equals(type.getTypeName())
          || double.class.getName().equals(type.getTypeName())) {

        field.set(object, 0);
      } else if (char.class.getName().equals(type.getTypeName())
          || Character.class.getName().equals(type.getTypeName())) {
        field.set(object, ' ');

      } else if (type.getTypeName().startsWith(List.class.getName())
          || type.getTypeName().startsWith(ArrayList.class.getName())) {
        field.set(object, new ArrayList<>());

      } else if (type.getTypeName().startsWith(Map.class.getName())
          || type.getTypeName().startsWith(HashMap.class.getName())) {
        field.set(object, new HashMap<>());

      } else if (type.getTypeName().startsWith(TreeMap.class.getName())) {
        field.set(object, new TreeMap<>());

      } else if (type.getTypeName().startsWith(LinkedHashMap.class.getName())) {
        field.set(object, new LinkedHashMap<>());

      } else if (type.getTypeName().startsWith(Set.class.getName())
          || type.getTypeName().startsWith(HashSet.class.getName())) {
        field.set(object, new HashSet<>());
      }
    }
  }
}
