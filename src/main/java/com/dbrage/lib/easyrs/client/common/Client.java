package com.dbrage.lib.easyrs.client.common;

import java.net.URI;
import java.net.URISyntaxException;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import com.dbrage.lib.easyrs.client.provider.BasicAuthenticationProvider;
import com.dbrage.lib.easyrs.client.provider.ClienResponseProvider;

/**
 * The Client
 * 
 * @author Dorin Brage
 *
 */
public class Client {

  private String endpoint = "";

  protected ResteasyClient client;
  public ResteasyWebTarget target;

  protected Object entity;

  public Client() {
    super();
    client = new ResteasyClientBuilder().build();
    client.register(new ClienResponseProvider());
  }

  /**
   * Set the endpoint path
   * 
   * @param endpoint the endpoint
   */
  public void setEndpoint(String endpoint) {
    this.endpoint = endpoint;

    try {
      target = client.target(new URI(endpoint));
    } catch (NullPointerException | URISyntaxException e) {
      e.printStackTrace();
    }
  }

  /**
   * Concatenates the ID/GUID for the given entity/entities on the url
   * 
   * @param id the ID for the persisted entity/entities
   */
  public void setPathToPersistedEntity(String id) {
    target = client.target(endpoint.concat("/").concat(id));
  }

  public void setBasicAuthentication(String user, String password) {
    client.register(new BasicAuthenticationProvider(user, password));
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

}
