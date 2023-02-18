package com.fabridinapoli.kotlinorms.componenttest

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured.given
import java.util.UUID
import org.apache.http.entity.ContentType
import org.junit.jupiter.api.Test

private val customerId = UUID.randomUUID()

class SignUpCustomerShould : BaseComponentTest() {

    @Test
    fun `sign up a customer successfully`() {
        val response = given()
            .contentType(ContentType.APPLICATION_JSON.toString())
            .port(servicePort)
            .and()
            .body(
                """
                {
                    "id": "$customerId",
                    "name": "Fabrizio",
                    "surname": "Di Napoli",
                    "email": "some.email@example.org"
                }
            """.trimIndent()
            )
            .`when`()
            .post("/customers")
            .then()
            .extract()

        response.statusCode() shouldBe 201
        response.body().asString() shouldEqualJson """
                {
                    "id": "$customerId"
                }
            """.trimIndent()
    }
}
