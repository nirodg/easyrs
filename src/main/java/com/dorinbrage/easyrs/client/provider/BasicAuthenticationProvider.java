package com.dorinbrage.easyrs.client.provider;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.DatatypeConverter;

/**
 * The Authenticator Provider
 * 
 * @author Dorin Brage
 */
@Provider
public class BasicAuthenticationProvider implements ClientRequestFilter {

  private static final String HEADER_AUTHORIZATION = "Authorization";
  private static final String HEADER_AUTHORIZATION_BASIC = "Basic ";

  private final String user;
  private final String password;

  public BasicAuthenticationProvider(String user, String password) {
    this.user = user;
    this.password = password;
  }

  @Override
  public void filter(ClientRequestContext requestContext) throws IOException {
    requestContext.getHeaders().add(HEADER_AUTHORIZATION, getBasicAuthentication());
  }

  private String getBasicAuthentication() {
    String token = this.user + ":" + this.password;
    try {
      return HEADER_AUTHORIZATION_BASIC
          + DatatypeConverter.printBase64Binary(token.getBytes("UTF-8"));
    } catch (UnsupportedEncodingException ex) {
      throw new IllegalStateException("Cannot encode with UTF-8", ex);
    }
  }
}
