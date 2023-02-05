package com.fabridinapoli.kotlinorms.infrastructure.adapters.inbound.http.controller

import arrow.core.Either
import com.fabridinapoli.kotlinorms.application.service.SignUpCustomerRequest
import com.fabridinapoli.kotlinorms.application.service.SignUpCustomerResponse
import com.fabridinapoli.kotlinorms.application.service.SignUpCustomerService
import com.fabridinapoli.kotlinorms.domain.model.DomainError
import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.json.shouldEqualJson
import io.mockk.every
import java.util.UUID
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

private val CUSTOMER_ID = UUID.randomUUID()

@Tag("integration")
@WebFluxTest(controllers = [SignUpController::class])
class SignUpControllerShould {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockkBean
    private lateinit var signUpCustomerService: SignUpCustomerService

    @Test
    fun `sign up a customer`() {
        val signUpCustomerRequest = SignUpCustomerRequest(
            id = CUSTOMER_ID,
            name = "Fabrizio",
            surname = "Di Napoli",
            email = "some.email@example.org",
        )
        every {
            signUpCustomerService(signUpCustomerRequest)
        } returns Either.Right(SignUpCustomerResponse(id = CUSTOMER_ID))

        val response = webTestClient
            .post()
            .uri("/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue("""
                {
                    "id": "$CUSTOMER_ID",
                    "name": "Fabrizio",
                    "surname": "Di Napoli",
                    "email": "some.email@example.org"
                }
            """.trimIndent())
            .exchange()

        response
            .expectStatus()
            .isCreated
            .expectBody<String>()
            .returnResult().responseBody!!.shouldEqualJson("""
                {
                    "id": "$CUSTOMER_ID"
                }
            """.trimIndent())
    }

    @Test
    fun `return a 500 if any error in the use case`() {
        val signUpCustomerRequest = SignUpCustomerRequest(
            id = CUSTOMER_ID,
            name = "Fabrizio",
            surname = "Di Napoli",
            email = "some.email@example.org",
        )
        every { signUpCustomerService(signUpCustomerRequest) } returns Either.Left(DomainError("Any error"))

        val response = webTestClient
            .post()
            .uri("/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue("""
                {
                    "id": "$CUSTOMER_ID",
                    "name": "Fabrizio",
                    "surname": "Di Napoli",
                    "email": "some.email@example.org"
                }
            """.trimIndent())
            .exchange()

        response
            .expectStatus()
            .is5xxServerError
    }
}
