plugins {
    id("java")
    id("application")
    id("io.freefair.lombok") version "8.13.1"
}

application {
    mainClass.set("hexlet.code.App")
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.javalin:javalin:6.2.0")
    implementation("org.slf4j:slf4j-simple:2.0.16")

    implementation("com.zaxxer:HikariCP:6.3.0")
    implementation("com.h2database:h2:2.3.232")
    implementation("org.postgresql:postgresql:42.7.4")

    testImplementation(platform("org.junit:junit-bom:5.11.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}