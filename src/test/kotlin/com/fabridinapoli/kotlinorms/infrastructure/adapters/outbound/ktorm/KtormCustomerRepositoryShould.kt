package com.fabridinapoli.kotlinorms.infrastructure.adapters.outbound.ktorm

import com.fabridinapoli.kotlinorms.domain.model.Customer
import com.fabridinapoli.kotlinorms.domain.model.CustomerId
import com.fabridinapoli.kotlinorms.domain.model.Email
import com.fabridinapoli.kotlinorms.domain.model.FullName
import com.fabridinapoli.kotlinorms.infrastructure.adapters.outbound.DatabaseContainer
import com.fabridinapoli.kotlinorms.infrastructure.adapters.outbound.jdbctemplate.DatabaseCustomer
import io.kotest.matchers.shouldBe
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.UUID
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.ktorm.database.Database
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.queryForObject

private val customerId = UUID.randomUUID()

@Tag("integration")
class KtormCustomerRepositoryShould {

    private val databaseContainer = DatabaseContainer()

    private val jdbcTemplate = JdbcTemplate(databaseContainer.dataSource)

    @Test
    fun `save customer`() {
        val instant = Instant.now().truncatedTo(ChronoUnit.MILLIS)
        val clock = Clock.fixed(instant, ZoneId.systemDefault())
        val customer = Customer(
            CustomerId(customerId),
            FullName("Fabrizio Di Napoli"),
            Email("some.email@example.org")
        )
        val database = Database.connect(
            url = databaseContainer.dataSource.jdbcUrl,
            driver = databaseContainer.dataSource.driverClassName,
            user = databaseContainer.dataSource.username,
            password = databaseContainer.dataSource.password
        )

        KtormCustomerRepository(database = database, clock = clock).save(customer)

        val result = jdbcTemplate.queryForObject("SELECT * FROM customers WHERE id = ? LIMIT 1", customerId) { rs, _ ->
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
