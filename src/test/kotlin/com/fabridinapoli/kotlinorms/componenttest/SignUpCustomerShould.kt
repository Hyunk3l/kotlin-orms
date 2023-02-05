package com.fabridinapoli.kotlinorms.componenttest

import com.fabridinapoli.kotlinorms.domain.model.Customer
import com.fabridinapoli.kotlinorms.domain.model.CustomerId
import com.fabridinapoli.kotlinorms.domain.model.Email
import com.fabridinapoli.kotlinorms.domain.model.FullName
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.shouldBe
import io.restassured.RestAssured.given
import java.util.UUID
import org.apache.http.entity.ContentType
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

private val CUSTOMER_ID = UUID.randomUUID()

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
                    "id": "$CUSTOMER_ID",
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
                    "id": "$CUSTOMER_ID"
                }
            """.trimIndent()
    }
}
