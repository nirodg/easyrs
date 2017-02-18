package com.dbrage.lib.easyrs.arquillian;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Map.Entry;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Rule;
import org.junit.rules.TestWatcher;

import com.dbrage.lib.easyrs.client.RestClient;
import com.dbrage.lib.easyrs.processor.annotation.common.DtoData;
import com.dbrage.lib.easyrs.processor.enums.ClientOperation;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

/**
 * Contains utilities
 * 
 * @author Dorin Brage
 *
 * @param <T>
 *            the type of Dto
 */
public abstract class Container<T> {

	private final static String CLIENT_HOST = "http://localhost/";
	private final static String SYSPROP_CLIENT_HOST = "client.host";
	private final static String SYSPROP_CLIENT_USER = "client.user";
	private final static String SYSPROP_CLIENT_PASS = "client.pass";

	/** Used to access the resource folder for the given Class */
	public Class<T> dtoClass;

	/** The JaxRs client */
	private RestClient client;

	/** The file reader */
	private FileReader fileReader;

	/** The GSON */
	private Gson gson;

	/** Reflects the JSON file */
	private DtoData dtoData;

	@SuppressWarnings("unchecked")
	public Container() {
		this.dtoClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

		initializeJaxRsClient();

		readJsonFile();
	}

	/**
	 * Reads the JSON file for the given DTO
	 */
	private void readJsonFile() {
		String[] resourceName = dtoClass.getSimpleName().split("Endpoint");
		String jsonFile = resourceName[0] + ".json";

		gson = new Gson();
		File resource = new File(dtoClass.getResource(jsonFile).getFile());
		try {

			if (!resource.exists()) {
				throw new Exception("Couldn't find the related JSON file for " + dtoClass.getSimpleName());
			}
			fileReader = new FileReader(resource);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		dtoData = gson.fromJson(fileReader, DtoData.class);

	}

	/**
	 * Initializes the JaxRs client
	 */
	private void initializeJaxRsClient() {
		this.client = new RestClient();

		// Set the host
		if (System.getProperty(SYSPROP_CLIENT_HOST) == null) {
			this.client.setEndpoint(CLIENT_HOST);
		} else {
			this.client.setEndpoint(System.getProperty(SYSPROP_CLIENT_HOST));
		}

		// Set authentication
		if (System.getProperty(SYSPROP_CLIENT_USER) != null || System.getProperty(SYSPROP_CLIENT_PASS) != null) {
			client.setBasicAuthentication(System.getProperty(SYSPROP_CLIENT_USER),
					System.getProperty(SYSPROP_CLIENT_PASS));
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

	/**
	 * Get the data
	 * 
	 * @param type
	 *            the type of operation
	 * @return the {@code Object}
	 */
	public Object getData(ClientOperation type) {
		switch (type) {
		case GET_ALL:
			return createDtoData(dtoData.getGetAll());
		case GET:
		case PUT:
		case DELETE:
			return createDtoData(dtoData.getCreate());
		case POST:
			return createDtoData(dtoData.getUpdate());
		default:
			return null;
		}
	}

	/**
	 * Creates a object based on the map's values
	 * 
	 * @param map
	 *            the Map
	 * @return the {@code Object}
	 */
	private Object createDtoData(Object map) {

		Object instance = null;
		Field[] fields = null;

		try {
			instance = Class.forName(dtoClass.getName()).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		fields = dtoClass.getDeclaredFields();

		if (map instanceof LinkedTreeMap) {
			for (Entry<String, Object> key : ((LinkedTreeMap<String, Object>) map).entrySet()) {
				setValueForField(instance, fields, key);
			}
			return instance;
		} else if (map instanceof Integer) {
			return new ArrayList<>(dtoData.getGetAll());
		} else {
			return null;
		}

	}

	/**
	 * Sets a value for the given instance
	 * @param instance the instance.
	 * @param fields the fields
	 * @param value the value to be assigned
	 */
	private void setValueForField(Object instance, Field[] fields, Entry<String, Object> value) {
		try {
			for (Field field : fields) {
				if (field.getName().equals(value.getKey())) {
					if (!field.isAccessible()) {
						field.setAccessible(true);
					}
					field.set(instance, value.getValue());
					System.out.println(String.format("Field %s = %s", field.getName(), value.getValue()));
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the client
	 */
	public RestClient getClient() {
		return client;
	}

	/**
	 * @param client
	 *            the client to set
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
