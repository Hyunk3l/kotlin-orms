package com.fabridinapoli.kotlinorms.infrastructure.adapters.outbound.jooq

import com.fabridinapoli.kotlinorms.Tables
import com.fabridinapoli.kotlinorms.domain.model.Customer
import com.fabridinapoli.kotlinorms.domain.model.CustomerRepository
import com.fabridinapoli.kotlinorms.tables.Customers
import org.jooq.DSLContext
import org.jooq.Record

import org.springframework.beans.factory.annotation.Autowired

class JooqCustomerRepository(@Autowired private val context: DSLContext) : CustomerRepository {

    override fun save(customer: Customer) {
        context
            .insertInto<Record, Any, Any>(
                Tables.CUSTOMERS,
                com.fabridinapoli.kotlinorms.tables.Customers.ID,
                com.fabridinapoli.kotlinorms.tables.Customers.EMAIL,
                com.fabridinapoli.kotlinorms.tables.Customers.FULL_NAME,
            )
            .values(customer.id, customer.email, customer.fullName)
            .execute()
    }
}
