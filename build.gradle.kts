import java.net.URI
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("java-library")
    id("maven-publish")
    id("io.github.goooler.shadow") version "8.1.8"
}

group = "de.varilx"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation("org.mongodb:mongodb-driver-sync:5.2.1")
    implementation("mysql:mysql-connector-java:8.0.33")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("org.hibernate:hibernate-core:6.3.1.Final")
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")

    implementation("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
    implementation("org.jetbrains:annotations:26.0.1")
    implementation("org.jetbrains:annotations:26.0.1")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}


tasks.named<ShadowJar>("shadowJar") {
    archiveClassifier.set("")
}

tasks.register<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allJava)
}

publishing {
    repositories {
        maven {
            name = "Reposilite"
            url = URI((System.getenv("REPOSILITE_URL") ?: "default").toString())
            credentials {
                username = System.getenv("REPOSILITE_USER")
                password = System.getenv("REPOSILITE_TOKEN")
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            groupId = "$group"
            version = "$version"
            artifact(tasks.named<ShadowJar>("shadowJar").get())
            artifact(tasks.named<Jar>("sourcesJar").get())
        }
    }
}


tasks.named("publishGprPublicationToReposiliteRepository") {
    dependsOn(tasks.named("jar"))
}

