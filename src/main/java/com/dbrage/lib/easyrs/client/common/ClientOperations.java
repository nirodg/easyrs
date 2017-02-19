package com.dbrage.lib.easyrs.client.common;

import java.util.LinkedHashMap;

/**
 * The Client Operations interface reflects the CRUD operations.
 * 
 * @author Dorin Brage
 */
public interface ClientOperations {

  /**
   * Returns the persisted entities
   * 
   * @return the list of entities as {@link LinkedHashMap}
   */
  Object getAll();

  /**
   * Return the persisted object
   * 
   * @param id the ID or GUID
   * @return the {@link Object}
   */
  Object getById(String id);

  /**
   * Insert the given entity
   * 
   * @param object the entity
   * @return the persisted entity
   */
  Object put(Object object);

  /**
   * Update the given entity by it's ID or GUID
   * 
   * @param id the ID or GUID
   * @param entity the entity
   * @return the updated entity
   */
  Object post(String id, Object entity);

  /**
   * It removes the entity by it's ID or GUID
   * 
   * @param id the ID or GUID
   * @return the deleted entity
   */
  Object delete(String id);
}
