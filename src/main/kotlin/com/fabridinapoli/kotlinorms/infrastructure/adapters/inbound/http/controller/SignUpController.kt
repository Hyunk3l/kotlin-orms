package com.fabridinapoli.kotlinorms.infrastructure.adapters.inbound.http.controller

import com.fabridinapoli.kotlinorms.application.service.SignUpCustomerRequest
import com.fabridinapoli.kotlinorms.application.service.SignUpCustomerService
import java.util.UUID
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class SignUpController {

    @Autowired
    private lateinit var signUpCustomerService: SignUpCustomerService

    @PostMapping("/customers")
    fun signUpCustomer(@RequestBody requestBody: HttpSignUpCustomerRequestBody): Any =
        requestBody.toServiceRequest()
            .let {
                signUpCustomerService.invoke(it)
            }
            .fold(
                {
                    ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build()
                }, {
                    HttpSignUpCustomerResponse(it.id).let { response ->
                        ResponseEntity.status(HttpStatus.CREATED).body(response)
                    }
                }
            )

    private fun HttpSignUpCustomerRequestBody.toServiceRequest() = SignUpCustomerRequest(
        id = this.id,
        name = this.name,
        surname = this.surname,
        email = this.email,
    )
}

data class HttpSignUpCustomerRequestBody(
    val id: UUID,
    val name: String,
    val surname: String,
    val email: String,
)

data class HttpSignUpCustomerResponse(val id: UUID)
