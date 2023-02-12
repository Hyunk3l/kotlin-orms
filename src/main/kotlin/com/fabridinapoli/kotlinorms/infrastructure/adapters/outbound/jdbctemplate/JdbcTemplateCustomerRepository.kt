package com.fabridinapoli.kotlinorms.infrastructure.adapters.outbound.jdbctemplate

import com.fabridinapoli.kotlinorms.domain.model.Customer
import com.fabridinapoli.kotlinorms.domain.model.CustomerRepository
import java.time.Clock
import java.time.LocalDateTime
import java.util.UUID
import org.springframework.jdbc.core.JdbcTemplate

class JdbcTemplateCustomerRepository(
    private val jdbcTemplate: JdbcTemplate,
    private val clock: Clock
) : CustomerRepository {

    override fun save(customer: Customer) {
        val databaseCustomer = customer.toDatabaseEntity()
        jdbcTemplate.update(
            """
            INSERT INTO customers
            (
                id, full_name, email, created_at
            )
            VALUES (?, ?, ?, ?)
        """.trimIndent(),
            databaseCustomer.id,
            databaseCustomer.fullName,
            databaseCustomer.email,
            databaseCustomer.createdAt
        )
    }

    private fun Customer.toDatabaseEntity() = DatabaseCustomer(
        id = this.id.id,
        fullName = this.fullName.fullName,
        email = this.email.email,
        createdAt = LocalDateTime.now(clock)
    )
}

data class DatabaseCustomer(
    val id: UUID,
    val fullName: String,
    val email: String,
    val createdAt: LocalDateTime
)
