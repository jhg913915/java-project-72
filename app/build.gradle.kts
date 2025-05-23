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
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.26.3")
    testImplementation("org.mockito:mockito-core:5.13.0")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
    implementation("io.javalin:javalin:6.3.0")
    implementation("io.javalin:javalin-rendering:6.3.0")
    implementation("io.javalin:javalin-bundle:6.3.0")
    implementation("io.javalin:javalin-testtools:6.3.0")
    implementation("gg.jte:jte:3.1.12")
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("com.h2database:h2:2.2.220")
    implementation("org.postgresql:postgresql:42.7.2")
    implementation("com.konghq:unirest-java:3.14.5")
    implementation("com.konghq:unirest-objectmapper-jackson:4.2.9")
    implementation("org.jsoup:jsoup:1.18.1")
}
tasks.test {
    useJUnitPlatform()
}