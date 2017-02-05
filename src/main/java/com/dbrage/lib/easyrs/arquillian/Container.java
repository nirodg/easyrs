package com.dbrage.lib.easyrs.arquillian;

import java.io.File;
import java.lang.reflect.ParameterizedType;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Rule;
import org.junit.rules.TestWatcher;

import com.dbrage.lib.easyrs.processor.enums.ClientRequest;

/**
 * Contains utilities for Arquillian 
 * @author Dorin_Brage
 *
 * @param <T> the type of Dto
 */
public abstract class Container<T> {

	/** Used to access the resource folder for the given Class */
	public Class<T> resourcePath;

	@SuppressWarnings("unchecked")
	public Container() {
		this.resourcePath = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
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

	public Object getData(ClientRequest type) {

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
					"---- successful test case: " + description.getClassName() + "." + description.getMethodName());
		};

		@Override
		protected void failed(Throwable e, org.junit.runner.Description description) {
			System.out.println("---- failed test case: " + description.getClassName() + "."
					+ description.getMethodName() + " response: " + e.getMessage());
		};
	};
}
