package com.dbrage.lib.easyrs.client;

import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.Assert;

/**
 * The JaxRS client
 * 
 * @author Dorin_Brage
 * 
 * @param <T> the type of entity which must be tested
 */
public class Client<T> {

	private String host;
	private String endpoint;
	private String fullPath;

	private ResteasyClient client;
	private ResteasyWebTarget target;

	private T entity;

	public Client() {
		client = new ResteasyClientBuilder().build();
	}

	public List<T> getAll() {
		Response response = target.request().get();
		return (List<T>) response.readEntity(entity.getClass());
	}

	public T getById(String id) {
		setPathToPersistedEntity(id);
		Response response = target.request().get();
		return (T) response.readEntity(entity.getClass());
	}

	public T put(T entity) {
		Response response = target.request().put(Entity.entity(entity, MediaType.APPLICATION_JSON));
		Assert.assertEquals(200, response.getStatus());
		return (T) response.readEntity(entity.getClass());
	}

	public T post(String id, T entity) {
		setPathToPersistedEntity(id);
		Response response = target.request().post(Entity.entity(entity, MediaType.APPLICATION_JSON));
		Assert.assertEquals(200, response.getStatus());
		return (T) response.readEntity(entity.getClass());
	}

	public T delete(String id) {
		setPathToPersistedEntity(id);
		Response response = target.request().delete();
		Assert.assertEquals(200, response.getStatus());
		return (T) response.readEntity(entity.getClass());
	}

	private void setPathToPersistedEntity(String id) {
		target = client.target(fullPath.concat("/").concat(id));
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
		this.fullPath.concat(endpoint);
		target = client.target(this.fullPath);
		System.out.println(this.fullPath);
	}

	public ResteasyClient getClient() {
		return client;
	}

	public void setClient(ResteasyClient client) {
		this.client = client;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getEndpoint() {
		return endpoint;
	}

}
