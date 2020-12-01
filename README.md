This is a simple mimic of GNU `nc` written in Java.

build:
```shell script
mvn package
```

run:
```shell script
java -jar target/netcat-1.0.0-SNAPSHOT.jar
```

This artifact has been uploaded to [Clojars](https://repo.clojars.org/cn/lexio/netcat/)
```xml
<dependency>
  <groupId>cn.lexio</groupId>
  <artifactId>netcat</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```