import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.github.ben-manes.versions") version ("0.29.0")
    // kotlin
    kotlin("jvm") version "1.3.72"
    // spring
    // see: https://kotlinlang.org/docs/reference/compiler-plugins.html
    kotlin("plugin.spring") version "1.3.72"
    kotlin("plugin.noarg") version "1.3.72"
    kotlin("plugin.allopen") version "1.3.72"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    id("org.springframework.boot") version "2.3.3.RELEASE"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    // logging
    implementation("io.github.microutils:kotlin-logging:1.7.+")

    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude(group="com.fasterxml.jackson.core")
    }
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // serialization: jackson json
    val jacksonVersion =  "2.11.2"
    implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-modules-java8:$jacksonVersion")
    implementation( "com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation( "com.fasterxml.jackson.module:jackson-module-parameter-names:$jacksonVersion")
    implementation( "com.fasterxml.jackson.datatype:jackson-datatype-jdk8:$jacksonVersion")
    implementation( "com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

    val swaggerVersion = "2.9.2"
    implementation("io.springfox:springfox-swagger2:$swaggerVersion")
    implementation("io.springfox:springfox-swagger-ui:$swaggerVersion")


    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.amshove.kluent:kluent:1.58")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}
