
plugins {
    java
    id("com.github.johnrengelman.shadow").version("6.0.0")
}

repositories {
    jcenter()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    compileOnly("mysql:mysql-connector-java:8.0.20")
    implementation("com.h2database:h2:1.4.200")
}

group = "cn.lexio"
version = "1.1"
description = "netcat"
java.sourceCompatibility = JavaVersion.VERSION_1_8

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}
