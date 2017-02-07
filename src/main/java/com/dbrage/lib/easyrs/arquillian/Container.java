package com.dbrage.lib.easyrs.arquillian;

import java.io.File;
import java.lang.reflect.ParameterizedType;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Rule;
import org.junit.rules.TestWatcher;

import com.dbrage.lib.easyrs.client.RestClient;
import com.dbrage.lib.easyrs.processor.enums.ClientOperation;

/**
 * Contains utilities
 * 
 * @author Dorin_Brage
 *
 * @param <T> the type of Dto
 */
public abstract class Container<T> {

	private final static String CLIENT_HOST = "http://localhost/";
	private final static String SYSPROP_CLIENT_HOST = "client.host";
	private final static String SYSPROP_CLIENT_USER = "client.user";
	private final static String SYSPROP_CLIENT_PASS = "client.pass";
	
	/** Used to access the resource folder for the given Class */
	public Class<T> resourcePath;
	
	/** The JaxRs client */
	private RestClient client;

	@SuppressWarnings("unchecked")
	public Container() {
		this.resourcePath = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
		
		initializeJaxRsClient();
	}

	/**
	 * Initializes the JaxRs client
	 */
	private void initializeJaxRsClient() {
		this.client = new RestClient();
		
		// Set the host
		if(System.getProperty(SYSPROP_CLIENT_HOST) == null){
			this.client.setEndpoint(CLIENT_HOST);
		}else{
			this.client.setEndpoint(System.getProperty(SYSPROP_CLIENT_HOST));
		}
		
		// Set authentication
		if(System.getProperty(SYSPROP_CLIENT_USER) != null || System.getProperty(SYSPROP_CLIENT_PASS) != null){
			client.setBasicAuthentication(System.getProperty(SYSPROP_CLIENT_USER), System.getProperty(SYSPROP_CLIENT_PASS));
		}
		
	}

	/**
	 * The deployment including all Maven dependencies
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

	public Object getData(ClientOperation type) {

		String[] resourceName = Container.class.getSimpleName().split("Endpoint");
		String jsonFile = resourceName[0] + ".json";
		File resource = new File(Container.class.getResource(jsonFile).getFile());
		if (!resource.exists()) {
			return null;
		}
		// TODO Must be implemented
		switch (type) {
		case ALL:
			break;
		case GET:
			break;
		case PUT:
			break;
		case POST:
			break;
		case DELETE:
			break;
		default:
			break;
		}
		return resource;
	}

	/**
	 * @return the client
	 */
	public RestClient getClient() {
		return client;
	}

	/**
	 * @param client the client to set
	 */
	public void setClient(RestClient client) {
		this.client = client;
	}

	/**
	 * For each test will be printed either the test started, passed
	 * successfully or it failed.
	 */
	@Rule
	public TestWatcher currentTestWatcher = new TestWatcher() {
		@Override
		protected void starting(org.junit.runner.Description description) {
			System.out.println(
					"---- starting test case: " + description.getClassName() + "." + description.getMethodName());
		};

		@Override
		protected void finished(org.junit.runner.Description description) {
			System.out.println(
					"---- finished test case: " + description.getClassName() + "." + description.getMethodName());
		};

		@Override
		protected void failed(Throwable e, org.junit.runner.Description description) {
			System.err.println("---- failed test case: " + description.getClassName() + "."
					+ description.getMethodName() + " response: " + e.getMessage());
		};
	};
}
