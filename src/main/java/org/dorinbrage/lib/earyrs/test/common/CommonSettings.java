package org.dorinbrage.lib.earyrs.test.common;

import java.io.File;
import java.lang.reflect.ParameterizedType;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Rule;
import org.junit.rules.TestWatcher;

public abstract class CommonSettings<T, E> extends CrudMapper<E> {

  /** System property. Used to specify the remote path */
  private static final String SYSPROP_HOST_PATH = "host.path";

  private static final String HOST_PATH_DEFAULT = "http://localhost";

  /** Used to access the resource folder for the given Class */
  public Class<T> resourcePath;
  
  /** It contains the path for the given endpoint **/
  private String endpointPath;

  /** The JaxRS client **/
  public HTTPClient client;
  
  public Class<E> dto;

  @SuppressWarnings("unchecked")
  public CommonSettings() {

    this.resourcePath = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    this.dto = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    
    print("Local resource path " + this.resourcePath.getName() + " >> " + this.resourcePath.getPackage().getName());

    this.endpointPath = UsefulClass.getPathFromEndpoint(getClass());
    print("Endpoint path " + this.endpointPath);

    
    fetchJsonFile();
    
    setupClient();
  }

  @SuppressWarnings("unchecked")
  private void fetchJsonFile() {
    try {
      File jsonFile = getJsonFile(resourcePath);
      
      System.out.println("/////>>>> " + dto.getName());
      System.out.println("from json: Create OBJ type is " + dto.getTypeName());
      
    } catch (Exception e) {
      e.printStackTrace();
    }
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
      client.setHost(HOST_PATH_DEFAULT);
    } else {
      print("The requests will be done to " + hostPath);
      client.setHost(hostPath);
    }
    
    // set the endpoint path. e.g. http://localhost/users
    client.setEndpoint(endpointPath);
    
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

    String[] resourceName = clazz.getSimpleName().split("Endpoint");
    String jsonFile = resourceName[0] + ".json";

    File resource = new File(clazz.getResource(jsonFile).getFile());

    if (!resource.exists()) {
      throw new Exception("The required JSON does not exists");
    }

    print(jsonFile + " found!");

    return resource;
  }

  /** This must be deleted afterwards. Only for debugging purposes **/
  @Deprecated
  public void print(Object obj) {
    System.err.println("######  " + obj);
  }

  /**
   * For each test will be printed either the test started, passed successfully
   * or it failed.
   */
  @Rule
  public TestWatcher currentTestWatcher = new TestWatcher() {

    @Override
    protected void starting(org.junit.runner.Description description) {
      System.out.println("---- starting test case: " + description.getClassName() + "." + description.getMethodName());
    };

    @Override
    protected void finished(org.junit.runner.Description description) {
      System.out
          .println("---- successful test case: " + description.getClassName() + "." + description.getMethodName());
    };

    @Override
    protected void failed(Throwable e, org.junit.runner.Description description) {
      System.out.println("---- failed test case: " + description.getClassName() + "." + description.getMethodName()
          + " response: " + e.getMessage());
    };

  };
  
}
