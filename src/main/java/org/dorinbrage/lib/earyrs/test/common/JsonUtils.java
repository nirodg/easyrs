package org.dorinbrage.lib.earyrs.test.common;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public class JsonUtils {

  /**
   * Deserialize from Json to any type of object
   * 
   * @param path
   * @param expectedTdeype
   * @return the type of object
   */
  public static <T> T toObject(File file, Object expectedType) {
    T result = null;

    // Used to map from JSON to Object
    ObjectMapper mapper = new ObjectMapper();

    try {
      result = mapper.readValue(file, (Class<T>) expectedType);
      System.out.println("----- JSON transfered to object");
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    return result;
  }

  /**
   * deserialization from Json to any type of object
   * 
   * @param path
   * @param expectedTdeype
   * @return the type of object
   */
  public static <T> List<T> toListObject(File file, Class<T> expectedType) {
    List<T> results = new ArrayList<T>();

    // Mapper - Used to map from JSON to Object
    ObjectMapper mapper = new ObjectMapper();

    try {

      for (JsonNode result : mapper.readTree(file)) {
        results.add(mapper.readValue(result, expectedType));
      }

    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    return results;
  }

  /**
   * Deserialize from Json to any type of object
   * 
   * @param path
   * @param expectedTdeype
   * @return the type of object
   */
  public static <T> T deserialize(File file, Class<T> expectedType) {
    T result = null;

    // Mapper - Used to map from JSON to Object
    ObjectMapper mapper = new ObjectMapper();

    try {
      result = mapper.readValue(file, expectedType);

    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    return result;
  }

  public static <D, E> D jsonToObject(LinkedHashMap<D, E> data) {
  }

  /**
   * Replace value for a private inner field.
   * 
   * @param entity
   * @param field
   * @param value
   */
  private <T> void invokeInnerField(Object entity, String field, T value) {
    Field fieldInvoke = null;
    try {
      fieldInvoke = entity.getClass().getSuperclass().getDeclaredField(field);
      fieldInvoke.setAccessible(true);
      fieldInvoke.set(entity, value);
    } catch (SecurityException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (NoSuchFieldException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
