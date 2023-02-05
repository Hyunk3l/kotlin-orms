package com.fabridinapoli.kotlinorms.domain.model

import arrow.core.left
import arrow.core.right
import io.kotest.matchers.shouldBe
import java.util.UUID
import org.junit.jupiter.api.Test

private val CUSTOMER_ID = UUID.randomUUID()

class CustomerShould {

    @Test
    fun `create a customer when all the data is valid`() {
        val expectedCustomer = Customer(
            id = CustomerId(id = CUSTOMER_ID),
            fullName = FullName("Fabrizio Di Napoli"),
            email = Email("some.email@example.org")
        )

        val customer = Customer.create(
            id = CUSTOMER_ID,
            name = "Fabrizio",
            surname = "Di Napoli",
            email = "some.email@example.org"
        )

        customer shouldBe expectedCustomer.right()
    }

    @Test
    fun `cannot create a customer when name is empty`() {
        val customer = Customer.create(
            id = CUSTOMER_ID,
            name = "",
            surname = "Surname",
            email = "some.email@example.org"
        )

        customer shouldBe DomainError("Name cannot be empty").left()
    }

    @Test
    fun `cannot create a customer when surname is empty`() {
        val customer = Customer.create(
            id = CUSTOMER_ID,
            name = "Fabrizio",
            surname = "",
            email = "some.email@example.org"
        )

        customer shouldBe DomainError("Surname cannot be empty").left()
    }

    @Test
    fun `cannot create a customer when email is empty`() {
        val customer = Customer.create(
            id = CUSTOMER_ID,
            name = "Fabrizio",
            surname = "Di Napoli",
            email = ""
        )

        customer shouldBe DomainError("Email cannot be empty").left()
    }
}
