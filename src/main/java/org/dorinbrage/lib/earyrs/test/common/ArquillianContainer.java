package org.dorinbrage.lib.earyrs.test.common;

import java.io.File;
import java.lang.reflect.ParameterizedType;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

public abstract class ArquillianContainer<D, E> extends EndpointTest<D, E> {

  private static final String SYSPROP_HOST_PATH = "host.path";
  private static final String HOST_PATH_DEFAULT = "http://localhost";
  public HTTPClient client;
  
  public Class<E> resourcePath;
  private String endpointPath;
  public Class<D> dto;

  
  public ArquillianContainer() {

    this.resourcePath = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    this.dto = (Class<D>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    
    print("Local resource path " + this.resourcePath.getName() + " >> " + this.resourcePath.getPackage().getName());

    this.endpointPath = UsefulClass.getPathFromEndpoint(getClass());
    print("Endpoint path " + this.endpointPath);

  }
  
  /**
   * The basic deployment including all Maven dependencies
   * 
   * @return the WAR file
   */
  public static WebArchive getDeployment(Class<?> clazz) {
    File[] mavenDependencies = Maven.configureResolver().workOffline().loadPomFromFile("pom.xml")
        .importCompileAndRuntimeDependencies().resolve().withoutTransitivity().asFile();

    WebArchive war = ShrinkWrap.create(WebArchive.class, clazz.getSimpleName() + ".war")
        .addAsLibraries(mavenDependencies);

    System.out.println(war.toString(true));

    return war;
  }
  
  public void setupClient() {
    print("---- starting test");

    client = new HTTPClient();

    // Get the system property passed by the user
    String hostPath = System.getProperty(SYSPROP_HOST_PATH);

    if (hostPath == null) {
      client.setHost(hostPath);
    } else {
      print("The requests will be done to " + hostPath);
      client.setHost(hostPath);
    }
    
    // set the endpoint path. e.g. http://localhost/users
    client.setEndpoint(endpointPath);
    
  }
  
  public void print(Object obj) {
    System.err.println("######  " + obj);
  }
}
