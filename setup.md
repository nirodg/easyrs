# Setup your project

In order to have it running please define it as a dependency and as an annotattion processor

## POM file

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