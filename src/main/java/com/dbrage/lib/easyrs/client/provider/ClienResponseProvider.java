package com.dbrage.lib.easyrs.client.provider;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * The Client Response Provider
 * 
 * @author Dorin_Brage
 */
@Provider
public class ClienResponseProvider implements ClientResponseFilter {

	@Override
	public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
		if (responseContext.getStatusInfo().equals(Response.Status.UNAUTHORIZED)) {
			// Unauthorized access
			System.err.println("Unauthorized access");
		} else if (responseContext.getStatusInfo().equals(Response.Status.BAD_REQUEST)) {
			// Bad request
			System.err.println("Bad request");
		} else if (responseContext.getStatusInfo().equals(Response.Status.BAD_GATEWAY)) {
			// Bad gateway
			System.err.println("Bad gateway");
		}else if (responseContext.getStatusInfo().equals(Response.Status.NOT_FOUND)) {
			// Not found
			System.err.println("Not found");
		}
	}
}
