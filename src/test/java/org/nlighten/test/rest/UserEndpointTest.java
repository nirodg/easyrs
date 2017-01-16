package org.nlighten.test.rest;

import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.JsonProcessingException;
import org.dorinbrage.lib.earyrs.test.common.CrudMapper;
import org.dorinbrage.lib.earyrs.test.common.EndpointTest;
import org.nlighten.rest.UserEndpoint;

public class UserEndpointTest extends EndpointTest<UserDAO, UserEndpoint>{
  

  public UserEndpointTest() throws JsonProcessingException, IOException {
    super();
    // TODO Auto-generated constructor stub
  }

  public static void main(String[] args) throws Exception{
   
    new UserEndpointTest();
    
    
//    File jsonFile = getJsonFile(UserEndpoint.class);
    
//    CrudMapper<UserDAO> josnEntity = JsonUtils.toObject(jsonFile, CrudMapper.class );
    
//    UserDAO created = josnEntity.getCreate();
//    UserDAO updated = josnEntity.getCreate();
    
//    System.out.println("CREATE " + created.getUser());
//    System.out.println("UPDATE " + updated.getUser());
    
  }
  
  private static  File getJsonFile(Class<?> clazz) throws Exception {

    String[] resourceName = clazz.getSimpleName().split("Endpoint");
    String jsonFile = resourceName[0] + ".json";

    File resource = new File(clazz.getResource(jsonFile).getFile());

    if (!resource.exists()) {
      throw new Exception("The required JSON does not exists");
    }

    return resource;
  }
  
}
