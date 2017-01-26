package com.dbrage.lib.easyrs.processor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.dbrage.lib.easyrs.processor.enums.ClientRequest;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface EndpointTest {
	
	Class<?> endpoint() default EndpointTest.class;
	
	Class<?> entity();
	
	ClientRequest[] operations() default {ClientRequest.ALL};
	
}
