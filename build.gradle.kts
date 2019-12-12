import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.2.2.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    kotlin("jvm") version "1.3.61"
    kotlin("plugin.spring") version "1.3.61"
    id("com.github.ben-manes.versions") version "0.27.0"
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
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // serialization: jackson json
    //val jacksonVersion =  "2.9.9"
    val jacksonVersion =  "2.10.1"
    implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-modules-java8:$jacksonVersion")
    implementation( "com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation( "com.fasterxml.jackson.module:jackson-module-parameter-names:$jacksonVersion")
    implementation( "com.fasterxml.jackson.datatype:jackson-datatype-jdk8:$jacksonVersion")
    implementation( "com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")


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
