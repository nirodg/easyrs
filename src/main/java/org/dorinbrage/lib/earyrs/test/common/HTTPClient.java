package org.dorinbrage.lib.earyrs.test.common;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.Assert;

public class HTTPClient {

  private String host;
  private String endpoint;
  private String fullPath;
  
  ResteasyClient client;
  ResteasyWebTarget target;
  

  public HTTPClient() {
    client = new ResteasyClientBuilder().build();
    
  }


  public Object post(Object entity) {
    Response response = target.request().post(Entity.entity(entity, MediaType.APPLICATION_JSON));
    Assert.assertEquals(200, response.getStatus());
    return response.readEntity(entity.getClass());
  }
  
  public void setHost(String hostPath) {
    this.host = hostPath;
    this.fullPath = hostPath;
  }

  public String getHost() {
    return host;
  }

  public String getEndpoint() {
    return endpoint;
  }

  public void setEndpoint(String endpoint) {
    this.endpoint = endpoint;
    this.fullPath.concat(endpoint);
    target = client.target(this.fullPath); // this should be enough
    System.out.println(this.fullPath);
  }

}
