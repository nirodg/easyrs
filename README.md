# `easyrs`

[![Join the chat at https://gitter.im/nirodg/easyrs](https://badges.gitter.im/nirodg/easyrs.svg)](https://gitter.im/nirodg/easyrs?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)[![Build Status](https://travis-ci.org/nirodg/easyrs.svg?branch=master)](https://travis-ci.org/nirodg/easyrs)[![Codacy Badge](https://api.codacy.com/project/badge/Grade/ab49fb3cf47744d28b95154f8cf18e14)](https://www.codacy.com/app/nirodg/easyrs?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=nirodg/easyrs&amp;utm_campaign=Badge_Grade)

This library provides an easy way to test the basic CRUD operation for your endpoints. The tests can be executed within Arquillian or as a Singleton.

# Installing

In order to have it running you should have your POM file as followed

```xml
<properties>
    <version.org.dbrage.easyrs>0.0.1-SNAPSHOT</version.org.dbrage.easyrs>
</properties>

<dependencies>

    <!-- -->
    <dependency>
        <groupId>org.dbrage.lib</groupId>
        <artifactId>easyrs-test</artifactId>
        <version>${version.org.dbrage.easyrs}</version>
    </dependency>
    <!-- -->

</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.5.1</version>
            <configuration>
                <source>1.8</source>
                <target>1.8</target>
                <annotationProcessorPaths>
                    <path>
                        <groupId>org.dbrage.lib</groupId>
                        <artifactId>easyrs-test</artifactId>
                        <version>${version.org.dbrage.easyrs}</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>
```

# Defining your endpoint test

1. Create an `inteface` under `src/test/java`. Example: UserRest

2. Add the annotation `@EndpointTest` . The fields dto and identifier are required.

3. Execute `mvn install`

4. The generated classes will be under `/target/generated-sources/`

5. _Optional_ - Right click on that folder and Use as Source Folder\`

At the end it should look like:

```java
package com.example.easyrs;

import com.dbrage.lib.easyrs.processor.annotation.EndpointTest;
import com.dbrage.lib.easyrs.processor.enums.UUIDIdentifier;

@EndpointTest(identifier=UUIDIdentifier.UUID, entity=UserDto.class)
public interface UserRest {

}
```

# RestClient

This library provides a way how to authenticate against realm's mechanism. By default the host will appoint to `http://localhost`unless you define the system property

In order to make the request to the proper host some system properties should be specified

* `client.host` By default the host will appoint to `http://localhost`
* `client.user` and `client.pass` to authenticate against a [basic http authentication](https://tools.ietf.org/html/rfc2617#page-3) 

# Contribute

In case you would like to contribute updating the documentation, improving the functionalities, reporting issues or fixing them please, you\`re more than welcome 😄 . However, please have a look to the already defined [\#contribute](#contribute)'s guide

# License

[MIT](http://showalicense.com/?year=2017&fullname=Dorin%20Gheorghe%20Brage#license-mit) © [Dorin Brage](https://github.com/nirodg/)

