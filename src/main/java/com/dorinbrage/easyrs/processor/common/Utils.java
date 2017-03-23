package com.dorinbrage.easyrs.processor.common;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.processing.ProcessingEnvironment;
import javax.ws.rs.Path;

import org.apache.commons.lang.ClassUtils;

import com.dorinbrage.easyrs.processor.enums.ProcessingError;
import com.dorinbrage.easyrs.processor.exception.ProcessingException;

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

  /**
   * Initializes all the fields given the object
   * 
   * @param clazz the class. It can be any POJO class
   * @param object the object to be initialized. It can be any POJO class
   * @throws IllegalAccessException in case the path was not created
   * @throws ClassNotFoundException
   * @throws InstantiationException
   * @throws IllegalArgumentException
   */
  public static void initializeFields(Class<?> clazz, Object object) throws IllegalAccessException,
      IllegalArgumentException, InstantiationException, ClassNotFoundException {
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

      } else if (type.getTypeName().startsWith(Date.class.getName())) {
        field.set(object, new Date());

      } else {
        Object complexTypeField = field.getType().newInstance();
        initializeFields(field.getType(), complexTypeField);
        field.set(object, complexTypeField);
      }
    }
  }

  /**
   * Returns either the default path or the specified one as an argument for the compiler
   * 
   * @param processingEnv the ProcessingEnvironment
   * @param path the path
   * @return the path
   * @throws ProcessingException in case the path was not created
   */
  public static String getResourceFolder(ProcessingEnvironment processingEnv, String path)
      throws ProcessingException {

    if (processingEnv.getOptions().get(path) == null) {
      throw new ProcessingException(ProcessingError.RESOURCE_FOLDER_ARGUMENT_MISSING);
    }

    if (!Files.exists(Paths.get(processingEnv.getOptions().get(path)))) {
      throw new ProcessingException(ProcessingError.DIRECTORY_DOES_NOT_EXIST);
    }

    return getCleanedPath(processingEnv.getOptions().get(path));
  }

  /**
   * Checks it the path exists. If it doesn't it creates it
   * 
   * @param processingEnv the ProcessingEnvironment
   * @param path the path
   * @param canonicalName The canonical name of the underlying class as defined by the Java Language
   * @return the path
   * @throws ProcessingException in case the path was not created
   */
  public static String getPathForResource(ProcessingEnvironment processingEnv, String path,
      String canonicalName) throws ProcessingException {
    String classNamePath = getResourceFolder(processingEnv, path) + ClassUtils
        .getPackageName(canonicalName).replace(".", System.getProperty("file.separator"));
    try {
      new File(classNamePath).mkdirs();
    } catch (SecurityException se) {
      throw new ProcessingException(ProcessingError.FAILED_TO_CREATE_DIRECTORY, se.getMessage());
    }

    return getCleanedPath(classNamePath);
  }

  /**
   * Fixes the given path
   * 
   * @param path the path
   * @return the fixed path
   */
  private static String getCleanedPath(String path) {
    // Replaces all the slashes and backshales based on the OS's file separator
    path = path.replace("\\", System.getProperty("file.separator"));
    path = path.replace("/", System.getProperty("file.separator"));

    if (!path.endsWith(System.getProperty("file.separator"))) {
      path = path + System.getProperty("file.separator");
    }
    return path;
  }
}
