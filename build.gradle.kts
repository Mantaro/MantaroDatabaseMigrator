import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("application")
}

group = "net.kodehawa.migrator"
version = "1.0"
application.mainClass.set("net.kodehawa.migrator.Migrator")

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.mongodb:mongodb-driver-sync:4.9.1")
    implementation("com.rethinkdb:rethinkdb-driver:2.4.4")
    implementation("com.fasterxml.jackson.core:jackson-core:2.14.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")
    implementation("ch.qos.logback:logback-classic:1.4.7")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("shadow")
        mergeServiceFiles()
        manifest {
            attributes(mapOf("Main-Class" to "net.kodehawa.migrator.Migrator"))
        }
    }
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "net.kodehawa.migrator.Migrator"
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}

tasks.test {
    useJUnitPlatform()
}