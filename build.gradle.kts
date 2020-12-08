
plugins {
    java
    id("com.github.johnrengelman.shadow").version("6.1.0")
}

repositories {
    jcenter()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    /*
    compileOnly("mysql:mysql-connector-java:8.0.20")
    compileOnly("org.postgresql:postgresql:42.2.18")
    compileOnly("org.xerial:sqlite-jdbc:3.32.3.2")
    compileOnly("com.h2database:h2:1.4.200") // */
}

group = "cn.lexio"
version = "1.1"
description = "netcat"
java.sourceCompatibility = JavaVersion.VERSION_1_8

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Jar>() {
    manifest {
        attributes["Implementation-Version"] = "1.1"
        attributes["Main-Class"] = "cn.lexio.Netcat"
    }
}