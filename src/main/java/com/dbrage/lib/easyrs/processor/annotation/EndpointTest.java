package com.dbrage.lib.easyrs.processor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.dbrage.lib.easyrs.processor.enums.ClientOperation;
import com.dbrage.lib.easyrs.processor.enums.ExecutionMode;
import com.dbrage.lib.easyrs.processor.enums.UUIDIdentifier;

/**
 * The Endpoint Test
 * 
 * @author Dorin Brage
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface EndpointTest {

  Class<?> endpoint();

  Class<?> entity();

  ClientOperation[] operations() default {ClientOperation.ALL};

  ExecutionMode execution() default ExecutionMode.SINGLETON;

  UUIDIdentifier identifier();
}
