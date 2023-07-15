plugins {
    java
    id("org.springframework.boot") version "3.0.5"
    id("io.spring.dependency-management") version "1.1.0"
    id("au.com.dius.pact") version "4.5.5"
    id("org.ajoberstar.grgit") version "4.1.1"
}

group = "com.example.contracttesting"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.amqp:spring-rabbit-test")
    testImplementation("au.com.dius.pact.consumer:junit5:4.5.5")
    testImplementation("au.com.dius.pact.provider:junit5spring:4.5.5")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// to publish the contracts generated after running the consumer contract tests
// command to publish: gradlew pactPublish
pact {
    publish {
        pactBrokerUrl = "http://localhost:9292/"
        version = grgit.head().abbreviatedId
        consumerBranch = grgit.branch.current().getName()
    }
}

// to pull the contracts from the pact broker when running the provider contract tests
tasks.withType<Test>() {
    systemProperty("pact.verifier.publishResults", "true")
    systemProperty("pact.provider.branch", grgit.branch.current().getName())
    systemProperty("pact.provider.version",  grgit.head().abbreviatedId)
}

tasks.register<Test>("runConsumerContractTests") {
    filter {
        includeTestsMatching("*ConsumerContractTest")
    }
}

tasks.register<Test>("runProviderContractTests") {
    filter {
        includeTestsMatching("*ProviderContractTest")
    }
}