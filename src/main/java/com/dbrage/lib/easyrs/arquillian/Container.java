package com.dbrage.lib.easyrs.arquillian;

import org.jboss.shrinkwrap.api.spec.WebArchive;

public abstract class Container {

	public static WebArchive getDeployment(Class<?> clazz) {
		WebArchive war = null;
		return war;
	}

}
