package com.fabridinapoli.kotlinorms.application.service

import arrow.core.left
import arrow.core.right
import com.fabridinapoli.kotlinorms.domain.model.Customer
import com.fabridinapoli.kotlinorms.domain.model.CustomerId
import com.fabridinapoli.kotlinorms.domain.model.CustomerRepository
import com.fabridinapoli.kotlinorms.domain.model.DomainError
import com.fabridinapoli.kotlinorms.domain.model.Email
import com.fabridinapoli.kotlinorms.domain.model.FullName
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import java.util.UUID
import org.junit.jupiter.api.Test

private val CUSTOMER_ID = UUID.randomUUID()

class SignUpCustomerServiceShould {
    private val repository = mockk<CustomerRepository>()

    @Test
    fun `sign up a customer successfully`() {
        val customer = Customer(
            CustomerId(CUSTOMER_ID),
            FullName("Fabrizio Di Napoli"),
            Email("some.email@example.org")
        )
        val request = SignUpCustomerRequest(
            id = CUSTOMER_ID,
            name = "Fabrizio",
            surname = "Di Napoli",
            email = "some.email@example.org",
        )
        every { repository.save(customer) } just runs

        val response = SignUpCustomerService(repository).invoke(request)

        response shouldBe SignUpCustomerResponse(id = CUSTOMER_ID).right()
    }

    @Test
    fun `return an error in case of customer data not passing business rules`() {
        val request = SignUpCustomerRequest(
            id = CUSTOMER_ID,
            name = "Fabrizio",
            surname = "",
            email = "some.email@example.org",
        )

        val response = SignUpCustomerService(repository).invoke(request)

        response shouldBe DomainError("Surname cannot be empty").left()
        verify(exactly = 0) { repository.save(any()) }
    }
}
