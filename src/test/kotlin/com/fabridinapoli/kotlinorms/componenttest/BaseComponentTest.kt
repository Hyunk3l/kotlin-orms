package com.fabridinapoli.kotlinorms.componenttest

import com.fabridinapoli.kotlinorms.Application
import com.fabridinapoli.kotlinorms.infrastructure.adapters.outbound.postgres.DatabaseContainer
import org.junit.jupiter.api.Tag
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.junit.jupiter.Testcontainers

@Suppress("UnnecessaryAbstractClass")
@Tag("component")
@Testcontainers
@SpringBootTest(classes = [Application::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class BaseComponentTest {

    @LocalServerPort
    val servicePort: Int = 0

    companion object {

        @JvmStatic
        @DynamicPropertySource
        fun postgresProperties(registry: DynamicPropertyRegistry) {
            val postgresContainer = DatabaseContainer().postgresContainer
            registry.add("spring.datasource.url", postgresContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgresContainer::getUsername)
            registry.add("spring.datasource.password", postgresContainer::getPassword)
        }
    }
}
