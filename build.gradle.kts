import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.meta.jaxb.*
import org.jooq.codegen.GenerationTool
import org.jooq.meta.jaxb.Target

plugins {
    id("java")
    id("java-library")
    id("org.springframework.boot") version "3.0.4"
    id("io.spring.dependency-management") version "1.1.0"
    id("io.gitlab.arturbosch.detekt") version "1.22.0"
    kotlin("jvm") version "1.8.10"
    kotlin("plugin.spring") version "1.8.10"
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.jooq:jooq-codegen:3.18.0")
        classpath("org.postgresql:postgresql:42.3.5")
    }
}

group = "{{ group_name }}"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.2")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.2.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.10")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.10")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.6.4")
    implementation("io.arrow-kt:arrow-core:1.1.5")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.postgresql:postgresql:42.5.2")
    implementation("org.flywaydb:flyway-core:9.15.2")
    implementation(platform("org.apache.logging.log4j:log4j-bom:2.19.0"))
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.ktorm:ktorm-core:3.6.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "mockito-core")
        exclude(group = "junit", module = "junit")
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("io.projectreactor:reactor-test:3.5.3")
    testImplementation("org.testcontainers:testcontainers:1.17.6")
    testImplementation("org.testcontainers:junit-jupiter:1.17.6")
    testImplementation("org.testcontainers:postgresql:1.17.6")
    testImplementation("io.kotest:kotest-assertions-core-jvm:5.5.5")
    testImplementation("io.kotest:kotest-assertions-json:5.5.4")
    testImplementation("io.kotest:kotest-runner-junit5:5.5.5")
    testImplementation("io.rest-assured:rest-assured:5.3.0")
    testImplementation("io.rest-assured:json-path:5.3.0")
    testImplementation("io.rest-assured:xml-path:5.3.0")
    testImplementation("io.rest-assured:json-schema-validator:5.3.0")
    testImplementation("io.mockk:mockk:1.13.4")
    testImplementation("com.ninja-squad:springmockk:4.0.0")
    testImplementation("com.tngtech.archunit:archunit:1.0.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }

    task<Test>("unitTest") {
        useJUnitPlatform {
            excludeTags("integration")
            excludeTags("component")
        }
        shouldRunAfter(test)
    }

    task<Test>("integrationTest") {
        description = "Runs integration tests."
        useJUnitPlatform {
            includeTags("integration")
        }
        shouldRunAfter(test)
    }

    task<Test>("componentTest") {
        useJUnitPlatform {
            includeTags("component")
        }
        shouldRunAfter(test)
    }
}

sourceSets {
    main {
        kotlin {
            srcDirs("src/main/java/generated", "src/main/kotlin")
        }
    }
}

tasks.register("generateJooqSources") {
    GenerationTool.generate(
        Configuration()
            .withJdbc(
                Jdbc()
                    .withDriver("org.postgresql.Driver")
                    .withUrl("jdbc:postgresql://localhost:5432/somedatabasename")
                    .withUser("jdbctemplate")
                    .withPassword("jdbctemplate")
            )
            .withGenerator(
                Generator()
                    .withDatabase(Database().withInputSchema("public"))
                    .withGenerate(Generate())
                    .withTarget(
                        Target()
                            .withPackageName("jooq.com.fabridinapoli.kotlinorms")
                            .withDirectory("${projectDir}/src/main/java")
                    )
            )
    )
}
