package com.dbrage.lib.easyrs.client;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.Assert;

import com.dbrage.lib.easyrs.client.provider.BasicAuthenticationProvider;
import com.dbrage.lib.easyrs.client.provider.ClienResponseProvider;

/**
 * The JaxRS client
 * 
 * @author Dorin_Brage
 * 
 * @param <T>
 *          the type of entity which must be tested
 */
public class Client<T> {

	private String endpoint = "";

	private ResteasyClient client;
	private ResteasyWebTarget target;

	private Object entity;
	private Object entities;

	public Client() {
		client = new ResteasyClientBuilder().build();
		client.register(new ClienResponseProvider());
	}

	public Object getAll() {
		Response response = target.request().get();
		return response.readEntity(entities.getClass());
	}

	public Object getById(String id) {
		setPathToPersistedEntity(id);
		Response response = target.request().get();
		Assert.assertEquals(200, response.getStatus());
		return response.readEntity(entity.getClass());
	}

	public Object put(T entity) {
		Response response = target.request().put(Entity.entity(entity, MediaType.APPLICATION_JSON));
		return response.readEntity(entity.getClass());
	}

	public Object post(String id, T entity) {

		setPathToPersistedEntity(id);
		Response response = target.request().post(Entity.entity(entity, MediaType.APPLICATION_JSON));
		Assert.assertEquals(200, response.getStatus());
		return response.readEntity(entity.getClass());
	}

	public Object delete(String id, T entity) {
		setPathToPersistedEntity(id);

		Response response = target.request().delete();
		Assert.assertEquals(200, response.getStatus());
		return response.readEntity(entity.getClass());
	}

	private void setPathToPersistedEntity(String id) {
		target = client.target(endpoint.concat("/").concat(id));
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;

		try {
			target = client.target(new URI(endpoint));
		} catch (NullPointerException | URISyntaxException e) {
			e.printStackTrace();
		}

		System.out.println(endpoint);
	}

	public ResteasyClient getClient() {
		return client;
	}

	public void setClient(ResteasyClient client) {
		this.client = client;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public Object getEntity() {
		return entity;
	}

	public void setEntity(Object entity) {
		this.entity = entity;
	}

	public Object getEntities() {
		return entities;
	}

	public void setEntities(Object entities) {
		this.entities = entities;
	}

	public void setBasicAuthentication(String user, String password) {
		client.register(new BasicAuthenticationProvider(user, password));
	}

}
