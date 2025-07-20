import java.net.URI
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("java-library")
    id("maven-publish")
    id("io.github.goooler.shadow") version "8.1.8"
}

group = "de.varilx"
version = project.property("project_version") as String

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    // Hibernate and Jakarta
    implementation("jakarta.persistence:jakarta.persistence-api:3.2.0")
    implementation("org.hibernate:hibernate-core:7.0.0.Final")
    implementation("org.hibernate.orm:hibernate-community-dialects:6.6.22.Final")
    testImplementation("org.hibernate.orm:hibernate-community-dialects:6.6.22.Final")

    // SQLite
    implementation("org.xerial:sqlite-jdbc:3.49.1.0")
    testImplementation("org.xerial:sqlite-jdbc:3.49.1.0")

    // PaperMC API
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    testImplementation("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")

    // Reflections
    implementation("org.reflections:reflections:0.10.2")

    // Lombok
    implementation("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
    compileOnly("org.projectlombok:lombok:1.18.38")
    testImplementation("org.projectlombok:lombok:1.18.38")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.38")

    // JetBrains Annotations
    implementation("org.jetbrains:annotations:26.0.2")

    // JUnit and SLF4J for testing
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.13.0-SNAPSHOT")
    testImplementation("org.junit.platform:junit-platform-launcher:1.13.0-SNAPSHOT")
    testImplementation("org.slf4j:slf4j-jdk14:2.0.17")
    testImplementation("org.assertj:assertj-core:3.27.3")
}

tasks.withType<Test> {
    useJUnitPlatform()
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

tasks.withType<Test> {
    useJUnitPlatform()
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

publishing {
    repositories {
        maven {
            name = "Reposilite"
            url = URI("https://reposilite.varilx.de/Varilx")
            credentials {
                username = "Dario"
                password = System.getenv("REPOSILITE_TOKEN")
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            groupId = "$group"
            version = version
            artifact(tasks.named<ShadowJar>("shadowJar").get())
            artifact(tasks.named<Jar>("sourcesJar").get())
        }
    }
}


tasks.named("publishGprPublicationToReposiliteRepository") {
    dependsOn(tasks.named("jar"))
}


