package com.fabridinapoli.kotlinorms.infrastructure.adapters.outbound.jooq

import com.fabridinapoli.kotlinorms.domain.model.Customer
import com.fabridinapoli.kotlinorms.domain.model.CustomerId
import com.fabridinapoli.kotlinorms.domain.model.Email
import com.fabridinapoli.kotlinorms.domain.model.FullName
import com.fabridinapoli.kotlinorms.infrastructure.adapters.outbound.postgres.DatabaseContainer
import com.fabridinapoli.kotlinorms.infrastructure.adapters.outbound.postgres.DatabaseCustomer
import io.kotest.matchers.shouldBe
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID
import org.jooq.SQLDialect
import org.jooq.impl.DataSourceConnectionProvider
import org.jooq.impl.DefaultConfiguration
import org.jooq.impl.DefaultDSLContext
import org.jooq.impl.DefaultExecuteListenerProvider
import org.jooq.tools.LoggerListener
import org.junit.jupiter.api.Test
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.queryForObject
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy

private const val FULL_NAME = "Some fullname"
private const val EMAIL = "email@gmail.com"
private val customerId = UUID.randomUUID()

class JooqCustomerRepositoryShould {

    private val databaseContainer = DatabaseContainer()

    private val jdbcTemplate = JdbcTemplate(databaseContainer.dataSource)

    @Test
    fun `save a customer into the db`() {
        val connectionProvider = DataSourceConnectionProvider(TransactionAwareDataSourceProxy(databaseContainer.dataSource))
        val config = DefaultConfiguration().also {
            it.set(connectionProvider)
            it.set(SQLDialect.POSTGRES)
            it.set(DefaultExecuteListenerProvider(LoggerListener()))
        }
        val clock = Clock.fixed(Instant.parse("2018-08-22T10:00:00Z"), ZoneOffset.UTC)
        val repository = JooqCustomerRepository(DefaultDSLContext(config), clock)
        val customer = Customer(
            id = CustomerId(id = customerId),
            fullName = FullName(fullName = FULL_NAME),
            email = Email(EMAIL)
        )

        repository.save(customer)
        val result = jdbcTemplate.queryForObject("SELECT * FROM customers WHERE id = ? LIMIT 1", customerId) { rs, _ ->
            DatabaseCustomer(
                id = UUID.fromString(rs.getString("id")),
                fullName = rs.getString("full_name"),
                email = rs.getString("email"),
                createdAt = rs.getTimestamp("created_at").toLocalDateTime()
            )
        }

        result shouldBe DatabaseCustomer(
            customerId,
            FULL_NAME,
            EMAIL,
            LocalDateTime.now(clock)
        )
    }
}
