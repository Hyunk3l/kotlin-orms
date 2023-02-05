package com.fabridinapoli.kotlinorms.domain.model

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import java.util.UUID

data class Customer(
    val id: CustomerId,
    val fullName: FullName,
    val email: Email,
) {
    companion object {
        fun create(
            id: UUID,
            name: String,
            surname: String,
            email: String,
        ): Either<DomainError, Customer> = when {
            name == "" -> DomainError("Name cannot be empty").left()
            surname == "" -> DomainError("Surname cannot be empty").left()
            email == "" -> DomainError("Email cannot be empty").left()
            else -> Customer(
                CustomerId(id),
                FullName("$name $surname"),
                Email(email),
            ).right()
        }
    }
}

data class CustomerId(val id: UUID)
data class FullName(val fullName: String)
data class Email(val email: String)
