This is a simple mimic of GNU `nc` written in Java.

build:
```shell script
gradle shadowJar
```

run:
```shell script
rlwrap java -cp build/libs/netcat-1.1-all.jar cn.lexio.sql.Shell --url 'jdbc:h2:/tmp/test'
```

This artifact has been uploaded to [Clojars](https://repo.clojars.org/cn/lexio/netcat/)
```xml
<dependency>
  <groupId>cn.lexio</groupId>
  <artifactId>netcat</artifactId>
  <version>1.0.0</version>
</dependency>
```