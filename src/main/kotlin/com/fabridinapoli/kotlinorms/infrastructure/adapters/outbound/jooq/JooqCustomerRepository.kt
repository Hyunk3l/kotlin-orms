package com.fabridinapoli.kotlinorms.infrastructure.adapters.outbound.jooq

import com.fabridinapoli.kotlinorms.domain.model.Customer
import com.fabridinapoli.kotlinorms.domain.model.CustomerRepository
import java.time.Clock
import java.time.LocalDateTime
import jooq.com.fabridinapoli.kotlinorms.Tables
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired

class JooqCustomerRepository(@Autowired private val context: DSLContext, private val clock: Clock) :
    CustomerRepository {

    override fun save(customer: Customer) {
        context
            .insertInto(
                Tables.CUSTOMERS,
                jooq.com.fabridinapoli.kotlinorms.tables.Customers.CUSTOMERS.ID,
                jooq.com.fabridinapoli.kotlinorms.tables.Customers.CUSTOMERS.EMAIL,
                jooq.com.fabridinapoli.kotlinorms.tables.Customers.CUSTOMERS.FULL_NAME,
                jooq.com.fabridinapoli.kotlinorms.tables.Customers.CUSTOMERS.CREATED_AT
            )
            .values(
                customer.id.id,
                customer.email.email,
                customer.fullName.fullName,
                LocalDateTime.now(clock)
            )
            .execute()
    }
}
