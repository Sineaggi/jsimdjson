plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(20)
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 20
    options.compilerArgs = listOf("--enable-preview", "--add-modules", "jdk.incubator.vector")
}

tasks.withType<Test>().configureEach {
    jvmArgs(listOf("--enable-preview", "--add-modules", "jdk.incubator.vector"))
}

testing {
    suites {
        named<JvmTestSuite>("test") {
            useJUnitJupiter("5.9.3")
        }
    }
}
