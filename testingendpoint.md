# Testing your endpoint

Before to start defining the test let's have a look to which options we can use
1. `@EndpointTest`
  1. Identifier, **required**
  2. Entity, **required**
  3. Operations, _optional_. By default will generate **getAll()**, **create()**, **update()** and **delete()** unless is specified manually 
  4. Execution,  _optional_. It can be executed as Singleton or with Arquillian
  5. Endpoint,  _optional_. Speficy your endpoint class/interface


So, shall we ?

1. Define your testing endpoint under `src/test/java`. `Example: UserRest`
2. Add the annotation `@EndpointTest` to your already created interface
3. Execute `mvn install`
4. The generated classes will be under /target/generated-sources/`
5. _Optional_ - Right click on that folder and Use as Source Folder`