package com.fabridinapoli.kotlinorms.infrastructure.adapters.outbound.ktorm

import com.fabridinapoli.kotlinorms.domain.model.Customer
import com.fabridinapoli.kotlinorms.domain.model.CustomerRepository
import java.time.Clock
import java.time.LocalDateTime
import org.ktorm.database.Database
import org.ktorm.dsl.insert
import org.ktorm.schema.Table
import org.ktorm.schema.datetime
import org.ktorm.schema.uuid
import org.ktorm.schema.varchar

class KtormCustomerRepository(private val database: Database, private val clock: Clock) : CustomerRepository {

    object Customers : Table<Nothing>("customers") {
        val id = uuid("id").primaryKey()
        val fullName = varchar("full_name")
        val email = varchar("email")
        val createdAt = datetime("created_at")
    }

    override fun save(customer: Customer) {
        database.insert(Customers) {
            set(it.id, customer.id.id)
            set(it.fullName, customer.fullName.fullName)
            set(it.email, customer.email.email)
            set(it.createdAt, LocalDateTime.now(clock))
        }
    }
}
