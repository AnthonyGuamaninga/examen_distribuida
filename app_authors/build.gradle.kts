import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.freefair.lombok") version "8.11"
}

group = "com.programacion.distribuida"
version = "unspecified"

repositories {
    mavenCentral()
}

var helidonVersion = "4.1.6"

dependencies {
    // Helidon MicroProfile
    implementation(enforcedPlatform("io.helidon:helidon-dependencies:$helidonVersion"))
    implementation("io.helidon.webserver:helidon-webserver")
//    implementation("io.helidon.microprofile.server:helidon-microprofile-server") //config property
    // Health
    implementation("io.helidon.webserver.observe:helidon-webserver-observe-health")
    implementation("io.helidon.health:helidon-health-checks")
//    implementation("org.eclipse.microprofile.health:microprofile-health-api:4.0")
//    implementation("com.ecwid.consul:consul-api:1.4.0")
//    implementation("io.helidon.health:helidon-health")

    // JSON (Gson)
    implementation("com.google.code.gson:gson:2.12.1")
    // CDI
    implementation("org.jboss.weld.se:weld-se-core:6.0.1.Final")
    implementation("jakarta.enterprise:jakarta.enterprise.cdi-api:4.1.0")

    // JPA
    implementation("org.hibernate.orm:hibernate-core:6.6.3.Final")
    // Base de datos (PostgreSQL)
    implementation("org.postgresql:postgresql:42.7.4")

    // OpenAPI
    implementation("io.helidon.openapi:helidon-openapi")
    implementation("io.helidon.integrations.openapi-ui:helidon-integrations-openapi-ui")
    implementation("io.smallrye:smallrye-open-api-ui:4.0.8")
    // Flyway
    implementation("org.flywaydb:flyway-core:11.3.3")
    implementation("org.apache.commons:commons-dbcp2:2.13.0")
}

sourceSets {
    main {
        output.setResourcesDir(file("${buildDir}/classes/java/main"))
    }
}

tasks.jar {
    manifest {
        attributes(
            mapOf(
                "Main-Class" to "com.programacion.distribuida.authors.PrincipalAuthorsRest",
                "Class-Path" to configurations.runtimeClasspath
                    .get()
                    .joinToString(separator = " ") { file ->
                        "${file.name}"
                    }
            )
        )
    }
}