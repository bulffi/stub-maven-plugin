# stub-maven-plugin

## usage

```xml
<plugin>
    <groupId>org.f4</groupId>
    <artifactId>stub-maven-plugin</artifactId>
    <version>1.0-SNAPSHOT</version>
    <executions>
      <execution>
        <id>make-stub</id>
        <phase>process-classes</phase>
        <goals>
          <goal>stub</goal>
        </goals>
      </execution>
      <execution>
        <id>redirect-output</id>
        <phase>process-test-classes</phase>
        <goals>
          <goal>redirect</goal>
        </goals>
      </execution>
    </executions>
</plugin>
```

