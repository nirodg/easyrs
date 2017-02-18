package com.dbrage.lib.easyrs.client;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Assert;

import com.dbrage.lib.easyrs.client.common.Client;
import com.dbrage.lib.easyrs.client.common.ClientOperations;

/**
 * The Rest Client 
 * 
 * @author Dorin Brage
 * 
 * @param <T>
 *          the type of entity which must be tested
 */
public class RestClient extends Client implements ClientOperations {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dbrage.lib.easyrs.client.CrudOperations#getAll()
	 */
	@Override
	public Object getAll() {
		Response response = target.request().get();
		Assert.assertEquals(200, response.getStatus());
		return response.readEntity(Object.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dbrage.lib.easyrs.client.CrudOperations#getById(java.lang.String)
	 */
	@Override
	public Object getById(String id) {
		setPathToPersistedEntity(id);
		Response response = target.request().get();
		Assert.assertEquals(200, response.getStatus());
		return response.readEntity(entity.getClass());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dbrage.lib.easyrs.client.CrudOperations#put(java.lang.Object)
	 */
	@Override
	public Object put(Object object) {
		Response response = target.request().put(Entity.entity(object, MediaType.APPLICATION_JSON));
		Assert.assertEquals(200, response.getStatus());
		return response.readEntity(entity.getClass());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dbrage.lib.easyrs.client.CrudOperations#post(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public Object post(String id, Object entity) {
		setPathToPersistedEntity(id);
		Response response = target.request().post(Entity.entity(entity, MediaType.APPLICATION_JSON));
		Assert.assertEquals(200, response.getStatus());
		return response.readEntity(entity.getClass());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dbrage.lib.easyrs.client.CrudOperations#delete(java.lang.String)
	 */
	@Override
	public Object delete(String id) {
		setPathToPersistedEntity(id);
		Response response = target.request().delete();
		Assert.assertEquals(200, response.getStatus());
		return response.readEntity(entity.getClass());
	}

}