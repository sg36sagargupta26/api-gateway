plugins {
	java
	id("org.springframework.boot") version "4.0.4"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.lld"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

repositories {
	mavenCentral()
}

dependencies {
    implementation("org.springframework.cloud:spring-cloud-gateway-server-webmvc:5.0.1")
    implementation("org.springframework.boot:spring-boot-starter-jdbc:4.0.4")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:4.0.4")
    implementation("org.postgresql:postgresql:42.7.9")
    implementation("org.springframework.boot:spring-boot-starter-data-redis:4.0.4")
    implementation("org.springframework.boot:spring-boot-starter-security:4.0.4")
    implementation("org.springframework.security:spring-security-oauth2-resource-server:7.0.4")
    implementation("org.springframework.security:spring-security-oauth2-jose:7.0.4")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
