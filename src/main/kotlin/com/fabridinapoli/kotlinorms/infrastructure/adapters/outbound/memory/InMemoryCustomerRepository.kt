package com.fabridinapoli.kotlinorms.infrastructure.adapters.outbound.memory

import com.fabridinapoli.kotlinorms.domain.model.Customer
import com.fabridinapoli.kotlinorms.domain.model.CustomerRepository

class InMemoryCustomerRepository : CustomerRepository {

    private val customers = mutableListOf<Customer>()

    override fun save(customer: Customer) {
        customers.add(customer)
    }
}
