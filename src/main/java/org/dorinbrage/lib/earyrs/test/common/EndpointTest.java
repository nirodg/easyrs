package org.dorinbrage.lib.earyrs.test.common;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

import org.codehaus.jackson.JsonProcessingException;

public abstract class EndpointTest<D, E> {

  private Class<E> resourcePath;

  private CrudMapper<D> data;

  private Class<D> dto;
  
  public EndpointTest(){

    System.out.println("Started");
    this.resourcePath = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    this.dto = (Class<D>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    
    File jsonFile = null;
    try {
      jsonFile = getJsonFile(resourcePath);
    } catch (Exception e) {
      e.printStackTrace();
    }

    data = JsonUtils.toObject(jsonFile, CrudMapper.class);
//    System.out.println("createEntity  " + createEntity.getClass().getTypeName());
    System.out.println("ok");
  }

  public CrudMapper<D> getData() {
    return data;
  }

  /**
   * Get JSON file from Resource's folder.
   * 
   * @param service
   *          The interface which is being tested
   * @param method
   *          The method which must me tested
   * @return the Json file
   * @throws Exception
   */
  private File getJsonFile(Class<?> clazz) throws Exception {

    System.out.println(clazz.getSimpleName());
    String[] resourceName = clazz.getSimpleName().split("Endpoint");
    String jsonFile = resourceName[0] + ".json";

    File resource = new File(clazz.getResource(jsonFile).getFile());

    if (!resource.exists()) {
      throw new Exception("The required JSON does not exists");
    }

    System.out.println(jsonFile + " found!");

    return resource;
  }

  /**
   * Replace value for a private inner field.
   * 
   * @param entity
   * @param field
   * @param value
   */
  public void invokeInnerField(Object entity, String field, D value) {
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
