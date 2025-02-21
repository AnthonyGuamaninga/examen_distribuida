plugins {
    id("java")
//    id("io.helidon.microprofile") version "4.0.0"
    id("application")
//    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.freefair.lombok") version "8.11"
}

group = "com.programacion.distribuida"
version = "unspecified"

repositories {
    mavenCentral()
}

var helidonVersion = "4.1.6"

dependencies {
    implementation("io.helidon.microprofile.bundles:helidon-microprofile")
    // Helidon BOM (Bill of Materials)
    implementation(enforcedPlatform("io.helidon:helidon-dependencies:${helidonVersion}"))

    // Helidon Core
    implementation("io.helidon.webserver:helidon-webserver")
    implementation("io.helidon.health:helidon-health-checks")

    // JSON-B
    implementation("jakarta.json.bind:jakarta.json.bind-api")
    implementation("org.glassfish.jersey.media:jersey-media-json-binding")

    // JPA / Hibernate (usando la integración oficial de Helidon)
    implementation("io.helidon.integrations.cdi:helidon-integrations-cdi-hibernate")

    // CDI y JPA APIs
    implementation("jakarta.annotation:jakarta.annotation-api")
    implementation("jakarta.enterprise:jakarta.enterprise.cdi-api")
    implementation("jakarta.inject:jakarta.inject-api")
    implementation("jakarta.persistence:jakarta.persistence-api")
    implementation("jakarta.transaction:jakarta.transaction-api")

    // DeltaSpike Data
    implementation("org.apache.deltaspike.modules:deltaspike-data-module-api:2.0.0")
    implementation("org.apache.deltaspike.core:deltaspike-core-api:2.0.0")
    implementation("org.apache.deltaspike.core:deltaspike-core-impl:2.0.0")

    // OpenAPI
    implementation("io.helidon.openapi:helidon-openapi")
    implementation("org.eclipse.microprofile.openapi:microprofile-openapi-api:4.0.2")
    implementation("org.eclipse.microprofile.config:microprofile-config-api:3.1")

    // Health Check
    implementation("io.helidon.health:helidon-health")

    // Flyway
    implementation("org.flywaydb:flyway-core:11.3.3")

}

sourceSets {
    main {
        output.setResourcesDir( file("${buildDir}/classes/java/main") )
    }
}

//tasks.jar {
//    manifest {
//        attributes(
//            mapOf("Main-Class" to "com.programacion.distribuida.authors.rest.AuthorRest",
//                "Class-Path" to configurations.runtimeClasspath
//                    .get()
//                    .joinToString(separator = " ") { file ->
//                        "${file.name}"
//                    })
//        )
//    }
//}