# `easyrs`

[![Join the chat at https://gitter.im/nirodg/easyrs](https://badges.gitter.im/nirodg/easyrs.svg)](https://gitter.im/nirodg/easyrs?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)[![Build Status](https://travis-ci.org/nirodg/easyrs.svg?branch=master)](https://travis-ci.org/nirodg/easyrs)[![Codacy Badge](https://api.codacy.com/project/badge/Grade/ab49fb3cf47744d28b95154f8cf18e14)](https://www.codacy.com/app/nirodg/easyrs?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=nirodg/easyrs&amp;utm_campaign=Badge_Grade)

What would you say if would be a way how to reduce the time writing comparison tests for your endpoints.. or what if you could test it as a Singleton or within Arquillian? 

Generating basic crud operations it´s so fast that within 5 minutes you have them already generated ( al right, might take you 2 minutes longer the first time 😉 )

So let´s define our test as it´s shown below:

```java
@EndpointTest(endpoint = UserEndpoint.class, entity = UserDTO.class)
public interface UserRest {
}
```

Define a JSON file for your entity where the keys represent the Entity's field :

```json
{
  "create": {
    "guid": "",
    "name": "",
    "email": ""
  },
  "update": {
    "guid": "",
    "name": "",
    "email": ""
  },
  "getAll": 0
}
```

and voilà, tests generated!

```java
public class UserRestTestEndpoint extends Container<UserDTO,UserEndpoint> {

  // Here you can define your global variables

  @Before
  public void setUp() {
    // Here you can initialize your variables

  }

  @Test
  public void getAll() {
    List<UserDTO> entities = (ArrayList<UserDTO>) getData(GET_ALL);
    List<UserDTO> fetchedEntities = (ArrayList<UserDTO>) getClient().getAll();

    Assert.assertEquals(entities.size(), fetchedEntities.size());
  }

  @Test
  public void create() {
    UserDTO entity = (UserDTO) getData(PUT);
    Assert.assertNotNull(entity);

    UserDTO fetchedEntity = (UserDTO) getClient().put(entity);
    Assert.assertNotNull(fetchedEntity);

    Assert.assertEquals(entity, fetchedEntity);
  }

  @Test
  public void update() {
    UserDTO entity = (UserDTO) getData(POST);
    Assert.assertNotNull(entity);

    entity = (UserDTO) getClient().put(entity);
    Assert.assertNotNull(entity);

    UserDTO fetchedEntity = (UserDTO) getClient().post(entity.getGuid(), entity);
    Assert.assertNotNull(fetchedEntity);

    Assert.assertEquals(entity, fetchedEntity);
  }

  @Test
  public void delete() {
    UserDTO entity = (UserDTO) getData(DELETE);
    Assert.assertNotNull(entity);

    entity = (UserDTO) getClient().put(entity);
    Assert.assertNotNull(entity);

    Object fetchedEntity = getClient().delete(entity.getGuid());
    if (fetchedEntity instanceof UserDTO) {
          Assert.assertNull(fetchedEntity);
        
  } else if (fetchedEntity.getClass().equals(boolean.class)
          || fetchedEntity.getClass().equals(Boolean.class)) { 
        Assert.assertTrue((boolean) fetchedEntity);
  };
  }
  
```

Still interested 😄?  Then please take a moment to check [how to setup](/docs/installation.md) properly EasyRs withit your project. 

Currently you can checkout [easyrs-example](https://github.com/nirodg/easyrs-example) where you can find a simple war project runing within EasyRs

# Contribute

In case you would like to contribute updating the documentation, improving the functionalities, reporting issues or fixing them please, you\`re more than welcome 😄 . However, please have a look to the already defined [contribute](/docs/CONTRIBUTING.md)'s guide

# License

[MIT](http://showalicense.com/?year=2017&fullname=Dorin%20Gheorghe%20Brage#license-mit) © [Dorin Brage](https://github.com/nirodg/)