package com.fabridinapoli.kotlinorms.infrastructure.adapters.outbound

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.FluentConfiguration
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

class DatabaseContainer {

    val postgresContainer: PostgreSQLContainer<*> = PostgreSQLContainer(DockerImageName.parse("postgres:15.1"))
        .withDatabaseName("somedatabasename")
        .withUsername("postgres")
        .withPassword("postgres")
        .also{ it.start() }

    val dataSource = HikariConfig()
        .apply {
            jdbcUrl = postgresContainer.jdbcUrl
            username = postgresContainer.username
            password = postgresContainer.password
            driverClassName = postgresContainer.driverClassName
        }.let {
            HikariDataSource(it)
        }.also {
            Flyway(
                FluentConfiguration().dataSource(it)
            ).migrate()
        }
}
