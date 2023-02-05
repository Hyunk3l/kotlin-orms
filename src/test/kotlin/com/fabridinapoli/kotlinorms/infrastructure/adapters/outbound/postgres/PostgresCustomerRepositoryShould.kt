package com.fabridinapoli.kotlinorms.infrastructure.adapters.outbound.postgres

import com.fabridinapoli.kotlinorms.domain.model.Customer
import com.fabridinapoli.kotlinorms.domain.model.CustomerId
import com.fabridinapoli.kotlinorms.domain.model.Email
import com.fabridinapoli.kotlinorms.domain.model.FullName
import io.kotest.matchers.shouldBe
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.UUID
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.queryForObject

private val CUSTOMER_ID = UUID.randomUUID()

@Tag("integration")
internal class PostgresCustomerRepositoryShould {

    private val databaseContainer = DatabaseContainer()

    private val jdbcTemplate = JdbcTemplate(databaseContainer.dataSource)

    @Test
    fun `save customer`() {
        val instant = Instant.now().truncatedTo(ChronoUnit.MILLIS)
        val clock = Clock.fixed(instant, ZoneId.systemDefault())
        val customer = Customer(
            CustomerId(CUSTOMER_ID),
            FullName("Fabrizio Di Napoli"),
            Email("some.email@example.org")
        )

        PostgresCustomerRepository(jdbcTemplate, clock).save(customer)

        val result = jdbcTemplate.queryForObject("SELECT * FROM customers WHERE id = ? LIMIT 1", CUSTOMER_ID) { rs, _ ->
            DatabaseCustomer(
                id = UUID.fromString(rs.getString("id")),
                fullName = rs.getString("full_name"),
                email = rs.getString("email"),
                createdAt = rs.getTimestamp("created_at").toLocalDateTime()
            )
        }
        result shouldBe DatabaseCustomer(
            id = customer.id.id,
            fullName = customer.fullName.fullName,
            email = customer.email.email,
            createdAt = LocalDateTime.now(clock)
        )
    }
}
