package com.fabridinapoli.kotlinorms.domain.model

interface CustomerRepository {
    fun save(customer: Customer)
}
