package com.dbrage.lib.easyrs.client;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpHost;
import org.apache.http.client.AuthCache;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.Assert;

/**
 * The JaxRS client
 * 
 * @author Dorin_Brage
 * 
 * @param <T>
 *            the type of entity which must be tested
 */
public class Client<T> {

	private static final String HTTP_HOST = System.getProperty("HTTP_HOST");
	private static final String HTTP_HOST_DEFAULT = "http://localhost";

	private String endpoint = "";

	private ResteasyClient client;
	private ResteasyWebTarget target;

	private Object entity;
	private Object entities;

	private AuthCache authCache;
	private BasicScheme basicAuth;
	private HttpHost post;

	public Client() {
		client = new ResteasyClientBuilder().build();

		authCache = new BasicAuthCache();
		basicAuth = new BasicScheme();

		if (HTTP_HOST != null) {
			post = new HttpHost(HTTP_HOST);
		} else {
			post = new HttpHost(HTTP_HOST_DEFAULT);
		}
		// Client.class.getCanonicalName().toString();
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
		Response response = target.request().header("Access-Control-Allow-Origin", "*")
				.put(Entity.entity(entity, MediaType.APPLICATION_JSON));
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

}
